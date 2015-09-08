package me.memleak.pomorello.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import io.realm.Realm;
import me.memleak.pomorello.activities.BaseActivity;

/**
 * Created by jafar_qaddoumi on 9/4/15.
 * <p/>
 * Copyright (c) 2015 memleak.me. All rights reserved.
 */
public abstract class BaseFragment extends Fragment {
    protected final String TAG = ((Object) this).getClass().getSimpleName();

    protected BaseActivity mParentActivity;

    @LayoutRes
    protected abstract int getLayoutResId();

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        mParentActivity = (BaseActivity) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(getLayoutResId(), container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ButterKnife.bind(BaseFragment.this, view);
    }

    @Override
    public void onDestroyView() {
        ButterKnife.unbind(BaseFragment.this);

        super.onDestroyView();
    }

    @Override
    public void onDetach() {
        mParentActivity = null;

        super.onDetach();
    }

    protected void setupActionBar(boolean showUp) {
        //mParentActivity.setupActionBar(showUp);
    }

    protected Realm getRealm() {
        return mParentActivity.getRealm();
    }
}
