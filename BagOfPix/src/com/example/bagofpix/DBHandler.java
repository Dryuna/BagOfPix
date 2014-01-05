package com.example.bagofpix;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DBHandler {
	Context context;
	
	public DBHandler(Context context){
		this.context = context;
	}
	
	public ArrayList<Story> get_stories(){
		DBReader mDbHelper = new DBReader(context);
		SQLiteDatabase db = mDbHelper.getWritableDatabase();
		
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
		DBReader mDbHelper = new DBReader(context);
		SQLiteDatabase db = mDbHelper.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put("name", name);
		values.put("url", url);
		values.put("comment", comment);
		
		db.insert("Story", null ,values);
	}
	
	public ArrayList<Photo> get_photos(int storyID){
		
		return null;
	}
	
	public void insert_photo(int storyID, String url, String comment){
		
	}
}
