package com.cognizant.aws.fse.userservice.dao;

import java.util.List;
import java.util.Optional;

import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.cognizant.aws.fse.userservice.model.User;
import com.cognizant.aws.fse.userservice.model.UserUniqueId;

//@EnableScan
//@Repository
public interface UserDao {// extends CrudRepository<User, UserUniqueId> {
 
	List<User> findAll();
	
	
}
