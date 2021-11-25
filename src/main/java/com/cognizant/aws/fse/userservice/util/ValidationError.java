package com.cognizant.aws.fse.userservice.util;

public class ValidationError {
	String property;
	String errorDescription;
	
	
	public ValidationError (String property,String errorDescription ) {
		this.property=property;
		this.errorDescription=errorDescription;
	}
	
	public ValidationError() {
		// TODO Auto-generated constructor stub
	}

	public String getProperty() {
		return property;
	}
	public void setProperty(String property) {
		this.property = property;
	}
	public String getErrorDescription() {
		return errorDescription;
	}
	public void setErrorDescription(String errorDescription) {
		this.errorDescription = errorDescription;
	}
	
}
