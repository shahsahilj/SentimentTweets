<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="java.sql.*" %>
<%@ page import="com.example.servlets.TweetTrends" %>
<meta http-equiv="refresh" content="15"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Twitter Map</title>
<style>
      html, body {
        height: 100%;
        margin: 0;
        padding: 0;
      }
      #map {
        height: 100%;
      }
      #legend {
    background: white;
    padding: 10px;
  }

</style>
</head>
<body>

<div id="map">
</div>
<%
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
	}
	catch(Exception e){
		e.printStackTrace();
	}
	%>
 <%
 double[] latitude=(double[])request.getAttribute("latitude");
double[] longitude=(double[])request.getAttribute("longitude");
String[] sentiment=(String[])request.getAttribute("sentiment");
int number=(int)request.getAttribute("number");
int positive=(int)request.getAttribute("positive");
int negative=(int)request.getAttribute("negative");
int neutral=(int)request.getAttribute("neutral");
String trendText[]=(String[])request.getAttribute("trendText");
double trendLat[]=(double[])request.getAttribute("trendLat");
double trendLon[]=(double[])request.getAttribute("trendLon");
 %>

<script>

var map;		 

function initMap(){
	

	map=new google.maps.Map(document.getElementById('map'), {
    zoom: 2,
    center: {lat: 0, lng: 0},
    mapTypeId: google.maps.MapTypeId.TERRAIN
  });
	map.controls[google.maps.ControlPosition.BOTTOM_RIGHT].push(
			  document.getElementById('legend'));
  
	var marker;
	<%
	for(int i=0;i<latitude.length;i++){
		%>
		var a='<%=latitude[i]%>';
		var b='<%=longitude[i]%>';
		var c='<%=sentiment[i]%>';
		if(c.localeCompare("negative")==0)
			marker = new google.maps.Marker({
				position: new google.maps.LatLng(a,b),
				icon:'http://maps.google.com/mapfiles/ms/icons/red-dot.png',
    	      	map:map
			});
		if(c.localeCompare("positive")==0)
			marker = new google.maps.Marker({
				position: new google.maps.LatLng(a,b),
				icon:'http://maps.google.com/mapfiles/ms/icons/blue-dot.png',
          		map:map
			});
		if(c.localeCompare("neutral")==0)
			marker = new google.maps.Marker({
				position: new google.maps.LatLng(a,b),
				icon:'http://maps.google.com/mapfiles/ms/icons/yellow-dot.png',
          		map:map
			});
	<%
	}
	for(int j=0;j<6;j++){
		%>
		var image='black_mark.png';
		var d='<%=trendLat[j]%>';
		var e='<%=trendLon[j]%>';
		var f='<%=trendText[j]%>';
		marker = new google.maps.Marker({
			position: new google.maps.LatLng(d,e),
			icon:image,
			width:10,
			height:10,
			title:f,
    	   	map:map
		});
	<%
	}
	//response.setIntHeader("Refresh", 10);
	%>
	
	

}
    </script>
<div id="legend">
  <div>
  <img height="20" width="20" src="http://maps.google.com/mapfiles/ms/icons/blue-dot.png">
  Positive Sentiments: <%=positive%>
  </div>
  <div>
  <img height="20" width="20" src="http://maps.google.com/mapfiles/ms/icons/red-dot.png">
  Negative Sentiments:<%=negative%>
  </div>
  <div>
  <img height="20" width="20" src="http://maps.google.com/mapfiles/ms/icons/yellow-dot.png">
  Neutral Sentiment:<%=neutral%>
  </div>
</div>
    
    <script async defer
        src="https://maps.googleapis.com/maps/api/js?key=&signed_in=true&libraries=visualization&callback=initMap">
</script>

</body>
</html>