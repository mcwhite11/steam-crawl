package com.may1635.domain;

import java.util.ArrayList;
import java.util.Date;

public class Game {
	private String title;
	private String description;
	private String developer;
	private int userReview;
	private int numUserReviews;
	private String releaseDate;
	private ArrayList<String> metaTags;
	private ArrayList<String> genres;
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getDeveloper() {
		return developer;
	}
	public void setDeveloper(String developer) {
		this.developer = developer;
	}
	public ArrayList<String> getGenres() {
		return genres;
	}
	public void setGenres(ArrayList<String> genres) {
		this.genres = genres;
	}
	public int getUserReview() {
		return userReview;
	}
	public void setUserReview(int userReview) {
		this.userReview = userReview;
	}
	public int getNumUserReviews() {
		return numUserReviews;
	}
	public void setNumUserReviews(int numUserReviews) {
		this.numUserReviews = numUserReviews;
	}
	public String getReleaseDate() {
		return releaseDate;
	}
	public void setReleaseDate(String releaseDate) {
		this.releaseDate = releaseDate;
	}
	public ArrayList<String> getMetaTags() {
		return metaTags;
	}
	public void setMetaTags(ArrayList<String> metaTags) {
		this.metaTags = metaTags;
	}

}
