package com.nowist.android.wikisearchapp.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nowist.android.wikisearchapp.R;
import com.nowist.android.wikisearchapp.models.SuggestionItemDataModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class WikiSuggestionListAdapter extends RecyclerView.Adapter {

    private final OnSuggestionItemClickListener mClickHandler;
    private ArrayList<SuggestionItemDataModel> mDataList;

    private final int VIEW_ITEM = 1;
    private final int VIEW_ITEM_PROGRESS = 2;

    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    private OnLoadMoreListener onLoadMoreListener;

    private RecyclerView mRecyclerView;


    public WikiSuggestionListAdapter(OnSuggestionItemClickListener clickListener, RecyclerView recyclerView, ArrayList<SuggestionItemDataModel> list) {
        mClickHandler = clickListener;
        mRecyclerView = recyclerView;
        mDataList = list;

        if (mRecyclerView.getLayoutManager() instanceof LinearLayoutManager) {
            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    totalItemCount = linearLayoutManager.getItemCount();
                    lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                    if (!loading
                            && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                        if (onLoadMoreListener != null) {
                            onLoadMoreListener.onLoadMore();
                        }
                        loading = true;
                    }
                }
            });
        }
    }

//    public void setDataList(ArrayList<SuggestionItemDataModel> dataList) {
//        mDataList = dataList;
//        notifyDataSetChanged();
//    }

    @Override
    public int getItemViewType(int position) {
        return mDataList.get(position) != null ? VIEW_ITEM : VIEW_ITEM_PROGRESS;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == VIEW_ITEM) {
            View view = inflater.inflate(R.layout.suggestion_item_layout, parent, false);
            viewHolder = new SuggestionItemViewHolder(view);
        } else if (viewType == VIEW_ITEM_PROGRESS) {
            View view = inflater.inflate(R.layout.progress_bar_item_layout, parent, false);
            viewHolder = new ProgressViewHolder(view);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof SuggestionItemViewHolder) {
            SuggestionItemViewHolder vh = (SuggestionItemViewHolder) holder;
            SuggestionItemDataModel data = mDataList.get(position);
            if (data != null) {
                if (!TextUtils.isEmpty(data.getTitle())) {
                    vh.mSuggestionTitle.setText(data.getTitle());
                }
                if (!TextUtils.isEmpty(data.getDesc())) {
                    vh.mSuggestionDesc.setText(data.getDesc());
                }
                if (!TextUtils.isEmpty(data.getImageUrl())) {
                    Picasso.with(vh.context).load(data.getImageUrl()).resize(50, 50)
                            .centerCrop().into(vh.mSuggestionAssociatedIv);
                }
            }
        } else if (holder instanceof ProgressViewHolder) {
            ProgressViewHolder vh = (ProgressViewHolder) holder;
            vh.progressBar.setIndeterminate(true);
        }
    }

    @Override
    public int getItemCount() {
        if (mDataList != null && !mDataList.isEmpty()) {
            return mDataList.size();
        }
        return 0;
    }

    private class SuggestionItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final TextView mSuggestionDesc, mSuggestionTitle;
        final ImageView mSuggestionAssociatedIv;
        final Context context;

        SuggestionItemViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            mSuggestionAssociatedIv = (ImageView) itemView.findViewById(R.id.imageView_suggestion_item);
            mSuggestionDesc = (TextView) itemView.findViewById(R.id.textView_suggestion_desc);
            mSuggestionTitle = (TextView) itemView.findViewById(R.id.textView_suggestion_title);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPostion = getAdapterPosition();
            String title = mDataList.get(adapterPostion).getTitle();
            mClickHandler.onClick(title);
        }
    }

    private static class ProgressViewHolder extends RecyclerView.ViewHolder {
        ProgressBar progressBar;

        ProgressViewHolder(View v) {
            super(v);
            progressBar = (ProgressBar) v.findViewById(R.id.progressBar);
        }
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    public void setLoaded() {
        loading = false;
    }

    public interface OnSuggestionItemClickListener {
        void onClick(String itemUri);
    }

    public interface OnLoadMoreListener {
        void onLoadMore();
    }
}
