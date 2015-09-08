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
import me.memleak.pomorello.models.PomorelloList;
import me.memleak.pomorello.models.TrelloBoard;

/**
 * Created by jafar_qaddoumi on 9/7/15.
 * <p/>
 * Copyright (c) 2015 memleak.me. All rights reserved.
 */
public class TasksFragment extends BaseFragment {


    private static final int[] TAB_NAMES_RES = {
            R.string.tasks_tab_todo,
            R.string.tasks_tab_doing,
            R.string.tasks_tab_done
    };

    public static TasksFragment newInstance(String boardId) {
        Bundle args = new Bundle();
        args.putString(TrelloBoard.EXTRA_BOARD_ID, boardId);

        TasksFragment fg = new TasksFragment();
        fg.setArguments(args);
        return fg;
    }

    @Bind(R.id.tasks_tbl_tabs)
    TabLayout tblTabs;
    @Bind(R.id.tasks_vpg_pages)
    ViewPager vpgPages;

    private TrelloBoard mBoard;
    private PomorelloList mPomorelloList;

    private String[] mTabNames;
    private TaskFragment[] mTabs;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String boardId = getArguments().getString(TrelloBoard.EXTRA_BOARD_ID);
        mBoard = getRealm().where(TrelloBoard.class)
                .equalTo("id", boardId)
                .findFirst();

        mPomorelloList = getRealm().where(PomorelloList.class)
                .equalTo("boardId", mBoard.getId())
                .findFirst();

        mTabNames = new String[TAB_NAMES_RES.length];
        for (int i = 0; i < mTabNames.length; i++) {
            mTabNames[i] = getString(TAB_NAMES_RES[i]);
        }

        mTabs = new TaskFragment[TAB_NAMES_RES.length];
        mTabs[0] = TaskFragment.newInstance(PomorelloList.TAB_TODO);
        mTabs[1] = TaskFragment.newInstance(PomorelloList.TAB_DOING);
        mTabs[2] = TaskFragment.newInstance(PomorelloList.TAB_DONE);
    }

    @Override
    public void onResume() {
        super.onResume();
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
            return mTabs[position];
        }

        @Override
        public int getCount() {
            if (null == mTabs) {
                return 0;
            }

            return mTabs.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTabNames[position];
        }
    }
}
