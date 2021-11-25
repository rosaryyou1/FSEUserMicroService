package com.cognizant.aws.fse.userservice.service;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amazonaws.services.servicediscovery.AWSServiceDiscovery;
import com.amazonaws.services.servicediscovery.model.DiscoverInstancesRequest;
import com.amazonaws.services.servicediscovery.model.DiscoverInstancesResult;
import com.amazonaws.services.servicediscovery.model.HttpInstanceSummary;

@Service
public class UserProfileProcessorSrvLocator implements ServiceLocationResolver{
	@Autowired
	AWSServiceDiscovery aWSServiceDiscovery;
	
	  private static final String AWS_INSTANCE_IPV_4_ATTRIBUTE = "AWS_INSTANCE_IPV4";
     private static final String AWS_INSTANCE_PORT_ATTRIBUTE = "AWS_INSTANCE_PORT";

     private static final Random RAND = new Random(System.currentTimeMillis());

	@Override
	public String resolve() {
	   	DiscoverInstancesRequest request = new DiscoverInstancesRequest();
        request.setNamespaceName("fseprofileprocessorsrvns");
        request.setServiceName("fseprofileprocesssrv");
        DiscoverInstancesResult discoverInstancesResult=aWSServiceDiscovery.discoverInstances(request);
        final HttpInstanceSummary result = discoverInstancesResult.getInstances().get(RAND.nextInt(discoverInstancesResult.getInstances().size()));
        final String serviceLocation = result.getAttributes().get(AWS_INSTANCE_IPV_4_ATTRIBUTE) + ":" + result.getAttributes().get(AWS_INSTANCE_PORT_ATTRIBUTE);
        return serviceLocation;
	}

}
