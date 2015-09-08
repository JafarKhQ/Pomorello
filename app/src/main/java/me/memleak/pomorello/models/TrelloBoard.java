package me.memleak.pomorello.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;

/**
 * Created by jafar_qaddoumi on 9/7/15.
 * <p/>
 * Copyright (c) 2015 memleak.me. All rights reserved.
 */
public class TrelloBoard extends RealmObject {

    @Ignore
    public static final String EXTRA_BOARD = "extra_board";
    @Ignore
    public static final String EXTRA_BOARD_ID = "extra_board_id";

    @Expose
    @SerializedName("id")
    @PrimaryKey
    private String id;

    @Expose
    @SerializedName("name")
    private String name;

    private boolean isConfigured;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isConfigured() {
        return isConfigured;
    }

    public void setIsConfigured(boolean isConfigured) {
        this.isConfigured = isConfigured;
    }
}
