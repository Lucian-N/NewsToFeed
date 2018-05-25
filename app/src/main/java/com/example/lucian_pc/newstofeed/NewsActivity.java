package com.example.lucian_pc.newstofeed;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class NewsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<News>> {

    public static final String LOG_TAG = NewsActivity.class.getName();

    // Constant value for news loader ID
    private static final int NEWS_LOADER_ID = 1;
    //private static final String GUARDIAN_API = "https://content.guardianapis.com/search?show-tags=contributor&api-key=57e90dbb-ecba-4eae-8ee7-780afa76112c";
    private static final String GUARDIAN_API = "https://content.guardianapis.com/search?";

    // TextView to display when list is empty
    private TextView mEmptyStateTextView;

    // Adapter for newsList
    private NewsAdapter newsAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Find ListView reference in layout
        ListView newsListView = findViewById(R.id.newsList);

        mEmptyStateTextView = findViewById(R.id.empty_view);
        newsListView.setEmptyView(mEmptyStateTextView);

        // Create new Adapter with empty list of news as input
        newsAdapter = new NewsAdapter(this, new ArrayList<News>());

        // Set adapter on ListView in order to populate list
        newsListView.setAdapter(newsAdapter);

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
    // Create a new Loader for given URL
    public Loader<List<News>> onCreateLoader(int id, Bundle args) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        //Retrieve value from preferences or use default
        String nr_of_news = sharedPreferences.getString(getString(R.string.news_number),
                getString(R.string.news_number_default));
        String orderByDate = sharedPreferences.getString(getString(R.string.order_by_key),
                getString(R.string.order_by_default));
        String sortBySection = sharedPreferences.getString(getString(R.string.sort_by_key),
                getString(R.string.sort_by_default));

        // Splits passed URI string in order to add search parameters
        Uri baseUri = Uri.parse(GUARDIAN_API);
        Uri.Builder uriBuilder = baseUri.buildUpon();
        // Append query parameter and its value.
        if (!sortBySection.equals(getString(R.string.all))) {
            uriBuilder.appendQueryParameter(getString(R.string.section), sortBySection);
        }
        uriBuilder.appendQueryParameter(getString(R.string.show_tags), getString(R.string.author));
        uriBuilder.appendQueryParameter(getString(R.string.order_by), orderByDate);
        uriBuilder.appendQueryParameter(getString(R.string.sort_by), nr_of_news);
        uriBuilder.appendQueryParameter(getString(R.string.api_key), getString(R.string.api_key_id));

        // Return the completed uri
        return new NewsLoader(this, uriBuilder.toString());
    }


    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> newsList) {
        // Hide loading indicator for Loaded Datum
        View loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);

        // Empty state to display if no News found
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

    @Override
    // This method initialize the contents of the Activity's options menu.
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the Options Menu we specified in XML
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    // Pass selected menu item
    public boolean onOptionsItemSelected(MenuItem item) {
        // Determine what item is selected
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            // Open activity via intent for matching menu
            Intent settingsIntent = new Intent(this, NewsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
