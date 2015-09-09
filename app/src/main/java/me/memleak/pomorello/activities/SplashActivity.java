package me.memleak.pomorello.activities;

import android.content.Intent;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;
import me.memleak.pomorello.BuildConfig;
import me.memleak.pomorello.R;
import me.memleak.pomorello.helpers.ToastHelper;
import me.memleak.pomorello.models.TrelloBoard;
import me.memleak.pomorello.rest.TrelloCallback;
import me.memleak.pomorello.rest.TrelloClient;
import me.memleak.pomorello.rest.apis.TrelloApi;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by jafar_qaddoumi on 9/4/15.
 * <p/>
 * Copyright (c) 2015 memleak.me. All rights reserved.
 */
public class SplashActivity extends BaseActivity {

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_splash;
    }

    @Override
    protected void setupViews() {
        if (mPomorelloUser.isAuthenticated()) {
            ToastHelper.top(mContext, getString(R.string.updating_data));
            TrelloApi trelloApi = TrelloClient.getTrelloApi();
            trelloApi.getAllBoards(boardsCallback);
        } else {
            ToastHelper.center(mContext, getString(R.string.please_singin));
            TrelloOuthActivity.startActivityForResult(mActivity,
                    BuildConfig.TRELLO_API_KEY,
                    BuildConfig.TRELLO_API_SECRET);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (TrelloOuthActivity.REQUEST_CODE_TRELLO_AUTH == requestCode) {
            if (RESULT_OK == resultCode) {
                mPomorelloUser.setAccessToken(data.getStringExtra(TrelloOuthActivity.EXTRA_ACCESS_TOKEN));
                mPomorelloUser.setAccessSecret(data.getStringExtra(TrelloOuthActivity.EXTRA_ACCESS_SECRET));
                mPomorelloUser.save(mContext);
            } else {
                String error;
                if (null != data) {
                    error = data.getStringExtra(TrelloOuthActivity.EXTRA_ERROR_MESSAGE);
                } else {
                    error = getString(R.string.error_empty_trello_msg);
                }

                ToastHelper.top(mContext, error);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private TrelloCallback<ArrayList<TrelloBoard>> boardsCallback = new TrelloCallback<ArrayList<TrelloBoard>>() {

        @Override
        public void success(ArrayList<TrelloBoard> boardList, Response response) {
            super.success(boardList, response);

            final Realm realm = getRealm();
            RealmResults<TrelloBoard> oldBoards = realm.allObjects(TrelloBoard.class);

            if (null != oldBoards && oldBoards.size() > 0) {
                // we have an old boards
                // set Configured flag to Configured boards
                for (int i = 0; i < boardList.size(); i++) {
                    TrelloBoard board = boardList.get(i);
                    for (int k = 0; k < oldBoards.size(); k++) {
                        TrelloBoard oldBoard = oldBoards.get(k);
                        if (board.getId().equals(oldBoard.getId())) {
                            board.setIsConfigured(oldBoard.isConfigured());
                        }
                    }
                }
            }

            realm.beginTransaction();
            // delete all old boards
            realm.clear(TrelloBoard.class);

            // save new Configured boards
            realm.copyToRealm(boardList);
            realm.commitTransaction();

            HomeActivity.startActivity(mActivity);
            mActivity.finish();
        }

        @Override
        public void failure(RetrofitError error) {
            super.failure(error);
        }
    };
}
