package com.heavenlyhell.instamemo.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.view.MenuItem;

import com.heavenlyhell.instamemo.Home.HomeActivity;
import com.heavenlyhell.instamemo.Likes.LikesActivity;
import com.heavenlyhell.instamemo.Profile.ProfileActivity;
import com.heavenlyhell.instamemo.R;
import com.heavenlyhell.instamemo.Search.SearchActivity;
import com.heavenlyhell.instamemo.Share.ShareActivity;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;


/**
 * Created by sande on 3/5/2018.
 */

public class BottomNavigationViewHelper {

    public static BottomNavigationViewEx bottomNavigation;

    public static void configureBottomNavigation(final Context context) {
        final BottomNavigationViewEx bottomNavigation = ((Activity) context).findViewById(R.id.bnv);
        BottomNavigationViewHelper.bottomNavigation = bottomNavigation;
        bottomNavigation.enableShiftingMode(false);
        bottomNavigation.enableItemShiftingMode(false);
        bottomNavigation.enableAnimation(false);

        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_home:
                        Intent intentHome = new Intent(context, HomeActivity.class);
                        context.startActivity(intentHome);
                        break;
                    case R.id.menu_search:
                        Intent intentSearch = new Intent(context, SearchActivity.class);
                        context.startActivity(intentSearch);
                        break;
                    case R.id.menu_share:
                        Intent intentShare = new Intent(context, ShareActivity.class);
                        context.startActivity(intentShare);
                        break;
                    case R.id.menu_likes:
                        Intent intentLikes = new Intent(context, LikesActivity.class);
                        context.startActivity(intentLikes);
                        break;
                    case R.id.menu_profile:
                        Intent intentProfile = new Intent(context, ProfileActivity.class);
                        context.startActivity(intentProfile);
                        break;
                }
                return false;
            }
        });
    }
}
