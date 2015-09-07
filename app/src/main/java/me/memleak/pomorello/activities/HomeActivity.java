package me.memleak.pomorello.activities;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;

import butterknife.Bind;
import io.realm.RealmResults;
import me.memleak.pomorello.R;
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

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_home;
    }

    @Override
    protected void setupViews() {
        final Menu menu = nvgBoards.getMenu();
        final String lastBoeardId = mPomorelloUser.getLastSelectedBoardId();
        RealmResults<TrelloBoard> trelloBoards = mRealm.allObjects(TrelloBoard.class);
        for (int i = 0; i < trelloBoards.size(); i++) {
            TrelloBoard board = trelloBoards.get(i);
            MenuItem item = menu.add(board.getName());
            item.setCheckable(true);

            if (TextUtils.isEmpty(lastBoeardId) && 0 == i) {
                //set the firest item as selected
                item.setChecked(true);
            } else if (board.getId().equals(lastBoeardId)) {
                item.setChecked(true);
            }
        }

        getSupportFragmentManager().beginTransaction().add(R.id.home_frl_content,
                TasksFragment.newInstance("aaa")).commit();

        nvgBoards.setNavigationItemSelectedListener(onNavigationItemSelectedListener);
    }


    private NavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener =
            new NavigationView.OnNavigationItemSelectedListener() {

                @Override
                public boolean onNavigationItemSelected(MenuItem menuItem) {
                    return false;
                }
            };
}
