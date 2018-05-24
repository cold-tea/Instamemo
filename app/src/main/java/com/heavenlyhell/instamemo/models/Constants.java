package com.heavenlyhell.instamemo.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sande on 3/7/2018.
 */

public class Constants {
    public static UserSetting userSetting;
    public static final int HOME_INDEX;
    public static final int SEARCH_INDEX;
    public static final int SHARE_INDEX;
    public static final int LIKES_INDEX;
    public static final int PROFILE_INDEX;

    public static List<PhotosModel> photos = new ArrayList<>();


    static {
        userSetting = new UserSetting();
        HOME_INDEX = 0;
        SEARCH_INDEX = 1;
        SHARE_INDEX = 2;
        LIKES_INDEX = 3;
        PROFILE_INDEX = 4;
    }
}
