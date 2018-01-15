package com.learnosity.sqs.test.tests;

import com.learnosity.sqs.test.utils.BaseTest;

import java.util.List;
import java.util.Map.Entry;

import org.testng.annotations.Test;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.sqs.model.CreateQueueRequest;
import com.amazonaws.services.sqs.model.DeleteMessageRequest;
import com.amazonaws.services.sqs.model.DeleteQueueRequest;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageRequest;

public class SendMessage extends BaseTest {
	
	@Test
	public void SendMessageCoreTest() {
		String MyQueue = RandomStringGenerator(10) + "_MyQueue";
		
	    System.out.println("===========================================");
	    System.out.println("Testing SendMessage() Amazon SQS");
	    System.out.println("===========================================\n");

        try {
            // Create a queue
            System.out.println("Creating a new SQS queue called " + MyQueue + ".\n");
            CreateQueueRequest createQueueRequest = new CreateQueueRequest(MyQueue);
            String myQueueUrl = sqsClient.createQueue(createQueueRequest).getQueueUrl();

            // Send a message
            System.out.println("Sending a message to " + MyQueue + ".\n");
            sqsClient.sendMessage(new SendMessageRequest(myQueueUrl, "This is my message text."));

            // Receive messages
            System.out.println("Receiving messages from " + MyQueue + ".\n");
            ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(myQueueUrl);
            List<Message> messages = sqsClient.receiveMessage(receiveMessageRequest).getMessages();
            for (Message message : messages) {
                System.out.println("  Message");
                System.out.println("    MessageId:     " + message.getMessageId());
                System.out.println("    ReceiptHandle: " + message.getReceiptHandle());
                System.out.println("    MD5OfBody:     " + message.getMD5OfBody());
                System.out.println("    Body:          " + message.getBody());
                for (Entry<String, String> entry : message.getAttributes().entrySet()) {
                    System.out.println("  Attribute");
                    System.out.println("    Name:  " + entry.getKey());
                    System.out.println("    Value: " + entry.getValue());
                }
            }
            System.out.println();

            // Delete a message
            System.out.println("Deleting a message.\n");
            String messageReceiptHandle = messages.get(0).getReceiptHandle();
            sqsClient.deleteMessage(new DeleteMessageRequest(myQueueUrl, messageReceiptHandle));

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
        } catch (AmazonClientException ace) {
            System.out.println("Caught an AmazonClientException, which means the client encountered " +
                    "a serious internal problem while trying to communicate with SQS, such as not " +
                    "being able to access the network.");
            System.out.println("Error Message: " + ace.getMessage());
        }	    
	    
	}
	
	public void SendMessageWithLongUnformattedText() throws Exception {
		throw new Exception ("Not implemeneted");
	}
	
	public void SendMessageWithoutQueue() throws Exception {
		throw new Exception ("Not implemeneted");
	}
	
	public void SendMessageJSON() throws Exception {
		throw new Exception ("Not implemented");
	}
	
	public void SendMessageXML() throws Exception {
		throw new Exception ("Not implemneted");
	}
	
	public void SendMessageUnicode() throws Exception {
		throw new Exception ("Not implemeneted");
	}
	
	public void SendMessageDelaySeconds() throws Exception {
		throw new Exception ("Not implemented");
	}
	
	public void SendMessgeWithAttributeString() throws Exception {
		throw new Exception ("Not implemented");
	}
	
	public void SendMessgeWithAttributeNumber() throws Exception {
		throw new Exception ("Not implemented");	
	}	
	
	public void SendMessgeWithAttributeBnary() throws Exception {
		throw new Exception ("Not implemented");
	}	
	
	public void SendMessageWithMessageDuplicationId() throws Exception {
		throw new Exception ("Not implemented");
	}

	public void SendMessageWithMessageMessageGroupId() throws Exception {
		throw new Exception ("Not implemented");
	}
	
}
