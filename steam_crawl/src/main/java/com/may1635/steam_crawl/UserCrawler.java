package com.may1635.steam_crawl;

import com.may1635.domain.User;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
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
		for(int i = 0; i < users.size(); i++) {
			System.out.println("Steam ID: " + users.get(i).getSteamID());
			System.out.println("Persona Name: " + users.get(i).getPersonaName());
			System.out.println("Profile URL: " + users.get(i).getProfileURL());
			
			System.out.println("Avatar URL: " + users.get(i).getAvatar());
			System.out.println("Medium Avatar URL : " + users.get(i).getAvatarMedium());
			System.out.println("Full Avatar URL : " + users.get(i).getAvatarFull());
			
			System.out.print("Persona State: ");
			switch(users.get(i).getPersonaState()) {
				case 0: System.out.println("Offine"); break;
				case 1: System.out.println("Online"); break;
				case 2: System.out.println("Busy"); break;
				case 3: System.out.println("Away"); break;
				case 4: System.out.println("Snooze"); break;
				case 5: System.out.println("Looking to Trade"); break;
				case 6: System.out.println("Looking to Play"); break;
			}
			
			System.out.print("Visibility State: ");
			switch(users.get(i).getVisibilityState()) {
				case 1: System.out.println("Private"); break;
				case 3: System.out.println("Public"); break;
			}
			
			System.out.print("Profile State: ");
			switch(users.get(i).getVisibilityState()) {
				case 1: System.out.println("community profile configured"); break;
				default: System.out.println("community profile NOT configured"); break;
			}
			
			System.out.println("Last Log-off Time: " + users.get(i).getLastLogOff());
			
			System.out.print("Comment Permission: ");
			switch(users.get(i).getCommentPermission()) {
				case 1: System.out.println("profile allows public comments"); break;
				default: System.out.println("profile does NOT allow public comments"); break;
			}
			
			System.out.println("Real Name: " + users.get(i).getRealName());
			System.out.println("Primary Clan ID: " + users.get(i).getPrimaryClanID());
			System.out.println("Time of Account Creation: " + users.get(i).getTimeCreated());
			
			System.out.println("Game ID: " + users.get(i).getGameID());
			System.out.println("Game Server IP: " + users.get(i).getGameServerIP());
			System.out.println("Game Extra Info: " + users.get(i).getGameExtraInfo());
			
			System.out.println("City ID: " + users.get(i).getCityID());
			System.out.println("LocCountryCode: " + users.get(i).getLocCountryCode());
			System.out.println("LocStateCode: " + users.get(i).getLocStateCode());
			System.out.println("LocCityID: " + users.get(i).getLocCityID());
		}
	}
	
	private static void fillStack(Stack<String> steamIDs) {
		ArrayList<String> friendsList = getFriendsList(steamIDs.pop());
		
		for(String friend : friendsList) {
		}
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
		    JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent())); //Convert the input stream to a json element
		    JsonObject rootobj = root.getAsJsonObject(); //May be an array, may be an object
		    String steamid = rootobj.get("steamid").getAsString(); //just grab the steamid
		    
		    //TODO
		    friendsList.add(steamid);
		    
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return friendsList;
	}
	
	public static HashMap<String, User> crawl(Stack<String> steamIDs) {
		HashMap<String, User> users = new HashMap<String, User>();
		return users;
	}
}