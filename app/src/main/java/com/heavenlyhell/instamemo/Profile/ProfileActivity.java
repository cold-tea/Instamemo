package com.heavenlyhell.instamemo.Profile;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.heavenlyhell.instamemo.Login.LoginActivity;
import com.heavenlyhell.instamemo.R;
import com.heavenlyhell.instamemo.utils.BottomNavigationViewHelper;
import com.heavenlyhell.instamemo.utils.GridImageAdapter;
import com.heavenlyhell.instamemo.utils.ItemOffsetDecoration;

import java.util.Arrays;

public class ProfileActivity extends AppCompatActivity {

    private static final String TAG = "ProfileActivity";

    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseAuth firebaseAuth;
    private FrameLayout flContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        flContainer = findViewById(R.id.flProfileContainer);
        authStateListener = null;
        firebaseAuth = FirebaseAuth.getInstance();
        setAuthStateListener();
        init();
    }

    private void init() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        ProfileFragment profileFragment = new ProfileFragment();
        transaction.replace(R.id.flProfileContainer, profileFragment, getString(R.string.fragment_profile));
        //transaction.addToBackStack("profile_fragment");
        transaction.commit();
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
                    Log.d(TAG, "onAuthStateChanged: Logged in..");
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
