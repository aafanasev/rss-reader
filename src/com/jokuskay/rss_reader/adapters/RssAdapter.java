package com.jokuskay.rss_reader.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.jokuskay.rss_reader.R;
import com.jokuskay.rss_reader.models.Rss;

import java.util.List;

public class RssAdapter extends RecyclerView.Adapter<RssAdapter.ViewHolder> {

    private OnRssListActionListener mListener;
    private List<Rss> mRssList;

    public RssAdapter(OnRssListActionListener listener, List<Rss> rssList) {
        mRssList = rssList;
        mListener = listener;
    }

    @Override
    public RssAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_rss, parent, false);
        return new ViewHolder(view, this);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Rss rss = mRssList.get(position);
        holder.mRssTitle.setText(rss.getTitle());
        holder.mRssTitle.setTag(rss.getId());
        holder.mRssUrl.setText(rss.getUrl());
    }

    @Override
    public int getItemCount() {
        return mRssList.size();
    }

    public void onItemClick(ViewHolder holder) {
        mListener.onItemClick(holder.getPosition());
    }

    private void onItemLongClick(ViewHolder holder) {
        mListener.onItemLongClick(holder.getPosition());
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        public TextView mRssTitle;
        public TextView mRssUrl;

        private RssAdapter mAdapter;

        public ViewHolder(View v, RssAdapter adapter) {
            super(v);
            v.setOnClickListener(this);
            v.setOnLongClickListener(this);

            mAdapter = adapter;

            mRssTitle = (TextView) v.findViewById(R.id.view_rss_title);
            mRssUrl = (TextView) v.findViewById(R.id.view_rss_url);
        }

        @Override
        public void onClick(View v) {
            mAdapter.onItemClick(this);
        }

        @Override
        public boolean onLongClick(View v) {
            mAdapter.onItemLongClick(this);
            return true;
        }
    }

    public static interface OnRssListActionListener {
        public abstract void onItemClick(int position);
        public abstract void onItemLongClick(int position);
    }

}
