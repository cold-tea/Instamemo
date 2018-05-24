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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.heavenlyhell.instamemo.Home.HomeActivity;
import com.heavenlyhell.instamemo.R;
import com.heavenlyhell.instamemo.models.User;
import com.heavenlyhell.instamemo.models.UserAccountSetting;
import com.heavenlyhell.instamemo.utils.BottomNavigationViewHelper;
import com.heavenlyhell.instamemo.utils.StringUtils;
import com.heavenlyhell.instamemo.utils.Validator;

import java.util.HashMap;
import java.util.Random;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "RegisterActivity";

    private EditText etRegisterEmail, etRegisterDisplayName;
    private EditText etRegisterPassword, etRegisterConfirmPassword;
    private Button btnRegister;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private ProgressBar pbRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        pbRegister = findViewById(R.id.pbRegister);
        etRegisterEmail= findViewById(R.id.etRegisterEmail);
        etRegisterPassword = findViewById(R.id.etRegisterPassword);
        etRegisterConfirmPassword = findViewById(R.id.etRegisterConfirmPassword);
        etRegisterDisplayName = findViewById(R.id.etRegisterDisplayName);

        btnRegister = findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Validator.checkEmpty(etRegisterEmail.getText().toString(),
                        etRegisterPassword.getText().toString(),
                        etRegisterConfirmPassword.getText().toString(),
                        etRegisterDisplayName.getText().toString())) {
                    if (Validator.isValidEmailAddress(etRegisterEmail.getText().toString())) {
                        if (etRegisterPassword.getText().toString()
                                .matches(etRegisterConfirmPassword.getText().toString())) {
                            pbRegister.setVisibility(View.VISIBLE);
                            firebaseAuth.createUserWithEmailAndPassword(etRegisterEmail.getText()
                                    .toString(), etRegisterPassword.getText().toString())
                                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if (task.isSuccessful()) {
                                                sendEmailVerification();
                                            } else {
                                                Toast.makeText(getApplicationContext(),getString
                                                        (R.string.sign_up_problem), Toast
                                                        .LENGTH_LONG).show();
                                            }
                                            pbRegister.setVisibility(View.GONE);
                                        }
                                    });
                        } else {
                            etRegisterPassword.setError(getString(R.string.password_mismatch));
                        }
                    } else {
                        etRegisterEmail.setError(getString(R.string.provide_valid_email));
                    }
                } else {
                    if (Validator.checkEmpty(etRegisterEmail.getText().toString()))
                        etRegisterEmail.setError(getString(R.string.provide_email));
                    else
                        etRegisterPassword.setError(getString(R.string.provide_password));
                }
            }
        });
    }

    private void sendEmailVerification() {
        firebaseAuth.getInstance().getCurrentUser().sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        checkIfUserExists();
                    }
                });
    }

    private void checkIfUserExists() {
        Log.d(TAG, "checkIfUserExists: called");
        databaseReference.child(getString(R.string.dbname_user_account_settings)).orderByChild(getString(R.string
                .dbfield_username))
                .equalTo(StringUtils.condenseString(etRegisterDisplayName.getText().toString()))
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        evaluateAndAppend(dataSnapshot);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    private void evaluateAndAppend(DataSnapshot snapshot) {
        if (snapshot.getChildrenCount() > 0) {
            Log.d(TAG, "evaluateAndAppend: called not zero");
            Random random = new Random(System.currentTimeMillis());
            long append = random.nextInt();
            setDefaultDatabase(StringUtils.condenseString(etRegisterDisplayName.getText()
                    .toString())
            + String.valueOf(append));
        } else {
            Log.d(TAG, "evaluateAndAppend: called is zero");
            setDefaultDatabase(StringUtils.condenseString(etRegisterDisplayName.getText()
                    .toString()));
        }
    }

    private void setDefaultDatabase(final String username) {
        Log.d(TAG, "setDefaultDatabase: setting database");
        User user = new User(username, firebaseAuth.getCurrentUser().getUid(),
                etRegisterEmail.getText().toString(), "1");
        databaseReference.child(getString(R.string.dbname_users))
                .child(firebaseAuth.getCurrentUser().getUid())
                .setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                UserAccountSetting userAccountSetting = new UserAccountSetting(
                        getString(R.string.default_status), etRegisterDisplayName.getText().toString(),
                        "0", "0", "0", username,"","");
                databaseReference.child(getString(R.string.dbname_user_account_settings))
                        .child(firebaseAuth.getCurrentUser().getUid())
                        .setValue(userAccountSetting).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplicationContext(), getString(R.string.email_verification),
                                Toast.LENGTH_LONG).show();

                        firebaseAuth.signOut();
                        Intent intentLogin = new Intent(getApplicationContext(), LoginActivity
                                .class);
                        startActivity(intentLogin);
                        finish();
                    }
                });
            }
        });
    }
}
