package com.example.lucian_pc.newstofeed;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

public class NewsLoader extends AsyncTaskLoader<List<News>> {

    // Tag to log messages
    private static final String LOG_TAG = NewsLoader.class.getName();

    private String newsUrl;

    // Constructor for activity context and url to load data from
    public NewsLoader(Context context, String url) {
        super(context);
        newsUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    // Background Thread
    @Override
    public List<News> loadInBackground() {
        if (newsUrl == null) {
            return null;
        }

        //Network request to parse response and extract news list
        List<News> newsList = QueryUtils.fetchNewsData(newsUrl);

        return newsList;
    }
}
