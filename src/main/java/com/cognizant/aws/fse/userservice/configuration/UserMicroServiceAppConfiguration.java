package com.cognizant.aws.fse.userservice.configuration;

import org.socialsignin.spring.data.dynamodb.repository.config.EnableDynamoDBRepositories;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverterFactory;
import com.amazonaws.util.StringUtils;

@Configuration
//@EnableDynamoDBRepositories
  //(basePackages = "com.cognizant.aws.fse.userservice.dao")
public class UserMicroServiceAppConfiguration {

    @Value("${amazon.dynamodb.endpoint}")
    private String amazonDynamoDBEndpoint;

    @Value("${amazon.aws.accesskey}")
    private String amazonAWSAccessKey;

    @Value("${amazon.aws.secretkey}")
    private String amazonAWSSecretKey;

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
    public AWSCredentials amazonAWSCredentials() {
        return new BasicAWSCredentials(
          amazonAWSAccessKey, amazonAWSSecretKey);
    }
    
 /*   @Bean
    public DynamoDBMapper dynamoDBMapper () {
    	DynamoDBMapper mapper = new DynamoDBMapper(amazonDynamoDB(), getDynamoDBMapperConfig());
    	return mapper;
    }*/
    
  /*  @Bean
    public DynamoDBMapperConfig getDynamoDBMapperConfig() {
        DynamoDBMapperConfig.Builder builder = new DynamoDBMapperConfig.Builder();
        builder.setTableNameOverride(null);
      //  builder.setTypeConverterFactory(DynamoDBTypeConverterFactory.standard());
        return builder.build();
    }*/
}
