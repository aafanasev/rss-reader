package com.jokuskay.rss_reader.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.jokuskay.rss_reader.helpers.DbColumns;
import com.jokuskay.rss_reader.helpers.DbHelper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Rss implements Serializable {

    public static final String TABLE_NAME = "rss";

    public static enum Columns implements DbColumns {
        _id("INTEGER PRIMARY KEY"), title, url, lastUpdate("INTEGER");

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

    private long mId;
    private long mLastUpdate;
    private String mTitle;
    private String mUrl;

    public Rss() {

    }

    public Rss(int id, String title, String url, int lastUpdate) {
        mId = id;
        mTitle = title;
        mUrl = url;
        mLastUpdate = lastUpdate;
    }

    public long getId() {
        return mId;
    }

    public void setId(int mId) {
        this.mId = mId;
    }

    public long getLastUpdate() {
        return mLastUpdate;
    }

    public void setLastUpdate(long mLastUpdate) {
        this.mLastUpdate = mLastUpdate;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String mUrl) {
        this.mUrl = mUrl;
    }

    private static List<Rss> query(Context context, String where, String[] whereArgs) {
        List<Rss> result = new ArrayList<>();

        DbHelper dbHelper = DbHelper.getInstance(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor c = db.rawQuery("SELECT * FROM " + TABLE_NAME + (where != null ? where : ""), whereArgs);
        if (c.moveToFirst()) {

            int iId = c.getColumnIndex(Columns._id.name());
            int iTitle = c.getColumnIndex(Columns.title.name());
            int iUrl = c.getColumnIndex(Columns.url.name());
            int iLastUpdate = c.getColumnIndex(Columns.lastUpdate.name());

            do {
                result.add(new Rss(c.getInt(iId), c.getString(iTitle), c.getString(iUrl), c.getInt(iLastUpdate)));
            } while (c.moveToNext());
        }

        c.close();
        return result;
    }

    public static List<Rss> getAll(Context context) {
        return query(context, null, null);
    }

    public static Rss getByUrl(Context context, String url) {
        Rss result = null;

        List<Rss> rssList = query(context, " WHERE url = ?", new String[]{url});
        if (rssList.size() > 0) {
            result = rssList.get(0);
        }

        return result;
    }

    public void save(Context context) {
        DbHelper dbHelper = DbHelper.getInstance(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Columns.title.name(), mTitle);
        values.put(Columns.url.name(), mUrl);
        values.put(Columns.lastUpdate.name(), mLastUpdate);

        if (mId == 0) {
            mId = db.insert(TABLE_NAME, null, values);
        } else {
            db.update(TABLE_NAME, values, Columns._id.name() + "=?", new String[]{String.valueOf(mId)});
        }
    }

}
