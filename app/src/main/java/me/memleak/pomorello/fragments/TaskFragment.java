package me.memleak.pomorello.fragments;

import me.memleak.pomorello.R;

/**
 * Created by jafar_qaddoumi on 9/4/15.
 * <p/>
 * Copyright (c) 2015 memleak.me. All rights reserved.
 */
public class TaskFragment extends BaseFragment {

    public static TaskFragment newInstance() {
        return new TaskFragment();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_task;
    }
}
