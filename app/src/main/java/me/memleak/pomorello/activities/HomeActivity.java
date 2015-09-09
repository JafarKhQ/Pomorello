package me.memleak.pomorello.activities;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;

import butterknife.Bind;
import de.greenrobot.event.EventBus;
import io.realm.RealmResults;
import me.memleak.pomorello.R;
import me.memleak.pomorello.fragments.ConfigFragment;
import me.memleak.pomorello.fragments.TasksFragment;
import me.memleak.pomorello.models.TrelloBoard;

/**
 * Created by jafar_qaddoumi on 9/4/15.
 * <p/>
 * Copyright (c) 2015 memleak.me. All rights reserved.
 */
public class HomeActivity extends BaseActivity {

    public static void startActivity(Activity activity) {
        Intent intent = new Intent(activity, HomeActivity.class);
        activity.startActivity(intent);
    }

    @Bind(R.id.home_drl_drawer)
    DrawerLayout drlDrawer;
    @Bind(R.id.home_nvg_boards)
    NavigationView nvgBoards;

    private RealmResults<TrelloBoard> mTrelloBoards;

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_home;
    }

    @Override
    protected void onResume() {
        super.onResume();

        EventBus.getDefault().register(this);
    }

    @Override
    protected void setupViews() {
        // not correct i know
        mActionBar.setHomeButtonEnabled(true);
        mActionBar.setDisplayHomeAsUpEnabled(true);

        mTrelloBoards = getRealm().allObjects(TrelloBoard.class);

        final Menu menu = nvgBoards.getMenu();
        final String lastBoardId = mPomorelloUser.getLastSelectedBoardId();

        int selectedBoardPos = -1;
        for (int i = 0; i < mTrelloBoards.size(); i++) {
            TrelloBoard board = mTrelloBoards.get(i);
            MenuItem item = menu.add(0, i, i, board.getName());
            item.setCheckable(true);

            if (0 == i && TextUtils.isEmpty(lastBoardId)) {
                //set the first item as selected
                selectedBoardPos = i;
                item.setChecked(true);
            } else if (board.getId().equals(lastBoardId)) {
                selectedBoardPos = i;
                item.setChecked(true);
            }
        }

        if (-1 != selectedBoardPos) {
            replaceFragment(mTrelloBoards.get(selectedBoardPos));
        }

        nvgBoards.setNavigationItemSelectedListener(onNavigationItemSelectedListener);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (drlDrawer.isDrawerOpen(Gravity.LEFT)) {
                    drlDrawer.closeDrawer(Gravity.LEFT);
                } else {
                    drlDrawer.openDrawer(Gravity.LEFT);
                }
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onPause() {
        EventBus.getDefault().unregister(this);

        super.onPause();
    }

    public void onEvent(TrelloBoard board) {
        replaceFragment(board);
    }

    private void replaceFragment(TrelloBoard selectedBoard) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.home_frl_content,
                        selectedBoard.isConfigured() ?
                                TasksFragment.newInstance(selectedBoard.getId()) :
                                ConfigFragment.newInstance(selectedBoard.getId()))
                .addToBackStack(null)
                .commit();
    }

    private NavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener =
            new NavigationView.OnNavigationItemSelectedListener() {

                @Override
                public boolean onNavigationItemSelected(MenuItem menuItem) {
                    final int position = menuItem.getItemId();
                    replaceFragment(mTrelloBoards.get(position));

                    // close all (i know we only have one)
                    drlDrawer.closeDrawer(Gravity.LEFT);
                    return true;
                }
            };
}
