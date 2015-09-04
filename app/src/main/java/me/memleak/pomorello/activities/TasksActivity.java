package me.memleak.pomorello.activities;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import butterknife.Bind;
import me.memleak.pomorello.R;
import me.memleak.pomorello.fragments.TaskFragment;

/**
 * Created by jafar_qaddoumi on 9/4/15.
 * <p/>
 * Copyright (c) 2015 memleak.me. All rights reserved.
 */
public class TasksActivity extends BaseActivity {

    public static void startActivity(Activity activity) {
        Intent intent = new Intent(activity, TasksActivity.class);
        activity.startActivity(intent);
    }

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

    @Bind(R.id.tasks_tbl_tabs)
    TabLayout tblTabs;
    @Bind(R.id.tasks_vpg_pages)
    ViewPager vpgPages;

    private String[] mTabNames;

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_tasks;
    }

    @Override
    protected void setupViews() {
        mTabNames = new String[TAB_NAMES_RES.length];
        for (int i = 0; i < mTabNames.length; i++) {
            mTabNames[i] = getString(TAB_NAMES_RES[i]);
        }

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
            super(mActivity.getSupportFragmentManager());
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
