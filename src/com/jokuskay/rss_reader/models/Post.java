package com.jokuskay.rss_reader.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.Html;
import com.jokuskay.rss_reader.helpers.DbColumns;
import com.jokuskay.rss_reader.helpers.DbHelper;

import java.util.ArrayList;
import java.util.List;

public class Post {

    public static final String TABLE_NAME = "posts";

    public static enum Columns implements DbColumns {
        _id("INTEGER PRIMARY KEY"), rss_id("INTEGER"), pubDate, title, description, link;

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

    private int mId;
    private long mRssId;
    private String mPubDate;
    private String mTitle;
    private String mDescription;
    private String mLink;

    public Post() {

    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        this.mId = id;
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

    public android.text.Spanned getDescriptionAsHtml() {
        return Html.fromHtml(mDescription);
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

    public static void removeAll(Context context, long rssId) {
        DbHelper dbHelper = DbHelper.getInstance(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME + " WHERE " + Columns.rss_id.name() + "=" + rssId);
    }

    public static void add(Context context, List<Post> posts) {
        DbHelper dbHelper = DbHelper.getInstance(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        for (Post post : posts) {
            ContentValues values = new ContentValues();
            values.put(Columns.rss_id.name(), post.getRssId());
            values.put(Columns.title.name(), post.getTitle());
            values.put(Columns.link.name(), post.getLink());
            values.put(Columns.pubDate.name(), post.getPubDate());
            values.put(Columns.description.name(), post.getDescription());
            db.insert(TABLE_NAME, null, values);
        }
    }

    private static List<Post> query(Context context, String where, String[] whereArgs) {
        List<Post> result = new ArrayList<>();

        DbHelper dbHelper = DbHelper.getInstance(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor c = db.rawQuery("SELECT * FROM " + TABLE_NAME + (where != null ? where : ""), whereArgs);
        if (c.moveToFirst()) {

            int iId = c.getColumnIndex(Columns._id.name());
            int iRssId = c.getColumnIndex(Columns.rss_id.name());
            int iTitle = c.getColumnIndex(Columns.title.name());
            int iLink = c.getColumnIndex(Columns.link.name());
            int iDesc = c.getColumnIndex(Columns.description.name());
            int iPubDate = c.getColumnIndex(Columns.pubDate.name());

            do {
                Post post = new Post();
                post.setId(c.getInt(iId));
                post.setRssId(c.getLong(iRssId));
                post.setTitle(c.getString(iTitle));
                post.setLink(c.getString(iLink));
                post.setDescription(c.getString(iDesc));
                post.setPubDate(c.getString(iPubDate));
                result.add(post);
            } while (c.moveToNext());
        }

        c.close();
        return result;
    }

    public static List<Post> getListByRssId(Context context, long rssId) {
        return query(context, " WHERE " + Columns.rss_id.name() + "=?", new String[]{String.valueOf(rssId)});
    }

    public static Post getById(Context context, int id) {
        List<Post> posts = query(context, " WHERE " + Columns._id.name() + "=?", new String[]{String.valueOf(id)});
        return posts.size() > 0 ? posts.get(0) : null;
    }

}
