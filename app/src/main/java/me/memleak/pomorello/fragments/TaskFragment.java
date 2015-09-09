package me.memleak.pomorello.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import me.memleak.pomorello.R;
import me.memleak.pomorello.adapters.TaskAdapter;
import me.memleak.pomorello.models.PomorelloList;
import me.memleak.pomorello.models.TrelloCard;
import me.memleak.pomorello.rest.TrelloCallback;
import me.memleak.pomorello.rest.TrelloClient;
import me.memleak.pomorello.widgets.DividerItemDecoration;
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

    private TaskAdapter mAdapter;
    private RealmResults<TrelloCard> mTasksLocal;
    private final List<TrelloCard> mTasksOnline = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAdapter = new TaskAdapter(mParentActivity);
        mTabType = getArguments().getInt(EXTRA_TAB_TYPE);
        final String[] tabIds = PomorelloList.getTabTaskIds(getArguments().getString(EXTRA_TAB_IDS));
        if (null == tabIds || tabIds.length < 1) {
            // no list for this tab, no tasks
            return;
        }

        RealmQuery<TrelloCard> realmQuery = getRealm().where(TrelloCard.class)
                .equalTo("type", mTabType)
                .beginGroup();
        for (int i = 0; i < tabIds.length; i++) {
            realmQuery.equalTo("id", tabIds[i]);
            if (i < tabIds.length - 1) {
                realmQuery.or();
            } else {
                realmQuery.endGroup();
            }

            TrelloClient.getTrelloApi().getCards(tabIds[i], new CallBack(i == tabIds.length - 1));
        }

        mTasksLocal = realmQuery.findAll();
        mAdapter.setLists(mTasksLocal);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_task;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rcvList.setHasFixedSize(false);
        rcvList.setLayoutManager(new LinearLayoutManager(mParentActivity));
        rcvList.addItemDecoration(new DividerItemDecoration(mParentActivity, DividerItemDecoration.VERTICAL_LIST));

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

            synchronized (mTasksOnline) {
                mTasksOnline.addAll(result);

                //all tasks have been fetched, save them
                if (true == isLast) {
                    Log.d(TAG, "save tasks list to db");

                    if (null != mTasksLocal) {
                        // copy old info to new tasks
                        for (int i = 0; i < mTasksOnline.size(); i++) {
                            TrelloCard online = mTasksOnline.get(i);
                            for (int k = 0; k < mTasksLocal.size(); k++) {
                                TrelloCard local = mTasksLocal.get(k);
                                online.setPomodorCount(local.getPomodorCount());
                                online.setLastWorkTime(local.getLastWorkTime());
                            }
                        }
                    }

                    for (int i = 0; i < mTasksOnline.size(); i++) {
                        // set the type of tasks
                        mTasksOnline.get(i).setType(mTabType);
                    }

                    mAdapter.setLists(mTasksOnline);

                    getRealm().beginTransaction();
                    getRealm().copyToRealmOrUpdate(mTasksOnline);
                    getRealm().commitTransaction();
                }
            }
        }

        @Override
        public void failure(RetrofitError error) {
            super.failure(error);
        }
    }
}
