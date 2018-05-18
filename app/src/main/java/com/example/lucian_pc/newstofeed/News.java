package com.example.lucian_pc.newstofeed;

public class News {


    private String newsAuthor;
    private String newsCategory;
    private String newsDate;
    private String newsTitle;
    private String newsUrl;
    private int newsImage = NO_IMAGE;
    private static final int NO_IMAGE = 1;

    // Custom news class Arguments
    public News(String newsDateId, String newsTitleId, String newsUrlId,
                int newsImageId, String newsAuthorId, String newsCategoryId) {
        newsDate = newsDateId;
        newsTitle = newsTitleId;
        newsUrl = newsUrlId;
        newsImage = newsImageId;
        newsAuthor = newsAuthorId;
        newsCategory = newsCategoryId;
    }

    public News(String newsDateId, String newsTitleId, String newsUrlId,
                String newsAuthorId, String newsCategoryId) {
        newsDate = newsDateId;
        newsTitle = newsTitleId;
        newsUrl = newsUrlId;
        newsAuthor = newsAuthorId;
        newsCategory = newsCategoryId;
    }

    // Getter methods to access News fields
    public News(String newsTitleId) {
        newsTitle = newsTitleId;
    }

    public String getNewsDate() {
        return newsDate;
    }

    public String getNewsAuthor() {
        return newsAuthor;
    }

    public String getNewsCategory() {
        return newsCategory;
    }

    public String getNewsTitle() {
        return newsTitle;
    }

    public String getNewsUrl() {
        return newsUrl;
    }

    public int getNewsImage() {
        return newsImage;
    }

    public boolean newsImageExists() {
        return newsImage != NO_IMAGE;
    }

}
