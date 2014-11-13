package com.jokuskay.rss_reader.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import com.jokuskay.rss_reader.R;
import com.jokuskay.rss_reader.models.Post;

import java.util.List;

public class PostListAdapter extends RecyclerView.Adapter<PostListAdapter.ViewHolder> {

    private AdapterView.OnItemClickListener mClickListener;
    private List<Post> mPosts;

    public PostListAdapter(List<Post> posts, AdapterView.OnItemClickListener clickListener) {
        mPosts = posts;
        mClickListener = clickListener;
    }

    @Override
    public PostListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_post, parent, false);
        return new ViewHolder(view, this);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Post post = mPosts.get(position);
        holder.mTitle.setText(post.getTitle());
        holder.mDate.setText(post.getPubDate());
    }

    @Override
    public int getItemCount() {
        return mPosts.size();
    }

    public void onItemClick(ViewHolder holder) {
        mClickListener.onItemClick(null, holder.itemView, holder.getPosition(), holder.getItemId());
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView mTitle;
        public TextView mDate;

        private PostListAdapter mAdapter;

        public ViewHolder(View v, PostListAdapter adapter) {
            super(v);
            v.setOnClickListener(this);

            mAdapter = adapter;

            mTitle = (TextView) v.findViewById(R.id.view_post_title);
            mDate = (TextView) v.findViewById(R.id.view_post_date);
        }

        @Override
        public void onClick(View v) {
            mAdapter.onItemClick(this);
        }
    }

}
