package com.example.instagramexample;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.app.ProgressDialog;
import android.content.Context;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import com.example.instagramexample.InstagramDialog.OAuthDialogListener;


public class InstagramApp {

    private InstagramSesion mSession;
    private InstagramDialog mDialog;
    private OAuthAuthenticationListener mListener;
    private ProgressDialog mProgress;
    private String mAuthUrl;
    private String mTokenUrl;
    private String mAccessToken;
    public Context mCtx;

    private String mClientId;
    private String mClientSecret;

    private static int WHAT_FINALIZE = 0;
    private static int WHAT_ERROR = 1;
    private static int WHAT_FETCH_INFO = 2;


    public static String mCallbackUrl = "";
    private static final String AUTH_URL = "https://api.instagram.com/oauth/authorize/";
    private static final String TOKEN_URL = "https://api.instagram.com/oauth/access_token";
    private static final String API_URL = "https://api.instagram.com/v1";

    private static final String TAG = "InstagramAPI";
    public ArrayList<InstagramClient> listPhoto;
    
    public InstagramApp(Context context, String clientId, String clientSecret,
            String callbackUrl) {

        mClientId = clientId;
        mClientSecret = clientSecret;
        mCtx = context;
        mSession = new InstagramSesion(context);
        mAccessToken = mSession.getAccessToken();
        mCallbackUrl = callbackUrl;
	        mTokenUrl = TOKEN_URL + "?client_id=" + clientId + "&client_secret="
	                + clientSecret + "&redirect_uri=" + mCallbackUrl
	                + "&grant_type=authorization_code";
	        mAuthUrl = AUTH_URL
	                + "?client_id="
	                + clientId
	                + "&redirect_uri="
	                + mCallbackUrl
	                + "&response_type=code&display=touch&scope=likes+comments+relationships";

        OAuthDialogListener listener = new OAuthDialogListener() {
            @Override
            public void onComplete(String code) {
                getAccessToken(code);
            }

            @Override
            public void onError(String error) {
                mListener.onFail("Authorization failed");
            }
        };

        mDialog = new InstagramDialog(context, mAuthUrl, listener);
        //this.getPhotos();
        
        mProgress = new ProgressDialog(context);
        mProgress.setCancelable(false);
    }

    public void getPhotos(){
    	new Thread(){
    		@Override
    		public void run(){
    			Log.i(TAG, "Getting photos...");
                int state = WHAT_FETCH_INFO;
    			try{
    				URL url = new URL(API_URL + "/tags/selfie/media/recent/"+"?client_id=" + mClientId);
    	
    				Log.i(TAG, "Opening Token URL " + url.toString());
                    HttpURLConnection urlConnection = (HttpURLConnection) url
                            .openConnection();
                    urlConnection.setRequestMethod("GET");
                    urlConnection.connect();
                    
                    listPhoto = new ArrayList<InstagramClient>();
                    if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    	String response = streamToString(urlConnection
                            .getInputStream());
                    	Log.i(TAG, "response " + response);
                    
                 
                    	JSONObject jsonObj = (JSONObject) new JSONTokener(response)
                            .nextValue();

                    	JSONArray items = jsonObj.getJSONArray("data");
                    	 for(int i=0;i<items.length();i++) {
                    	     JSONObject c=(JSONObject) items.get(i);
                    	     JSONObject user = c.getJSONObject("user");
                    	     String name= user.getString("username");
                    	     
                    	     JSONObject img=c.getJSONObject("images");
                    	     JSONObject thum=img.getJSONObject("low_resolution");
                    	     String urlOfPic = thum.getString("url");
                    	     
                    	     JSONObject caption=c.getJSONObject("caption");
                    	     String textPic = caption.getString("text");

                    	     listPhoto.add(new InstagramClient(urlOfPic,name,textPic));
                    	 }
                    }
    			}
    			catch(Exception ex) {
                    state = WHAT_ERROR;
                    ex.printStackTrace();
                }

                mHandler.sendMessage(mHandler.obtainMessage(state, 1, 0));
    		}
    	}.start();
    }
    private void getAccessToken(final String code) {
        mProgress.setMessage("Getting access token ...");
        mProgress.show();

        new Thread() {
            @Override
            public void run() {
                Log.i(TAG, "Getting access token");
                int state = WHAT_FETCH_INFO;
                try {
                    URL url = new URL(TOKEN_URL);
                    Log.i(TAG, "Opening Token URL " + url.toString());
                    HttpURLConnection urlConnection = (HttpURLConnection) url
                            .openConnection();
                    urlConnection.setRequestMethod("POST");
                    urlConnection.setDoInput(true);
                    urlConnection.setDoOutput(true);

                    OutputStreamWriter writer = new OutputStreamWriter(
                            urlConnection.getOutputStream());
                    writer.write("client_id=" + mClientId + "&client_secret="
                            + mClientSecret + "&grant_type=authorization_code"
                            + "&redirect_uri=" + mCallbackUrl + "&code=" + code);
                    writer.flush();
                    String response = streamToString(urlConnection
                            .getInputStream());
                    Log.i(TAG, "response " + response);
                    JSONObject jsonObj = (JSONObject) new JSONTokener(response)
                            .nextValue();

                    mAccessToken = jsonObj.getString("access_token");
                    Log.i(TAG, "Got access token: " + mAccessToken);

                    String id = jsonObj.getJSONObject("user").getString("id");
                    String user = jsonObj.getJSONObject("user").getString(
                            "username");
                    String name = jsonObj.getJSONObject("user").getString(
                            "full_name");
                    String userImage = jsonObj.getJSONObject("user").getString(
                            "profile_picture");
                    mSession.storeAccessToken(mAccessToken, id, user, name,
                            userImage);

                } catch (Exception ex) {
                    state = WHAT_ERROR;
                    ex.printStackTrace();
                }

                mHandler.sendMessage(mHandler.obtainMessage(state, 1, 0));
            }
        }.start();
    }

   
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == WHAT_ERROR) {
                mProgress.dismiss();
                if (msg.arg1 == 1) {
                    mListener.onFail("Failed to get access token");
                } else if (msg.arg1 == 2) {
                    mListener.onFail("Failed to get user information");
                }
            } else if (msg.what == WHAT_FETCH_INFO) {
                mProgress.dismiss();
                mListener.onSuccess();
            } else {
            	Log.i(TAG, "Error " + msg.toString());
            }
        }
    };

    public boolean hasAccessToken() {
        return (mAccessToken == null) ? false : true;
    }

    public void setListener(OAuthAuthenticationListener listener) {
        mListener = listener;
    }

    // getting username
    public String getUserName() {
        return mSession.getUsername();
    }

    // getting user id
    public String getId() {
        return mSession.getId();
    }

    // getting username
    public String getName() {
        return mSession.getName();
    }

    // getting user image
    public String getUserPicture() {
        return mSession.getUserImage();
    }

    // getting accesstoken
    public String getAccessToken() {
        return mSession.getAccessToken();
    }

    public void authorize() {

        mDialog.show();
    }

    private String streamToString(InputStream is) throws IOException {
        String str = "";

        if (is != null) {
            StringBuilder sb = new StringBuilder();
            String line;

            try {
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(is));

                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }

                reader.close();
            } finally {
                is.close();
            }

            str = sb.toString();
        }

        return str;
    }

    public void resetAccessToken() {
        if (mAccessToken != null) {
            mSession.resetAccessToken();
            mAccessToken = null;
        }
    }

    public interface OAuthAuthenticationListener {
        public abstract void onSuccess();

        public abstract void onFail(String error);
    }
}