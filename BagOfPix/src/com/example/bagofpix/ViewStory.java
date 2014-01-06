package com.example.bagofpix;

import java.util.ArrayList;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ViewStory extends Activity {

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_story);
		Intent intent = getIntent();
		int storyId = Integer.parseInt(intent.getStringExtra("storyId"));
		DBHandler db = new DBHandler();
		Story story = db.get_story(storyId);
		TextView textView = (TextView) findViewById(R.id.story_name_view);
		textView.setText(story.getName());
		LinearLayout ll = (LinearLayout) findViewById(R.id.view_story_scroll);
		ArrayList<Photo> photos = db.get_photos(storyId);
		for (int i = 0; i < photos.size(); i++) {
			LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			lp.setMargins(10,10,10,10);
			
			ImageView imView = new ImageView(this);
			String imgUrl = photos.get(i).getUrl();
			Bitmap bm = BitmapFactory.decodeFile(imgUrl);
			imView.setImageBitmap(bm);
			imView.setLayoutParams(lp);
			ll.addView(imView);
			TextView tView = new TextView(this);
			String imgComment = photos.get(i).getComment();
			tView.setText(imgComment);
			tView.setGravity(Gravity.CENTER);
			tView.setLayoutParams(lp);
			ll.addView(tView);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.view_story, menu);
		return true;
	}
	
	public void deleteStory(View view) {
		Intent intent = getIntent();
		int storyId = Integer.parseInt(intent.getStringExtra("storyId"));
		DBHandler db = new DBHandler();
		db.delete_story(storyId);
		Intent i = new Intent(this, MainActivity.class);
		startActivity(i);
	}
	
	public void importPhoto(View view) {
		Intent intent = getIntent();
		int storyId = Integer.parseInt(intent.getStringExtra("storyId"));
		Intent i = new Intent(this, ImportPhoto.class);
		i.putExtra("storyId", storyId + "");
		startActivity(i);
	}

}
