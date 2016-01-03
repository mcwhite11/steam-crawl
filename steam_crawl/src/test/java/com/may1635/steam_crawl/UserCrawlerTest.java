package com.may1635.steam_crawl;

import static org.junit.Assert.*;
import java.io.IOException;

/**
 * Unit test for simple App.
 */
public class UserCrawlerTest {
	@org.junit.Test
	public void testGetFriendsList() {
		UserCrawler.getFriendsList("76561197960435530"); //Robin Walker
		fail("getFriendsList did not throw an Exception!");
	}
	
}
