package com.example.bagofpix;

public class Photo {
	private int id, storyID;
	private String comment, url;
	public Photo(int id, int storyID, String url, String comment) {
		super();
		this.id = id;
		this.storyID = storyID;
		this.comment = comment;
		this.url = url;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getStoryID() {
		return storyID;
	}
	public void setStoryID(int storyID) {
		this.storyID = storyID;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
}
