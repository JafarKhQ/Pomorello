package me.memleak.pomorello.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;

import butterknife.Bind;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import me.memleak.pomorello.R;
import me.memleak.pomorello.adapters.TaskAdapter;
import me.memleak.pomorello.models.PomorelloList;
import me.memleak.pomorello.models.TrelloCard;
import me.memleak.pomorello.rest.TrelloCallback;
import me.memleak.pomorello.rest.TrelloClient;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by jafar_qaddoumi on 9/4/15.
 * <p/>
 * Copyright (c) 2015 memleak.me. All rights reserved.
 */
public class TaskFragment extends BaseFragment {
    private static final String EXTRA_TAB_TYPE = "extra_tab_type";
    private static final String EXTRA_TAB_IDS = "extra_tab_ids";

    public static TaskFragment newInstance(int type, String ids) {
        Bundle args = new Bundle();
        args.putInt(EXTRA_TAB_TYPE, type);
        args.putString(EXTRA_TAB_IDS, ids);

        TaskFragment fg = new TaskFragment();
        fg.setArguments(args);
        return fg;
    }

    @Bind(R.id.task_rcv_list)
    RecyclerView rcvList;

    private int mTabType;
    private String[] mTabIds;

    private TaskAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAdapter = new TaskAdapter(mParentActivity);
        mTabType = getArguments().getInt(EXTRA_TAB_TYPE);
        mTabIds = PomorelloList.getTabTaskIds(getArguments().getString(EXTRA_TAB_IDS));

        RealmQuery<TrelloCard> realmQuery = getRealm().where(TrelloCard.class);
        for (int i = 0; null != mTabIds && i < mTabIds.length; i++) {
            realmQuery.equalTo("id", mTabIds[i]);
            if (i < mTabIds.length - 1) {
                realmQuery.or();
            }

            TrelloClient.getTrelloApi().getCards(mTabIds[i], new CallBack(i == mTabIds.length - 1));
        }

        RealmResults<TrelloCard> tasks = realmQuery.findAll();
        mAdapter.setLists(tasks);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_task;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rcvList.setHasFixedSize(true);
        rcvList.setLayoutManager(new LinearLayoutManager(mParentActivity));

        rcvList.setAdapter(mAdapter);
    }

    private class CallBack extends TrelloCallback<ArrayList<TrelloCard>> {

        private boolean isLast;

        public CallBack(boolean isLast) {
            this.isLast = isLast;
        }

        @Override
        public void success(ArrayList<TrelloCard> result, Response response) {
            super.success(result, response);

            mAdapter.addLists(result);
        }

        @Override
        public void failure(RetrofitError error) {
            super.failure(error);
        }
    }
}
