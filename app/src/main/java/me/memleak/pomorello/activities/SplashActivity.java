package me.memleak.pomorello.activities;

import android.content.Intent;

import java.util.List;

import butterknife.OnClick;
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
            // AuthenticateTrelloActivity.startActivity(mActivity);
            ToastHelper.top(mContext, "load all boards");
            TrelloApi trelloApi = TrelloClient.getTrelloApi();
            trelloApi.getAllBoards(boardsCallback);
        } else {

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

    @OnClick(R.id.splash_btn_login)
    void clickLogin() {
        TrelloOuthActivity.startActivityForResult(mActivity,
                BuildConfig.TRELLO_API_KEY,
                BuildConfig.TRELLO_API_SECRET);
    }

    private TrelloCallback<List<TrelloBoard>> boardsCallback = new TrelloCallback<List<TrelloBoard>>() {

        @Override
        public void success(List<TrelloBoard> boardList, Response response) {
            super.success(boardList, response);

            mRealm.beginTransaction();
            mRealm.copyToRealmOrUpdate(boardList);
            mRealm.commitTransaction();

            HomeActivity.startActivity(mActivity);
            mActivity.finish();
        }

        @Override
        public void failure(RetrofitError error) {
            super.failure(error);
        }
    };
}
