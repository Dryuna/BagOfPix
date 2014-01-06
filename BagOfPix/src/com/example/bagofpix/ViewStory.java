package com.example.bagofpix;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

public class ViewStory extends Activity {

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
