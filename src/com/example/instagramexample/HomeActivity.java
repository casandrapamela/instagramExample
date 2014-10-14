package com.example.instagramexample;

import java.util.ArrayList;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.os.Build;
import com.example.instagramexample.DownloadImageTask;

public class HomeActivity extends ActionBarActivity {

	private String strNameUser;
	private String strPhotoUser;
	private ArrayList<InstagramClient> listPhoto;
	 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);

		Intent intent = getIntent();
		strNameUser = intent.getStringExtra(MainActivity.EXTRA_NAME_USER);
		strPhotoUser = intent.getStringExtra(MainActivity.EXTRA_USER_PHOTO);
		ArrayListSerializable tmpSerializable= (ArrayListSerializable)intent.getSerializableExtra("mApp.listPhoto");
		listPhoto = tmpSerializable.getParliaments();
		
		TextView textNameUser = (TextView) findViewById(R.id.textNameUser);
		textNameUser.setText("Welcome back!! " + strNameUser);
		
		new DownloadImageTask((ImageView) findViewById(R.id.imagePhotoUser)).execute(strPhotoUser);

		ListView listView = (ListView)this.findViewById(R.id.postListView);
		ImageAdapter imageAdapter = new ImageAdapter(this, R.layout.image_list, listPhoto);
		listView.setAdapter(imageAdapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_home, container,
					false);
			return rootView;
		}
	}

}
