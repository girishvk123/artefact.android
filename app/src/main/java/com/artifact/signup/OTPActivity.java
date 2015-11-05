package com.artifact.signup;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.artifact.R;
import com.artifact.common.AFUtils;
import com.artifact.common.Const;
import com.artifact.common.DialogUtils;
import com.artifact.common.UserPreferences;
import com.artifact.interest.InterestActivity;
import com.artifact.navigationdrawer.MainActivity;
import com.artifact.navigationdrawer.MainActivity1;
import com.artifact.user.Profile;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by girish.kulkarni on 9/24/2015.
 */
public class OTPActivity extends ActionBarActivity /*implements View.OnClickListener*/{

    public static final String FROM_LOGIN_SCREEN = "login";
    public static final String FROM_SIGNUP_SCREEN = "signup";

    public static final String KEY_PHONE_NUMBER = "phone_number";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_FROM_SCREEN = "from_screen";
    public static final String KEY_USER_NAME = "user_name";
    public static final String KEY_EMAIL_ID = "email";
    public static final String KEY_PROFILE_PIC = "profile_pic";

    EditText otpEditText;
    String otpString;
    String phoneNumber;
    String password;
    String userName;
    String emailId;
    String profilePicPath;

    Activity activity;
    private Toolbar mToolbar;
    android.support.v7.app.ActionBar mActionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.otp_screen);
        mToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        setSupportActionBar(mToolbar);

        mActionBar = getSupportActionBar();
        mActionBar .hide();
        activity = this;

        final Intent intent = getIntent();

        otpEditText = (EditText)findViewById(R.id.enterotp);
        //otpEditText.setOnClickListener(this);

        Button verifyButton = (Button)findViewById(R.id.verify_otp);
        verifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                otpString = otpEditText.getText().toString();
                if (!TextUtils.isEmpty(otpString)) {
                    if (otpString.equals("1234")) {
                        // check from screen
                        if (intent != null) {
                            if (intent.getStringExtra(KEY_FROM_SCREEN).equalsIgnoreCase(FROM_LOGIN_SCREEN)) {
                                phoneNumber = intent.getStringExtra(KEY_PHONE_NUMBER);
                                password = intent.getStringExtra(KEY_PASSWORD);
                                new LoginTask().execute();
                            } else if (intent.getStringExtra(KEY_FROM_SCREEN).equalsIgnoreCase(FROM_SIGNUP_SCREEN)) {
                                phoneNumber = intent.getStringExtra(KEY_PHONE_NUMBER);
                                password = intent.getStringExtra(KEY_PASSWORD);
                                userName = intent.getStringExtra(KEY_USER_NAME);
                                emailId = intent.getStringExtra(KEY_EMAIL_ID);
                                profilePicPath = intent.getStringExtra(KEY_PROFILE_PIC);
                                new SignupTask().execute();
                            }
                        }


                    }else{
                        DialogUtils.showPutBackgroundAlerDialog(OTPActivity.this, getResources().getString(R.string.error_otp_invalid));
                    }
                } else {
                    DialogUtils.showPutBackgroundAlerDialog(OTPActivity.this, getResources().getString(R.string.error_otp));
                }
            }
        });

    }



    private class LoginTask extends AsyncTask<Void, Void, String> {
        private ProgressDialog pDialog = null;
        private String responseData = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(activity);
            pDialog.setMessage(/*activity.getResources().getString(R.string.loading)*/"Loading");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(Void... arg0) {
            String url = Const.BASE_URL;
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(url);

        /* FOR LOGIN API ***************************************************************************************/

            try {
                MultipartEntity entity = new MultipartEntity();
                String android_id = Settings.Secure.getString(activity.getContentResolver(), Settings.Secure.ANDROID_ID);

                entity.addPart("counter", new StringBody("login"));
                entity.addPart("mobile", new StringBody(phoneNumber));
                entity.addPart("pass", new StringBody(password));
                entity.addPart("device", new StringBody(android_id));
                entity.addPart("secretkey", new StringBody("nzyeU3RMt0OudAKtmX/O6v7gK6IXQ/LeqOls6nuysB6jbwj0yaWKRI6AFc5FYbdBsWRCiHWVzpUobbn94uFl/F6TgH9xGv3GceqfivgXYiGJIXikRRccDB0BfXKqHkBuJxnvNmnBHy3nER191I4dR91oxuQu+9DuGxPVK3H56U8="));
                entity.addPart("OTP", new StringBody("1234"));
                Log.d("TAG", "Login DEVICE_ID : " + android_id);
                httppost.setEntity(entity);
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity resEntity = response.getEntity();
                if (resEntity != null) {
                    InputStream instream = resEntity.getContent();
                    responseData = convertStreamToString(instream);
                    instream.close();
                    Log.d("TAG", "Login Response : " + responseData);
                }

            }catch(Exception e){
                e.printStackTrace();
            }

            return responseData;

        }

        /**
         * Converting stream to string
         */
        public String convertStreamToString(InputStream is) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is),
                    2 * 1024);
            StringBuilder sb = new StringBuilder(2 * 1024);

            String line = null;
            try {
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return sb.toString();
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if(pDialog != null && pDialog.isShowing()){
                pDialog.dismiss();
            }
            if(result != null){
                try {
                    JSONObject resObject = new JSONObject(result);
                    JSONObject responseObject = resObject.getJSONObject("Response");
                    if (responseObject.getInt("status") == 200) {
                        Profile userProfile = new Profile();
                        userProfile.setPhoneNumber(phoneNumber);
                        userProfile.setUserId(responseObject.getString("UserID"));
                        userProfile.setUserName(responseObject.getString("Name"));
                        userProfile.setPicUrl(Const.IMAGE_URL + File.separator + responseObject.getString("DP"));
                       // userProfile.setProfilePicture();
                        SharedPreferences preferences = UserPreferences.getUserPreferences(activity);
                        SharedPreferences.Editor preEditor = preferences.edit();
                        preEditor.putString(UserPreferences.USER_NAME, userProfile.getUserName());
                        preEditor.putString(UserPreferences.EMAIL_ID, userProfile.getEmailId());
                        preEditor.putString(UserPreferences.PHONE_NUMBER, userProfile.getPhoneNumber());
                        preEditor.putString(UserPreferences.PROFILE_PICTURE_URL, userProfile.getPicUrl());
                        preEditor.putString(UserPreferences.USER_ID, userProfile.getUserId());
                        preEditor.commit();

                        Intent loginIntent = new Intent(activity, MainActivity1.class);
                        startActivity(loginIntent);
                        finish();
                    }
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }


    }





    private class SignupTask extends AsyncTask<Void, Void, String> {
        private ProgressDialog pDialog = null;
        private String responseData = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(activity);
            pDialog.setMessage(/*activity.getResources().getString(R.string.loading)*/"Loading");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(Void... arg0) {
            String url = Const.BASE_URL;
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(url);

        /* FOR SIGNUP API ***************************************************************************************/

            try {
                MultipartEntity entity = new MultipartEntity();
                String android_id = Settings.Secure.getString(activity.getContentResolver(), Settings.Secure.ANDROID_ID);

                entity.addPart("secretkey", new StringBody("nzyeU3RMt0OudAKtmX/O6v7gK6IXQ/LeqOls6nuysB6jbwj0yaWKRI6AFc5FYbdBsWRCiHWVzpUobbn94uFl/F6TgH9xGv3GceqfivgXYiGJIXikRRccDB0BfXKqHkBuJxnvNmnBHy3nER191I4dR91oxuQu+9DuGxPVK3H56U8="));
                entity.addPart("counter", new StringBody("signup"));
                entity.addPart("OTP", new StringBody("1234"));
                entity.addPart("name", new StringBody(userName));
                entity.addPart("email", new StringBody(emailId));
                entity.addPart("mobile", new StringBody(phoneNumber));
                String dString = android_id/* + AFUtils.getFourDigitRandomNumber()*/;
                Log.d("TAG", "DEv id : " + dString);
                entity.addPart("device", new StringBody(dString));
                entity.addPart("password", new StringBody(password));
                //entity.addPart("interest", new StringBody("Music"));

                if(profilePicPath != null && profilePicPath.length()>0){
                    File imageFile = new File(profilePicPath);
                    entity.addPart("images", new FileBody(imageFile));
                }else{
                    entity.addPart("images",  new StringBody(""));
                }

                httppost.setEntity(entity);
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity resEntity = response.getEntity();
                if (resEntity != null) {

                    InputStream instream = resEntity.getContent();
                    responseData = convertStreamToString(instream);
                    instream.close();
                    Log.d("TAG", "Signup Response : " + responseData);
                }

            }catch(Exception e){
                e.printStackTrace();
            }

        /* FOR SIGNUP API ***************************************************************************************/

            return responseData;
        }

        /**
         * Converting stream to string
         */
        public String convertStreamToString(InputStream is) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is),
                    2 * 1024);
            StringBuilder sb = new StringBuilder(2 * 1024);

            String line = null;
            try {
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return sb.toString();
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if(pDialog != null && pDialog.isShowing()){
                pDialog.dismiss();
            }
            if(result != null){
                try {
                    //String resSplit[] = result.split("</html>");
                    //Log.d("TAG", "Profile split : " + resSplit[1]);
                    JSONObject resObject = new JSONObject(result/*resSplit[1]*/);
                    JSONObject signupObject = resObject.getJSONObject("Signup");
                     if (signupObject.getInt("status") == 200) {
                   // if (signupObject.contains("200")) {
                        //String resSplit[] = result.split("\"UserID\":");
                        SharedPreferences preferences = UserPreferences.getUserPreferences(activity);
                        SharedPreferences.Editor preEditor = preferences.edit();
                        preEditor.putString(UserPreferences.USER_NAME, userName);
                        preEditor.putString(UserPreferences.EMAIL_ID, emailId);
                        preEditor.putString(UserPreferences.PHONE_NUMBER, phoneNumber);
                        preEditor.putString(UserPreferences.PROFILE_PICTURE, profilePicPath);
                        preEditor.putString(UserPreferences.USER_ID, /*resSplit[1].replaceAll("[^0-9]", ""));*/signupObject.getString("UserID"));/*userProfile.getUserId());*/
                        preEditor.commit();

                        Intent loginIntent = new Intent(activity, InterestActivity.class);
                        startActivity(loginIntent);
                        finish();
                    }
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }


    }
}
