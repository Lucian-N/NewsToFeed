package com.example.lucian_pc.newstofeed;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Helper methods related to requesting and receiving news data from Guardian API.
 */
public final class QueryUtils {

    //  Tag for the log messages
    private static final String LOG_QUERY = QueryUtils.class.getSimpleName();
    // Constants in milliseconds for URL connection
    private static final int READ_TIMEOUT = 10000;
    private static final int CONNECT_TIMEOUT = 15000;
    // GET request OK status constant
    private static final int GET_OK = 200;

    // Create private constructor to hold static variables and methods
    private QueryUtils() {
    }

    // Return list of News Objects built from JSON parsing of server response
    private static List<News> extractFeatureFromJson(String newsJSON) {
        // Sanity check for null
        if (TextUtils.isEmpty(newsJSON)) {
            return null;
        }
        // Create empty ArrayList to populate with News objects
        List<News> newsList = new ArrayList<>();
        // Try to parseJSON
        try {
            // Create JSON object from JSON response string
            JSONObject baseJsonResponse = new JSONObject(newsJSON);

            // JSON object associated with response
            JSONObject responseJson = baseJsonResponse.getJSONObject("response");

            // Extract JSON array associated with 'features'
            JSONArray newsArray = responseJson.getJSONArray("results");

            // For each News item in NewsArray create an object
            for (int i = 0; i < newsArray.length(); i++) {

                // Get single News at index position within the newsArray
                JSONObject currentNews = newsArray.getJSONObject(i);

                // For any given News found, extract associated JSON key
                String newsSectionName = currentNews.getString("sectionName");

                // Extract values for keys
                // Check if date is available
                String newsDate = "Date Unavailable";
                if (currentNews.has("webPublicationDate")) {
                    newsDate = currentNews.getString("webPublicationDate");
                }

                String newsTitle = currentNews.getString("webTitle");
                String newsUrl = currentNews.getString("webUrl");

                // Extract Author Array - tags
                JSONArray newsAuthorArray = currentNews.getJSONArray("tags");

                String newsAuthor = "Author Unavailable";
                if (newsAuthorArray.length() == 1) {
                    JSONObject newsAuthorObject = newsAuthorArray.getJSONObject(0);
                    newsAuthor = newsAuthorObject.getString("webTitle");
                }

                // Create News object with retrieved JSON keys
                News newsObject = new News(newsTitle);
                // Add previously created News object to newsList
                newsList.add(newsObject);
            }

        } catch (JSONException e) {
            // Catch any errors and print message to log
            Log.e(LOG_QUERY, "JSON parse error");
        }

        // Returns list of news
        return newsList;
    }

    // Returns a new URL object from given URL String
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_QUERY, "Problem with URL", e);
        }
        return url;
    }

    // GET JSON Responce from API and convert to String
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If empty URL return early
        if (url == null) {
            return jsonResponse;
        }
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(READ_TIMEOUT);
            urlConnection.setConnectTimeout(CONNECT_TIMEOUT);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If successful request read input stream and parse response
            if (urlConnection.getResponseCode() == GET_OK) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_QUERY, "Error connecting to URL, response code:"
                        + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_QUERY, "Problem retrieving news JSON object from Guardian API.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }

        return jsonResponse;
    }

    // Static method to read each line from stream and append each line to a String
    private static String readFromStream(InputStream inputStream) throws IOException {

        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    // Query Guardian API and return list of News Objects.
    public static List<News> fetchNewsData(String requestUrl) {

        // Create URL obj
        URL url = createUrl(requestUrl);
        // HTTP request to URL and receive JSON response
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_QUERY, "Problem with HTTP request", e);
        }
        // Extract fields from JSOn response to create a new list of News objects
        List<News> news = extractFeatureFromJson(jsonResponse);

        // Return list of news
        return news;
    }


}
