package me.memleak.pomorello.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import de.greenrobot.event.EventBus;
import io.realm.Realm;
import me.memleak.pomorello.R;
import me.memleak.pomorello.adapters.ConfigAdapter;
import me.memleak.pomorello.models.PomorelloList;
import me.memleak.pomorello.models.TrelloBoard;
import me.memleak.pomorello.models.TrelloList;
import me.memleak.pomorello.rest.TrelloCallback;
import me.memleak.pomorello.rest.TrelloClient;
import me.memleak.pomorello.rest.apis.TrelloApi;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by jafar_qaddoumi on 9/8/15.
 * <p/>
 * Copyright (c) 2015 memleak.me. All rights reserved.
 */
public class ConfigFragment extends BaseFragment {

    public static ConfigFragment newInstance(String boardId) {
        Bundle args = new Bundle();
        args.putString(TrelloBoard.EXTRA_BOARD_ID, boardId);

        ConfigFragment fg = new ConfigFragment();
        fg.setArguments(args);
        return fg;
    }

    @Bind(R.id.config_rcv_list)
    RecyclerView rcvList;

    private TrelloBoard mBoard;
    private ConfigAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        String boardId = getArguments().getString(TrelloBoard.EXTRA_BOARD_ID);
        mBoard = getRealm().where(TrelloBoard.class)
                .equalTo("id", boardId)
                .findFirst();

        mAdapter = new ConfigAdapter(mParentActivity);

        List<TrelloList> lists = getRealm().allObjects(TrelloList.class);
        mAdapter.setTrelloList(lists);

        TrelloApi trelloApi = TrelloClient.getTrelloApi();
        trelloApi.getAllLists(mBoard.getId(), listTrelloCallback);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_config;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rcvList.setHasFixedSize(true);
        rcvList.setLayoutManager(new LinearLayoutManager(mParentActivity));

        rcvList.setAdapter(mAdapter);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.config, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.config_done:
                //TODO move to methods
                Realm realm = getRealm();

                PomorelloList pomorelloList = new PomorelloList();
                pomorelloList.setBoardId(mBoard.getId());
                List<TrelloList> trelloList = mAdapter.getList();
                ArrayList<String> todo = new ArrayList<>(trelloList.size());
                ArrayList<String> doing = new ArrayList<>(trelloList.size());
                ArrayList<String> done = new ArrayList<>(trelloList.size());
                for (int i = 0; i < trelloList.size(); i++) {
                    TrelloList list = trelloList.get(i);
                    switch (list.getType()) {
                        case PomorelloList.TAB_TODO:
                            todo.add(list.getId());
                            break;

                        case PomorelloList.TAB_DOING:
                            doing.add(list.getId());
                            break;

                        case PomorelloList.TAB_DONE:
                            done.add(list.getId());
                            break;

                    }
                }

                pomorelloList.setTodoIds(PomorelloList.getTaskIdsString(todo));
                pomorelloList.setDoingIds(PomorelloList.getTaskIdsString(doing));
                pomorelloList.setDoneIds(PomorelloList.getTaskIdsString(done));

                realm.beginTransaction();
                mBoard.setIsConfigured(true);
                realm.clear(TrelloList.class);
                realm.copyToRealm(trelloList);
                realm.copyToRealmOrUpdate(pomorelloList);
                realm.commitTransaction();

                EventBus.getDefault().post(mBoard);

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private TrelloCallback<ArrayList<TrelloList>> listTrelloCallback = new TrelloCallback<ArrayList<TrelloList>>() {

        @Override
        public void success(ArrayList<TrelloList> result, Response response) {
            super.success(result, response);

            mAdapter.setTrelloList(result);
        }

        @Override
        public void failure(RetrofitError error) {
            super.failure(error);
        }
    };
}
