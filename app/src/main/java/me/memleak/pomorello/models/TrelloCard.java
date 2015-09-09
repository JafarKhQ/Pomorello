package me.memleak.pomorello.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by jafar_qaddoumi on 9/8/15.
 * <p/>
 * Copyright (c) 2015 memleak.me. All rights reserved.
 */
public class TrelloCard extends RealmObject {

    @PrimaryKey
    @Expose
    @SerializedName("id")
    private String id;

    @Expose
    @SerializedName("name")
    private String name;

    private int type;
    private int pomodorCount;
    private int lastWorkTime;

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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getPomodorCount() {
        return pomodorCount;
    }

    public void setPomodorCount(int pomodorCount) {
        this.pomodorCount = pomodorCount;
    }

    public int getLastWorkTime() {
        return lastWorkTime;
    }

    public void setLastWorkTime(int lastWorkTime) {
        this.lastWorkTime = lastWorkTime;
    }
}
