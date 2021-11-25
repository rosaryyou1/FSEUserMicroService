package com.cognizant.aws.fse.userservice.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.cognizant.aws.fse.userservice.Json.domain.Skill;
import com.cognizant.aws.fse.userservice.Json.domain.UserJsonModel;
import com.cognizant.aws.fse.userservice.model.User;

public class UserModelJsonConverter {
	public static User convertUserJsonModel(UserJsonModel userJson) {
		User user = new User();
		user.setAssociateId(userJson.getAssociateId());
		user.setAssociateName(userJson.getAssociateName());
		user.setEmail(userJson.getEmail());
		user.setMobile(userJson.getMobile());
		List<Skill> lstSkill = userJson.getSkills();
		for(Skill skill : lstSkill) {
			if("Angular".equalsIgnoreCase(skill.getSkillName())){
				user.setAngular(skill.getSkillName());
				user.setAngularLevel(skill.getSkillLevel());
				continue;
			}
			if("Aptitude".equalsIgnoreCase(skill.getSkillName())){
				user.setAptitude(skill.getSkillName());
				user.setAptitudeLevel(skill.getSkillLevel());
				continue;
			}
			if("AWS".equalsIgnoreCase(skill.getSkillName())){
				user.setAws(skill.getSkillName());
				user.setAwsLevel(skill.getSkillLevel());
				continue;
			}
			if("Communication".equalsIgnoreCase(skill.getSkillName())){
				user.setCommunication(skill.getSkillName());
				user.setCommunicationLevel(skill.getSkillLevel());
				continue;
			}
			if("Docker".equalsIgnoreCase(skill.getSkillName())){
				user.setDocker(skill.getSkillName());
				user.setDockerLevel(skill.getSkillLevel());
				continue;
			}

			if("Git".equalsIgnoreCase(skill.getSkillName())){
				user.setGit(skill.getSkillName());
				user.setGitLevel(skill.getSkillLevel());
				continue;
			}
			if("Hibernate".equalsIgnoreCase(skill.getSkillName())){
				user.setHibernate(skill.getSkillName());
				user.setHibernateLevel(skill.getSkillLevel());
				continue;
			}
			if("HtmlCss".equalsIgnoreCase(skill.getSkillName())){
				user.setHtmlCss(skill.getSkillName());
				user.setHtmlCssLevel(skill.getSkillLevel());
				continue;
			}
			if("Jenkins".equalsIgnoreCase(skill.getSkillName())){
				user.setJenkins(skill.getSkillName());
				user.setJenkinsLevel(skill.getSkillLevel());
				continue;
			}
			if("React".equalsIgnoreCase(skill.getSkillName())){
				user.setReact(skill.getSkillName());
				user.setReactLevel(skill.getSkillLevel());
				continue;
			}

			if("Rest".equalsIgnoreCase(skill.getSkillName())){
				user.setRest(skill.getSkillName());
				user.setRestLevel(skill.getSkillLevel());
				continue;
			}
			if("Spoken".equalsIgnoreCase(skill.getSkillName())){
				user.setSpoken(skill.getSkillName());
				user.setSpokenLevel(skill.getSkillLevel());
				continue;
			}
			if("Spring".equalsIgnoreCase(skill.getSkillName())){
				user.setSpring(skill.getSkillName());
				user.setSpringLevel(skill.getSkillLevel());
				continue;
			}
		}
		return user;
	}

