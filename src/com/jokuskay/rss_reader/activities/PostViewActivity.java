package com.jokuskay.rss_reader.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;
import com.jokuskay.rss_reader.R;
import com.jokuskay.rss_reader.models.Post;


public class PostViewActivity extends ActionBarActivity {

    private static final String TAG = "PostViewActivity";
    private static final int OPEN_LINK = 123;

    private String mLink;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_view);

        int postId = getIntent().getIntExtra(Post.Columns._id.name(), 0);
        Log.d(TAG, "postId " + postId);

        Post post = Post.getById(this, postId);
        if (post == null) {
            Toast.makeText(this, "Error: post not found", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            setupUi(post);
        }

    }

    private void setupUi(Post post) {

        getSupportActionBar().setTitle(post.getTitle());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView title = (TextView) findViewById(R.id.post_view_title);
        TextView date = (TextView) findViewById(R.id.post_view_date);
        TextView desc = (TextView) findViewById(R.id.post_view_desc);

        title.setText(post.getTitle());
        date.setText(post.getPubDate());
        desc.setText(post.getDescriptionAsHtml());
        desc.setMovementMethod(LinkMovementMethod.getInstance());

        mLink = post.getLink();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, OPEN_LINK, 0, "Open in browser");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case OPEN_LINK:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(mLink)));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}