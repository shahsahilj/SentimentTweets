package com.example.servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.amazonaws.services.sqs.model.Message;



/**
 * Servlet implementation class ThreadServlet
 */
@WebServlet("/ThreadServlet")
public class ThreadServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ThreadServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		ExecutorService executor = Executors.newFixedThreadPool(5);
		AWSSimpleQueueServiceUtil awssqsUtil =   AWSSimpleQueueServiceUtil.getInstance();
		String queueUrl  = awssqsUtil.getQueueUrl(awssqsUtil.getQueueName());
        boolean flag = true;
        while(flag){
            List<Message> messages =  awssqsUtil.getMessagesFromQueue(queueUrl);
            if(messages == null || messages.size() == 0){
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            }else{
                //flag = false;
                for (Message message : messages) {
                    String messageTweet = message.getBody();
                    System.out.println("Tweet to be processed : " + messageTweet);
                    //TweetProcessor.generateImage(path, source, target, 150);
                    Runnable worker = new SQSTweetManager(messageTweet);
                    executor.execute(worker);
                    try {
						Thread.sleep(20000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                }

                for (Message message : messages) {
                      awssqsUtil.deleteMessageFromQueue(queueUrl, message);
                }

            }
        }
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	
	

}
