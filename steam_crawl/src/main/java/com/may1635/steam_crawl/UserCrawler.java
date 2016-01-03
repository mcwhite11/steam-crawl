package com.may1635.steam_crawl;

import com.may1635.domain.User;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.net.HttpURLConnection;
import java.net.URL;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class UserCrawler {
	
	private static String key = "EE754BC31EC90048E3D53C788A3DC43D";
	
	public static void main(String[] args) {
		Stack<String> steamIDs = new Stack<String>();
		steamIDs.push("76561197960435530"); // Robin Walker
		fillStack(steamIDs);
		
		HashMap<String, User> users = new HashMap<String, User>();
		
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
	
	private static void fillStack(Stack<String> steamIDs) {
		ArrayList<String> friendsList = getFriendsList(steamIDs.pop());
		
		for(String friend : friendsList) {
			steamIDs.push(friend);
		}
		
		//TODO "recurse" through the stack and add friends friends friendlists to the stack...
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
		    JsonElement friendEle = jp.parse(new InputStreamReader((InputStream) request.getContent())); //Convert the input stream to a json element
		    JsonObject friendObj = friendEle.getAsJsonObject(); //May be an array, may be an object
		    String friend = friendObj.get("steamid").getAsString(); //just grab the steamid
		    
		    //TODO add all friends not just one
		    friendsList.add(friend);
		    
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return friendsList;
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
		    JsonElement userEle = jp.parse(new InputStreamReader((InputStream) request.getContent())); //Convert the input stream to a json element
		    JsonObject userObj = userEle.getAsJsonObject(); //May be an array, may be an object
		    
		    //TODO This really should be a constructor...
			user.setSteamID(userObj.get("steamid").getAsString());
			user.setPersonaName(userObj.get("personaname").getAsString());
			user.setProfileURL(userObj.get("profileurl").getAsString());
			user.setFriendsList(getFriendsList(steamID));
			user.setAvatar(userObj.get("avatar").getAsString());
			user.setAvatarMedium(userObj.get("avatarmedium").getAsString());
			user.setAvatarFull(userObj.get("avatarfull").getAsString());
			user.setPersonaState(userObj.get("personastate").getAsInt());
			user.setVisibilityState(userObj.get("visibilitystate").getAsInt());
			user.setProfileState(userObj.get("profilestate").getAsInt());
			user.setLastLogOff(userObj.get("lastlogoff").getAsInt());
			user.setCommentPermission(userObj.get("commentpermission").getAsInt());
			user.setRealName(userObj.get("realname").getAsString());
			user.setPrimaryClanID(userObj.get("primaryclanid").getAsString());
			user.setTimeCreated(userObj.get("timecreated").getAsString());
			user.setGameID(userObj.get("gameid").getAsString());
			user.setGameServerIP(userObj.get("gameserverip").getAsString());
			user.setGameExtraInfo(userObj.get("gameextrainfo").getAsString());
			user.setLocCountryCode(userObj.get("loccountrycode").getAsString());
			user.setLocStateCode(userObj.get("locstatecode").getAsString());
			user.setLocCityID(userObj.get("loccityid").getAsString());

		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return user;
	}
}