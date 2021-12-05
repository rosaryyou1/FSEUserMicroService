package com.cognizant.aws.fse.userservice.Json.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;

@JsonIgnoreProperties(ignoreUnknown =  true)
public class Skill {
	@ApiModelProperty(notes = "Skills Name values",name="Skill",required=true,value="{Angular,AWS,Spoken}")
	@JsonProperty("Skill")
	String skillName;
	@ApiModelProperty(notes = "Skills Type",name="Type",required=false,value="{Tech,Non-Tech}")
	@JsonProperty("Type")
	String skillType;
	@ApiModelProperty(notes = "Skills Level >0 and <=20",name="Level",required=false,value="Skills Level >0 and <=20")
	@JsonProperty("Level")
	int skillLevel;
	
	public String getSkillName() {
		return skillName;
	}
	public void setSkillName(String skillName) {
		this.skillName = skillName;
	}
	public String getSkillType() {
		return skillType;
	}
	public void setSkillType(String skillType) {
		this.skillType = skillType;
	}
	public int getSkillLevel() {
		return skillLevel;
	}
	public void setSkillLevel(int skillLevel) {
		this.skillLevel = skillLevel;
	}
	
}
