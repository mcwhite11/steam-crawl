package com.may1635.steam_crawl;

import java.util.ArrayList;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.may1635.domain.Game;

/**
 * Hello world!
 * 
 */
public class GameCrawler {
	private static int numGamesCrawled = 0;
	private static int numberGamesToCrawl = 1000;
	private static int actualNumberGamesCrawled = 0;
	private static ArrayList<String> gamesCrawled = new ArrayList<String>();
	
	public static ArrayList<Game> crawl(ArrayList<String> appids) {

		ArrayList<Game> games = new ArrayList<Game>();

		for (int i = 0; i < numberGamesToCrawl; i++) {
			String appid = appids.get(i);
			if(shouldCrawlGame(appid))
			{
				gamesCrawled.add(appid);
				
				try {
					Document doc = Jsoup
							.connect("http://store.steampowered.com/app/" + appid)
							.cookie("birthtime", "-1000000000").get();
					Game newGame = new Game();
					newGame.setAppId(appid);
					newGame.setTitle(doc.title().replace(" on Steam", "").trim());
					newGame.setDescription(doc
							.getElementsByClass("game_description_snippet").first()
							.text());
					newGame.setDeveloper(getDeveloperFromDetailsBlock(doc));
					newGame.setGenres(getGenresFromDetailsBlock(doc));
					newGame.setReleaseDate(getReleaseDateFromDetailsBlock(doc));
					
					printGame(newGame);
					games.add(newGame);

				} catch (Exception e) {
					//game not found
					actualNumberGamesCrawled--;
				}
			}
			
			
			/*try {
				TimeUnit.SECONDS.sleep(3);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/

		}
		return games;
	}
	
	public static String getDeveloperFromDetailsBlock(Document doc) {
		Elements details = doc.getElementsByClass("details_block").first()
				.children();
		for (int i = 0; i < details.size(); i++) {
			if(details.get(i).text().equals("Developer:"))
			{
				return details.get(i).nextElementSibling().text().trim();
			}
		}
		return null;

	}
	
	public static ArrayList<String> getGenresFromDetailsBlock(Document doc)
	{
		
		ArrayList<String> genres = new ArrayList<String>();
		Elements details = doc.getElementsByClass("details_block").first().children();
		for (int i = 0; i < details.size(); i++) {
			if(details.get(i).text().equals("Genre:"))
			{
				for(int j=i+1; j<details.size(); j++)
				{
					
					
					if(details.get(j).tag().toString().equals("a"))
					{
						genres.add(details.get(j).text());
					}
						
					
					if(details.get(j).tag().toString().equals("br"))
						break;
				}
				
				return genres;
			}
		}
		return genres;
	}
	
	public static ArrayList<String> getMetaTags(Document doc)
	{
		return null;
	}
	
	public static String getReleaseDateFromDetailsBlock(Document doc) {
		Elements details = doc.getElementsByClass("details_block").first()
				.children();
		for (int i = 0; i < details.size(); i++) {
			if(details.get(i).text().equals("Release Date:"))
			{
				return details.get(i).nextSibling().toString().trim();
			}
		}
		return null;

	}
	
	public static int getNumUserReviews(Document doc)
	{
		return 0;
	}
	
	public static int getAverageUserReview(Document doc)
	{
		return 0;
	}
	
	public static void printGame(Game game)
	{	numGamesCrawled++;
		System.out.println("#" + numGamesCrawled);
		System.out.println("Title: " + game.getTitle());
		System.out.println("App ID: " + game.getAppId());
		System.out.println("Description: " + game.getDescription());
		System.out.println("Developer: " + game.getDeveloper());
		System.out.println("Genre(s): "+game.getGenres().toString());
		System.out.println("Release Date: "+game.getReleaseDate());
		System.out.print("\n\n");
		
	}
	
	/**
	 * Matt, this needs to be updated so that we do something with the DB to check if we should crawl the game or not.
	 * 
	 * Couple ideas: If game has never been crawled, or if the last time a game was crawled is longer than a week ago crawl...
	 * 
	 * Feel free to do whatever
	 * 
	 */
	public static boolean shouldCrawlGame(String appID)
	{
		return !gamesCrawled.contains(appID);
	}
	
	
}