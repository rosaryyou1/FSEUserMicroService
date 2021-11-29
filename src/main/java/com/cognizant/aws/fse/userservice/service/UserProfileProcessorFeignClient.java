package com.cognizant.aws.fse.userservice.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.cognizant.aws.fse.userservice.Json.domain.UserJsonModel;
import com.cognizant.aws.fse.userservice.model.User;

@FeignClient(name="fseprofileprocesssrv",
url="http://FSEFargateAlb-1289736730.us-east-2.elb.amazonaws.com:8080",fallback = UserProfileProcCircuitBrk.class)
public interface UserProfileProcessorFeignClient {
	@PostMapping(value="/internal/skill-tracker/api/v1/engineer/save-profile",consumes = MediaType.APPLICATION_JSON_VALUE)
	public String saveUser(@RequestBody UserJsonModel usermodel);
	
	@PostMapping(value="/internal/skill-tracker/api/v1/engineer/update-profile",consumes = MediaType.APPLICATION_JSON_VALUE)
	public String updateUser(@RequestBody User use);
	
	@GetMapping(value="/internal/skill-tracker/api/v1/engineer/users/{userId}")
	public User getUserByUserId(@PathVariable String userId); 
}
