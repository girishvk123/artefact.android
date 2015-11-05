package com.artifact.common;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

import com.artifact.R;

/**
 * Created by girish.kulkarni on 10/1/2015.
 */
public class DialogUtils {

    public static void showPutBackgroundAlerDialog(Activity activity,String msg){
        AlertDialog.Builder adb = new AlertDialog.Builder(activity);
        adb.setTitle(activity.getResources().getString(R.string.alert));
        adb.setMessage(msg);
        adb.setPositiveButton(activity.getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        adb.setNegativeButton(activity.getResources().getString(R.string.no), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // do nothing

            }
        });

        adb.create().show();
    }

    public static void showAlerDialog(Activity activity, String title, String msg){
        AlertDialog.Builder adb = new AlertDialog.Builder(activity);
        adb.setTitle(title);
        adb.setMessage(msg);
        adb.setPositiveButton(activity.getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        adb.create().show();
    }
}
