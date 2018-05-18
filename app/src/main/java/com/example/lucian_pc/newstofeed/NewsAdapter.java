package com.example.lucian_pc.newstofeed;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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

        TextView newsTitle = convertView.findViewById(R.id.newsTitle);
        String newsTitleString = currentNews.getNewsTitle();
        newsTitle.setText(newsTitleString);

        TextView newsCategory = convertView.findViewById(R.id.newsCategory);
        String newsCategoryString = currentNews.getNewsCategory();
        newsCategory.setText(newsCategoryString);

        TextView newsAuthor = convertView.findViewById(R.id.newsAuthor);
        String newsAuthorString = currentNews.getNewsAuthor();
        newsAuthor.setText(newsAuthorString);

        TextView newsDate = convertView.findViewById(R.id.newsDate);
        String newsDateString = currentNews.getNewsDate();
        newsDate.setText(newsDateString);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date newsDateFormat = dateFormat.parse(currentNews.getNewsDate());
            String date = dateFormat.format(newsDateFormat);
        } catch (ParseException e) {
            Log.e("Date formatting error", "Error Message: " + e.getMessage());
            e.printStackTrace();
        }

        return convertView;
    }
}
