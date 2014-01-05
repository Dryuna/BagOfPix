package com.example.bagofpix;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

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
		EditText et1 = (EditText) findViewById(R.id.story_name);
		String name = et1.getText().toString();
		EditText et2 = (EditText) findViewById(R.id.story_description);
		String description = et2.getText().toString();
		if (!(name.equals("") || description.equals(""))) {
			DBHandler db = new DBHandler();
			db.create_story(name, "", description);
			Intent intent = new Intent(this, MainActivity.class);
			startActivity(intent);
		} else {
			Toast.makeText(this, "One of the parameters is missing", 3000).show();
		}
	}

}
