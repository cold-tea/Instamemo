package com.heavenlyhell.instamemo.Login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.heavenlyhell.instamemo.Home.HomeActivity;
import com.heavenlyhell.instamemo.R;
import com.heavenlyhell.instamemo.utils.BottomNavigationViewHelper;
import com.heavenlyhell.instamemo.utils.Validator;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    private EditText etLoginEmail;
    private EditText etLoginPassword;
    private Button btnLogin;
    private FirebaseAuth firebaseAuth;
    private ProgressBar pbLogin;
    private TextView tvRegister;
    private FirebaseAuth.AuthStateListener authStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        authStateListener = null;
        pbLogin = findViewById(R.id.pbLogin);
        firebaseAuth = FirebaseAuth.getInstance();
        etLoginEmail = findViewById(R.id.etLoginEmail);
        etLoginPassword = findViewById(R.id.etLoginPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvRegister = findViewById(R.id.tvRegister);

        setAuthStateListener();

        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentRegister = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intentRegister);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Validator.checkEmpty(etLoginEmail.getText().toString(),
                        etLoginPassword.getText().toString())) {
                    if (Validator.isValidEmailAddress(etLoginEmail.getText().toString())) {
                        pbLogin.setVisibility(View.VISIBLE);
                        firebaseAuth.signInWithEmailAndPassword(etLoginEmail.getText().toString(),
                                etLoginPassword.getText().toString())
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {

                                        } else {
                                            Toast.makeText(getApplicationContext(), getString(R
                                                            .string.authentication_failed),
                                                    Toast
                                                    .LENGTH_LONG).show();
                                        }
                                        pbLogin.setVisibility(View.GONE);
                                    }
                                });
                    } else {
                        etLoginEmail.setError(getString(R.string.provide_valid_email));
                    }
                } else {
                    if (Validator.checkEmpty(etLoginEmail.getText().toString()))
                        etLoginEmail.setError(getString(R.string.provide_email));
                    else
                        etLoginPassword.setError(getString(R.string.provide_password));
                }
            }
        });

    }

    private void setAuthStateListener() {
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null) {
                    if (firebaseAuth.getCurrentUser().isEmailVerified()) {
                        Intent intentHome = new Intent(getApplicationContext(),
                                HomeActivity.class);
                        intentHome.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intentHome.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intentHome);
                        finish();
                    } else {
                        firebaseAuth.signOut();
                        Toast.makeText(getApplicationContext(), getString(R.string.email_unverified), Toast
                                .LENGTH_LONG).show();
                        Log.d(TAG, "onAuthStateChanged: unVerified email..");
                    }
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
