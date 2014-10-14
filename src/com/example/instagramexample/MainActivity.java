package com.example.instagramexample;
//Example Pamela Sanchez
import java.util.ArrayList;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Build;

import com.example.instagramexample.InstagramApp.OAuthAuthenticationListener;
import com.example.instagramexample.InstagramDialog;
import com.example.instagramexample.HomeActivity;

public class MainActivity extends ActionBarActivity {

	private InstagramApp mApp;
	private int state;
	
	public final static int AUTHORIZATION = 1;
	public final static int GALERY_HOME = 2;
	
	public final static String EXTRA_NAME_USER = "com.example.instagramexample.NAME_USER";
	public final static String EXTRA_USER_PHOTO = "com.example.instagramexample.USER_PHOTO";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		state = AUTHORIZATION;
		
		mApp = new InstagramApp(this, ClientInformation.CLIENT_ID,
				ClientInformation.CLIENT_SECRET, ClientInformation.CALLBACK_URL);
		OAuthAuthenticationListener listener = new OAuthAuthenticationListener() {

			@Override
			public void onSuccess() {
				//Open second activity
				if(state == AUTHORIZATION){
					 mApp.getPhotos();
					 state = GALERY_HOME;
				}else if(state == GALERY_HOME){
					System.out.println("Connected as " + mApp.getUserName());
					System.out.println("Disconnect");
					Intent myIntent = new Intent(mApp.mCtx, HomeActivity.class);
					myIntent.putExtra(EXTRA_NAME_USER,mApp.getUserName());
					myIntent.putExtra(EXTRA_USER_PHOTO, mApp.getUserPicture());
					myIntent.putExtra("mApp.listPhoto", new ArrayListSerializable(mApp.listPhoto));
					
			        startActivity(myIntent);
				}
				
			}

			@Override
			public void onFail(String error) {
				Toast.makeText(MainActivity.this, error, Toast.LENGTH_SHORT).show();
			}
		};

		mApp.setListener(listener);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
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
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			return rootView;
		}
	}
	 public void loginAction(View view) {
	     // Go to Instagram 
		 if(state == AUTHORIZATION){
			 mApp.authorize();
		 }
	 }

}