	/*public static UserJsonModel convertUser(User user) {
		UserJsonModel userJsonModel = new UserJsonModel();
		userJsonModel.setUserId(user.getUserId());
		userJsonModel.setAssociateId(user.getAssociateId());
		userJsonModel.setAssociateName(user.getAssociateName());
		userJsonModel.setEmail(user.getEmail());
		userJsonModel.setMobile(user.getMobile());
		List<Skill> lstSkill = new ArrayList<>();
		Skill angularSkill = new Skill();
		Skill aptitudeSkill = new Skill();
		Skill awsSkill = new Skill();
		Skill communicationSkill = new Skill();
		Skill dockerSkill = new Skill();
		Skill gitSkill = new Skill();
		Skill hibernateSkill = new Skill();
		Skill htmlCssSkill = new Skill();
		Skill jenkinsSkill = new Skill();
		Skill reactSkill = new Skill();
		Skill restSkill = new Skill();
		Skill spokenSkill = new Skill();
		Skill springSkill = new Skill();

		angularSkill.setSkillLevel(user.getAngularLevel());
		angularSkill.setSkillName(user.getAngular());
		angularSkill.setSkillType("Tech");
		aptitudeSkill.setSkillLevel(user.getAptitudeLevel());
		aptitudeSkill.setSkillName(user.getAptitude());
		aptitudeSkill.setSkillType("Non-Tech");
		awsSkill.setSkillLevel(user.getAwsLevel());
		awsSkill.setSkillName(user.getAws());
		awsSkill.setSkillType("Tech");
		communicationSkill.setSkillLevel(user.getCommunicationLevel());
		communicationSkill.setSkillName(user.getCommunication());
		communicationSkill.setSkillType("Non-Tech");
		dockerSkill.setSkillLevel(user.getDockerLevel());
		dockerSkill.setSkillName(user.getDocker());
		dockerSkill.setSkillType("Tech");
		gitSkill.setSkillLevel(user.getGitLevel());
		gitSkill.setSkillName(user.getGit());
		gitSkill.setSkillType("Tech");
		hibernateSkill.setSkillLevel(user.getHibernateLevel());
		hibernateSkill.setSkillName(user.getHibernate());
		hibernateSkill.setSkillType("Tech");
		htmlCssSkill.setSkillLevel(user.getHtmlCssLevel());
		htmlCssSkill.setSkillName(user.getHtmlCss());
		htmlCssSkill.setSkillType("Tech");
		jenkinsSkill.setSkillLevel(user.getJenkinsLevel());
		jenkinsSkill.setSkillName(user.getJenkins());
		jenkinsSkill.setSkillType("Tech");
		reactSkill.setSkillLevel(user.getReactLevel());
		reactSkill.setSkillName(user.getReact());
		reactSkill.setSkillType("Tech");
		restSkill.setSkillLevel(user.getRestLevel());
		restSkill.setSkillName(user.getRest());
		restSkill.setSkillType("Tech");
		spokenSkill.setSkillLevel(user.getSpokenLevel());
		spokenSkill.setSkillName(user.getSpoken());
		spokenSkill.setSkillType("Non-Tech");
		springSkill.setSkillLevel(user.getSpringLevel());
		springSkill.setSkillName(user.getSpring());
		springSkill.setSkillType("Tech");

		lstSkill.add(angularSkill);
		lstSkill.add(aptitudeSkill);
		lstSkill.add(awsSkill);
		lstSkill.add(communicationSkill);
		lstSkill.add(dockerSkill);
		lstSkill.add(gitSkill);
		lstSkill.add(hibernateSkill);
		lstSkill.add(htmlCssSkill);
		lstSkill.add(jenkinsSkill);
		lstSkill.add(reactSkill);
		lstSkill.add(restSkill);
		lstSkill.add(spokenSkill);
		lstSkill.add(springSkill);

		userJsonModel.setSkills(lstSkill);

		return userJsonModel;
	}*/

	public static void mapSkills(User user, Map<String, Integer> mapSkills) {
		for(Entry<String, Integer> entry : mapSkills.entrySet()) {
			if("Angular".equalsIgnoreCase(entry.getKey())) {
				user.setAngularLevel(entry.getValue());
			}
			else if("Aptitude".equalsIgnoreCase(entry.getKey())){
				user.setAptitudeLevel(entry.getValue());
			}  else if("AWS".equalsIgnoreCase(entry.getKey())){
				user.setAwsLevel(entry.getValue());
			} else if("Communication".equalsIgnoreCase(entry.getKey())){
				user.setCommunicationLevel(entry.getValue());
			} else if("Docker".equalsIgnoreCase(entry.getKey())){
				user.setDockerLevel(entry.getValue());
			} else if("Git".equalsIgnoreCase(entry.getKey())){
				user.setGitLevel(entry.getValue());
			} else if("Hibernate".equalsIgnoreCase(entry.getKey())){
				user.setHibernateLevel(entry.getValue());
			} else if("Jenkins".equalsIgnoreCase(entry.getKey())){
				user.setJenkinsLevel(entry.getValue());
			} else if("React".equalsIgnoreCase(entry.getKey())){
				user.setReactLevel(entry.getValue());
			} else if("Rest".equalsIgnoreCase(entry.getKey())){
				user.setRestLevel(entry.getValue());
			} else if("HtmlCss".equalsIgnoreCase(entry.getKey())){
				user.setHtmlCssLevel(entry.getValue());
			} else if("Spoken".equalsIgnoreCase(entry.getKey())){
				user.setSpokenLevel(entry.getValue());
			} else if("Spring".equalsIgnoreCase(entry.getKey())){
				user.setSpringLevel(entry.getValue());
			}

		}


	}
}
