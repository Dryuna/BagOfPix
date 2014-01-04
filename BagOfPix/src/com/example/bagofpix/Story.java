package com.example.bagofpix;

public class Story {
	private int id;
	private String name, url, comment;
	public Story(int id, String name, String url, String comment) {
		super();
		this.id = id;
		this.name = name;
		this.url = url;
		this.comment = comment;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	
}
