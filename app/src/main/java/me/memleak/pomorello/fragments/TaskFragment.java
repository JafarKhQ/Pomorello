package me.memleak.pomorello.fragments;

import android.os.Bundle;

import me.memleak.pomorello.R;

/**
 * Created by jafar_qaddoumi on 9/4/15.
 * <p/>
 * Copyright (c) 2015 memleak.me. All rights reserved.
 */
public class TaskFragment extends BaseFragment {
    private static final String EXTRA_TAB_TYPE = "extra_tab_type";

    public static TaskFragment newInstance(int type) {
        Bundle args = new Bundle();
        args.putInt(EXTRA_TAB_TYPE, type);

        TaskFragment fg = new TaskFragment();
        fg.setArguments(args);
        return fg;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_task;
    }
}
