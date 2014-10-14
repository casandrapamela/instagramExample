package com.example.instagramexample;

public class InstagramClient {
	private String urlPhoto;
	private String namePhoto;
	private String detailPhoto;
	
	public InstagramClient (String strUrlPhoto, String strNamePhoto,String strDetailPhoto){
		this.urlPhoto = strUrlPhoto;
		this.namePhoto = strNamePhoto;
		this.detailPhoto = strDetailPhoto;
	}
	
	public String getUrlPhoto() {
	    return this.urlPhoto;
	}

	public String getNamePhoto() {
	    return this.namePhoto;
	}

	public String getDetailPhoto() {
	    return this.detailPhoto;
	}
}