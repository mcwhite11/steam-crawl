package com.may1635.domain;

import java.util.ArrayList;

/**
 * This is the User class which holds all the information pulled from Steam Web API,
 * namely form the listed links below:
 * Player Summary		| http://api.steampowered.com/ISteamUser/GetPlayerSummaries/v2/?key=XXXXXXXXXXXXXXXXXXXXXXX&steamids=YYYYYYYYYYYYYYYYY
 * Friends List			| http://api.steampowered.com/ISteamUser/GetFriendList/v0001/?key=XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX&steamid=76561197960435530&relationship=friend
 * Player Achievements	| http://api.steampowered.com/ISteamUserStats/GetPlayerAchievements/v1/?appid=440&key=XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX&steamid=76561197972495328
 */
public class User {
	private String steamID;			//64bit Steam ID of the user
	private String personaName;		//The player's persona name (display name)
	private String profileURL;		//The full URL of the player's Steam Community profile
	private ArrayList<String> friendsList; //list of user's friend's Steam IDs
	
	private String avatar;			//The full URL of the player's 32x32px avatar
	private String avatarMedium;	//The full URL of the player's 64x64px avatar
	private String avatarFull;		//The full URL of the player's 184x184px avatar
	
	private int personaState;		//0 - Offline, 1 - Online, 2 - Busy, 3 - Away, 4 - Snooze, 5 - looking to trade, 6 - looking to play
	private int visibilityState;	//1 - profile is not visible (Private, Friends Only, etc), 3 - profile is visible (Public) 
	private int profileState;		//If set, indicates the user has a community profile configured (will be set to '1')
	private int lastLogOff;			//The last time the user was online, in unix time.
	private int commentPermission;	//If set, indicates the profile allows public comments.
	
	private String realName;		//The player's "Real Name", if they have set it.
	private String primaryClanID;	//The player's primary group, as configured in their Steam Community profile.
	private String timeCreated;		//The time the player's account was created.
	
	private String gameID;			//If the user is currently in-game, this value will be returned and set to the gameid of that game.
	private String gameServerIP;	//The ip and port of the game server the user is currently playing on, if they are playing on-line in a game using Steam matchmaking. Otherwise will be set to "0.0.0.0:0".
	private String gameExtraInfo;	//If the user is currently in-game, this will be the name of the game they are playing. This may be the name of a non-Steam game shortcut.
	
	private String cityID;			//This value will be removed in a future update (see loccityid)
	private String locCountryCode;	//If set on the user's Steam Community profile, The user's country of residence, 2-character ISO country code
	private String locStateCode;	//If set on the user's Steam Community profile, The user's state of residence
	private String locCityID;		//An internal code indicating the user's city of residence. A future update will provide this data in a more useful way.
	
	
	public String getSteamID() { return steamID; }
	public void setSteamID(String steamID) { this.steamID = steamID; }
	
	public String getPersonaName() { return personaName; }
	public void setPersonaName(String personaName) { this.personaName = personaName; }
	
	public String getProfileURL() { return profileURL; }
	public void setProfileURL(String profileURL) { this.profileURL = profileURL; }
	
	public ArrayList<String> getFriendsList() { return friendsList; }
	public void setFriendsList(ArrayList<String> friendsList) { this.friendsList = friendsList; }
	
	
	public String getAvatar() { return avatar; }
	public void setAvatar(String avatar) { this.avatar = avatar; }
	
	public String getAvatarMedium() { return avatarMedium; }
	public void setAvatarMedium(String avatarMedium) { this.avatarMedium = avatarMedium; }
	
	public String getAvatarFull() { return avatarFull; }
	public void setAvatarFull(String avatarFull) { this.avatarFull = avatarFull; }
	
	
	public int getPersonaState() { return personaState; }
	public void setPersonaState(int personaState) { this.personaState = personaState; }
	
	public int getVisibilityState() { return visibilityState; }
	public void setVisibilityState(int visibilityState) { this.visibilityState = visibilityState; }
	
	public int getProfileState() { return profileState; }
	public void setProfileState(int profileState) { this.profileState = profileState; }
	
	public int getLastLogOff() { return lastLogOff; }
	public void setLastLogOff(int lastLogOff) {this.lastLogOff = lastLogOff; }
	
	public int getCommentPermission() { return commentPermission; }
	public void setCommentPermission(int commentPermission) { this.commentPermission = commentPermission; }
	
	
	public String getRealName() { return realName; }
	public void setRealName(String realName) { this.realName = realName; }
	
	public String getPrimaryClanID() { return primaryClanID; }
	public void setPrimaryClanID(String primaryClanID) { this.primaryClanID = primaryClanID; }
	
	public String getTimeCreated() { return timeCreated; }
	public void setTimeCreated(String timeCreated) { this.timeCreated = timeCreated; }
	
	public String getGameID() { return gameID; }
	public void setGameID(String gameID) { this.gameID = gameID; }
	
	public String getGameServerIP() { return gameServerIP; }
	public void setGameServerIP(String gameServerIP) { this.gameServerIP = gameServerIP; }
	
	public String getGameExtraInfo() { return gameExtraInfo; }
	public void setGameExtraInfo(String gameExtraInfo) { this.gameExtraInfo = gameExtraInfo; }
	
	
	public String getCityID() { return cityID; }
	public void getCityID(String cityID) { this.cityID = cityID; }
	
	public String getLocCountryCode() { return locCountryCode; }
	public void getLocCountryCode(String locCountryCode) { this.locCountryCode = locCountryCode; }
	
	public String getLocStateCode() { return locStateCode; }
	public void getLocStateCode(String locStateCode) { this.locStateCode = locStateCode; }
	
	public String getLocCityID() { return locCityID; }
	public void getLocCityID(String locCityID) { this.locCityID = locCityID; }
}