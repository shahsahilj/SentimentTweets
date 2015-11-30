package com.example.servlets;

import com.alchemyapi.api.AlchemyAPI;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;
import com.amazonaws.services.sqs.model.Message;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
/*
 * Alchemy
 * JDBC
 * AWS
 */
public class SQSTweetManager implements Runnable{
    private String text;
    

    public SQSTweetManager(String text){
        this.text=text;
    }

    @Override
    public void run() {
    	processTweet(text);
    	
    }
    public static void processTweet(String text){
    	AlchemyAPI alchemyObj = AlchemyAPI.GetInstanceFromString();
    	Document doc=null;
    	String sentiment=null;
		try {
			doc = alchemyObj.TextGetTextSentiment(text);
			NodeList temp=doc.getElementsByTagName("type");
	        Node t=temp.item(0);
	        sentiment=t.getTextContent();
		} catch (XPathExpressionException | IOException | SAXException
				| ParserConfigurationException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
    	
        if(sentiment==null){
        	sentiment="neutral";
        }
        try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        Connection con = null;
		try {
			con = DriverManager.getConnection("jdbc:mysql://tweetmap.cfkl2huwwkyh.us-east-1.rds.amazonaws.com:3306/tweetmap","sduser","");
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        Statement stmt = null;
		try {
			stmt = con.createStatement();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        //System.out.println("Created DB Connection....");
        String query="";
        query="UPDATE `tweetmap`.`tweets` SET `sentiment`='"+sentiment+"' WHERE `text`='"+text+"';";
        //System.out.println(query);
        try {
			stmt.executeUpdate(query);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("error");
			return;
		}
        
        AWSCredentials credentials = new BasicAWSCredentials();
    	AmazonSNSClient snsClient = new AmazonSNSClient(credentials);
        //String msg = "My text published to SNS topic with email endpoint";
    	String topicArn="arn:aws:sns:us-east-1:577624266841:TweetSNS";
        PublishRequest publishRequest = new PublishRequest(topicArn, text);
        PublishResult publishResult = snsClient.publish(publishRequest);
        System.out.println("Publishing tweet:"+text);
        
        //print MessageId of message published to SNS topic
        //System.out.println("MessageId - " + publishResult.getMessageId());
    	
    }
}