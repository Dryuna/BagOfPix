package com.example.bagofpix;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DBHandler {
	static Context context;
	DBReader mDbHelper;
	SQLiteDatabase db;
	
	public DBHandler(){
		mDbHelper = new DBReader(context);
		db = mDbHelper.getWritableDatabase();	
	}
	
	public ArrayList<Story> get_stories(){
		String[] projection = {"id", "name", "url", "comment"};
		
		Cursor c = db.query("Story",  projection, null, null, null, null, null);
		
		ArrayList<Story> stories = new ArrayList<Story>();
		c.moveToFirst();
		while(!c.isAfterLast()){
			int id = c.getInt(c.getColumnIndexOrThrow("id"));
			String name, url, comment;
			name = c.getString(c.getColumnIndexOrThrow("name"));
			url = c.getString(c.getColumnIndexOrThrow("url"));
			comment = c.getString(c.getColumnIndexOrThrow("comment"));
			
			stories.add(new Story(id, name, url, comment));
			c.moveToNext();
		}
		
		return stories;
	}
	
	public void create_story(String name, String url, String comment){
		ContentValues values = new ContentValues();
		values.put("name", name);
		values.put("url", url);
		values.put("comment", comment);
		
		db.insert("Story", null ,values);
	}
	
	public void delete_story(int storyID){
		// Define 'where' part of query.
		String selection = "storyId";
		// Specify arguments in placeholder order.
		String[] selectionArgs = {storyID+""};
		// Issue SQL statement.
		db.delete("Story", selection, selectionArgs);
	}

	public ArrayList<Photo> get_photos(int storyID){
		String[] projection = {"id", "url", "comment"};
		String selection = "storyId";
		String[] selectionArgs = {""+storyID};
		
		Cursor c = db.query("Story",  projection, selection, selectionArgs, null, null, null);
		
		ArrayList<Photo> photos = new ArrayList<Photo>();
		c.moveToFirst();
		while(!c.isAfterLast()){
			int id = c.getInt(c.getColumnIndexOrThrow("id"));
			String url, comment;
			url = c.getString(c.getColumnIndexOrThrow("url"));
			comment = c.getString(c.getColumnIndexOrThrow("comment"));
			
			photos.add(new Photo(id, storyID, url, comment));
			c.moveToNext();
		}
		
		return photos;
	}
	
	public void insert_photo(int storyID, String url, String comment){
		ContentValues values = new ContentValues();
		values.put("storyId", storyID);
		values.put("url", url);
		values.put("comment", comment);
		
		db.insert("Photo", null ,values);
	}
	
	public void delete_photo(int photoID){
		// Define 'where' part of query.
		String selection = "photoId";
		// Specify arguments in placeholder order.
		String[] selectionArgs = {photoID+""};
		// Issue SQL statement.
		db.delete("Photo", selection, selectionArgs);
	}
	
	public Story get_story(int storyId) {
		ArrayList<Story> stories = get_stories();
		for (int i = 0; i < stories.size(); i++) {
			if (stories.get(i).getId() == storyId) {
				return stories.get(i); 
			}
		}
		return null;
	}
}
