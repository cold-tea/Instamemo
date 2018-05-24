package com.heavenlyhell.instamemo.Profile;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.heavenlyhell.instamemo.Home.CameraFragment;
import com.heavenlyhell.instamemo.Home.HomeFragment;
import com.heavenlyhell.instamemo.Home.MessagesFragment;
import com.heavenlyhell.instamemo.R;
import com.heavenlyhell.instamemo.utils.BottomNavigationViewHelper;
import com.heavenlyhell.instamemo.utils.SectionPagerAdapter;
import com.heavenlyhell.instamemo.utils.SectionStatePagerAdapter;

public class AccountsettingActivity extends AppCompatActivity {

    private ImageView ivSettingAppBar;
    private ListView lvSettings;
    private ViewPager vpHome;
    private RelativeLayout rlSettingsMain;
    private SectionStatePagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accountsetting);

        ivSettingAppBar = findViewById(R.id.ivAccountSettingAppbar);
        lvSettings = findViewById(R.id.lvSettings);
        vpHome = findViewById(R.id.vpHome);
        rlSettingsMain = findViewById(R.id.rlSettingsMain);

        ivSettingAppBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout
                .simple_list_item_1, new String[] {getString(R.string.edit_profile),
            getString(R.string.sign_out)});
        lvSettings.setAdapter(adapter);
        lvSettings.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                setCurrentItem(position);
            }
        });

        setUpBottomNavigation();
        setUpFragments();
    }

    private void setUpBottomNavigation() {
        BottomNavigationViewHelper.configureBottomNavigation(this);
        MenuItem item = BottomNavigationViewHelper.bottomNavigation.getMenu().getItem(4);
        item.setChecked(true);
    }

    private void setCurrentItem(int position) {
        rlSettingsMain.setVisibility(View.GONE);
        vpHome.setAdapter(adapter);
        vpHome.setCurrentItem(position);
    }

    private void setUpFragments() {
        adapter = new SectionStatePagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new EditProfileFragment());
        adapter.addFragment(new SignOutFragment());
    }
}
