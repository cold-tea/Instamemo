package com.heavenlyhell.instamemo.Share;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.heavenlyhell.instamemo.Home.CameraFragment;
import com.heavenlyhell.instamemo.Home.HomeFragment;
import com.heavenlyhell.instamemo.Home.MessagesFragment;
import com.heavenlyhell.instamemo.Login.LoginActivity;
import com.heavenlyhell.instamemo.R;
import com.heavenlyhell.instamemo.models.Constants;
import com.heavenlyhell.instamemo.utils.BottomNavigationViewHelper;
import com.heavenlyhell.instamemo.utils.Permissions;
import com.heavenlyhell.instamemo.utils.SectionPagerAdapter;

public class ShareActivity extends AppCompatActivity {

    private static final String TAG = "ShareActivity";
    private static final int PERMISSION_REQUEST_CODE = 1;

    private ViewPager vpShare;
    private TabLayout tlShare;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);

        authStateListener = null;
        firebaseAuth = FirebaseAuth.getInstance();
        vpShare = findViewById(R.id.vpHome);
        tlShare = findViewById(R.id.tlShare);

        if (checkForPermissions()) {

        } else {
            ActivityCompat.requestPermissions(this, Permissions.PERMISSIONS,
                    PERMISSION_REQUEST_CODE);
        }
        setAuthStateListener();
        setUpFragments();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                for (int grantResult : grantResults) {
                    if (grantResult != PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(getApplicationContext(), "Grant all required permissions " +
                                "to continue", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
        }
    }

    private boolean checkForPermissions() {
        for (String permission : Permissions.PERMISSIONS) {
            if (!checkPermission(permission)) {
                return false;
            }
        }
        return true;
    }

    private boolean checkPermission(String permission) {
        if (ContextCompat.checkSelfPermission(this, permission)
                != PackageManager.PERMISSION_GRANTED) {
            return false;
        }
        return true;
    }

    private void setUpFragments() {
        SectionPagerAdapter adapter = new SectionPagerAdapter(getSupportFragmentManager());
        adapter.addFragments(new GalleryFragment());
        adapter.addFragments(new PhotoFragment());
        adapter.addFragments(new VideoFragment());

        vpShare.setAdapter(adapter);

        tlShare.setupWithViewPager(vpShare);
        tlShare.getTabAt(0).setText(getString(R.string.gallery));
        tlShare.getTabAt(1).setText(getString(R.string.photo));
        tlShare.getTabAt(2).setText(getString(R.string.video));
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

    public void setCurrentItem(int position) {
        vpShare.setCurrentItem(position);
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
