package com.example.bagofpix;

import android.R.layout;
import android.net.Uri;
import android.os.Bundle;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.support.v4.app.NavUtils;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.provider.MediaStore;

public class ImportPhoto extends Activity {
	private static final int SELECT_PHOTO = 100;
	private String url = null;
	private int story_id = -1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_import_photo);
		setupActionBar();
		// Display gallery picker first
		loadGallery();
		Intent intent = getIntent();
		story_id = Integer.parseInt(intent.getStringExtra("storyId"));
	}

	public void loadGallery() {
		Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
		photoPickerIntent.setType("image/*");
		startActivityForResult(photoPickerIntent, SELECT_PHOTO);
	}

	public void save(View v) {
		if (url == null)
			return;
		EditText textBox = (EditText) findViewById(R.id.comment);
		String comment = textBox.getText().toString();
		
		DBHandler db = new DBHandler();
		db.insert_photo(story_id, url, comment);
		
		Intent intent = new Intent(this, ViewStory.class);
		intent.putExtra("storyId", story_id+""); 
		startActivity(intent);
		
		//Intent intent = new Intent(this, MainActivity.class);
		//startActivity(intent);
	}

	@SuppressLint("NewApi")
	protected void onActivityResult(int requestCode, int resultCode,
			Intent imageReturnedIntent) {
		super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
		switch (requestCode) {
		case SELECT_PHOTO:
			if (resultCode == RESULT_OK) {
				Uri selectedImage = imageReturnedIntent.getData();
				String[] filePathColumn = { MediaStore.Images.Media.DATA };
				Cursor cursor = getContentResolver().query(selectedImage,
						filePathColumn, null, null, null);
				cursor.moveToFirst();
				int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
				url = cursor.getString(columnIndex);
				cursor.close();
				ImageView imView = new ImageView(this);
				LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
				imView.setLayoutParams(lp);
				Bitmap bm = BitmapFactory.decodeFile(url);
				imView.setImageBitmap(bm);
				LinearLayout ll = (LinearLayout) findViewById(R.id.image_container);
				ll.addView(imView);
			}
		}
	}

	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}