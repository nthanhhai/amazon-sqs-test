package com.learnosity.sqs.test.utils;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.sqs.*;
import org.testng.annotations.*;

import static java.lang.String.format;

import java.util.Collection;


public class BaseTest {
    private static final int ELASTICMQ_PORT = 9324;
	private static final String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    protected AmazonSQS sqsClient;
    
    @BeforeTest
    protected AmazonSQS createDefaultSQSClient() {
        sqsClient = new AmazonSQSClient(new BasicAWSCredentials("1234", "1234")); // TODO: need to update AmazonSQSClient
        sqsClient.setEndpoint(format("http://localhost:%s", ELASTICMQ_PORT)); // TODO: need to update setEndpoint()        
        return sqsClient;
    }
    
    @AfterTest
    public void afterTest() {
    	sqsClient.shutdown(); 	
    }
    
    
    /*
     * This function will return a random string based on Alphanumeric
     * Parameters:
     * 		- Input:
     * 			+ int Length: length of expected string
     * 		- Output:
     *  		+ String: randomised string
     */
    public static String RandomStringGenerator(int Length) {
    	StringBuilder builder = new StringBuilder();
    	while (Length-- != 0) {
	    	int character = (int)(Math.random()*ALPHA_NUMERIC_STRING.length());
	    	builder.append(ALPHA_NUMERIC_STRING.charAt(character));
    	}    	
    	return builder.toString();
	}    
    
    /*
     * This function will return expected QueueUrl with
     * Parameters:
     * 		- Input:
     * 			+ String QueueName: name of queue
     * 		- Output:
     * 			+ String: URL of queue
     */
    public static String expectedQueueUrl(String QueueName) {
    	return format("http://localhost:%s/queue/%s", ELASTICMQ_PORT, QueueName);
    }
    
    public static Collection<String> ListQueuesWithPrefix(AmazonSQSClient client, String prefix) {
    	return client.listQueues().getQueueUrls();
    }
}