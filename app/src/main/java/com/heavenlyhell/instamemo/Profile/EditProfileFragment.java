package com.heavenlyhell.instamemo.Profile;


import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.ProviderQueryResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.heavenlyhell.instamemo.Login.LoginActivity;
import com.heavenlyhell.instamemo.R;
import com.heavenlyhell.instamemo.dialogs.ConfirmPasswordDialog;
import com.heavenlyhell.instamemo.models.Constants;
import com.heavenlyhell.instamemo.models.User;
import com.heavenlyhell.instamemo.models.UserAccountSetting;
import com.heavenlyhell.instamemo.models.UserSetting;
import com.heavenlyhell.instamemo.utils.BottomNavigationViewHelper;
import com.heavenlyhell.instamemo.utils.FirebaseHelper;
import com.heavenlyhell.instamemo.utils.StringUtils;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.picasso.Picasso;

import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfileFragment extends Fragment {

    private static final String TAG = "EditProfileFragment";

    private ImageView ivEditBack, ivEditCheck;
    private MaterialEditText etUsername, etWebsite;
    private MaterialEditText etDisplayname, etDescription;
    private MaterialEditText etEmail, etPhone;
    private CircleImageView ivEditMain;
    private ProgressBar pbEditProfile;

    private DatabaseReference dbReference;
    private FirebaseUser firebaseUser;

    private UserSetting userSetting;

    private boolean isEmailUpdated = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);

        dbReference = FirebaseDatabase.getInstance().getReference();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        ivEditBack = view.findViewById(R.id.ivEditProfileAppbarBack);
        ivEditCheck = view.findViewById(R.id.ivEditProfileAppbarCheckmark);
        ivEditMain = view.findViewById(R.id.ivEditProfileMain);
        etDescription = view.findViewById(R.id.etEditProfileDescription);
        etUsername = view.findViewById(R.id.etEditProfileUsername);
        etWebsite = view.findViewById(R.id.etEditProfileWeb);
        etDisplayname = view.findViewById(R.id.etEditProfileDisplayname);
        etEmail = view.findViewById(R.id.etEditProfileEmail);
        etPhone = view.findViewById(R.id.etEditProfilePhone);
        pbEditProfile = view.findViewById(R.id.pbEditProfile);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ivEditBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        ivEditCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: checkMark");
                updateCredentials();
            }
        });

        setUpBottomNavigation();
        setUpWidgets();
    }

    private void updateCredentials() {
        Log.d(TAG, "updateCredentials: called");
        String username = etUsername.getText().toString();
        String displayname = etDisplayname.getText().toString();
        String email = etEmail.getText().toString();
        String phone = etPhone.getText().toString();
        String description = etDescription.getText().toString();
        String website = etWebsite.getText().toString();

        User user = new User(username, firebaseUser.getUid(), email, phone);
        UserAccountSetting accountSetting = new UserAccountSetting(description, displayname, "0",
                "0", "0", username, website, "");
        userSetting = new UserSetting(user, accountSetting);

        if (email.equals(Constants.userSetting.getUser().getEmail())) {
            Log.d(TAG, "updateCredentials: Email not changed.");
            updateAllFields(username);
        } else {
            Log.d(TAG, "updateCredentials: Email changed");
            showDialog();
        }
    }

    private void updateAllFields(String username) {
        if (username.equals(Constants.userSetting.getUser().getUsername())) {
            pbEditProfile.setVisibility(View.GONE);
            Log.d(TAG, "updateCredentials: Username not changed");
            FirebaseHelper.updateUserandUserAccount(userSetting);
            afterUpdate(null);
        } else {
            Log.d(TAG, "updateCredentials: Username changed");
            checkIfUserExists();
        }
    }

    private void showDialog() {
        final EditText etPassword = new EditText(getContext());
        etPassword.setInputType(InputType.TYPE_TEXT_VARIATION_WEB_PASSWORD);
        etPassword.setWidth(100);
        etPassword.setHint(getString(R.string.password));
        new AlertDialog.Builder(getContext())
                .setTitle(getString(R.string.confirm_password))
                .setMessage(getString(R.string.email_change_notify))
                .setView(etPassword)
                .setPositiveButton(getString(R.string.confirm), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String password = etPassword.getText().toString();
                        proceedWithPassword(password);
                        dialog.dismiss();
                    }
                })
                .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();

                    }
                })
                .show();
    }

    private void proceedWithPassword(final String password) {
        if (TextUtils.isEmpty(password)) {
            Log.d(TAG, "proceedWithPassword: Password empty");
            Toast.makeText(getContext(), getString(R.string.provide_password),
                    Toast.LENGTH_LONG).show();
        }
        else {
            Log.d(TAG, "proceedWithPassword: Password not empty");
            AuthCredential credential = EmailAuthProvider
                    .getCredential(Constants.userSetting.getUser().getEmail(), password);
            pbEditProfile.setVisibility(View.VISIBLE);
            firebaseUser.reauthenticate(credential)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "onComplete: Reauthentication successful");
                                FirebaseAuth.getInstance().fetchProvidersForEmail(
                                        userSetting.getUser().getEmail())
                                        .addOnCompleteListener
                                        (new OnCompleteListener<ProviderQueryResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<ProviderQueryResult> task) {
                                                try {

                                                    if (task.getResult().getProviders().size()
                                                            == 1) {
                                                        pbEditProfile.setVisibility(View.GONE);
                                                        Log.d(TAG, "onComplete: Email exists ");
                                                        Toast.makeText(getContext(), getString(R
                                                                        .string.email_update_failed_exists),
                                                                Toast.LENGTH_LONG).show();

                                                    } else {
                                                        Log.d(TAG, "onComplete: Email does not exist");
                                                        firebaseUser.updateEmail(userSetting
                                                                .getUser().getEmail())
                                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                        if (task.isSuccessful()) {
                                                                            sendVerificationAndProceed(password);
                                                                        }
                                                                    }
                                                                });
                                                    }
                                                } catch (NullPointerException ex) {
                                                    pbEditProfile.setVisibility(View.GONE);
                                                    Log.d(TAG, "onComplete: NullPointerException");
                                                }
                                            }
                                        });


                            } else {
                                pbEditProfile.setVisibility(View.GONE);
                                Log.d(TAG, "onComplete: Reauthentication failed");
                                Toast.makeText(getContext(), getString(R.string.reauthentication_failed),
                                        Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }
    }

    private void sendVerificationAndProceed(String password) {
        Log.d(TAG, "User email address updated.");
        isEmailUpdated = true;
        FirebaseAuth.getInstance().signInWithEmailAndPassword(userSetting.getUser().getEmail(),
                password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                FirebaseAuth.getInstance().getCurrentUser().sendEmailVerification()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                FirebaseAuth.getInstance().signOut();
                                updateAllFields(userSetting.getUser().getUsername());
                            }
                        });
            }
        });

    }

    private void checkIfUserExists() {
        Log.d(TAG, "checkIfUserExists: called");
        dbReference.child(getString(R.string.dbname_user_account_settings)).orderByChild(getString(R.string
                .dbfield_username))
                .equalTo(userSetting.getUser().getUsername())
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
            String newUsername = userSetting.getUser().getUsername() + append;
            userSetting.getUser().setUsername(newUsername);
            userSetting.getUserAccount().setUsername(newUsername);
            FirebaseHelper.updateUserandUserAccount(userSetting);
            afterUpdate("");
        } else {
            Log.d(TAG, "evaluateAndAppend: called is zero");
            FirebaseHelper.updateUserandUserAccount(userSetting);
            afterUpdate(null);
        }
    }

    private void afterUpdate(@Nullable String value) {
        if (isEmailUpdated) {
            FirebaseAuth.getInstance().signOut();
            Intent intentLogin = new Intent(getContext(), LoginActivity.class);
            intentLogin.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intentLogin);
            getActivity().finish();
            if (value == null) {
                Toast.makeText(getContext(), getString(R.string.profile_update_success), Toast
                        .LENGTH_LONG).show();
            } else {
                Toast.makeText(getContext(), getString(R.string.profile_update_success_appended_random),
                        Toast.LENGTH_LONG).show();
            }
            Toast.makeText(getContext(), getString(R.string.email_update_success), Toast.LENGTH_LONG).show();
        } else {
            if (value == null)
                Toast.makeText(getContext(), getString(R.string.profile_update_success), Toast.LENGTH_LONG).show();
            else
                Toast.makeText(getContext(), getString(R.string.profile_update_success_appended_random),
                        Toast.LENGTH_LONG).show();
            Constants.userSetting = userSetting;
        }
        pbEditProfile.setVisibility(View.GONE);
    }

    private void setUpWidgets() {
        etUsername.setText(Constants.userSetting.getUserAccount().getUsername());
        etDisplayname.setText(Constants.userSetting.getUserAccount().getDisplay_name());
        etDescription.setText(Constants.userSetting.getUserAccount().getDescription());
        etWebsite.setText(Constants.userSetting.getUserAccount().getWebsite());

        etEmail.setText(Constants.userSetting.getUser().getEmail());
        etPhone.setText(Constants.userSetting.getUser().getPhone_number());
        if (!TextUtils.isEmpty(Constants.userSetting.getUserAccount().getProfile_photo()))
            Picasso.with(getContext()).load(Constants.userSetting.getUserAccount().getProfile_photo())
                    .placeholder(R.drawable.ic_user)
                    .error(R.drawable.ic_user)
                    .into(ivEditMain);
    }

    private void setUpBottomNavigation() {
        BottomNavigationViewHelper.configureBottomNavigation(getActivity());
        MenuItem item = BottomNavigationViewHelper.bottomNavigation.getMenu().getItem(Constants.PROFILE_INDEX);
        item.setChecked(true);
    }

}
