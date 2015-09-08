package me.memleak.pomorello.models;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import java.util.ArrayList;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;

/**
 * Created by jafar_qaddoumi on 9/8/15.
 * <p/>
 * Copyright (c) 2015 memleak.me. All rights reserved.
 */
public class PomorelloList extends RealmObject {
    @Ignore
    public static final int TAB_TODO = 0;
    @Ignore
    public static final int TAB_DOING = 1;
    @Ignore
    public static final int TAB_DONE = 2;
    @Ignore
    private static final String SEPARATOR_IDS = ",";
    @Ignore
    private static final int LENGTH_ID_WITH_SEPARATOR = 25;

    @PrimaryKey
    private String boardId;

    // Currently Realm dosenot support Array/List of String
    // So I will add all ids ase String with ',' suppurated
    private String todoIds;
    private String doingIds;
    private String doneIds;

    public String getDoneIds() {
        return doneIds;
    }

    public void setDoneIds(String doneIds) {
        this.doneIds = doneIds;
    }

    public String getBoardId() {
        return boardId;
    }

    public void setBoardId(String boardId) {
        this.boardId = boardId;
    }

    public String getTodoIds() {
        return todoIds;
    }

    public void setTodoIds(String todoIds) {
        this.todoIds = todoIds;
    }

    public String getDoingIds() {
        return doingIds;
    }

    public void setDoingIds(String doingIds) {
        this.doingIds = doingIds;
    }

    @Nullable
    public static String[] getTabTaskIds(@NonNull PomorelloList pomorelloList, int type) {
        String source;
        switch (type) {
            case TAB_TODO:
                source = pomorelloList.getTodoIds();
                break;

            case TAB_DOING:
                source = pomorelloList.getDoingIds();
                break;

            case TAB_DONE:
                source = pomorelloList.getDoneIds();
                break;

            default:
                throw new IllegalArgumentException("Invalid tasks type " + type);
        }

        return getTabTaskIds(source);
    }

    @Nullable
    private static String[] getTabTaskIds(String ids) {
        if (TextUtils.isEmpty(ids)) {
            return null;
        }

        return ids.split(SEPARATOR_IDS);
    }

    @Nullable
    public static String getTaskIdsString(ArrayList<String> ids) {
        if (null == ids || ids.size() < 1) {
            return null;
        }

        // id length + 1 for separator(25), plus one extra
        StringBuilder stringBuilder = new StringBuilder((ids.size() + 1) * LENGTH_ID_WITH_SEPARATOR);
        for (int i = 0; i < ids.size(); i++) {
            stringBuilder.append(ids.get(i));
            stringBuilder.append(SEPARATOR_IDS);
        }

        // remove the last separator
        stringBuilder.setLength(stringBuilder.length() - 1);
        return stringBuilder.toString();
    }
}
