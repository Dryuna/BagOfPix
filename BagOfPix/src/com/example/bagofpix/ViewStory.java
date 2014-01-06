package com.example.bagofpix;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.Request.Callback;

public class ViewStory extends Activity {

	Story story;
	DBHandler db;
	
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_story);
		Intent intent = getIntent();
		int storyId = Integer.parseInt(intent.getStringExtra("storyId"));
		db = new DBHandler();
		story = db.get_story(storyId);
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
			tView.setLayoutParams(lp);
			ll.addView(tView);
		}
		Button share = (Button) findViewById(R.id.share);
		share.setOnClickListener(shareListener);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.view_story, menu);
		return true;
	}
	
	OnClickListener shareListener = new OnClickListener() {
		
		JSONArray albums;
		
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			if (MainActivity.session==null){
				MainActivity.session = startSession();
			}
			
		}
		
		public JSONObject findAlbum(){
			try {
				for (int i=0;i<albums.length();i++){
					JSONObject jo;
					jo = albums.getJSONObject(i); 
					if (jo.getString("name").equals(ViewStory.this.story.getName()) && 
							jo.getString("description").equals(ViewStory.this.story.getComment())){
						return jo;
					}
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
		
		Request.Callback getAlbums = new Request.Callback() {
			
			JSONObject jo;
			
			@Override
			public void onCompleted(Response response) {
				// TODO Auto-generated method stub
				try {
					albums = response.getGraphObject().getInnerJSONObject().getJSONArray("data");
					jo = findAlbum();
					if (jo==null){
						Toast.makeText(ViewStory.this, "Could not find album!", Toast.LENGTH_LONG).show();
						createAlbum();
					}
					else uploadImagesToAlbum(jo);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					Toast.makeText(ViewStory.this, "Could not fetch albums!", Toast.LENGTH_LONG).show();
				}
			}

			private void createAlbum() {
				// TODO Auto-generated method stub
				Bundle params = new Bundle();
				params.putString("name", ViewStory.this.story.getName());
				params.putString("message", ViewStory.this.story.getComment());
				Request request = new Request(MainActivity.session, "me/albums", params, 
                        HttpMethod.POST);
				request.setCallback(new Request.Callback() {
					@Override
					public void onCompleted(Response response) {
						// TODO Auto-generated method stub
						try {
							jo = response.getGraphObject().getInnerJSONObject().getJSONArray("data").getJSONObject(0);
							Toast.makeText(ViewStory.this, "ALBUM CREATION COMPLETED\n", Toast.LENGTH_LONG).show();
							uploadImagesToAlbum(jo);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							Toast.makeText(ViewStory.this, "ALBUM CREATION FAILED\n", Toast.LENGTH_LONG).show();
						} 
						
					}
				});
				request.executeAsync();
			}

			private void uploadImagesToAlbum(JSONObject jo) throws JSONException {
				// TODO Auto-generated method stub
				ArrayList<Photo> photos = db.get_photos(ViewStory.this.story.getId());
				for (int i = 0; i < photos.size(); i++) {
					String imgUrl = photos.get(i).getUrl();
					Bitmap bmp = BitmapFactory.decodeFile(imgUrl);
					String imgComment = photos.get(i).getComment();
					ByteArrayOutputStream stream = new ByteArrayOutputStream();
					bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
					byte[] byteArray = stream.toByteArray();
					Bundle params = new Bundle();
					params.putByteArray("source", byteArray);
					params.putString("message", imgComment);
					Request rr = new Request(MainActivity.session, jo.getString("id")+"/photos", params, HttpMethod.POST);
					rr.setCallback(new Callback() {
						
						@Override
						public void onCompleted(Response response) {
							// TODO Auto-generated method stub
							Toast.makeText(ViewStory.this, "PHOTO ADDED SUCESSFULLY", Toast.LENGTH_LONG).show();
						}
					});
					rr.executeAsync();
				}

			}
		};

		private Session startSession() {
			// TODO Auto-generated method stub
			return Session.openActiveSession(ViewStory.this, true, new Session.StatusCallback() {
				
				@Override
				public void call(Session session, SessionState state, Exception exception) {
					Toast.makeText(ViewStory.this, "STEP 1: "+session.getAccessToken()+"\n"+session.getState(), Toast.LENGTH_LONG).show();
					// TODO Auto-generated method stub
					if (session.isOpened()){
						Toast.makeText(ViewStory.this, "OPENED: "+session.getAccessToken()+"\n"+session.getApplicationId(), Toast.LENGTH_LONG).show();
						Request request = new Request(MainActivity.session, "me/albums", null, 
			                    HttpMethod.GET, getAlbums);
						request.executeAsync();
					}
				}
			});
		}
	};
	
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
