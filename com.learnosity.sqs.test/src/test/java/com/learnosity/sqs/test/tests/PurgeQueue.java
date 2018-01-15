package com.learnosity.sqs.test.tests;

import com.learnosity.sqs.test.utils.BaseTest;

import java.util.List;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.sqs.model.CreateQueueRequest;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.PurgeQueueRequest;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageRequest;

public class PurgeQueue extends BaseTest {

	@Test
	public void PurgeQueueCoreTest() throws InterruptedException {
		String MyQueue = RandomStringGenerator(10) + "_MyQueue";
		int i = 0;

        System.out.println("===========================================");
        System.out.println("Testing PurgeQueue() Amazon SQS");
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

            // Populate queue with 10 messages
            System.out.println("Sending 10 messages to " + MyQueue + ".\n");
            while ( i < 10) {             	
	            sqsClient.sendMessage(new SendMessageRequest(myQueueUrl, "This is my message text  #" + i));
	            i += 1;
            }

            // Purge queue
            System.out.println("Purging the test queue.\n");
            sqsClient.purgeQueue((new PurgeQueueRequest(myQueueUrl)));
            
            // Sleep for 60 seconds
            Thread.sleep(60000);
            
            // ensuring all the messages were deleted
            System.out.println("Validing messages in purged queue");
            ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(myQueueUrl);
            List<Message> messages = sqsClient.receiveMessage(receiveMessageRequest).getMessages();            
            if (!messages.isEmpty())
            	Assert.fail("Messages are not deleted");
            System.out.println("Messages are deleted");
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
	public void PurgeNonExistingQueue() throws Exception {
		String MyQueue = RandomStringGenerator(10) + "_MyQueue";

        System.out.println("================================================");
        System.out.println("Testing PurgeQueue() of invalid queue Amazon SQS");
        System.out.println("================================================\n");	
        
        try {
        	sqsClient.purgeQueue((new PurgeQueueRequest(expectedQueueUrl(MyQueue))));        
        } catch (AmazonServiceException ase) { // this catch is supposed to catch AmazonServiceException dedicated for NonExistentQueue 	
        	if (ase.getErrorCode() != ("AWS.SimpleQueueService.NonExistentQueue")) {
        		Assert.fail("Wrong error message returned. Received: " + ase.getMessage());
        	}
		} catch (Exception ex) { // if return some other exception, fail the test
    		Assert.fail("Wrong error message returned. Received: " + ex.getMessage());
		}
	}
}
