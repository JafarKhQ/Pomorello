package me.memleak.pomorello.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import butterknife.Bind;
import me.memleak.pomorello.R;

/**
 * Created by jafar_qaddoumi on 9/7/15.
 * <p/>
 * Copyright (c) 2015 memleak.me. All rights reserved.
 */
public class TasksFragment extends BaseFragment {
    public static final int TAB_TODO = 0;
    public static final int TAB_DOING = 1;
    public static final int TAB_DONE = 2;

    private static final TaskFragment[] TAB_FRAGMENTS = {
            TaskFragment.newInstance(TAB_TODO),
            TaskFragment.newInstance(TAB_DOING),
            TaskFragment.newInstance(TAB_DONE)
    };
    private static final int[] TAB_NAMES_RES = {
            R.string.tasks_tab_todo,
            R.string.tasks_tab_doing,
            R.string.tasks_tab_done
    };

    private static final String EXTRA_BOARD_ID = "extra_board_id";

    public static TasksFragment newInstance(String boardId) {
        Bundle args = new Bundle();
        args.putString(EXTRA_BOARD_ID, boardId);

        TasksFragment fg = new TasksFragment();
        fg.setArguments(args);
        return fg;
    }

    @Bind(R.id.tasks_tbl_tabs)
    TabLayout tblTabs;
    @Bind(R.id.tasks_vpg_pages)
    ViewPager vpgPages;

    private String[] mTabNames;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mTabNames = new String[TAB_NAMES_RES.length];
        for (int i = 0; i < mTabNames.length; i++) {
            mTabNames[i] = getString(TAB_NAMES_RES[i]);
        }
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_tasks;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        vpgPages.setAdapter(new PagesAdapter());

        tblTabs.setTabsFromPagerAdapter(vpgPages.getAdapter());
        tblTabs.setupWithViewPager(vpgPages);
        tblTabs.setOnTabSelectedListener(onTabSelectedListener);
    }

    private TabLayout.OnTabSelectedListener onTabSelectedListener = new TabLayout.OnTabSelectedListener() {

        @Override
        public void onTabSelected(TabLayout.Tab tab) {

        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {

        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {

        }
    };

    class PagesAdapter extends FragmentPagerAdapter {

        public PagesAdapter() {
            super(getChildFragmentManager());
        }

        @Override
        public Fragment getItem(int position) {
            return TAB_FRAGMENTS[position];
        }

        @Override
        public int getCount() {
            return TAB_FRAGMENTS.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTabNames[position];
        }
    }
}
