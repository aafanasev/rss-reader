package com.jokuskay.rss_reader.services;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import com.jokuskay.rss_reader.models.Post;
import com.jokuskay.rss_reader.models.Rss;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class GetPostsService extends IntentService {

    private static final String TAG = "GetPostsService";
    private static final String ITEM = "item";

    private Rss mRss;

    public GetPostsService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
//        String url = intent.getStringExtra(Rss.Columns.url.name());
//        String url = "http://lenta.ru/rss";
        String url = "http://habrahabr.ru/rss";

        mRss = Rss.getByUrl(this, url);
        if (mRss == null) {
            mRss = new Rss();
            mRss.setUrl(url);
            mRss.setLastUpdate(System.currentTimeMillis());
            mRss.save(this);
        }

        List<Post> posts = getPostsFromWeb(url);
        if (posts.size() > 0) {
            Post.removeAll(this, mRss.getId());
            Post.add(this, posts);
        }

        sendBroadcast();
    }

    private List<Post> getPostsFromWeb(String urlString) {
        List<Post> posts = new ArrayList<>();

        try {
            URL url = new URL(urlString);

            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);

            XmlPullParser parser = factory.newPullParser();
            parser.setInput(url.openStream(), null);

            boolean searchChannelTitle = true;
            int eventType;
            do {
                eventType = parser.next();
                if (searchChannelTitle && eventType == XmlPullParser.START_TAG && "title".equals(parser.getName())) {
                    eventType = parser.next();
                    if (eventType == XmlPullParser.TEXT) {
                        mRss.setTitle(parser.getText());
                        mRss.save(this);
                    }
                    searchChannelTitle = false;
                } else if (eventType == XmlPullParser.START_TAG && ITEM.equals(parser.getName())) {
                    posts.add(parsePost(parser));
                }
            } while (eventType != XmlPullParser.END_DOCUMENT);


        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }

        return posts;
    }

    private Post parsePost(XmlPullParser parser) throws IOException, XmlPullParserException {
        Post post = new Post();

        boolean inProcess = true;
        do {
            int eventType = parser.next();
            String value = "";

            switch (eventType) {
                case XmlPullParser.TEXT:
                    value = parser.getText();
                    break;
                case XmlPullParser.END_TAG:
                    String tag = parser.getName();
                    if (Post.Columns.title.name().equals(tag)) {
                        post.setTitle(value);
                    } else if (Post.Columns.link.name().equals(tag)) {
                        post.setLink(value);
                    } else if (Post.Columns.description.name().equals(tag)) {
                        post.setDescription(value);
                    } else if (Post.Columns.pubDate.name().equals(tag)) {
                        post.setPubDate(value);
                    } else if (ITEM.equals(tag)) {
                        inProcess = false;
                    }
                    break;
            }

        } while (inProcess);

        post.setRssId(mRss.getId());
        return post;
    }

    private void sendBroadcast() {
        Intent intent = new Intent(getPackageName());
        intent.putExtra("rss", mRss);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }


}