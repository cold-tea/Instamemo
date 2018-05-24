package com.heavenlyhell.instamemo.utils;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.heavenlyhell.instamemo.models.User;
import com.heavenlyhell.instamemo.models.UserAccountSetting;
import com.heavenlyhell.instamemo.models.UserSetting;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sande on 3/7/2018.
 */

public class FirebaseHelper {

    private static final String TAG = "FirebaseHelper";

    private static final DatabaseReference dbUsers;
    private static final DatabaseReference dbUserAccountSettings;

    static {
        dbUsers = FirebaseDatabase.getInstance().getReference().child("users");
        dbUserAccountSettings = FirebaseDatabase.getInstance().getReference().child("user_account_settings");
    }

    public static void updateUserandUserAccount(final UserSetting userSetting) {
        Map<String, Object> mapUsers = new HashMap<>();
        mapUsers.put("email", userSetting.getUser().getEmail());
        mapUsers.put("user_id", userSetting.getUser().getUser_id());
        mapUsers.put("phone_number", userSetting.getUser().getPhone_number());
        mapUsers.put("username", userSetting.getUser().getUsername());

        Map<String, Object> mapUserAccount= new HashMap<>();
        mapUserAccount.put("username", userSetting.getUser().getUsername());
        mapUserAccount.put("display_name", userSetting.getUserAccount().getDisplay_name());
        mapUserAccount.put("description", userSetting.getUserAccount().getDescription());
        mapUserAccount.put("website", userSetting.getUserAccount().getWebsite());


        dbUsers.child(userSetting.getUser().getUser_id())
                .updateChildren(mapUsers, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        Log.d(TAG, "onComplete: Operation completed..");
                    }
                });

        dbUserAccountSettings.child(userSetting.getUser().getUser_id())
                .updateChildren(mapUserAccount, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        Log.d(TAG, "onComplete: Operation completed..");
                    }
                });
    }

    public static void updateUserandUserAccount(final UserSetting userSetting, final FirebaseUser firebaseUser) {
        dbUsers.child(firebaseUser.getUid()).removeValue(new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                Log.d(TAG, "onComplete: User removed and added");
                dbUsers.child(firebaseUser.getUid()).setValue(userSetting.getUser())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "onSuccess: Users node addition successful");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFailure: " + e.getMessage());
                        e.printStackTrace();
                    }
                });
            }
        });

        dbUserAccountSettings.child(firebaseUser.getUid()).removeValue(new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                Log.d(TAG, "onComplete: UserAccountSettings removed and added");
                dbUserAccountSettings.child(firebaseUser.getUid()).setValue(userSetting.getUserAccount())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "onSuccess: UserAccountSettings node addition successful");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFailure: " + e.getMessage());
                        e.printStackTrace();
                    }
                });
            }
        });
    }
}
