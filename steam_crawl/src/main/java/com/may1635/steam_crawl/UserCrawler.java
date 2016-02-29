package com.may1635.steam_crawl;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import com.may1635.domain.User;

public class UserCrawler {
	
	
	private static String key = "EE754BC31EC90048E3D53C788A3DC43D";
	
	private static final int MAX_NUM_USERS_TO_CRAWL = 100;
	/**
	 * Matt, instead of using this list to keep track of which IDs have been crawled we should probably use the DB so that we don't 
	 * use up all the server's memory.
	 */
	private static ArrayList<String> crawledIDs;	
	public static void startUserCrawl() {
		
		Stack<String> steamIDs = new Stack<String>();
		
		crawledIDs = new ArrayList<String>();
		
		/**
		 * Matt, we should change the way search is started to use existing data in the DB. Maybe we could randomly choose a user ID
		 * from the DB each time we crawl?
		 */
		String rootUserID = "76561197960435530"; // Robin Walker
		
		steamIDs.push(rootUserID); 
		
		HashMap<String, User> users = new HashMap<String, User>();
		
		while(crawledIDs.size() < MAX_NUM_USERS_TO_CRAWL)
		{	
			crawledIDs.add(steamIDs.peek());
			
			User newUser = populateUser(steamIDs.peek());
			
			if(newUser != null)
				{
					users.put(newUser.getSteamID(), newUser);
					
					//send the just crawled user's list of games to the GameCrawler
					GameCrawler.crawl(newUser.getGames());
				}
			
			gatherSteamIDs(steamIDs);
		}
		
		
		
		
		
		// Print out all the information put into the users list
		for(User user : users.values()) {
			System.out.println("Steam ID: " + user.getSteamID());
			System.out.println("Persona Name: " + user.getPersonaName());
			System.out.println("Profile URL: " + user.getProfileURL());
			
			System.out.println("Avatar URL: " + user.getAvatar());
			System.out.println("Medium Avatar URL : " + user.getAvatarMedium());
			System.out.println("Full Avatar URL : " + user.getAvatarFull());
			
			System.out.print("Persona State: ");
			switch(user.getPersonaState()) {
				case 0: System.out.println("Offine"); break;
				case 1: System.out.println("Online"); break;
				case 2: System.out.println("Busy"); break;
				case 3: System.out.println("Away"); break;
				case 4: System.out.println("Snooze"); break;
				case 5: System.out.println("Looking to Trade"); break;
				case 6: System.out.println("Looking to Play"); break;
			}
			
			System.out.print("Visibility State: ");
			switch(user.getVisibilityState()) {
				case 1: System.out.println("Private"); break;
				case 3: System.out.println("Public"); break;
			}
			
			System.out.print("Profile State: ");
			switch(user.getVisibilityState()) {
				case 1: System.out.println("community profile configured"); break;
				default: System.out.println("community profile NOT configured"); break;
			}
			
			System.out.println("Last Log-off Time: " + user.getLastLogOff());
			
			System.out.print("Comment Permission: ");
			switch(user.getCommentPermission()) {
				case 1: System.out.println("profile allows public comments"); break;
				default: System.out.println("profile does NOT allow public comments"); break;
			}
			
			System.out.println("Real Name: " + user.getRealName());
			System.out.println("Primary Clan ID: " + user.getPrimaryClanID());
			System.out.println("Time of Account Creation: " + user.getTimeCreated());
			
			System.out.println("Game ID: " + user.getGameID());
			System.out.println("Game Server IP: " + user.getGameServerIP());
			System.out.println("Game Extra Info: " + user.getGameExtraInfo());
			
			System.out.println("LocCountryCode: " + user.getLocCountryCode());
			System.out.println("LocStateCode: " + user.getLocStateCode());
			System.out.println("LocCityID: " + user.getLocCityID());
		}
	}
	
	private static void gatherSteamIDs(Stack<String> steamIDs) {
		ArrayList<String> friendsList = getFriendsList(steamIDs.pop());
		
		if(friendsList != null)
			for(String friendID : friendsList) {
				if(shouldCrawlUser(friendID))
					steamIDs.push(friendID);
				
			}
		
		//TODO "recurse" through the stack adding friend's friendlists to the stack...
		// Still need a terminating case
	}

	public static ArrayList<String> getFriendsList(String steamID) {
		ArrayList<String> friendsList = new ArrayList<String>();
		
		try {    
			URL url = new URL("http://api.steampowered.com/ISteamUser/GetFriendList/v1/"
					+ "?key=" + key
					+ "&steamid=" + steamID
					+ "&relationship=friend");
			
			// Connect to the URL using java's native library
			HttpURLConnection request = (HttpURLConnection) url.openConnection();
			request.connect();

			// Convert to a JSON object to print data
			JsonParser jp = new JsonParser(); //from gson
			InputStreamReader stream = new InputStreamReader((InputStream) request.getContent());
			JsonObject root = jp.parse(stream).getAsJsonObject();
			JsonObject obj = root.get("friendslist").getAsJsonObject();
			String friendsStr = obj.get("friends").toString();
			
			Gson gson = new Gson();
			Type type = new TypeToken<List<Friend>>(){}.getType();
			ArrayList<Friend> friends = gson.fromJson(friendsStr, type);
			
			for(Friend friend : friends) {
				friendsList.add(friend.getSteamID());
			}
			
		} catch (IOException e) {
			return null;
		}
		
		return friendsList;
	}
	
