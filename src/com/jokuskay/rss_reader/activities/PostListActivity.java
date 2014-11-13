package com.jokuskay.rss_reader.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import com.jokuskay.rss_reader.R;
import com.jokuskay.rss_reader.adapters.PostListAdapter;
import com.jokuskay.rss_reader.models.Post;
import com.jokuskay.rss_reader.models.Rss;
import com.jokuskay.rss_reader.services.GetPostsService;

import java.util.List;

public class PostListActivity extends ActionBarActivity implements AdapterView.OnItemClickListener {

    private static final String TAG = "PostListActivity";

    private SwipeRefreshLayout mRefresh;

    private PostListAdapter mAdapter;
    private List<Post> mPosts;
    private long mRssId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_list);

        mRefresh = (SwipeRefreshLayout) findViewById(R.id.post_list_refresh);
        RecyclerView listView = (RecyclerView) findViewById(R.id.post_list);

        listView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        listView.setLayoutManager(mLayoutManager);
        listView.setItemAnimator(new DefaultItemAnimator());

        final Rss rss = (Rss) getIntent().getExtras().getSerializable("rss");

        mRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Intent service = new Intent(PostListActivity.this, GetPostsService.class);
                service.putExtra(Rss.Columns.url.name(), rss.getUrl());
                startService(service);
            }
        });

        mRssId = rss.getId();
        getSupportActionBar().setTitle(rss.getTitle());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setPosts();
        mAdapter = new PostListAdapter(mPosts, this);
        listView.setAdapter(mAdapter);

    }

    private void setPosts() {
        mPosts = Post.getListByRssId(this, mRssId);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, PostViewActivity.class);
        intent.putExtra(Post.Columns._id.name(), mPosts.get(position).getId());
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver, new IntentFilter(getPackageName()));
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mReceiver);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            setPosts();
            mAdapter.notifyDataSetChanged();
            mRefresh.setRefreshing(false);
        }
    };

}