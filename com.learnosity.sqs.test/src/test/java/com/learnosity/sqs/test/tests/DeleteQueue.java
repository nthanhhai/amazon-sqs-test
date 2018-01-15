package com.learnosity.sqs.test.tests;

import com.learnosity.sqs.test.utils.BaseTest;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.sqs.model.CreateQueueRequest;
import com.amazonaws.services.sqs.model.DeleteQueueRequest;

public class DeleteQueue extends BaseTest {
	
	public void DeleteQueueCoreTest() throws Exception {
		String MyQueue = RandomStringGenerator(10) + "_MyQueue";
		
        System.out.println("===========================================");
        System.out.println("Testing DeleteQueue() Amazon SQS");
        System.out.println("===========================================\n");

        try {
            // Create a queue
            System.out.println("Creating a new SQS queue called " + MyQueue + ".\n");
            CreateQueueRequest createQueueRequest = new CreateQueueRequest(MyQueue);
            String myQueueUrl = sqsClient.createQueue(createQueueRequest).getQueueUrl();

            // Delete a queue
            System.out.println("Deleting the test queue.\n");
            sqsClient.deleteQueue(new DeleteQueueRequest(myQueueUrl));
            
            // Check if the queue is deleted
            
        } catch (AmazonServiceException ase) {
            System.out.println("Caught an AmazonServiceException, which means your request made it " +
                    "to Amazon SQS, but was rejected with an error response for some reason.");
            System.out.println("Error Message:    " + ase.getMessage());
            System.out.println("HTTP Status Code: " + ase.getStatusCode());
            System.out.println("AWS Error Code:   " + ase.getErrorCode());
            System.out.println("Error Type:       " + ase.getErrorType());
            System.out.println("Request ID:       " + ase.getRequestId());
            Assert.fail("Amazon SQS rejected the request. " + ase.getMessage());
        } catch (AmazonClientException ace) {
            System.out.println("Caught an AmazonClientException, which means the client encountered " +
                    "a serious internal problem while trying to communicate with SQS, such as not " +
                    "being able to access the network.");
            System.out.println("Error Message: " + ace.getMessage());
            Assert.fail("Serious internal problem encountered. " + ace.getMessage());
        }		
	}
	
	@Test
	public void DeleteQueueWithInvalidQueueUrl() throws Exception {
		String myQueue = RandomStringGenerator(20) + "_MyQueue";
		
        System.out.println("===========================================================");
        System.out.println("Testing DeleteQueue() with invalid QueueUrl Amazon SQS");
        System.out.println("===========================================================\n");

        try {
            // Delete a queue
            System.out.println("Deleting queue with invalid QueueUrl.\n");
            sqsClient.deleteQueue(new DeleteQueueRequest(myQueue));
        } catch (AmazonServiceException ase) {
            System.out.println("Caught an AmazonServiceException, which means your request made it " +
                    "to Amazon SQS, but was rejected with an error response for some reason.");
            System.out.println("Error Message:    " + ase.getMessage());
            System.out.println("HTTP Status Code: " + ase.getStatusCode());
            System.out.println("AWS Error Code:   " + ase.getErrorCode());
            System.out.println("Error Type:       " + ase.getErrorType());
            System.out.println("Request ID:       " + ase.getRequestId());
            Assert.fail("Amazon SQS rejected the request. " + ase.getMessage());
        } catch (AmazonClientException ace) {
            System.out.println("Caught an AmazonClientException, which means the client encountered " +
                    "a serious internal problem while trying to communicate with SQS, such as not " +
                    "being able to access the network.");
            System.out.println("Error Message: " + ace.getMessage());
            Assert.fail("Serious internal problem encountered. " + ace.getMessage());
        }			
	}
}
