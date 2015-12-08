package com.may1635.steam_crawl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import javax.xml.parsers.*;
import org.xml.sax.*;
import org.xml.sax.helpers.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.may1635.domain.Game;
import com.may1635.utilities.xmlParser;

/**
 * Hello world!
 * 
 */
public class App {
	private static int numberGamesToCrawl = 500;
	private static int actualNumberGamesCrawled = 0;
	
	public static void main(String[] args) {
		
		actualNumberGamesCrawled = numberGamesToCrawl;
		ArrayList<String> appids = xmlParser.parseXMLFile();
		System.out.println(appids.size());
		
		for(int i = 0; i < numberGamesToCrawl; i++) {
			System.out.println(appids.get(i));
		}
		
		ArrayList<Game> games = crawl(appids);

		for (int i = 0; i < actualNumberGamesCrawled; i++) {
			System.out.println("Title: " + games.get(i).getTitle());
			System.out.println("Description: " + games.get(i).getDescription());
			System.out.println("Developer: " + games.get(i).getDeveloper());
			System.out.println("Genre(s): "+games.get(i).getGenres().toString());
			System.out.println("Release Date: "+games.get(i).getReleaseDate());
			System.out.print("\n\n");
		}

	}

	public static ArrayList<Game> crawl(ArrayList<String> appids) {

		ArrayList<Game> games = new ArrayList<Game>();

		for (int i = 0; i < numberGamesToCrawl; i++) {
			String appid = appids.get(i);

			try {
				Document doc = Jsoup
						.connect("http://store.steampowered.com/app/" + appid)
						.cookie("birthtime", "-1000000000").get();
				Game newGame = new Game();
				newGame.setTitle(doc.title().replace(" on Steam", "").trim());
				newGame.setDescription(doc
						.getElementsByClass("game_description_snippet").first()
						.text());
				newGame.setDeveloper(getDeveloperFromDetailsBlock(doc));
				newGame.setGenres(getGenresFromDetailsBlock(doc));
				newGame.setReleaseDate(getReleaseDateFromDetailsBlock(doc));
				games.add(newGame);

			} catch (Exception e) {
				//game not found
				actualNumberGamesCrawled--;
			}

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
	
	
}
