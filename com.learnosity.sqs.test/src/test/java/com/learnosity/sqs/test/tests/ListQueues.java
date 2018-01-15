package com.learnosity.sqs.test.tests;

import com.learnosity.sqs.test.utils.BaseTest;

import java.util.List;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.sqs.model.CreateQueueRequest;
import com.amazonaws.services.sqs.model.DeleteQueueRequest;

public class ListQueues extends BaseTest {
	private static String _randomString = RandomStringGenerator(20);
	
	@Test
	public void ListQueuesCoreTest() throws Exception {
		String MyQueue = _randomString + "_MyQueue";
		List<String> tempQueueName;
		
        System.out.println("===========================================");
        System.out.println("Testing ListQueues() Amazon SQS");
        System.out.println("===========================================\n");

        try {
            // Create a queue
            System.out.println("Creating a new SQS queue called " + MyQueue + ".\n");
            CreateQueueRequest createQueueRequest = new CreateQueueRequest(MyQueue);
            String myQueueUrl = sqsClient.createQueue(createQueueRequest).getQueueUrl();

            // List queues
            System.out.println("Listing all queues in your account.\n");
            for (String queueUrl : sqsClient.listQueues().getQueueUrls()) {
                System.out.println("  QueueUrl: " + queueUrl);
            }
            System.out.println();

            // Delete a queue
            System.out.println("Deleting the test queue.\n");
            sqsClient.deleteQueue(new DeleteQueueRequest(myQueueUrl));
            
            // check whether the queue is deleted
            tempQueueName = sqsClient.listQueues(MyQueue).getQueueUrls();
            if (!tempQueueName.isEmpty())
            	Assert.fail("Fail to delete queue");
        } catch (AmazonServiceException ase) {
            System.out.println("Caught an AmazonServiceException, which means your request made it " +
                    "to Amazon SQS, but was rejected with an error response for some reason.");
            System.out.println("Error Message:    " + ase.getMessage());
            System.out.println("HTTP Status Code: " + ase.getStatusCode());
            System.out.println("AWS Error Code:   " + ase.getErrorCode());
            System.out.println("Error Type:       " + ase.getErrorType());
            System.out.println("Request ID:       " + ase.getRequestId());
        } catch (AmazonClientException ace) {
            System.out.println("Caught an AmazonClientException, which means the client encountered " +
                    "a serious internal problem while trying to communicate with SQS, such as not " +
                    "being able to access the network.");
            System.out.println("Error Message: " + ace.getMessage());
        }		
	}
	
	
	public void ListQueuesWithQueueNamePrefix() throws Exception { // this test is not necessary as it is covered in first test
		throw new Exception ("Not implemeneted");
	}
	
}
