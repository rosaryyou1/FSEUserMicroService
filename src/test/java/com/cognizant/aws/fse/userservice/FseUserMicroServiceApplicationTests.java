package com.cognizant.aws.fse.userservice;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doNothing;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.cognizant.aws.fse.userservice.Json.domain.Skill;
import com.cognizant.aws.fse.userservice.Json.domain.UserJsonModel;
import com.cognizant.aws.fse.userservice.controller.UserServiceController;
import com.cognizant.aws.fse.userservice.model.User;
import com.cognizant.aws.fse.userservice.service.UserService;
import com.cognizant.aws.fse.userservice.util.ValidationException;

//@RunWith(SpringRunner.class)
//@WebMvcTest(value = UserServiceController.class)
//@ContextConfiguration(classes=FseUserMicroServiceApplication.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc //need this in Spring Boot test
@ActiveProfiles("test")
class FseUserMicroServiceApplicationTests {
	
	@Autowired
	private MockMvc mockMvc;

	@MockBean
	UserService userService;
	
	@Test
	void contextLoads() {
	}
	
	@Test
	public void testaddUser() {
		UserJsonModel mockUserJsonModel = new UserJsonModel();
		try {
			doNothing().when(userService).saveUser(mockUserJsonModel);
			RequestBuilder requestBuilder = MockMvcRequestBuilders.post(
					"/skill-tracker/api/v1/engineer/add-profile").accept(
					MediaType.APPLICATION_JSON).content("{}").contentType(MediaType.APPLICATION_JSON);
			MvcResult result = mockMvc.perform(requestBuilder).andReturn();
			MockHttpServletResponse response = result.getResponse();
			assertEquals(HttpStatus.OK.value(), response.getStatus());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	@Test
	public void testUpdateUser() {
		try {
			Mockito.when(userService.updateUser(Mockito.anyString(), Mockito.anyList())).thenReturn("{\"status\":\"success\"}");
			HttpHeaders httpHeaders = new HttpHeaders();
			httpHeaders.add("userName", "cyrilkar");
			httpHeaders.add("password", "#MyShop-17");
			RequestBuilder requestBuilder = MockMvcRequestBuilders.put(
					"/skill-tracker/api/v1/engineer/update-profile/1232132131").accept(
					MediaType.APPLICATION_JSON).content("[]").contentType(MediaType.APPLICATION_JSON).headers(httpHeaders);
			MvcResult result = mockMvc.perform(requestBuilder).andReturn();
			MockHttpServletResponse response = result.getResponse();
			assertEquals(HttpStatus.OK.value(), response.getStatus());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void testUpdateUserDataError() {
		try {
			Mockito.when(userService.updateUser(Mockito.anyString(), Mockito.anyList())).thenThrow(new ValidationException("Test Message"));
			HttpHeaders httpHeaders = new HttpHeaders();
			httpHeaders.add("userName", "cyrilkar");
			httpHeaders.add("password", "#MyShop-17");
			RequestBuilder requestBuilder = MockMvcRequestBuilders.put(
					"/skill-tracker/api/v1/engineer/update-profile/1232132131").accept(
					MediaType.APPLICATION_JSON).content("[]").contentType(MediaType.APPLICATION_JSON).headers(httpHeaders);
			
			MvcResult result = mockMvc.perform(requestBuilder).andReturn();
			MockHttpServletResponse response = result.getResponse();
			assertEquals(HttpStatus.OK.value(), response.getStatus());
			assertEquals("Test Message", response.getContentAsString());
		} catch (Exception e) {
			e.getMessage();
		}
	}
	
	@Test
	public void testgetUserById() {
		try {
			User mockUser = new User();
			Mockito.when(userService.getUserByUserId(Mockito.anyString())).thenReturn(mockUser);
			RequestBuilder requestBuilder = MockMvcRequestBuilders.get(
					"/skill-tracker/api/v1/engineer//users/1232132131").contentType(MediaType.APPLICATION_JSON);
			MvcResult result = mockMvc.perform(requestBuilder).andReturn();
			MockHttpServletResponse response = result.getResponse();
			assertEquals(HttpStatus.OK.value(), response.getStatus());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
