package com.jokuskay.rss_reader.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import com.jokuskay.rss_reader.R;
import com.jokuskay.rss_reader.models.Rss;

import java.util.List;

public class RssAdapter extends RecyclerView.Adapter<RssAdapter.ViewHolder> {

    private AdapterView.OnItemClickListener mClickListener;
    private List<Rss> mRssList;

    public RssAdapter(List<Rss> rssList, AdapterView.OnItemClickListener clickListener) {
        mRssList = rssList;
        mClickListener = clickListener;
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
        mClickListener.onItemClick(null, holder.itemView, holder.getPosition(), holder.getItemId());
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView mRssTitle;
        public TextView mRssUrl;

        private RssAdapter mAdapter;

        public ViewHolder(View v, RssAdapter adapter) {
            super(v);
            v.setOnClickListener(this);

            mAdapter = adapter;

            mRssTitle = (TextView) v.findViewById(R.id.view_rss_title);
            mRssUrl = (TextView) v.findViewById(R.id.view_rss_url);
        }

        @Override
        public void onClick(View v) {
            mAdapter.onItemClick(this);
        }
    }


}
