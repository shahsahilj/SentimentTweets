package com.example.servlets;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.sqs.model.*;
/*
 * AWS
 */
import java.util.List;

public class AwsSNSUtil {
    private BasicAWSCredentials credentials;
    private AmazonSQS sqs;
    private String simpleQueue = "TweetProcessedQueue";
    private static volatile  AwsSNSUtil awssqsUtil = new AwsSNSUtil();

    private   AwsSNSUtil(){
        try{
            this.credentials = new   BasicAWSCredentials();
            this.simpleQueue = "TweetProcessedQueue";

            this.sqs = new AmazonSQSClient(this.credentials);
            
            //AmazonSQS sqs = new AmazonSQSClient(new ClasspathPropertiesFileCredentialsProvider());

        }catch(Exception e){
            System.out.println("exception while creating awss3client : " + e);
        }
    }

    public static AwsSNSUtil getInstance(){
        return awssqsUtil;
    }

    public AmazonSQS getAWSSQSClient(){
         return awssqsUtil.sqs;
    }

    public String getQueueName(){
         return awssqsUtil.simpleQueue;
    }

    /**
     * Creates a queue in your region and returns the url of the queue
     * @param queueName
     * @return
     */
    public String createQueue(String queueName){
        CreateQueueRequest createQueueRequest = new CreateQueueRequest(queueName);
        String queueUrl = this.sqs.createQueue(createQueueRequest).getQueueUrl();
        return queueUrl;
    }

    /**
     * returns the queueurl for for sqs queue if you pass in a name
     * @param queueName
     * @return
     */
    public String getQueueUrl(String queueName){
        GetQueueUrlRequest getQueueUrlRequest = new GetQueueUrlRequest(queueName);
        return this.sqs.getQueueUrl(getQueueUrlRequest).getQueueUrl();
    }

    /**
     * lists all your queue.
     * @return
     */
    public ListQueuesResult listQueues(){
       return this.sqs.listQueues();
    }

    /**
     * send a single message to your sqs queue
     * @param queueUrl
     * @param message
     */
    public void sendMessageToQueue(String queueUrl, String message){
        SendMessageResult messageResult =  this.sqs.sendMessage(new SendMessageRequest(queueUrl, message));
        //System.out.println(messageResult.toString());
    }

    /**
     * gets messages from your queue
     * @param queueUrl
     * @return
     */
    public List<Message> getMessagesFromQueue(String queueUrl){
       ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(queueUrl);
       List<Message> messages = sqs.receiveMessage(receiveMessageRequest).getMessages();
       return messages;
    }

    /**
     * deletes a single message from your queue.
     * @param queueUrl
     * @param message
     */
    public void deleteMessageFromQueue(String queueUrl, Message message){
        String messageRecieptHandle = message.getReceiptHandle();
        //System.out.println("message deleted : " + message.getBody() + "." + message.getReceiptHandle());
        sqs.deleteMessage(new DeleteMessageRequest(queueUrl, messageRecieptHandle));
    }

    public static void main(String[] args){

    }

}