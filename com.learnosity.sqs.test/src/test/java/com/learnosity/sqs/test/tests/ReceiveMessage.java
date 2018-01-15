package com.learnosity.sqs.test.tests;

import com.learnosity.sqs.test.utils.BaseTest;

import java.util.List;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.sqs.model.CreateQueueRequest;
import com.amazonaws.services.sqs.model.DeleteQueueRequest;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageRequest;

public class ReceiveMessage extends BaseTest {

	@Test
	public void ReceiveMessageCoreTest() {
		String MyQueue = RandomStringGenerator(20) + "_MyQueue";
		String MyMessageBody = "This is my message text.";
		
	    System.out.println("===========================================");
	    System.out.println("Testing ReceiveMessage() Amazon SQS");
	    System.out.println("===========================================\n");

        try {
            // Create a queue
            System.out.println("Creating a new SQS queue called " + MyQueue + ".\n");
            CreateQueueRequest createQueueRequest = new CreateQueueRequest(MyQueue);
            String myQueueUrl = sqsClient.createQueue(createQueueRequest).getQueueUrl();

            // Send a message
            System.out.println("Sending a message to " + MyQueue + ".\n");
            sqsClient.sendMessage(new SendMessageRequest(myQueueUrl, MyMessageBody));

            // Receive messages
            System.out.println("Receiving messages from " + MyQueue + ".\n");
            ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(myQueueUrl);
            List<Message> messages = sqsClient.receiveMessage(receiveMessageRequest).getMessages();
            
            // Check whether the message is delivered in queue. Should have only 1 message
            Assert.assertEquals(messages.get(0).getBody(), MyMessageBody);

            System.out.println();

            // Delete a queue
            System.out.println("Deleting the test queue.\n");
            sqsClient.deleteQueue(new DeleteQueueRequest(myQueueUrl));
            
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
	
	
	public void ReceiveMostRecentMessages() throws Exception {
		throw new Exception ("Not implemneted");
	}
	
	
	public void ReceiveMessageWithInvalidQueue() throws Exception {
		throw new Exception ("Not implemeneted");
	}
	
	public void ReceiveMessageWithVisibilityTimeoutMultiple() throws Exception {
		throw new Exception ("Not implemented");
	}
	 
	@Test
	public void ReceiveMessageWithVisibilityTimeoutSingle() throws Exception {
		String MyQueue = RandomStringGenerator(20) + "_MyQueue";
		String MyMessageBody = "This is my message text.";
		int timeout = 10000;
		
	    System.out.println("===========================================");
	    System.out.println("Testing ReceiveMessage() Amazon SQS");
	    System.out.println("===========================================\n");

        try {
            // Create a queue
            System.out.println("Creating a new SQS queue called " + MyQueue + ".\n");
            CreateQueueRequest createQueueRequest = new CreateQueueRequest(MyQueue);
            String myQueueUrl = sqsClient.createQueue(createQueueRequest).getQueueUrl();

            // Send a message
            System.out.println("Sending a message to " + MyQueue + ".\n");
            sqsClient.sendMessage(new SendMessageRequest(myQueueUrl, MyMessageBody));

            // Receive messages
            System.out.println("Receiving messages from " + MyQueue + ".\n");
            ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(myQueueUrl);
            String receipt = sqsClient.receiveMessage(receiveMessageRequest).getMessages().get(0).getReceiptHandle();
            
            // set VisibilityTimeout
            sqsClient.changeMessageVisibility(myQueueUrl, receipt, timeout);
            
            // attempt to receive message within 10 seconds
            System.out.println("Attempt to receive message within 10 seconds");
            ReceiveMessageRequest newReceiveMessageRequest = new ReceiveMessageRequest(myQueueUrl);
            List<Message> messages = sqsClient.receiveMessage(newReceiveMessageRequest).getMessages();
            
            if (!messages.isEmpty())
            	throw new Exception ("Message is still visible");
            
            System.out.println("nothing received");

            // Delete a queue
            System.out.println("Deleting the test queue.\n");
            sqsClient.deleteQueue(new DeleteQueueRequest(myQueueUrl));
            
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
	
	 
	public void ReceiveMEssageWithWaitTimeSeconds() throws Exception {
		throw new Exception ("Not implemented");
	}
	
	 
	public void ReceiveMessageAttributeAll() throws Exception {
		throw new Exception ("Not implemented");
	}
	
	
	public void ReceiveMessageAttributeApproximateFirstReceiveTimestamp () throws Exception {
		throw new Exception ("Not implemented");
	}
	
	public void ReceiveMessageAttributeApproximateReceiveCount () throws Exception {
		throw new Exception ("Not implemented");
	}
	
	public void ReceiveMessageAttributeSenderId () throws Exception {
		throw new Exception ("Not implemented");
	}
	
	public void ReceiveMessageAttributeSentTimestamp () throws Exception {
		throw new Exception ("Not implemented");
	}
	
	public void ReceiveMessageAttributeMessageDeduplicationId () throws Exception {
		throw new Exception ("Not implemented");
	}
	
	public void ReceiveMessageAttributeMessageGroupId () throws Exception {
		throw new Exception ("Not implemented");
	}
	
	public void ReceiveMessageAttributeSequenceNumber () throws Exception {
		throw new Exception ("Not implemneted");
	}
	
	public void ReceiveMessageMaxNumberOfMessages () throws Exception {
		throw new Exception ("Not implemeneted");
	}
	
	public void ReceiveMessgeReceiveRequestAttemptId () throws Exception {
		throw new Exception ("Not implemneted");
	}
	
}
