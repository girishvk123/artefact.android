package com.artifact.notification;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.util.Log;

import com.artifact.R;
import com.artifact.common.UserPreferences;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by girish.kulkarni on 10/19/15.
 */
public class GCMRegister {

    public static final String PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_APP_VERSION = "appVersion";
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    // please enter your sender id
    private static String SENDER_ID = "220999625659";//"945168256749"; // agriapp@criyagen.com
    //"670762540323";//"372798323248";//"111111111111";

    public static void checkPlayServices(Context context) {
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(context);
        //if(CheckNetwork.isNetworkAvailable(activity)){
        String regid = getRegistrationId(context);
        if (regid.isEmpty()) {
            //if(CheckNetwork.isNetworkAvailable(activity)){
            new RegisterBackground(regid, gcm, context).execute();
                /*}else{
                    CriyagenDialog.showAlerDialog(activity, activity.getResources().getString(R.string.check_network_connection));
                }*/
        }
        /*}else{
            CriyagenDialog.showAlerDialog(activity, getResources().getString(R.string.check_network_connection));
        }*/
    }

    private static class RegisterBackground extends AsyncTask<String, String, String> {

        GoogleCloudMessaging gcm;
        Context context;
        String regID;

        RegisterBackground(String regid, GoogleCloudMessaging gcm, Context context) {
            this.gcm = gcm;
            this.context = context;
            regID = regid;
        }

        @Override
        protected String doInBackground(String... arg0) {
            String msg = "";
            try {
                if (gcm == null) {
                    gcm = GoogleCloudMessaging.getInstance(context);
                }
                regID = gcm.register(SENDER_ID);
                msg = "Dvice registered, registration ID=" + regID;
                Log.d("111", msg);
                sendRegistrationIdToBackend(regID);

                storeRegistrationId(context, regID);
            } catch (IOException ex) {
                msg = "Error :" + ex.getMessage();
            }
            return msg;
        }

        @Override
        protected void onPostExecute(String msg) {


        }

        private void sendRegistrationIdToBackend(String regid) {
            String url = "http://criyagen.pacewisdom.com/index.php/Tblchat/index1/";
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("regid", regid));
            HttpPost httpPost = new HttpPost(url);
            try {
                httpPost.setEntity(new UrlEncodedFormEntity(params));
            } catch (UnsupportedEncodingException e1) {
                e1.printStackTrace();
            }
        }

        private void storeRegistrationId(Context context, String regId) {
            final SharedPreferences prefs = UserPreferences.getUserPreferences(context);
            int appVersion = getAppVersion(context);
            Log.i("TAG", "Saving regId on app version " + appVersion);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString(PROPERTY_REG_ID, regId);
            editor.putInt(PROPERTY_APP_VERSION, appVersion);
            editor.commit();
        }
    }

    private static String getRegistrationId(Context context) {
        final SharedPreferences prefs = UserPreferences.getUserPreferences(context);
        String registrationId = prefs.getString(PROPERTY_REG_ID, "");
        if (registrationId.isEmpty()) {
            Log.i("TAG", "Registration not found.");
            return "";
        }

        int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
        int currentVersion = getAppVersion(context);
        if (registeredVersion != currentVersion) {
            Log.i("TAG", "App version changed.");
            return "";
        }
        return registrationId;
    }

    /*private SharedPreferences getGCMPreferences(Context context) {

        return getSharedPreferences("TAG",
                Context.MODE_PRIVATE);
    }*/

    private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

}
