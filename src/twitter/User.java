package twitter;
import java.util.ArrayList;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


/**This class contains a Twitter user (https://twitter.com/).
 * @author Andrea Angiolillo
 */

@XmlRootElement

public class User {
	
	private String id;
	private String name;
	private String location;
	private String description;
	
	private ArrayList<Integer> friends;
	private ArrayList<Integer> following;
	
	private String tweets;

	private ArrayList<String> tweetsFriends;
	

	private ArrayList<String> tweetsFollowing;
	
	
	
	
	/**Constructor n1**/
	public User() {
		tweetsFollowing = new ArrayList<String>();
		tweetsFriends = new ArrayList<String>();
		tweets = "";
		following = new ArrayList<Integer>();
		friends = new ArrayList<Integer>();
		name = "";
		location = "";
		id = "";
		description = "";
		
	}




	/**Constructor n2**/
	public User(String id, String name, String location, ArrayList<Integer> friends,
			ArrayList<Integer> following, String tweets, ArrayList<String> tweetsFriends,
			ArrayList<String> tweetsFollowing, String description) {
		super();
		this.id = id;
		this.name = name;
		this.location = location;
		this.friends = friends;
		this.following = following;
		this.tweets = tweets;
		this.tweetsFriends = tweetsFriends;
		this.tweetsFollowing = tweetsFollowing;
		System.out.println("tweetsFollowing : " +tweetsFollowing.size() + " this.tweetsFollowing: " + this.tweetsFollowing.size());
		this.description = description;
	}




	public String getName() {
		return name;
	}




	public void setName(String name) {
		this.name = name;
	}




	public String getLocation() {
		return location;
	}




	public void setLocation(String location) {
		this.location = location;
	}





	public ArrayList<Integer> getFriends() {
		return friends;
	}




	public void setFriends(ArrayList<Integer> friends) {
		this.friends = friends;
	}




	public ArrayList<Integer> getFollowing() {
		return following;
	}




	public void setFollowing(ArrayList<Integer> following) {
		this.following = following;
	}




	public String getTweets() {
		return tweets;
	}




	public void setTweets(String tweets) {
		this.tweets = tweets;
	}




	public ArrayList<String> getTweetsFriends() {
		return tweetsFriends;
	}




	public void setTweetsFriends(ArrayList<String> tweetsFriends) {
		this.tweetsFriends = tweetsFriends;
	}




	public ArrayList<String> getTweetsFollowing() {
		return tweetsFollowing;
	}




	public void setTweetsFollowing(ArrayList<String> tweetsFollowing) {
		this.tweetsFollowing = tweetsFollowing;
	}




	public String getId() {
		return id;
	}




	public void setId(String id) {
		this.id = id;
	}




	public String getDescription() {
		return description;
	}




	public void setDescription(String description) {
		this.description = description;
	}




	@Override
	public String toString() {
		
		System.out.println("Tweets: \n");

		System.out.println(tweets + "\n");

		
		
		return "User [id="+ id + "\n name=" + name + ",\n location=" + location + ",\n friends="
				+ friends + ",\n following=" + following + "\n descriptio="+ description +",\n tweets=" + tweets + "]";
	}
	
	
	

}
