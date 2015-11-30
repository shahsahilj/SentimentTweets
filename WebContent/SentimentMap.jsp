<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
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

<div id="map">
</div>

 

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
        src="https://maps.googleapis.com/maps/api/js?key=AIzaSyBH5b18K4biQQwGoEz4EK7su9eBhf-ROY8&signed_in=true&libraries=visualization&callback=initMap">
</script>

</body>
</html>