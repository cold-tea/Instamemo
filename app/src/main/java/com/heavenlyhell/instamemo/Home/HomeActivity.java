package com.heavenlyhell.instamemo.Home;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.heavenlyhell.instamemo.Login.LoginActivity;
import com.heavenlyhell.instamemo.R;
import com.heavenlyhell.instamemo.models.Constants;
import com.heavenlyhell.instamemo.utils.BottomNavigationViewHelper;
import com.heavenlyhell.instamemo.utils.SectionPagerAdapter;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

public class HomeActivity extends AppCompatActivity {

    private static final String TAG = "HomeActivity";

    private ViewPager vpHome;
    private TabLayout tlHome;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        authStateListener = null;
        firebaseAuth = FirebaseAuth.getInstance();
        vpHome = findViewById(R.id.vpHome);
        tlHome = findViewById(R.id.tlHome);


        setUpBottomNavigation();
        setUpFragments();
        setAuthStateListener();
    }

    private void setUpBottomNavigation() {
        BottomNavigationViewHelper.configureBottomNavigation(this);
        MenuItem item = BottomNavigationViewHelper.bottomNavigation.getMenu().getItem(Constants.HOME_INDEX);
        item.setChecked(true);
    }

    private void setUpFragments() {
        SectionPagerAdapter adapter = new SectionPagerAdapter(getSupportFragmentManager());
        adapter.addFragments(new CameraFragment());
        adapter.addFragments(new HomeFragment());
        adapter.addFragments(new MessagesFragment());

        vpHome.setAdapter(adapter);

        tlHome.setupWithViewPager(vpHome);
        tlHome.getTabAt(0).setIcon(R.drawable.ic_camera);
        tlHome.getTabAt(1).setIcon(R.drawable.ic_insta);
        tlHome.getTabAt(2).setIcon(R.drawable.ic_messages);
    }

    private void setAuthStateListener() {
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {
                    Log.d(TAG, "onAuthStateChanged: Not logged in..");
                    Intent intentLogin = new Intent(getApplicationContext(), LoginActivity.class);
                    intentLogin.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intentLogin.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intentLogin);
                    finish();
                } else {
                    Log.d(TAG, "onAuthStateChanged: Logged in.." + firebaseAuth.getCurrentUser().getUid());
                }
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (authStateListener != null)
            firebaseAuth.removeAuthStateListener(authStateListener);
    }
}
