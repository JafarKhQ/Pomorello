package me.memleak.pomorello.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import me.memleak.pomorello.R;

/**
 * Created by jafar_qaddoumi on 9/4/15.
 * <p/>
 * Copyright (c) 2015 memleak.me. All rights reserved.
 */
public class AuthenticateTrelloActivity extends BaseActivity {

    public static void startActivity(Activity activity) {
        Intent intent = new Intent(activity, AuthenticateTrelloActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_authenticate_trello;
    }

    @Override
    protected void setupViews() {

    }
}
