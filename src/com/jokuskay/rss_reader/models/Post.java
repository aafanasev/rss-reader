package com.jokuskay.rss_reader.models;

import com.jokuskay.rss_reader.helpers.DbColumns;

public class Post {

    public static final String TABLE_NAME = "posts";

    public static enum Columns implements DbColumns {
        rss_id("INTEGER"), pubDate, title, description, link;

        private String type;

        private Columns() {
            this("TEXT");
        }

        private Columns(String type) {
            this.type = type;
        }

        public String getType() {
            return type;
        }

    }

    private long mRssId;
    private String mPubDate;
    private String mTitle;
    private String mDescription;
    private String mLink;

    public Post() {

    }

    public long getRssId() {
        return mRssId;
    }

    public void setRssId(long rssId) {
        this.mRssId = rssId;
    }

    public String getPubDate() {
        return mPubDate;
    }

    public void setPubDate(String pubDate) {
        this.mPubDate = pubDate;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        this.mDescription = description;
    }

    public String getLink() {
        return mLink;
    }

    public void setLink(String link) {
        this.mLink = link;
    }
}
