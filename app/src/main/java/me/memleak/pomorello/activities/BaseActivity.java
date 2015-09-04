package me.memleak.pomorello.activities;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.MenuRes;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import butterknife.ButterKnife;
import me.memleak.pomorello.PomorelloApp;
import me.memleak.pomorello.models.PomorelloUser;

/**
 * Created by jafar_qaddoumi on 9/4/15.
 * <p/>
 * Copyright (c) 2015 memleak.me. All rights reserved.
 */
public abstract class BaseActivity extends AppCompatActivity {
    protected final String TAG = ((Object) this).getClass().getSimpleName();
    private static final int MENU_RES_NONE = -1;

    protected Context mContext;
    protected ActionBar mActionBar;
    protected BaseActivity mActivity;
    protected PomorelloUser mPomorelloUser;

    @LayoutRes
    protected abstract int getLayoutResID();

    protected abstract void setupViews();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResID());

        mActivity = this;
        mContext = mActivity.getApplicationContext();
        mPomorelloUser = PomorelloApp.getPomorelloUser();

        try {
            // on activities without actionbar a nullPointerException will be thrown here (ICS)
            // but why, why you master Google !!!!!
            mActionBar = getSupportActionBar();
        } catch (Exception e) {
        }

        ButterKnife.bind(mActivity);
        setupViews();
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();

        getWindow().setFormat(PixelFormat.RGBA_8888);
    }

    @MenuRes
    protected int getMenuRes() {
        return MENU_RES_NONE;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        int menuResId = getMenuRes();
        if (MENU_RES_NONE == menuResId) {
            return super.onCreateOptionsMenu(menu);
        } else {
            getMenuInflater().inflate(menuResId, menu);
            return true;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void setupActionBar(boolean showUp, int resId) {
        setupActionBar(showUp, getString(resId));
    }

    public void setupActionBar(boolean showUp, String customTitle) {
        mActionBar.setDisplayShowHomeEnabled(!showUp);
        mActionBar.setHomeButtonEnabled(showUp);
        mActionBar.setDisplayHomeAsUpEnabled(showUp);

        updateActionBarTitle(customTitle);
    }

    public void updateActionBarTitle(int resId) {
        updateActionBarTitle(getString(resId));
    }

    public void updateActionBarTitle(String customTitle) {
        if (null == customTitle) {
            mActionBar.setDisplayShowTitleEnabled(false);
        } else {
            mActionBar.setDisplayShowTitleEnabled(true);
            mActionBar.setTitle(customTitle);
        }
    }

    public void updateActionBarTitle(String customTitle, String subTitle) {
        if (null == customTitle) {
            mActionBar.setDisplayShowTitleEnabled(false);
        } else {
            mActionBar.setDisplayShowTitleEnabled(true);
            mActionBar.setTitle(customTitle);
            mActionBar.setSubtitle(subTitle);
        }
    }
}
