package com.jokuskay.rss_reader.helpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import com.jokuskay.rss_reader.models.Post;
import com.jokuskay.rss_reader.models.Rss;

public class DbHelper extends SQLiteOpenHelper {

    private static final String TAG = "DbHelper";

    private static DbHelper instance;

    private static final String DB_NAME = "rss.db";
    private static final int DB_VERSION = 2;

    public static DbHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DbHelper(context);
        }
        return instance;
    }

    private DbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        Log.d(TAG, "create DbHelper");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "onCreate");

        String sql = getTableCreateSql(Rss.TABLE_NAME, Rss.Columns.values());
        db.execSQL(sql);

        sql = getTableCreateSql(Post.TABLE_NAME, Post.Columns.values());
        db.execSQL(sql);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, "onUpgrade from " + oldVersion + " to" + newVersion + " version");

        db.execSQL("DROP TABLE IF EXISTS " + Rss.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Post.TABLE_NAME);
    }

    private String getTableCreateSql(String tableName, DbColumns[] cols) {
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder
                .append("CREATE TABLE IF NOT EXISTS ")
                .append(tableName)
                .append("(");

        String dot = "";
        for (DbColumns col : cols) {
            sqlBuilder.append(dot);
            sqlBuilder.append(col.name());
            sqlBuilder.append(" ");
            sqlBuilder.append(col.getType());
            dot = ", ";
        }

        sqlBuilder.append(");");
        return sqlBuilder.toString();
    }

}
