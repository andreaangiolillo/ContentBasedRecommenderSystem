/*
 *	FOR ERROR: message - Rate limit exceededcode - 88: (https://blog.twitter.com/2008/what-does-rate-limit-exceeded-mean-updated)
 *	There are rate limits in the Twitter API. You cannot call a given Twitter API endpoint more than a given number of times per 15 minutes
 *	(on behalf of the authencated user or not).
 *	What happened to you is that your code must have reached the rate limit very quickly (the endpoint to retrieve followers 
 *	IDs is limited to 15 calls per 15 minutes for a given authenticated user) so you will have to wait (904 seconds) before trying again.
 *	Be careful to the Twitter API endpoints you are calling (through Twitter4J) in order to economize your API calls and thus
 *	avoid reaching the rate limit.
 *
 *	LIMIT IS: 100 calls
 *
 *
 *	FOR ERROR:  The Twitter servers are up, but overloaded with requests. - Over capacity code - 130:
 *	Corresponds with an HTTP 503 - Twitter is temporarily over capacity.
 */


package twitter;
import java.io.File;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import com.google.gson.*;

import twitter4j.HashtagEntity;
import twitter4j.IDs;
import twitter4j.PagableResponseList;
import twitter4j.Paging;
import twitter4j.RateLimitStatus;
import twitter4j.Relationship;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.conf.ConfigurationBuilder;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**This class creates a twitter.User (https://newsapi.org/).
 * @author Andrea Angiolillo
 */


public class TwittersUser {
	
	TwitterFactory tf;
	twitter4j.Twitter twitter; 
	ConfigurationBuilder cb;
	int twCount;
	
	
	
	/**Constructor**/

    public TwittersUser(){
		
    	cb = new ConfigurationBuilder();
    	cb.setDebugEnabled(true)
    		.setOAuthConsumerKey("K7XqqmIk17MC9GCO1GXTrgsdY")
    		.setOAuthConsumerSecret("eFvhKL8VVAvBZ9ZkEMJ0QK3nkOrCdT1MX0HDlGkdj37jo5ZAP4")
    		.setOAuthAccessToken("250822105-TkkDiAvsS0XBhJjTBy9iHSma21RF3I2OwT2tKSyW")
    		.setOAuthAccessTokenSecret("wAognO24T3CiOs13VOyF0CH0PjzeTh3fUa4ZN9uPRbxi6");
		this.tf = new TwitterFactory(cb.build());
		this.twitter = tf.getInstance();
		twCount = 0;
	}
    




	/**
	 * This method creates a User (twitter.User) 
	 *   
	 * @param username String id user
	 * @throws JAXBException 
	 */

    public /*User*/ void createUser (String username) throws TwitterException, JAXBException{
		
    	System.out.println("STEP 1 - " + username);
    	twitter4j.User user1 = twitter.showUser(username);
    	twCount ++;
    	
    	List<Status> status = twitter.getHomeTimeline();
    	twCount ++;
    	
    	String name = user1.getName();
    	String location = user1.getLocation();
    	String description = user1.getDescription();
    	
    	ArrayList<Integer> follower = new ArrayList<Integer>();
    	ArrayList<Integer> following = new ArrayList<Integer>();
    	ArrayList<Integer> friend = new ArrayList<Integer>();
    	
    	String tweets = getTweets((int)user1.getId(), 200);
    	ArrayList<String> tweetsFollowing = new ArrayList<String>();
    	ArrayList<String> tweetsFriends = new ArrayList<String>();
    	

		
		
    	
		IDs ids, ids2;
		ids = twitter.getFollowersIDs(username, -1); // follower
		twCount ++;
		ids2 = twitter.getFriendsIDs(username, -1); // following
		twCount ++;
		

		
	   	System.out.println("STEP 2 - " + username);
		

		/*Follower*/ /*MAX 5000*/
        for (long id : ids.getIDs()) { 
	    	follower.add((int) id);


            }
               
       
       /**Following**/ /*MAX 5000*/
        for (long id : ids2.getIDs()) {     
            	following.add((int) id);
 
        }
        

        
        /*Friends*/
        
    	/*showFriendship(int sourceId,int targetId) throws TwitterException
		Gets the detailed relationship status between a source user and a target user
		This method calls http://api.twitter.com/1/friendships/show.json */
        
        Relationship r = null;
        int z = 0;
        for (long id : ids2.getIDs()){
        	 r = twitter.showFriendship(user1.getId(),id);
        	 if (r.isSourceFollowedByTarget()){
        		 z += 1;
        		// System.out.println(id + ",number "+ i );
        		 friend.add((int)id);
        	 }
        	 
        	 if (z >= 20){
        		 break;
        	 }
        	
        }
       
        
    
        
        
        
      // 	System.out.println("\n hddjkdsakjsadkjsdkj     " + friend.size() + "      " + following.size());
        
        
        
    	System.out.println("STEP 3 - " + username);
    	
    	
    	
    	
        String u = "";
        
        
        int count = 0;
        /*tweetsFollowing*/
        for (int j : following){
        //	System.out.println("\n\n\n prints following: " + j );
        	u = getTweets(j, 10);
         // 	System.out.println("u.size " + u.length() );
        	if( u.length() > 0){
        		tweetsFollowing.add(u);
        		count ++;
        	}
            if (count >= 10){
             	break;
            }
        	
        }
        
        
        
        /*tweetsFriends*/
		count = 0;
        if (friend.size() >= 10){
	        for (int i : friend){
	      //  	System.out.println("\n there is some friends:  " + friend.size() );
	        	u = getTweets(i, 10);
	        //	System.out.println("\n U.LENGHT  " + u.length() );
	        	if (u.length() > 0){
	        		tweetsFriends.add(u);
	        		count ++;
	        	}
	        	
	            if (count >= 10){
	             	break;
	            }
	        }
	        
        }else if (following.size() > 21){ // no friends, I consider the following (not in tweetsFollowing) like friends 
        //	System.out.println("There is not a friend - >  considers some following like friends");
        	for (int i = 11; i < following.size(); i++){ // considers some following like friends
	        	//System.out.println("\n\n\n prints follower: " + i );
	        	u = getTweets(i, 10);
	      //  	System.out.println("u.size friend" + u.length() );
	        	if (u.length() > 0){
	        		tweetsFriends.add(u);
	        		count ++;
	        	}
	        	
	            if (count >= 10){
	             	break;
	            }
	        }
        	
        }else{// I consider the following like friends
        	tweetsFriends = tweetsFollowing;
        }
        
        
//		

  
        
        //System.out.println(tweetsFriends.size() + "siiixx");
        
    	twitter.User user = new twitter.User(username, name, location, friend, following, tweets, tweetsFriends, tweetsFollowing, description);
    //	System.out.println(user.getTweetsFriends().size() + "siiixx");
    	
    	File file = new File("otherFile/users/"+username+".xml");
		JAXBContext jaxbContext = JAXBContext.newInstance(twitter.User.class);
		Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

		// output pretty printed
		jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

		jaxbMarshaller.marshal(user, file);
		jaxbMarshaller.marshal(user, System.out);

        
    }
    
	/**
	 * This method takes the tweets of user  
	 *   
	 * @param username String id user
	 * @param n int number of tweets to download - max is 200
	 * @return out  String with tweets
	 */
    
    public String getTweets(int user, int n) throws TwitterException{
	

		try {
			
			
	        twitter4j.User user1 = twitter.showUser(user);
	       // System.out.println("INIZIO PER " + user1.getScreenName() + "  - isProtect: " + user1.isProtected()); 
	        
	        
	        if (user1.getLang().equalsIgnoreCase("en") && user1.isProtected() == false /*&& user1.getFollowersCount() > 50*/ ){
				//First param of Paging() is the page number, second is the number per page (this is capped around 200 I think.
				Paging paging = new Paging(1, n);// fet 200 twett
				List<Status> statuses = twitter.getUserTimeline(user, paging);
		    	String out = "";
				/*User's Tweets*/
				for(Status st : statuses)
				{
				//	System.out.println(st.getUser().getLocation() + " " + st.getUser().getScreenName()+" ----------- " + st.getText() + "\n");
					if (st.getLang().equals("en")){
						out = out + "\n" + st.getText();
					//	System.out.println(st.getText()+ "         " + user);
					}
					
				}
				return out;
	        } else {
	        	return "";
	        }

		} catch (TwitterException name) {
			
		//	System.out.println("This username is not valid " + name.getErrorMessage());
			return "";
		} 
		
    	
    }
    
    
        
}
    
    
    
    
    
    
    