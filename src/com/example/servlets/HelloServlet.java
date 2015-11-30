package com.example.servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class HelloServlet
 */
/*
 * JDBC
 */
@WebServlet("/HelloServlet")
public class HelloServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public HelloServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		System.out.println("IN HELLOSERVLET");
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		try{
			Class.forName("com.mysql.jdbc.Driver");
	        Connection con = DriverManager.getConnection("jdbc:mysql://tweetmap.cfkl2huwwkyh.us-east-1.rds.amazonaws.com:3306/tweetmap","sduser","");
	        Statement stmt = con.createStatement();
	        String query="SELECT COUNT(*) FROM tweets WHERE `sentiment` IS NOT NULL";
	        ResultSet rs=stmt.executeQuery(query);
	        int number=0;
	        if(rs.next()){
	        	number=rs.getInt(1);
	        }
	        query="SELECT * FROM tweets WHERE `sentiment` IS NOT NULL";
	        rs=stmt.executeQuery(query);
	        double[] latitude=new double[number];
	        double[] longitude=new double[number];
	        String[] text=new String[number];
	        String[] sentiment=new String[number];
	        int i=0;
	        int pos_count=0;
	        int neg_count=0;
	        int neutral_count=0;
	        while(rs.next()){
	        	latitude[i]=rs.getDouble("latitude");
	        	longitude[i]=rs.getDouble("longitude");
	        	text[i]=rs.getString("text");
	        	sentiment[i]=rs.getString("sentiment");
	        	if(sentiment[i].equals("neutral"))
	        		neutral_count++;
	        	if(sentiment[i].equals("positive"))
	        		pos_count++;
	        	if(sentiment[i].equals("negative"))
	        		neg_count++;
	        	i++;
	        }

	        int la=2442047;
	        int lon=44418;
	        int ny=2459115;
	        int paris=615702;
	        int istanbul=2344116;
	        int rio=455825;
	        String trendText[]=new String[6];
	        trendText[0]=TweetTrends.getTrends(la);
	        trendText[1]=TweetTrends.getTrends(lon);
	        trendText[2]=TweetTrends.getTrends(ny);
	        trendText[3]=TweetTrends.getTrends(paris);
	        trendText[4]=TweetTrends.getTrends(istanbul);
	        trendText[5]=TweetTrends.getTrends(rio);
	        double trendLat[]= new double[6];
	        double trendLon[]=new double[6];
	        trendLat[0]=34.05;
	        trendLat[1]=51.5072;
	        trendLat[2]=40.7127;
	        trendLat[3]=48.8567;
	        trendLat[4]=41.0136;
	        trendLat[5]=-22.9068;
	        trendLon[0]=-118.25;
	        trendLon[1]=-0.1275;
	        trendLon[2]=-74.0059;
	        trendLon[3]=2.3508;
	        trendLon[4]=28.9550;
	        trendLon[5]=-43.17;
	        request.setAttribute("latitude",latitude);
	        request.setAttribute("longitude",longitude);
	        request.setAttribute("text", text);
	        request.setAttribute("sentiment",sentiment);
	        request.setAttribute("number", number);
	        request.setAttribute("neutral", neutral_count);
	        request.setAttribute("positive", pos_count);
	        request.setAttribute("negative", neg_count);
	        request.setAttribute("trendText", trendText);
	        request.setAttribute("trendLon", trendLon);
	        request.setAttribute("trendLat", trendLat);
	        request.getRequestDispatcher("SentimentMap.jsp").forward(request, response);
	        
		
	        
		}
		catch(Exception e){
			e.printStackTrace();
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
