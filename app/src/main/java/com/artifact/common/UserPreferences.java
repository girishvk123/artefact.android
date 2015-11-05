package com.artifact.common;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by girish.kulkarni on 9/30/2015.
 */
public class UserPreferences {
    private static final String PREFERENCE_FILE = "pref_file";
    public static final String USER_NAME = "user_name";
    public static final String EMAIL_ID = "email_id";
    public static final String PHONE_NUMBER = "phone_number";
    public static final String PROFILE_PICTURE = "profile_picture";
    public static final String PROFILE_PICTURE_URL = "profile_picture_url";
    public static final String USER_ID = "user_id";
    public static final String USER_INTEREST = "user_interest";

    public static SharedPreferences getUserPreferences(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREFERENCE_FILE,Context.MODE_PRIVATE);
        return preferences;
    }
}
