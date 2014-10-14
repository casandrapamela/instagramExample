package com.example.instagramexample;

import java.io.Serializable;
import java.util.ArrayList;

public class ArrayListSerializable implements Serializable {

	private ArrayList<InstagramClient> parliaments;

	public ArrayListSerializable(ArrayList<InstagramClient> data) {
		this.parliaments = data;
	}

	public ArrayList<InstagramClient> getParliaments() {
		return this.parliaments;
	}

}

