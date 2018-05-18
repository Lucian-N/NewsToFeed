package com.example.lucian_pc.newstofeed;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<News>> {

    public static final String LOG_TAG = MainActivity.class.getName();

    // Constant value for news loader ID
    private static final int NEWS_LOADER_ID = 1;
    private static final String GUARDIAN_API = "https://content.guardianapis.com/search?show-tags=contributor&api-key=57e90dbb-ecba-4eae-8ee7-780afa76112c";

    // TextView to display when list is empty
    private TextView mEmptyStateTextView;

    // Adapter for newsList
    private NewsAdapter newsAdapter;
    private ListView newsView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Find ListView reference in layout
        ListView newsListView = (ListView) findViewById(R.id.newsList);

        // Create new Adapter with empty list of news as input
        newsAdapter = new NewsAdapter(this, new ArrayList<News>());

        // Set adapter on ListView in order to populate list
        newsListView.setAdapter(newsAdapter);

        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        newsListView.setEmptyView(mEmptyStateTextView);

        // Set ItemClickListener on ListView to send access web browser
        newsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                // Find current News Object clicked on
                News currentNews = newsAdapter.getItem(position);

                // Convert String URL into a URI object to pass intent into
                Uri newsUri = Uri.parse(currentNews.getNewsUrl());

                // Convert new intent to view News URI
                Intent webIntent = new Intent(Intent.ACTION_VIEW, newsUri);

                // Send intent to launch activity
                startActivity(webIntent);
            }
        });

        // Get reference to ConnectivityManager to check Network status
        ConnectivityManager connectivityManager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get Details on current active data network
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        // if Network connection exists, fetch data
        if (networkInfo != null && networkInfo.isConnected()) {
            LoaderManager loaderManager = getLoaderManager();

            loaderManager.initLoader(NEWS_LOADER_ID, null, this);
        } else {
            View loadingIndicator = findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.GONE);
            mEmptyStateTextView.setText(R.string.no_internet_connection);
        }
    }

    @Override
    public Loader<List<News>> onCreateLoader(int i, Bundle bundle) {
        // Create a new Loader for given URL
        return new NewsLoader(this, GUARDIAN_API);
    }

    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> newsList) {
        // Hide loading indicator for Loaded Datum
        View loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);

        // Empty state to display if no News foound
        mEmptyStateTextView.setText(R.string.no_news);

        // Clear adapter of previous news data
        newsAdapter.clear();

        // If valid news list exits then add them to adapter data, triggers Listview update
        if (newsList != null && !newsList.isEmpty()) {
            newsAdapter.addAll(newsList);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<News>> loader) {
        // Loader reset, clear existing data
        newsAdapter.clear();
    }
}
