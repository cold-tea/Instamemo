package com.heavenlyhell.instamemo.utils;

/**
 * Created by sande on 3/6/2018.
 */

public class StringUtils {
    public static String condenseString(String displayName) {
        return displayName.replace(" ", ".");
    }

    public static String expandString(String userName) {
        return userName.replace(".", " ");
    }
}
