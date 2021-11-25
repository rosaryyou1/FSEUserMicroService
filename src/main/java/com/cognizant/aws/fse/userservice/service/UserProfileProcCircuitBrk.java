package com.cognizant.aws.fse.userservice.service;

import org.springframework.web.bind.annotation.PathVariable;

import com.cognizant.aws.fse.userservice.Json.domain.UserJsonModel;
import com.cognizant.aws.fse.userservice.model.User;

public class UserProfileProcCircuitBrk implements UserProfileProcessorFeignClient{

	@Override
	public String saveUser(UserJsonModel usermodel) {
		return "{\"status\":\"Service Not available\"}";
	}
	

	@Override
	public String updateUser(User use) {
		return "{\"status\":\"Service Not available\"}";
	}
	
	@Override
	public User getUserByUserId(@PathVariable String userId) {
		return new User();
	}

}
