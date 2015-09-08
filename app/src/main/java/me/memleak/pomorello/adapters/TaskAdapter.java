package me.memleak.pomorello.adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.memleak.pomorello.R;
import me.memleak.pomorello.models.TrelloCard;

/**
 * Created by jafar_qaddoumi on 9/8/15.
 * <p/>
 * Copyright (c) 2015 memleak.me. All rights reserved.
 */
public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {

    private List<TrelloCard> mLists;
    private LayoutInflater mInflater;

    public TaskAdapter(Activity activity) {
        mInflater = LayoutInflater.from(activity);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mInflater.inflate(R.layout.list_item_task, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        TrelloCard list = mLists.get(position);
        holder.txvListName.setText(list.getName());
    }

    @Override
    public int getItemCount() {
        if (null == mLists) {
            return 0;
        }

        return mLists.size();
    }

    public List<TrelloCard> getLists() {
        return mLists;
    }

    public void setLists(List<TrelloCard> lists) {
        mLists = new ArrayList<>(lists);
    }

    public void addLists(List<TrelloCard> lists) {
        if (null == mLists) {
            mLists = new ArrayList<>(lists);
        } else {
            mLists.addAll(lists);
        }

        notifyDataSetChanged();
    }

    public static final class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.task_txv_list_name)
        TextView txvListName;

        public ViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }
}
