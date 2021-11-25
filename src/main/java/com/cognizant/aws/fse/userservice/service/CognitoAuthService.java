package com.cognizant.aws.fse.userservice.service;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.model.AuthFlowType;
import com.amazonaws.services.cognitoidp.model.InitiateAuthRequest;
import com.amazonaws.services.cognitoidp.model.InitiateAuthResult;
import com.cognizant.aws.fse.userservice.util.ValidationError;
import com.cognizant.aws.fse.userservice.util.ValidationUtil;
import com.google.common.hash.Hashing;


@Service
public class CognitoAuthService {

	@Autowired
	AWSCognitoIdentityProvider awsCognitoIDPClient;
	
	@Value("${spring.security.oauth2.client.registration.cognito.client-id}")
	private String CLIENT_ID;

	public void authenticateUser(String userName,String password) throws Exception {
		try {
			Map<String,String>  params =new HashMap<>();

			params.put("USERNAME",userName);
			params.put("PASSWORD",password);
			final InitiateAuthRequest initiateAuthRequest = new InitiateAuthRequest();
			initiateAuthRequest.withClientId(CLIENT_ID);
			initiateAuthRequest.withAuthFlow(AuthFlowType.USER_PASSWORD_AUTH);
			initiateAuthRequest.withAuthParameters(params);
			final InitiateAuthResult result = awsCognitoIDPClient.initiateAuth(initiateAuthRequest);
		}catch(Exception e) {
			ValidationError validationError = new ValidationError();
			validationError.setProperty("AuthFailed");
			validationError.setErrorDescription(e.getMessage());
			List<ValidationError> errorLst = new ArrayList<>();
			errorLst.add(validationError);
			ValidationUtil.constructError(errorLst);

		}
	}

}
