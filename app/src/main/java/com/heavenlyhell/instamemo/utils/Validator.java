package com.heavenlyhell.instamemo.utils;

import android.text.TextUtils;

/**
 * Created by sande on 3/6/2018.
 */

public class Validator {

    public static boolean checkEmpty(String... values) {
        for (String value : values) {
            if (TextUtils.isEmpty(value))
                return true;
        }
        return false;
    }

    public static boolean isValidEmailAddress(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }
}
