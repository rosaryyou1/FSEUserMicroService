package com.cognizant.aws.fse.userservice.model;

import java.io.Serializable;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;

public class UserUniqueId implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/*
	 * String associateName; String associateId;
	 * 
	 * @DynamoDBHashKey(attributeName ="AssociateId") public String getAssociateId()
	 * { return associateId; } public void setAssociateId(String associateId) {
	 * this.associateId = associateId; }
	 * 
	 * 
	 * @DynamoDBRangeKey(attributeName ="AssociateName")
	 * //@DynamoDBAttribute(attributeName="AssociateName") public String
	 * getAssociateName() { return associateName; } public void
	 * setAssociateName(String assoicateName) { this.associateName = assoicateName;
	 * }
	 */
}
