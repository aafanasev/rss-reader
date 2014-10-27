package com.jokuskay.rss_reader.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import com.jokuskay.rss_reader.R;
import com.jokuskay.rss_reader.models.Rss;

public class PostListActivity extends ActionBarActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_list);

        long rssId = getIntent().getLongExtra(Rss.Columns._id.name(), 0);
        String rssTitle = getIntent().getStringExtra(Rss.Columns.title.name());

        getSupportActionBar().setTitle(rssTitle);

    }
}