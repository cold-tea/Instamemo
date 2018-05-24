package com.heavenlyhell.instamemo.models;

import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by sande on 3/7/2018.
 */

public class UserSetting implements Serializable {
    private User user;
    private UserAccountSetting userAccount;

    public UserSetting(User user, UserAccountSetting userAccount) {
        this.user = user;
        this.userAccount = userAccount;
    }

    public UserSetting() {
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public UserAccountSetting getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(UserAccountSetting userAccount) {
        this.userAccount = userAccount;
    }

    @Override
    public String toString() {
        return "UserSetting{" +
                "user=" + user +
                ", userAccount=" + userAccount +
                '}';
    }
}
