package com.cognizant.aws.fse.userservice.controller;

import java.util.List;

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

	@GetMapping("/users/{id}")
	public User getUser(@PathVariable String id) {
		User user =  userService.getUserByUserId(id);
		return user;

	}

	@PostMapping(path="/add-profile",consumes = MediaType.APPLICATION_JSON_VALUE)
	public String addUser(@RequestBody UserJsonModel userModel) {
		String status = "{ status : success}";
		try {
			userService.saveUser(userModel);
		} catch (Exception e) {
			status = e.getMessage();
		}
		return status;

	}

	@PutMapping(path="/update-profile/{userId}",consumes = MediaType.APPLICATION_JSON_VALUE)
	public String updateUser(@PathVariable String userId,@RequestBody List<Skill> lstSkill
			,@RequestHeader(name = "userName") String userName,@RequestHeader(name="password") String password) {
		String status = "{ status : success}";
		try {
			authService.authenticateUser(userName,password);
			userService.updateUser(userId,lstSkill);
		} catch (Exception e) {
			status = e.getMessage();
		}
		return status;
	}


}
