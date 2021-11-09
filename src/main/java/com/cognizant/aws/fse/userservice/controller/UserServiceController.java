package com.cognizant.aws.fse.userservice.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cognizant.aws.fse.userservice.Json.Domain.Skill;
import com.cognizant.aws.fse.userservice.model.User;
import com.cognizant.aws.fse.userservice.service.UserService;

@RestController
@RequestMapping("/v1")
public class UserServiceController {

@Autowired
UserService userService;

@GetMapping("/users")
public List<User> getUsers(){
	return userService.getUsers();
}

@GetMapping("/users/{id}")
public User getUser(@PathVariable String id) {
	User user =  userService.getUserByUserId(id);
	return user;

}

@PostMapping(path="/saveusers",consumes = MediaType.APPLICATION_JSON_VALUE)
public User getUser(@RequestBody User user) {
	System.out.println(user);
	userService.saveUser(user);
	return user;

}

@GetMapping("/users/{criteria}/{criteriavalue}")
public List<User> getUser(@PathVariable String criteria,@PathVariable String criteriavalue) {
	System.out.println(criteriavalue);
	List<User> users =  userService.findByNameAndAssociateIdAndSkill(criteria,criteriavalue);
	return users;

}

@PutMapping(path="/updateusers/{userId}",consumes = MediaType.APPLICATION_JSON_VALUE)
public User updateUser(@PathVariable String userId,@RequestBody List<Skill> lstSkill) {
	User user =  userService.updateUser(userId,lstSkill);
	return user;
}

}
