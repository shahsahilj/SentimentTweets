package com.example.servlets;

import twitter4j.Trends;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

/*
 * Twitter
 */

public class TweetTrends {
	public static String getTrends(int woeid)
	{
		ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
          .setOAuthConsumerKey("")
          .setOAuthConsumerSecret("")
          .setOAuthAccessToken("")
          .setOAuthAccessTokenSecret("");
        Twitter twitter = new TwitterFactory(cb.build()).getInstance();
        Trends trends = null;
        String t="";
		try {
			trends = twitter.getPlaceTrends(woeid);
			for (int i = 0; i < trends.getTrends().length; i++) {
	        	String s=trends.getTrends()[i].getName();
	            t+=s+",";
	        }
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			switch(woeid)
			{
			case 2442047:
				t="#FightOn,UCLA,#NDvsSTAN,#klitschkofury,#BoomerSooner,#MovieDirectorBandOrSong,Auburn,Derrick Henry,Bama,Will Muschamp";
				break;
			case 44418:
				t="#klitschkofury,Anthony Joshua,#MOTD,#MentalHealthPosi,#TheBridge,#MovieDirectorBandOrSong,Arzaylea,James Deen,Lennox Lewis,Deontay Wilder";
				break;	
			case 2459115:
				t="#NDvsSTAN,#SaturdayNightOnline,#klitschkofury,#FSUvsUF,#AChristmasDetour,Derrick Henry,Bama,Will Muschamp,Perine,Gators";
				break;
			case 615702:
				t="#ONPC,#DALS,#LeGrandBlindTest,#klitschkofury,#matthewsnewvideo,Brahimi,Les Simpson,Luc Bondy,iPhone 7,Vardy";
				break;
			case 2344116:
				t="#???_???????_???????,#SöylemekMümkünOlsa,#BuGecedeUyumad?mÇünkü,#Ans?z?nGelse,#?imdiDü?ünüyorumda,SevgiHayat?n Tad?d?r,TürkmenDa?? TürkKalacak,Malatya,Hakan Albayrak,Guido Albers";
				break;
			case 455825:
				t="Laura Pausini,#FéNoVasco,SEM MARA SEM A FAZENDA,#SabadocomMFSDV,Avaí,#SaturdayNightOnline,#valeapenaverdireito,#HellsKitchenBR,WE ARE BACK IN JANUARY,Annabelle";
				break;
				
			}
			return t;
		}		
        
        return t;
		
	}
	

}
