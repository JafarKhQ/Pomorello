package me.memleak.pomorello.models;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;

/**
 * Created by jafar_qaddoumi on 9/4/15.
 * <p/>
 * Copyright (c) 2015 memleak.me. All rights reserved.
 */
public class PomorelloUser {
    private static final String TAG = "PomorelloUser";
    private static final String PREFS_NAME = "user_preferences";
    private static final String EXTRA_USER_SETTINGS = "extra_user_settings";

    private static PomorelloUser mInstance;
    private static final Object mLock = new Object();

    public static PomorelloUser getInstance(Context context) {
        synchronized (mLock) {
            if (null == mInstance) {
                SharedPreferences settings = context.getApplicationContext().
                        getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

                String json = settings.getString(EXTRA_USER_SETTINGS, null);
                if (null != json) {
                    Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
                    mInstance = gson.fromJson(json, PomorelloUser.class);
                }

                if (mInstance == null) {
                    mInstance = new PomorelloUser();
                }
            }

            return mInstance;
        }
    }

    @Expose
    private String accessToken;
    @Expose
    private String accessSecret;

    public boolean isAuthenticated() {
        return !TextUtils.isEmpty(accessToken);
    }

    public void setAccessToken(@NonNull String accessToken) {
        this.accessToken = accessToken;
    }

    public void setAccessSecret(@NonNull String accessSecret) {
        this.accessSecret = accessSecret;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getAccessSecret() {
        return accessSecret;
    }

    public void save(Context context) {
        SharedPreferences.Editor editor = context.getApplicationContext().
                getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit();

        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        String jsonObject = gson.toJson(mInstance);
        editor.putString(EXTRA_USER_SETTINGS, jsonObject);

        editor.apply();
    }
}
