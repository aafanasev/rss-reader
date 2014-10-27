package com.jokuskay.rss_reader.activities;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import com.jokuskay.rss_reader.R;
import com.jokuskay.rss_reader.models.Post;
import com.jokuskay.rss_reader.models.Rss;

import java.util.List;

public class PostListActivity extends ActionBarActivity {

    private static final String TAG = "PostListActivity";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_list);

        Rss rss = (Rss) getIntent().getExtras().getSerializable("rss");

        getSupportActionBar().setTitle(rss.getTitle());

        List<Post> posts = Post.getListByRssId(this, rss.getId());
        Log.d(TAG, posts.size() + "");

    }
}