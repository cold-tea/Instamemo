package com.heavenlyhell.instamemo.Search;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.heavenlyhell.instamemo.Home.CameraFragment;
import com.heavenlyhell.instamemo.Home.HomeFragment;
import com.heavenlyhell.instamemo.Home.MessagesFragment;
import com.heavenlyhell.instamemo.R;
import com.heavenlyhell.instamemo.models.Constants;
import com.heavenlyhell.instamemo.utils.BottomNavigationViewHelper;
import com.heavenlyhell.instamemo.utils.SectionPagerAdapter;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

public class SearchActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        setUpBottomNavigation();
    }

    private void setUpBottomNavigation() {
        BottomNavigationViewHelper.configureBottomNavigation(this);
        MenuItem item = BottomNavigationViewHelper.bottomNavigation.getMenu().getItem(Constants.SEARCH_INDEX);
        item.setChecked(true);
    }
}
