package me.memleak.pomorello.activities;

import android.content.Intent;

import butterknife.OnClick;
import me.memleak.pomorello.BuildConfig;
import me.memleak.pomorello.R;
import me.memleak.pomorello.helpers.ToastHelper;

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
            TasksActivity.startActivity(mActivity);
            mActivity.finish();
        } else {
            // AuthenticateTrelloActivity.startActivity(mActivity);
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
}
