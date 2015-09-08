package me.memleak.pomorello.models;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

/**
 * Created by jafar_qaddoumi on 9/8/15.
 * <p/>
 * Copyright (c) 2015 memleak.me. All rights reserved.
 */
public class TrelloCard extends RealmObject {

    @SerializedName("id")
    private String id;

    @SerializedName("name")
    private String name;

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
}
