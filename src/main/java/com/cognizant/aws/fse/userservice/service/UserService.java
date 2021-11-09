package com.cognizant.aws.fse.userservice.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Index;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.ItemCollection;
import com.amazonaws.services.dynamodbv2.document.QueryOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.cognizant.aws.fse.userservice.Json.Domain.Skill;
import com.cognizant.aws.fse.userservice.dao.UserDao;
import com.cognizant.aws.fse.userservice.model.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class UserService {
	
	//@Autowired
	//UserDao userDao;
	
//	@Autowired
	//DynamoDBMapper dynamoDBmapper;
	
	@Autowired
	AmazonDynamoDB amzonDynamoDB;

	public List<User> getUsers(){
	 //List<User> lst = userDao.findAll();
	 return null;
	}
	
	public User getUserByUserId(String userId) {
		//List<User> users = userDao.findByUserId(userId);
		ValueMap valueMap = new ValueMap();
		String keyExpression = "UserId = :v1";
		valueMap.withString(":v1", userId);
		QuerySpec spec = new QuerySpec()
			    .withKeyConditionExpression(keyExpression)
			    .withValueMap(valueMap);
		DynamoDB dynamoDB = new DynamoDB(amzonDynamoDB);
		Table table = dynamoDB.getTable("FSEProfile");
		Index index = table.getIndex("GSI-search-index4");
		ItemCollection<QueryOutcome> items = index.query(spec);
		//List<User> users = userDao.findByNameAndAssociateIdAndSkill("*","357495","Java");
		Iterator<Item> iterator = items.iterator();
		User user = null;
		while (iterator.hasNext()) {
		    String json = iterator.next().toJSON();
		    System.out.println(json);
		    ObjectMapper obj = new ObjectMapper();
		    try {
				 user = obj.readValue(json, User.class);
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return user;
	}
	
	public void saveUser(User user) {
		DynamoDBMapper dynamoDBmapper = new DynamoDBMapper(amzonDynamoDB);
		dynamoDBmapper.save(user);
	}
	
	public  List<User> findByNameAndAssociateIdAndSkill(String criteria, String criteriaValue){
		String keyExpression = "";
		String indexName = "";
		ValueMap valueMap = new ValueMap();
		switch(criteria){
			case "searchName" : 
				
				String[] criteriaValueArray = criteriaValue.split("-");
				if(criteriaValueArray.length==2) {
					keyExpression = "AssociateId = :v1 and begins_with(AssociateName , :v2)";
					valueMap.withString(":v1", criteriaValueArray[0]);
					valueMap.withString(":v2", criteriaValueArray[1]);
				}else if(criteriaValueArray.length==1){
					keyExpression = "AssociateId = :v1";
					valueMap.withString(":v1", criteriaValueArray[0]);
				}
				//indexName = "GSI-search-index1";
				break;
			/*case "searchAssociateId" : 
				keyExpression = "AssociateId = :v1";
				valueMap.withString(":v1", criteriaValue);
				//indexName = "GSI-search-index2";
				break;*/
			case "searchSkill" : 
				keyExpression = criteriaValue+" = :v1 and "+criteriaValue+"Level > :v2";
				System.out.println(keyExpression);
				valueMap.withString(":v1",criteriaValue);
				valueMap.withNumber(":v2", 10);
				indexName = "GSI-search-index1";
				break;
			default :
				break;
				
		}
		QuerySpec spec = new QuerySpec()
			    .withKeyConditionExpression(keyExpression)
			    .withValueMap(valueMap);
		DynamoDB dynamoDB = new DynamoDB(amzonDynamoDB);
		Table table = dynamoDB.getTable("FSEProfile");
		ItemCollection<QueryOutcome> items = null;
		if(!indexName.isEmpty()) {
		Index index = table.getIndex(indexName);
		items= index.query(spec);
		}else {
			items = table.query(spec);
		}
		//List<User> users = userDao.findByNameAndAssociateIdAndSkill("*","357495","Java");
		Iterator<Item> iterator = items.iterator();
		List<User> lstUser = new ArrayList<>();
		while (iterator.hasNext()) {
		    String json = iterator.next().toJSON();
		    System.out.println(json);
		    ObjectMapper obj = new ObjectMapper();
		    try {
				User user = obj.readValue(json, User.class);
				lstUser.add(user);
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return lstUser;
	}
	
	public User updateUser(String userId, List<Skill> lstSkills) {
		User user = this.getUserByUserId(userId);
		System.out.println(user);
		Map<String, Integer> mapSkills = lstSkills.stream().collect(Collectors.toMap(skill->skill.getSkillName(),skill->skill.getSkillLevel()));
		if(mapSkills.get("Angular")!=null) {
			user.setAngularLevel(mapSkills.get("Angular"));
		}
		DynamoDBMapper dynamoDBmapper = new DynamoDBMapper(amzonDynamoDB);
		dynamoDBmapper.save(user);
		return user;
	}
}
