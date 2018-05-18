package com.example.lucian_pc.newstofeed;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class NewsAdapter extends ArrayAdapter<News> {

    // News Adapter constructor
    public NewsAdapter(Context context, ArrayList<News> newsList) {
        super(context, 0, newsList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.news_list_item, parent, false);
        }
        // get News at current position
        News currentNews = getItem(position);

        /**
         *Find respective resource files to update from xml
         *Update news information with image, title, date and URL
         */

        TextView newsTitle = (TextView) convertView.findViewById(R.id.newsTitle);
        String newsTitleString = currentNews.getNewsTitle();

        return convertView;
    }
}
