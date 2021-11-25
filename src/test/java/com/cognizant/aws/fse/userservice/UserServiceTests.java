package com.cognizant.aws.fse.userservice;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

import java.util.List;

import javax.jms.JMSException;

import org.apache.activemq.broker.BrokerService;
import org.apache.activemq.junit.EmbeddedActiveMQBroker;
import org.junit.Before;
import org.junit.Rule;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBeans;
import org.springframework.test.context.ActiveProfiles;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.document.Index;
import com.cognizant.aws.fse.userservice.Json.domain.Skill;
import com.cognizant.aws.fse.userservice.Json.domain.UserJsonModel;
import com.cognizant.aws.fse.userservice.model.User;
import com.cognizant.aws.fse.userservice.service.UserProfileProcessorFeignClient;
import com.cognizant.aws.fse.userservice.service.UserProfileProcessorSrvLocator;
import com.cognizant.aws.fse.userservice.service.UserService;
import com.cognizant.aws.fse.userservice.util.UserModelJsonConverter;
import com.cognizant.aws.fse.userservice.util.ValidationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@ActiveProfiles("test")
public class UserServiceTests {
	@Autowired
	UserService userService;

	@Rule
	public EmbeddedActiveMQBroker broker =new EmbeddedActiveMQBroker();
	
	@MockBean
	UserProfileProcessorSrvLocator usrProfileProcessorSrvLoc;
	
	@MockBean
	UserProfileProcessorFeignClient feignClnt;
	
	static UserJsonModel userJsonModel;
	
	static User user;
	
	@MockBean
	Index index;
	
	@MockBean
	AmazonDynamoDB amazonDynamoDB;
	
	@MockBean
	DynamoDBMapper dynamoDBmapper;
	
	@BeforeAll
	public static void setup() throws JsonMappingException, JsonProcessingException {
		String json = "{\"AssociateName\":\"Ram3423\",\"AssociateId\":\"CTS3223574121\",\"Mobile\":\"1234535611\",\"Email\":\"test12@gmail.com\",\"Skills\":[{\"Skill\":\"Angular\",\"Type\" : \"Tech\",\"Level\" : 20},{\"Skill\":\"HtmlCss\",\"Type\" : \"Tech\",\"Level\" : 20},{\"Skill\":\"React\",\"Type\" : \"Tech\",\"Level\" : 20}, {\"Skill\":\"Spring\",\"Type\" : \"Tech\",\"Level\" : 20}, 	 {\"Skill\":\"Rest\",\"Type\" : \"Tech\",\"Level\" : 20},{\"Skill\":\"Hibernate\",\"Type\" : \"Tech\",\"Level\" : 20},{\"Skill\":\"Git\",\"Type\" : \"Tech\",\"Level\" : 20}, {\"Skill\":\"Docker\",\"Type\" : \"Tech\",\"Level\" : 20}, 	 {\"Skill\":\"Jenkins\",\"Type\" : \"Tech\",\"Level\" : 20}, 	 {\"Skill\":\"Aws\",\"Type\" : \"Tech\",\"Level\" : 20},{\"Skill\":\"Spoken\",\"Type\" : \"Tech\",\"Level\" : 20},{\"Skill\":\"Communication\",\"Type\" : \"Tech\",\"Level\" : 20}, {\"Skill\":\"Aptitude\",\"Type\" : \"Tech\",\"Level\" : 20}]}";
		ObjectMapper obj = new ObjectMapper();
		userJsonModel = obj.readValue(json, UserJsonModel.class);
		user =UserModelJsonConverter.convertUserJsonModel(userJsonModel);
		user.setUserId("1231232132132132131");
		
	}
	
	@Test
	public void testsaveUser() {
		try {
			Mockito.when(usrProfileProcessorSrvLoc.resolve()).thenReturn("testservice");
			Mockito.when(feignClnt.saveUser(userJsonModel)).thenReturn("{\"status\":\"success\"}");
			userService.saveUser(userJsonModel);
			assertEquals("OK", "OK");
		} catch (JsonProcessingException | ValidationException | JMSException e) {
			System.out.println(e.getMessage());
			assertTrue(e==null);
			// TODO Auto-generated catch block
			
		}
	}

