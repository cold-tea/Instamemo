package com.heavenlyhell.instamemo.Likes;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.heavenlyhell.instamemo.R;
import com.heavenlyhell.instamemo.models.Constants;
import com.heavenlyhell.instamemo.utils.BottomNavigationViewHelper;

public class LikesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_likes);
        setUpBottomNavigation();
    }

    private void setUpBottomNavigation() {
        BottomNavigationViewHelper.configureBottomNavigation(this);
        MenuItem item = BottomNavigationViewHelper.bottomNavigation.getMenu().getItem(Constants.LIKES_INDEX);
        item.setChecked(true);
    }
}
