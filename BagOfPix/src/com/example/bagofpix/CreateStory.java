package com.example.bagofpix;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;

public class CreateStory extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_story);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.create_story, menu);
		return true;
	}
	
	public void createStory(View view) {
		// TODO
	}

}
