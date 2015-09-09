package me.memleak.pomorello;

import android.app.Application;
import android.support.annotation.NonNull;

import io.realm.Realm;
import me.memleak.pomorello.models.PomorelloUser;

/**
 * Created by jafar_qaddoumi on 9/4/15.
 * <p/>
 * Copyright (c) 2015 memleak.me. All rights reserved.
 */
public class PomorelloApp extends Application {

    private static Realm sRealm;
    private static PomorelloUser sPomorelloUser;

    @NonNull
    public static PomorelloUser getPomorelloUser() {
        return sPomorelloUser;
    }

    @NonNull
    public static Realm getRealm() {
        return sRealm;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        // by design(Java) this not a correct way to deal with static
        //Realm.deleteRealmFile(this);
        sRealm = Realm.getInstance(this);
        sPomorelloUser = PomorelloUser.getInstance(this);
    }

    @Override
    public void onLowMemory() {
        sRealm.close();
        sPomorelloUser.save(this);

        super.onLowMemory();
    }

    @Override
    public void onTerminate() {
        sRealm.close();
        sPomorelloUser.save(this);

        super.onTerminate();
    }
}
