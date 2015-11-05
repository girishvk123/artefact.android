package com.artifact;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;

import com.artifact.common.UserPreferences;
import com.artifact.login.LoginActivity;
import com.artifact.navigationdrawer.MainActivity;
import com.artifact.navigationdrawer.MainActivity1;
import com.artifact.notification.GCMRegister;

/**
 *  Welcome screen of the app
 */
public class SplashScreen extends Activity {

    private static final long SPLASH_DELAY = 1500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_splash_screen);
//        getActionBar().hide();

        // proceed to login screen after delay
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
               // GCMRegister.checkPlayServices(SplashScreen.this);
                SharedPreferences preferences = UserPreferences.getUserPreferences(SplashScreen.this);
                //SharedPreferences.Editor editor = preferences.edit();
                //editor.putString(UserPreferences.USER_ID, "13");
                //editor.commit();
                if (!TextUtils.isEmpty(preferences.getString(UserPreferences.USER_ID, null))){
                    Intent loginIntent = new Intent(SplashScreen.this, MainActivity1.class);
                    startActivity(loginIntent);
                    finish();
                } else {
                    Intent loginIntent = new Intent(SplashScreen.this, LoginActivity.class);
                    startActivity(loginIntent);
                    finish();
                }
            }
        }, SPLASH_DELAY);
    }

}
