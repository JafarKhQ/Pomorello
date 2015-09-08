package me.memleak.pomorello.adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.memleak.pomorello.R;
import me.memleak.pomorello.models.TrelloList;

/**
 * Created by jafar_qaddoumi on 9/8/15.
 * <p/>
 * Copyright (c) 2015 memleak.me. All rights reserved.
 */
public class ConfigAdapter extends RecyclerView.Adapter<ConfigAdapter.ViewHolder> {

    private List<TrelloList> mLists;
    private LayoutInflater mInflater;

    public ConfigAdapter(Activity activity) {
        mInflater = LayoutInflater.from(activity);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(mInflater.inflate(R.layout.list_item_config, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        TrelloList list = mLists.get(i);
        viewHolder.txvListName.setText(list.getName());
    }

    @Override
    public int getItemCount() {
        if (null == mLists) {
            return 0;
        }

        return mLists.size();
    }

    public void setTrelloList(List<TrelloList> lists) {
        mLists = lists;
        notifyDataSetChanged();
    }

    public List<TrelloList> getList() {
        return mLists;
    }

    public static final class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.config_txv_list_name)
        TextView txvListName;

        public ViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }
}
