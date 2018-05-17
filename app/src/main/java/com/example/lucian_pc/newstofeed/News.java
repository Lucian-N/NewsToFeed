package com.example.lucian_pc.newstofeed;

public class News {

    private long newsDate;
    private String newsTitle;
    private String newsUrl;
    private int newsImage = NO_IMAGE;
    private static final int NO_IMAGE = 1;

    // Custom news class Arguments
    public News(long newsDateId, String newsTitleId, String newsUrlId, int newsImageId) {
        newsDate = newsDateId;
        newsTitle = newsTitleId;
        newsUrl = newsUrlId;
        newsImage = newsImageId;
    }

    public long getNewsDate() {
        return newsDate;
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
