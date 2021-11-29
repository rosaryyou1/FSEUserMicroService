package com.cognizant.aws.fse.userservice.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.jms.pool.PooledConnectionFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.cognizant.aws.fse.userservice.Json.domain.Skill;
import com.cognizant.aws.fse.userservice.Json.domain.UserJsonModel;
import com.cognizant.aws.fse.userservice.model.User;
import com.cognizant.aws.fse.userservice.util.UserModelJsonConverter;
import com.cognizant.aws.fse.userservice.util.ValidationError;
import com.cognizant.aws.fse.userservice.util.ValidationException;
import com.cognizant.aws.fse.userservice.util.ValidationUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class UserService {

	@Autowired
	PooledConnectionFactory pooledActiveMQConnectionFactory;

	@Autowired
	ActiveMQConnectionFactory activeMQConnectionFactory;

	@Autowired
	UserProfileProcessorSrvLocator usrProfileProcessorSrvLoc;

	@Autowired
	UserProfileProcessorFeignClient feignClnt; 

	@Value("${consumer.queues}")
	String queueName;
	
	@Autowired
	DynamoDBMapper dynamoDBmapper;
	
	Logger logger = LogManager.getLogger(UserService.class);
	
	public User getUserByUserId(String userId) {
		User user = feignClnt.getUserByUserId(userId);
		return user;
	}

	@JmsListener(destination = "${consumer.queues}")
	public void  receiveMessage(TextMessage message) throws JMSException, JsonMappingException, JsonProcessingException {
		logger.debug("method receiveMessage - start :"+message.getText());
		String srvEndpoint = usrProfileProcessorSrvLoc.resolve();
		ObjectMapper obj = new ObjectMapper();
		UserJsonModel userJsonModel= obj.readValue(message.getText(),UserJsonModel.class);
		feignClnt.saveUser(userJsonModel);
		logger.debug("method receiveMessage - start :"+message.getText());
	}

	public void publishUser(UserJsonModel userModel) throws JMSException, JsonProcessingException {
		logger.debug("method publishUser - start :"+userModel.getAssociateName());
		Connection producerConnection = pooledActiveMQConnectionFactory.createConnection();
		producerConnection.start();
		final Session producerSession = producerConnection
				.createSession(false, Session.AUTO_ACKNOWLEDGE);
		final Destination producerDestination = producerSession
				.createQueue(queueName);
		final MessageProducer producer = producerSession
				.createProducer(producerDestination);
		producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
		ObjectMapper obj = new ObjectMapper();
		String userJson = obj.writeValueAsString(userModel);
		//final String text = "Hello from Amazon MQ!";
		final TextMessage producerMessage = producerSession
				.createTextMessage(userJson);
		producer.send(producerMessage);
		producer.close();
		producerSession.close();
		producerConnection.close();
		logger.debug("method publishUser - end :"+userModel.getAssociateName());
	}

	public void saveUser(UserJsonModel userModel) throws ValidationException, JsonProcessingException, JMSException {
		logger.debug("method saveUser - start :"+userModel.getAssociateName());
		List<ValidationError> lstError = ValidationUtil.validateUser(userModel);
		if(lstError.size()>0) {
			ValidationUtil.constructError(lstError);
		}
		this.publishUser(userModel);
		logger.debug("method saveUser - end :"+userModel.getAssociateName());
	}

	public String updateUser(String userId, List<Skill> lstSkills) throws ValidationException {
		logger.debug("method updateUser - start :"+userId);
		List<ValidationError> lstError = ValidationUtil.validateOnlySkills(lstSkills);
		User user = this.getUserByUserId(userId);
		if(userId==null || user==null || user.getUserId()==null) {
			lstError.add(new ValidationError("UserId","Invalid userid"));
			ValidationUtil.constructError(lstError);
		}
		if(user.getCre8Ts()==null) {
			user.setCre8Ts(LocalDate.now().toString());
		}
		if(user.getCre8Ts()!=null && 
				LocalDate.now().minusDays(10).compareTo(LocalDate.parse(user.getCre8Ts()))>0) {
			lstError.add(new ValidationError("Cre8Ts","Date exceed, Update can not be done"));
		}
		ValidationUtil.constructError(lstError);
		Map<String, Integer> mapSkills = lstSkills.stream().collect(Collectors.toMap(skill->skill.getSkillName(),skill->skill.getSkillLevel()));
		UserModelJsonConverter.mapSkills(user,mapSkills);
		user.setLstUptTs(LocalDate.now().toString());
		String status = this.callUpdateUserMethod(user);
		logger.debug("method updateUser - end :"+status);
		return status;
	}
	
	public String callUpdateUserMethod(User user) {
		String status = feignClnt.updateUser(user);
		return status;
	}
	
}
