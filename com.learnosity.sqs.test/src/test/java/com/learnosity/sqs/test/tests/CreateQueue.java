package com.learnosity.sqs.test.tests;

import com.learnosity.sqs.test.utils.BaseTest;

import java.util.HashMap;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.sqs.model.AmazonSQSException;
import com.amazonaws.services.sqs.model.CreateQueueRequest;
import com.amazonaws.services.sqs.model.DeleteQueueRequest;
import com.amazonaws.services.sqs.model.GetQueueAttributesRequest;
import com.amazonaws.services.sqs.model.GetQueueAttributesResult;
import com.amazonaws.services.sqs.model.SetQueueAttributesRequest;

public class CreateQueue extends BaseTest {
	@Test
    public void CreateQueueCoreTest () throws Exception {
		String MyQueue = RandomStringGenerator(10) + "_MyQueue";
		
        System.out.println("===========================================");
        System.out.println("Testing Statandard Queue - CreateQueue() Amazon SQS");
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
            
            Assert.assertEquals(myQueueUrl, expectedQueueUrl(MyQueue));

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
	
	// This test is reported with error when doing testing with ElasticMQ. Needs to test against AWS
    // TODO: retest this test with AWS
	@Test
    public void CreateQueueCoreTestFIFO () throws Exception {
		String MyQueue = RandomStringGenerator(10) + "_MyQueue.fifo";
		
        System.out.println("===========================================");
        System.out.println("Testing FIFO Queue - CreateQueue() Amazon SQS");
        System.out.println("===========================================\n");

        try {
            // Create a FIFO queue
            System.out.println("Creating a new SQS queue called " + MyQueue + ".\n");
            //Map<String, String> attributes = new HashMap<String, String>();
            //attributes.put("FifoQueue", "true");
            //CreateQueueRequest createQueueRequest = new CreateQueueRequest(MyQueue).withAttributes(attributes);
            CreateQueueRequest createQueueRequest = new CreateQueueRequest(MyQueue).addAttributesEntry("FifoQueue", "true");
            System.out.println("DEBUG: Create Queue Request: " + createQueueRequest);
            String myQueueUrl = sqsClient.createQueue(createQueueRequest).getQueueUrl();

            // List queues
            System.out.println("Listing all queues in your account.\n");
            for (String queueUrl : sqsClient.listQueues().getQueueUrls()) {
                System.out.println("  QueueUrl: " + queueUrl);
            }
            System.out.println();
            
            Assert.assertEquals(myQueueUrl, expectedQueueUrl(MyQueue));

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
	public void CreateQueueEmptyQueueName() throws Exception {
		throw new Exception ("Not implemented");
	}
	
	@Test
	public void CreateQueueWithAttributeRedrivePolicy() throws Exception {
		String MyQueue = RandomStringGenerator(10) + "_MyQueue";
		String MyDLQueue = MyQueue + "_DL";
		
        System.out.println("===========================================================");
        System.out.println("Testing Queue - CreateQueue() with RedrivePolicy Amazon SQS");
        System.out.println("===========================================================\n");

        try {
            // Create a Source queue
            System.out.println("Creating a new source SQS queue called " + MyQueue + ".\n");
            CreateQueueRequest createSrcQueueRequest = new CreateQueueRequest(MyQueue);
            String mySrcQueueUrl = sqsClient.createQueue(createSrcQueueRequest).getQueueUrl();
            
            System.out.println("Validating Source Queue URL");
            Assert.assertEquals(mySrcQueueUrl, expectedQueueUrl(MyQueue));

            // Create a DL queue
            CreateQueueRequest createDLQueueRequest = new CreateQueueRequest(MyDLQueue);
            String myDLQueueUrl = sqsClient.createQueue(createDLQueueRequest).getQueueUrl();
                      
            System.out.println("Validating DL Queue URL");
            Assert.assertEquals(myDLQueueUrl, expectedQueueUrl(MyDLQueue));
                        
            GetQueueAttributesResult attributes = sqsClient.getQueueAttributes(
                    new GetQueueAttributesRequest(myDLQueueUrl)
                        .withAttributeNames("QueueArn"));
            
            String DLQueueARN = attributes.getAttributes().get("QueueArn");
            
            SetQueueAttributesRequest request = new SetQueueAttributesRequest()
                    .withQueueUrl(mySrcQueueUrl)
                    .addAttributesEntry("RedrivePolicy",
                            "{\"maxReceiveCount\":\"5\", \"deadLetterTargetArn\":\""
                            + DLQueueARN + "\"}");

            sqsClient.setQueueAttributes(request);            
            
            // Delete queues
            System.out.println("Deleting the test queues.\n");
            sqsClient.deleteQueue(new DeleteQueueRequest(mySrcQueueUrl));
            sqsClient.deleteQueue(new DeleteQueueRequest(myDLQueueUrl));            
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
	public void CreateQueueWithDuplicatedName() throws Exception {
		String MyQueue = RandomStringGenerator(10) + "_MyQueue";
		
        System.out.println("===========================================================");
        System.out.println("Testing Queue - CreateQueue() duplicated name Amazon SQS");
        System.out.println("===========================================================\n");

        try {
            // Create a Source queue
            System.out.println("Creating a new SQS queue called " + MyQueue + ".\n");
            CreateQueueRequest createQueueRequest = new CreateQueueRequest(MyQueue);
            String myQueueUrl = sqsClient.createQueue(createQueueRequest).getQueueUrl();
            
            System.out.println("Validating Queue URL");
            Assert.assertEquals(myQueueUrl, expectedQueueUrl(MyQueue));

            System.out.println("Creating duplicated queue");
            try {
	            CreateQueueRequest createDuplicatedQueueRequest = new CreateQueueRequest(MyQueue);
	            sqsClient.createQueue(createDuplicatedQueueRequest).getQueueUrl();
            } catch (AmazonSQSException ex){
                if (!ex.getErrorCode().equals("QueueAlreadyExists")) {
                    throw new Exception ("No error returned for duplicated queue name. " + ex);            	
                }
            }
            // Delete queues
            System.out.println("Deleting the test queues.\n");
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
	
	public void CreateQueueWithAttributeDelaySeconds() throws Exception {
		throw new Exception ("Not implemented");
	}
	
	public void CreateQueueWithAttributeMaximumMessageSize() throws Exception {
		throw new Exception ("Not implemented");
	}
	
	public void CreateQueueWithAttributeMessageRetentionPeriod() throws Exception {
		throw new Exception ("Not implemented");
	}
	
	public void CreateQueueWithAttributePolicy() throws Exception {
		throw new Exception ("Not implemented");
	}
	
	public void CreateQueueWithAttributeVisibilityTimeout() throws Exception {
		throw new Exception ("Not implemented");
	}
}