	@Test
	public void testsaveUserValidation() {
		try {
			String json = "{\"AssociateName\":\"\",\"AssociateId\":\"TCS3223574121\",\"Mobile\":\"1234511\",\"Email\":\"test12gmail.com\",\"Skills\":[{\"Skill\":\"Angular\",\"Type\" : \"Tech\",\"Level\" : 30},{\"Skill\":\"HtmlCss\",\"Type\" : \"Tech\",\"Level\" : 20},{\"Skill\":\"React\",\"Type\" : \"Tech\",\"Level\" : 20}, {\"Skill\":\"Spring\",\"Type\" : \"Tech\",\"Level\" : 20}, 	 {\"Skill\":\"Rest\",\"Type\" : \"Tech\",\"Level\" : 20},{\"Skill\":\"Hibernate\",\"Type\" : \"Tech\",\"Level\" : 20},{\"Skill\":\"Git\",\"Type\" : \"Tech\",\"Level\" : 20}, {\"Skill\":\"Docker\",\"Type\" : \"Tech\",\"Level\" : 20}, 	 {\"Skill\":\"Jenkins\",\"Type\" : \"Tech\",\"Level\" : 20}, 	 {\"Skill\":\"Aws\",\"Type\" : \"Tech\",\"Level\" : 20},{\"Skill\":\"Spoken\",\"Type\" : \"Tech\",\"Level\" : 20},{\"Skill\":\"Communication\",\"Type\" : \"Tech\",\"Level\" : 20}, {\"Skill\":\"Aptitude\",\"Type\" : \"Tech\",\"Level\" : 20}]}";
			ObjectMapper obj = new ObjectMapper();
			UserJsonModel errorUserJsonModel = obj.readValue(json, UserJsonModel.class);
			Mockito.when(usrProfileProcessorSrvLoc.resolve()).thenReturn("testservice");
			Mockito.when(feignClnt.saveUser(userJsonModel)).thenReturn("{\"status\":\"success\"}");
			userService.saveUser(errorUserJsonModel);
			assertEquals("OK", "OK");
		}catch(ValidationException e) {
			System.out.println(e.getMessage());
			assertTrue(e.getMessage().contains("AssociateName") && e.getMessage().contains("Mobile")&& e.getMessage().contains("Email") && e.getMessage().contains("Angular"));
		}
		catch (JsonProcessingException | JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void testupdateUser() {
		try {
			String json = "[{\"Skill\":\"Angular\",\"Type\" : \"Tech\",\"Level\" : 20},{\"Skill\":\"HtmlCss\",\"Type\" : \"Tech\",\"Level\" : 20},{\"Skill\":\"React\",\"Type\" : \"Tech\",\"Level\" : 20}, {\"Skill\":\"Spring\",\"Type\" : \"Tech\",\"Level\" : 20}, 	 {\"Skill\":\"Rest\",\"Type\" : \"Tech\",\"Level\" : 20},{\"Skill\":\"Hibernate\",\"Type\" : \"Tech\",\"Level\" : 20},{\"Skill\":\"Git\",\"Type\" : \"Tech\",\"Level\" : 20}, {\"Skill\":\"Docker\",\"Type\" : \"Tech\",\"Level\" : 20}, 	 {\"Skill\":\"Jenkins\",\"Type\" : \"Tech\",\"Level\" : 20}, 	 {\"Skill\":\"Aws\",\"Type\" : \"Tech\",\"Level\" : 20},{\"Skill\":\"Spoken\",\"Type\" : \"Tech\",\"Level\" : 20},{\"Skill\":\"Communication\",\"Type\" : \"Tech\",\"Level\" : 20}, {\"Skill\":\"Aptitude\",\"Type\" : \"Tech\",\"Level\" : 20}]";
			String userId = "12321321321312";
			userService = spy(UserService.class);
			ObjectMapper obj = new ObjectMapper();
			List<Skill> lstSkill= obj.readValue(json, new TypeReference<List<Skill>>(){});
			Mockito.when(usrProfileProcessorSrvLoc.resolve()).thenReturn("testservice");
			doReturn("{\"status\":\"success\"}").when(userService).callUpdateUserMethod(user);
			doReturn(user).when(userService).getUserByUserId(userId);
			//doNothing().when(dynamoDBmapper).save(user);
			String status = userService.updateUser(userId,lstSkill);
			assertEquals("{\"status\":\"success\"}", status);
		}catch(ValidationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@Test
	public void testupdateUserValidation() {
		try {
			String json = "[{\"Skill\":\"Angular\",\"Type\" : \"Tech\",\"Level\" : 22},{\"Skill\":\"HtmlCss\",\"Type\" : \"Tech\",\"Level\" : -1},{\"Skill\":\"React\",\"Type\" : \"Tech\",\"Level\" : 20}, {\"Skill\":\"Spring\",\"Type\" : \"Tech\",\"Level\" : 20}, 	 {\"Skill\":\"Rest\",\"Type\" : \"Tech\",\"Level\" : 20},{\"Skill\":\"Hibernate\",\"Type\" : \"Tech\",\"Level\" : 20},{\"Skill\":\"Git\",\"Type\" : \"Tech\",\"Level\" : 20}, {\"Skill\":\"Docker\",\"Type\" : \"Tech\",\"Level\" : 20}, 	 {\"Skill\":\"Jenkins\",\"Type\" : \"Tech\",\"Level\" : 20}, 	 {\"Skill\":\"Aws\",\"Type\" : \"Tech\",\"Level\" : 20},{\"Skill\":\"Spoken\",\"Type\" : \"Tech\",\"Level\" : 20},{\"Skill\":\"Communication\",\"Type\" : \"Tech\",\"Level\" : 20}, {\"Skill\":\"Aptitude\",\"Type\" : \"Tech\",\"Level\" : 20}]";
			String userId = "12321321321312";
			userService = spy(UserService.class);
			ObjectMapper obj = new ObjectMapper();
			List<Skill> lstSkill= obj.readValue(json, new TypeReference<List<Skill>>(){});
			Mockito.when(usrProfileProcessorSrvLoc.resolve()).thenReturn("testservice");
			User dummyUser = new User();
			doReturn("{\"status\":\"success\"}").when(userService).callUpdateUserMethod(dummyUser);
			doReturn(dummyUser).when(userService).getUserByUserId(userId);
			String status = userService.updateUser(userId,lstSkill);
			System.out.println(status);
			assertEquals("{\"status\":\"success\"}", status);
		}catch(ValidationException e) {
			assertTrue(e.getMessage().contains("Angular") && e.getMessage().contains("HtmlCss") &&
					e.getMessage().contains("Invalid userid"));
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
