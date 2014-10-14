package com.example.instagramexample;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ImageAdapter extends ArrayAdapter<InstagramClient>{

    private ArrayList<InstagramClient> imageURLArray;

    private LayoutInflater inflater;

    public ImageAdapter(Context context, int textViewResourceId,
    		ArrayList<InstagramClient> imageArray) {
        super(context, textViewResourceId, imageArray);

        inflater = ((Activity)context).getLayoutInflater();
        imageURLArray = imageArray;
    }

    private static class ViewHolder {
        ImageView imageView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if(convertView == null) {
            convertView = inflater.inflate(R.layout.image_list, null);
            viewHolder = new ViewHolder();
            viewHolder.imageView = (ImageView)convertView.findViewById(R.id.postThumb);
            convertView.setTag(viewHolder);
        }
        viewHolder = (ViewHolder)convertView.getTag();

        InstagramClient client = imageURLArray.get(position);
        new DownloadImageTask((ImageView) viewHolder.imageView).execute(client.getUrlPhoto());

        TextView detail =  (TextView)convertView.findViewById(R.id.postTitleLabel);
        detail.setText(client.getDetailPhoto());
        
        TextView name =  (TextView)convertView.findViewById(R.id.postDateLabel);
        name.setText(client.getNamePhoto());
        
        return convertView;
    }
}

