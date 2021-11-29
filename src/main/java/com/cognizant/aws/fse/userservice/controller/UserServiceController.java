package com.cognizant.aws.fse.userservice.controller;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cognizant.aws.fse.userservice.Json.domain.Skill;
import com.cognizant.aws.fse.userservice.Json.domain.UserJsonModel;
import com.cognizant.aws.fse.userservice.model.User;
import com.cognizant.aws.fse.userservice.service.CognitoAuthService;
import com.cognizant.aws.fse.userservice.service.UserService;
import com.cognizant.aws.fse.userservice.util.ValidationException;

import net.spy.memcached.MemcachedClient;

@RestController
@RequestMapping("/skill-tracker/api/v1/engineer")
public class UserServiceController {

	@Autowired
	UserService userService;

	//@Autowired
	MemcachedClient memcachedClient;

	@Autowired
	CognitoAuthService authService;

	Logger logger = LogManager.getLogger(UserServiceController.class);


	@GetMapping("/users/{id}")
	public User getUser(@PathVariable String id) {
		logger.info("Service getUser - start :"+id);
		User user =  userService.getUserByUserId(id);
		logger.info("Service getUser - success :"+id);
		return user;

	}

	@PostMapping(path="/add-profile",consumes = MediaType.APPLICATION_JSON_VALUE)
	public String addUser(@RequestBody UserJsonModel userModel) {
		logger.info("Service Add profile - start :"+userModel.getAssociateName());
		String status = "{ status : success}";
		try {
			userService.saveUser(userModel);
			logger.info("Service Add profile - success :"+userModel.getAssociateName());
		} catch (Exception e) {
			logger.error("Service Add profile - failed :"+userModel.getAssociateName());
			status = e.getMessage();
			logger.error(status);			
		}
		return status;

	}

	@PutMapping(path="/update-profile/{userId}",consumes = MediaType.APPLICATION_JSON_VALUE)
	public String updateUser(@PathVariable String userId,@RequestBody List<Skill> lstSkill
			,@RequestHeader(name = "userName") String userName,@RequestHeader(name="password") String password) {
		logger.info("Service update profile - start :"+userId+":updated by:"+userName);
		String status = "{ status : success}";
		try {
			authService.authenticateUser(userName,password);
			userService.updateUser(userId,lstSkill);
			logger.info("Service update profile - success :"+userId+":updated by:"+userName);
		} catch (Exception e) {
			logger.info("Service update profile - failed :"+userId+":updated by:"+userName);
			status = e.getMessage();
			logger.error(status);
		}
		return status;
	}


}
