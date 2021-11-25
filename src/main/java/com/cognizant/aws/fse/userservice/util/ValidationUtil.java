package com.cognizant.aws.fse.userservice.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.cognizant.aws.fse.userservice.Json.domain.Skill;
import com.cognizant.aws.fse.userservice.Json.domain.UserJsonModel;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ValidationUtil {
	public static List<ValidationError> validateUser(UserJsonModel user) throws ValidationException {
		List<ValidationError> errorLst = new ArrayList<>();
		if(user.getAssociateName()==null || user.getAssociateName().length()<5 
				|| user.getAssociateName().length()>30) {
			ValidationError validationError = new ValidationError();
			validationError.setProperty("AssociateName");
			validationError.setErrorDescription("Name is not null , min 5 and max 30 characters");
			errorLst.add(validationError);
		}
		if(user.getAssociateId()==null || user.getAssociateId().length()<5 
				|| user.getAssociateId().length()>30 || !user.getAssociateId().startsWith("CTS")) {
			ValidationError validationError = new ValidationError();
			validationError.setProperty("AssociateId");
			validationError.setErrorDescription("AssociateID is not null, min 5 and max 30 characters, and must start with \"CTS\"");
			errorLst.add(validationError);
		}
		if(user.getEmail()==null || !patternMatches(user.getEmail(), "^(.+)@(.+)$")) {
			ValidationError validationError = new ValidationError();
			validationError.setProperty("Email");
			validationError.setErrorDescription("Email is not null, and it should be valid email pattern, containing a single @");
			errorLst.add(validationError);

		}
		if(user.getMobile()==null || !patternMatches(user.getMobile(), "^\\d{10}$")) {
			ValidationError validationError = new ValidationError();
			validationError.setProperty("Mobile");
			validationError.setErrorDescription("Mobile is not null, Mobile should have 10 digit");
			errorLst.add(validationError);

		}
		skillsValidation(user.getSkills(),errorLst);
		return errorLst;
	}
	
	public static List<ValidationError> validateOnlySkills(List<Skill> skills) throws ValidationException{
		List<ValidationError> errorLst = new ArrayList<>();
		skillsValidation(skills,errorLst);
		return errorLst;
	}

	public static void constructError(List<ValidationError> errorLst) throws ValidationException {
		if(!errorLst.isEmpty()) {
			ObjectMapper obj = new ObjectMapper();
			String json = "Validation Error";
			try {
				json = obj.writeValueAsString(errorLst);
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
			throw new ValidationException(json);
		}
	}

	private static void skillsValidation(List<Skill> skills, List<ValidationError> errorLst) {
		List<Skill> errorSkills = skills.stream().filter(skill->skill.getSkillLevel()<=0 || 
				skill.getSkillLevel()>20).collect(Collectors.toList());
		if(errorSkills.size()>0) {
			for(Skill erroSkill : errorSkills) {
				ValidationError validationError = new ValidationError();
				validationError.setProperty(erroSkill.getSkillName());
				validationError.setErrorDescription("Expertise level must not be empty or a non-numeric value ,"
						+ "Expertise level for each skill must range between 0-20");
				errorLst.add(validationError);
			}
		}
	}
	
	public static boolean patternMatches(String validateString, String regexPattern) {
	    return Pattern.compile(regexPattern)
	      .matcher(validateString)
	      .matches();
	}
}
