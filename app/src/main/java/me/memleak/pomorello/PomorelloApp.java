package me.memleak.pomorello;

import android.app.Application;
import android.support.annotation.NonNull;

import me.memleak.pomorello.models.PomorelloUser;

/**
 * Created by jafar_qaddoumi on 9/4/15.
 * <p/>
 * Copyright (c) 2015 memleak.me. All rights reserved.
 */
public class PomorelloApp extends Application {

    private static PomorelloUser sPomorelloUser;

    @NonNull
    public static PomorelloUser getPomorelloUser() {
        return sPomorelloUser;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        // by design(Java) this not a correct way to deal with static
        sPomorelloUser = PomorelloUser.getInstance(this);
    }

    @Override
    public void onLowMemory() {
        sPomorelloUser.save(this);

        super.onLowMemory();
    }
}
