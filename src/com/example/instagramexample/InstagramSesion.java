package com.example.instagramexample;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;


public class InstagramSesion {

    private SharedPreferences sharedPref;
    private Editor editor;

    private static final String SHARED = "Instagram_Preferences";
    private static final String API_USERNAME = "username";
    private static final String API_ID = "id";
    private static final String API_NAME = "name";
    private static final String API_ACCESS_TOKEN = "access_token";
    private static final String API_USER_IMAGE = "user_image";

    public InstagramSesion(Context context) {
        sharedPref = context.getSharedPreferences(SHARED, Context.MODE_PRIVATE);
        editor = sharedPref.edit();
    }

    public void storeAccessToken(String accessToken, String id,
            String username, String name, String image) {
        editor.putString(API_ID, id);
        editor.putString(API_NAME, name);
        editor.putString(API_ACCESS_TOKEN, accessToken);
        editor.putString(API_USERNAME, username);
        editor.putString(API_USER_IMAGE, image);
        editor.commit();
    }

    public void storeAccessToken(String accessToken) {
        editor.putString(API_ACCESS_TOKEN, accessToken);
        editor.commit();
    }

    public void resetAccessToken() {
        editor.putString(API_ID, null);
        editor.putString(API_NAME, null);
        editor.putString(API_ACCESS_TOKEN, null);
        editor.putString(API_USERNAME, null);
        editor.putString(API_USER_IMAGE, null);
        editor.commit();
    }

    public String getUsername() {
        return sharedPref.getString(API_USERNAME, null);
    }

    public String getId() {
        return sharedPref.getString(API_ID, null);
    }

    public String getName() {
        return sharedPref.getString(API_NAME, null);
    }

    public String getAccessToken() {
        return sharedPref.getString(API_ACCESS_TOKEN, null);
    }

    public String getUserImage() {
        return sharedPref.getString(API_USER_IMAGE, null);
    }
}
