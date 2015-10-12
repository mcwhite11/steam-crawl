package com.may1635.steam_crawl;

import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.may1635.domain.Game;

/**
 * Hello world!
 * 
 */
public class App {
	public static void main(String[] args) {
		ArrayList<String> appids = new ArrayList<String>();
		appids.add("282140");

		ArrayList<Game> games = crawl(appids);

		for (int i = 0; i < appids.size(); i++) {
			System.out.println("Title: " + games.get(i).getTitle());
			System.out.println("Description: " + games.get(i).getDescription());
			System.out.println("Developer: " + games.get(i).getDeveloper());

		}

	}

	public static ArrayList<Game> crawl(ArrayList<String> appids) {

		ArrayList<Game> games = new ArrayList<Game>();

		for (int i = 0; i < appids.size(); i++) {
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
				
				games.add(newGame);
				return games;

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return null;
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
}
