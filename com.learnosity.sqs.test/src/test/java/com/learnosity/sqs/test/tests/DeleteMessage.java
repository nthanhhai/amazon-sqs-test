package com.learnosity.sqs.test.tests;

import com.learnosity.sqs.test.utils.BaseTest;

import java.util.List;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.sqs.model.CreateQueueRequest;
import com.amazonaws.services.sqs.model.DeleteMessageRequest;
import com.amazonaws.services.sqs.model.DeleteQueueRequest;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageRequest;

public class DeleteMessage extends BaseTest {
	@Test
	public void DeleteMessageCoreTest() throws Exception {		
		String MyQueue = RandomStringGenerator(10) + "_MyQueue";

        System.out.println("===========================================");
        System.out.println("Testing DeleteMessage() Amazon SQS");
        System.out.println("===========================================\n");

        try {
            // Create a queue
            System.out.println("Creating a new SQS queue called " + MyQueue + ".\n");
            CreateQueueRequest createQueueRequest = new CreateQueueRequest(MyQueue);
            String myQueueUrl = sqsClient.createQueue(createQueueRequest).getQueueUrl();

            // Send a message
            System.out.println("Sending a message to " + MyQueue + ".\n");
            sqsClient.sendMessage(new SendMessageRequest(myQueueUrl, "This is my message text."));
          
            System.out.println("Receiving messages from " + MyQueue + ".\n");
            ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(myQueueUrl);
            List<Message> messages = sqsClient.receiveMessage(receiveMessageRequest).getMessages(); 
            
            // Delete a message
            System.out.println("Deleting a message.\n");
            String messageReceiptHandle = messages.get(0).getReceiptHandle();
            sqsClient.deleteMessage(new DeleteMessageRequest(myQueueUrl, messageReceiptHandle));            
            
            // TODO: Check whether the message is deleted in queue
            // should find another API to check for a specified message
            // currently accepting that the API doesn't return exception then the method is executed successfully
            // Get all messages in queue, expecting no other message in queue
            System.out.println("Receiving messages from " + MyQueue + ".\n");
            List<Message> remaingingMessages = sqsClient.receiveMessage(new ReceiveMessageRequest(myQueueUrl)).getMessages();            
            if (!remaingingMessages.isEmpty())
            	Assert.fail("Unable to delete message");
            
            // Delete queue
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
	

	public void DeleteMessageWithoutMessageHadle() throws Exception {
		throw new Exception ("Not implemented");
	}
	
	public void DeleteMessageWithoutQueue() throws Exception {
		throw new Exception ("Not implemented");
	}
	
	public void DeleteMessageWithInvalidQueue() throws Exception {
		throw new Exception ("Not implemented");		
	}
	
	public void DeleteMessageWithInvalidMessageHanle() throws Exception {
		throw new Exception ("Not implemented");
	}
	
	public void DeleteMessageWithInvalidMessageHanleFormat() throws Exception {
		throw new Exception ("Not implemented");
	}
}