	// this wrapper class is needed to be able to pull the json objects
	// requested from the steam api because that's how gson works
	class Friend {
		@SerializedName("steamid")
		private String steamid;
		@SerializedName("relationship")
		private String relationship;
		@SerializedName("friend_since")
		private int friend_since;
		
		public Friend(){}
		
		public final String getSteamID() { return this.steamid; }
		public final String getRelationship() { return this.relationship; }
		public final int getFriendSince() { return this.friend_since; }
	}
	
	public static Map<String, User> crawl(Stack<String> steamIDs) {
		HashMap<String, User> users = new HashMap<String, User>();
		
		while(!steamIDs.empty()) {
			String steamID = steamIDs.pop();
			if(!users.containsKey(steamID)) {
				User user = populateUser(steamID);
				users.put(steamID, user);
			}
		}
		
		return users;
	}
	
	public static User populateUser(String steamID) {
		User user = new User();
		
		//http://api.steampowered.com/ISteamUser/GetPlayerSummaries/v2/?key=XXXXXXXXXXXXXXXXXXXXXXX&steamids=YYYYYYYYYYYYYYYYY
		try {    
			URL url = new URL("http://api.steampowered.com/ISteamUser/GetPlayerSummaries/v2/"
					+ "?key=" + key
					+ "&steamids=" + steamID);
			
			// Connect to the URL using java's native library
			HttpURLConnection request = (HttpURLConnection) url.openConnection();
			request.connect();

			// Convert to a JSON object to print data	
			JsonParser jp = new JsonParser(); //from gson
			InputStreamReader stream = new InputStreamReader((InputStream) request.getContent());
			JsonObject root = jp.parse(stream).getAsJsonObject();
			JsonObject response = root.get("response").getAsJsonObject();
			JsonObject player = response.get("players").getAsJsonArray().get(0).getAsJsonObject();
			System.out.println(player);
			
		    // This really should be a constructor...
			user.setSteamID(player.get("steamid").getAsString());
			user.setPersonaName(player.get("personaname").getAsString());
			user.setProfileURL(player.get("profileurl").getAsString());
			user.setFriendsList(getFriendsList(steamID));
			user.setAvatar(player.get("avatar").getAsString());
			user.setAvatarMedium(player.get("avatarmedium").getAsString());
			user.setAvatarFull(player.get("avatarfull").getAsString());
			user.setPersonaState(player.get("personastate").getAsInt());
			if(player.get("visibilitystate") != null)
				user.setVisibilityState(player.get("visibilitystate").getAsInt());
			if(player.get("profilestate") != null)
				user.setProfileState(player.get("profilestate").getAsInt());
			user.setLastLogOff(player.get("lastlogoff").getAsInt());
			if(player.get("commentpermission") != null)
				user.setCommentPermission(player.get("commentpermission").getAsInt());
			if(player.get("realname") != null)
				user.setRealName(player.get("realname").getAsString());
			if(player.get("primaryclanid") != null)
				user.setPrimaryClanID(player.get("primaryclanid").getAsString());
			if(player.get("timecreated") != null)
				user.setTimeCreated(player.get("timecreated").getAsString());
			if(player.get("gameid") != null)
				user.setGameID(player.get("gameid").getAsString());
			if(player.get("gameserverip") != null)
				user.setGameServerIP(player.get("gameserverip").getAsString());
			if(player.get("gameextrainfo") != null)
				user.setGameExtraInfo(player.get("gameextrainfo").getAsString());
			if(player.get("loccountrycode") != null)
				user.setLocCountryCode(player.get("loccountrycode").getAsString());
			if(player.get("locstatecode") != null)
				user.setLocStateCode(player.get("locstatecode").getAsString());
			if(player.get("loccityid") != null)
				user.setLocCityID(player.get("loccityid").getAsString());
			
			user.setGames(populateUsersGames(user.getSteamID()));
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		
		return user;
	}
	
	public static ArrayList<String> populateUsersGames(String steamID)
	{
		ArrayList<String> appIDs = new ArrayList<String>();
		
		//http://api.steampowered.com/IPlayerService/GetOwnedGames/v0001/?key=EE754BC31EC90048E3D53C788A3DC43D&steamid=76561197960434622&format=json
		try {    
			URL url = new URL("http://api.steampowered.com/IPlayerService/GetOwnedGames/v0001/?key="+key+
					"&steamid="+ steamID +"&format=json");
			
			// Connect to the URL using java's native library
			HttpURLConnection request = (HttpURLConnection) url.openConnection();
			request.connect();

			// Convert to a JSON object to print data	
			JsonParser jp = new JsonParser(); //from gson
			InputStreamReader stream = new InputStreamReader((InputStream) request.getContent());
			JsonObject root = jp.parse(stream).getAsJsonObject();
			JsonObject response = root.get("response").getAsJsonObject();
			if(response.get("game_count").getAsInt() == 0)
				return null;
			JsonArray games = response.get("games").getAsJsonArray();
			
			for(JsonElement game: games)
			{
				appIDs.add(game.getAsJsonObject().get("appid").getAsString());
			}
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		
		return appIDs;
	}
	
	/**
	 * 
	 * Matt, here's where you can add a check to see if the user is already in the database or whatever you think is the best way we should
	 * determine whether or not to crawl a user.
	 */
	private static boolean shouldCrawlUser(String userID)
	{
		return !crawledIDs.contains(userID);
		
	}
	
}