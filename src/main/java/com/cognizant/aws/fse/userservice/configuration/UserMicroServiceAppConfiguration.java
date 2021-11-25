package com.cognizant.aws.fse.userservice.configuration;

import java.io.IOException;
import java.net.InetSocketAddress;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.jms.pool.PooledConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProviderClient;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Index;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.servicediscovery.AWSServiceDiscovery;
import com.amazonaws.services.servicediscovery.AWSServiceDiscoveryClientBuilder;
import com.amazonaws.util.StringUtils;

import net.spy.memcached.MemcachedClient;

@Configuration
@EnableJms
@EnableFeignClients(basePackages="com.cognizant.aws.fse.userservice.service")
//@EnableDynamoDBRepositories
//(basePackages = "com.cognizant.aws.fse.userservice.dao")
public class UserMicroServiceAppConfiguration {

	@Value("${amazon.dynamodb.endpoint}")
	private String amazonDynamoDBEndpoint;

	@Value("${amazon.aws.accesskey}")
	private String amazonAWSAccessKey;

	@Value("${amazon.aws.secretkey}")
	private String amazonAWSSecretKey;

	@Value("${spring.activemq.host}")
	private String WIRE_LEVEL_ENDPOINT;

	@Value("${spring.activemq.port}")
	private String WIRE_LEVEL_ENDPOINT_PORT;

	@Value("${spring.activemq.username}")
	private String ACTIVE_MQ_USERNAME;

	@Value("${spring.activemq.password}")
	private String ACTIVE_MQ_PASSWORD;

	@Value("${spring.memchae.endpoint}")
	private String MEMCACHE_END_POINT;

	@Value("${spring.memchae.port}")
	private int MEMCACHE_PORT;
	@Bean
	public AmazonDynamoDB amazonDynamoDB() {
		//AmazonDynamoDB amazonDynamoDB 
		//= new AmazonDynamoDBClient(amazonAWSCredentials()).withRegion(Regions.US_EAST_2);
		AmazonDynamoDB amazonDynamoDB  = AmazonDynamoDBClientBuilder.standard().build();
		if (!StringUtils.isNullOrEmpty(amazonDynamoDBEndpoint)) {
			amazonDynamoDB.setEndpoint(amazonDynamoDBEndpoint);
		}

		return amazonDynamoDB;
	}

	@Bean
	public DynamoDBMapper dynamoDBmapper() {
		DynamoDBMapper dynamoDBmapper = new DynamoDBMapper(amazonDynamoDB());
		System.out.println("dynamoDBmapper>>>>>>"+dynamoDBmapper);
		return dynamoDBmapper;
	}

	@Bean
	public Index index () {
		DynamoDB dynamoDB = new DynamoDB(amazonDynamoDB());
		Table table = dynamoDB.getTable("FSEProfile");
		Index index = table.getIndex("GSI-search-index4");

		return index;
	}

	@Bean
	public AWSCredentials amazonAWSCredentials() {
		return new BasicAWSCredentials(
				amazonAWSAccessKey, amazonAWSSecretKey);
	}


	@Bean
	public ActiveMQConnectionFactory activeMQConnectionFactory() {
		ActiveMQConnectionFactory connectionFactory = null;
		if(!WIRE_LEVEL_ENDPOINT_PORT.isEmpty()) {
			connectionFactory =
					new ActiveMQConnectionFactory(WIRE_LEVEL_ENDPOINT+":"+WIRE_LEVEL_ENDPOINT_PORT);
		} else {
			connectionFactory =
					new ActiveMQConnectionFactory(WIRE_LEVEL_ENDPOINT);
		}
		// Pass the username and password.
		connectionFactory.setUserName(ACTIVE_MQ_USERNAME);
		connectionFactory.setPassword(ACTIVE_MQ_PASSWORD);

		return connectionFactory;

	}

	@Bean
	public PooledConnectionFactory pooledActiveMQConnectionFactory() {
		final ActiveMQConnectionFactory connectionFactory =
				activeMQConnectionFactory();
		final PooledConnectionFactory pooledConnectionFactory =
				new PooledConnectionFactory();
		pooledConnectionFactory.setConnectionFactory(connectionFactory);
		pooledConnectionFactory.setMaxConnections(10);
		return pooledConnectionFactory;

	}

	@Bean
	public DefaultJmsListenerContainerFactory jmsListenerContainerFactory() {
		DefaultJmsListenerContainerFactory factory =new DefaultJmsListenerContainerFactory();
		factory.setConnectionFactory(activeMQConnectionFactory());
		return factory;
	}

	@Bean
	public AWSServiceDiscovery aWSServiceDiscovery () {
		AWSServiceDiscovery client = AWSServiceDiscoveryClientBuilder
				.standard()
				.withRegion("us-east-2")
				.build();
		return client;
	}

	@Bean
	public AWSCognitoIdentityProvider awsCognitoIDPClient() {
		AWSCognitoIdentityProvider awsCognitoIDPClient=null;

		try{
			awsCognitoIDPClient = new AWSCognitoIdentityProviderClient(amazonAWSCredentials()).withRegion(Regions.US_EAST_2);
		}catch(Exception e){
			e.printStackTrace();
		}
		return awsCognitoIDPClient;
	}


	//@Bean
	public MemcachedClient memcachedClient() throws IOException {
		MemcachedClient client;
		try {
			client = new MemcachedClient(
					new InetSocketAddress(MEMCACHE_END_POINT, 
							MEMCACHE_PORT));
			return client;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw e;
		} 

	}

}
