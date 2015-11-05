package com.artifact.notification;
import org.json.JSONObject;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

import com.artifact.R;
import com.artifact.events.EventsDetailActivity;


/**
 * Created by girish.kulkarni on 10/20/15.
 */

public class GcmBroadcastReceiver extends WakefulBroadcastReceiver {

    public static final int NOTIFICATION_ID = 1;
    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;
    public static final String TAG = "GcmIntentService";
    private SharedPreferences mPreferences = null;

    @SuppressWarnings("unused")
    @Override
    public void onReceive(Context context, Intent intent) {

        // TODO Auto-generated method stub
        mPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String chatMsg = null;
        String mediaUrl = null;
        String videoUrl = null;
        String audioUrl = null;
        String pdfUrl = null;
        String msg = null;
        Log.i("TAG", "GCM onReceived: ");
        /*if (intent.hasExtra("Latest News")) {
            msg = intent.getStringExtra("Latest News");
            CacheDataManager.clearNewsCache();
        } else if (intent.hasExtra("message")) {
            msg = intent.getStringExtra("message");
        } else if (intent.hasExtra("call us")) {
            msg = intent.getStringExtra("message");
            CacheDataManager.putCallData(msg);    //For Setting the Call us numbet
        }

        Log.i("TAG", "GCM onReceived: " + msg);
        try {
            if (mPreferences.getBoolean("LOGIN_SUCCESS", false)) {
                JSONObject cObject = new JSONObject(msg);
                if (cObject.has("message")) {
                    JSONObject mObject = cObject.getJSONObject("message");
                    if (mObject.has("text")) {
                        chatMsg = mObject.getString("text");
                    }
                    if (mObject.has("image")) {
                        mediaUrl = mObject.getString("image");
                    }

                    if (mObject.has("videos")) {
                        videoUrl = mObject.getString("videos");
                    }
                    if (mObject.has("audio")) {
                        audioUrl = mObject.getString("audio");
                    }
                    if (ExpertChat.isChatActive) {
                        if (chatMsg != null && chatMsg.length() > 0) {
                            Log.i("TAG", "GCM onReceived: text" + chatMsg);
                            CriyagenDB criyagenDB = new CriyagenDB(context);
                            criyagenDB.open();
                            criyagenDB.insertIntoChatDB("", 0, ExpertChat.RECEIVER_FLAG, chatMsg, Common.getTimeStamp(), "", "", "", "", "", 0, 0);
                            criyagenDB.closeDB();
                            ExpertChat.updateChat(chatMsg);
                        } else if (mediaUrl != null && mediaUrl.length() > 0) {
                            Log.i("TAG", "GCM onReceived: Image" + mediaUrl);
                            ExpertChat.callChatImage(mediaUrl);
                        } else if (videoUrl != null && videoUrl.length() > 0) {
                            ExpertChat.callChatVideo(videoUrl);
                        } else if (audioUrl != null && audioUrl.length() > 0) {
                            ExpertChat.callChatAudio(audioUrl);
                        }

                    } else {
                        ChatType cType = null;
                        if (chatMsg != null && chatMsg.length() > 0) {
                            sendNotification(chatMsg, context);
                            Log.i("TAG", "GCM onReceived: text" + chatMsg);
                            cType = new ChatType();
                            cType.setChatType(Common.CHAT_MESSAGE);
                            cType.setChatData(chatMsg);
                            Common.chatList.add(cType);
                        } else if (mediaUrl != null && mediaUrl.length() > 0) {
                            Log.i("TAG", "GCM onReceived: Image" + mediaUrl);
                            sendImageNotification(mediaUrl, context);
                            cType = new ChatType();
                            cType.setChatType(Common.CHAT_IMAGE);
                            cType.setChatData(mediaUrl);
                            Common.chatList.add(cType);
                        } else if (videoUrl != null && videoUrl.length() > 0) {
                            sendVideoNotification(videoUrl, context);
                            cType = new ChatType();
                            cType.setChatType(Common.CHAT_VIDEO);
                            cType.setChatData(videoUrl);
                            Common.chatList.add(cType);
                        } else if (audioUrl != null && audioUrl.length() > 0) {
                            sendAudioNotification(audioUrl, context);
                            cType = new ChatType();
                            cType.setChatType(Common.CHAT_AUDIO);
                            cType.setChatData(audioUrl);
                            Common.chatList.add(cType);
                        }
                    }
                } else if (cObject.has("Latest News")) {
                    JSONObject lnObject = cObject.getJSONObject("Latest News");
                    if (lnObject.has("heading")) {
                        sendNewsNotification(lnObject.getString("heading"), lnObject.getString("description"), lnObject.getString("picture"), context);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }

    private void sendNotification(String msg, Context ctx) {
        mNotificationManager = (NotificationManager)
                ctx.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent myintent = new Intent(ctx, EventsDetailActivity.class);
        myintent.putExtra("message", msg);
        PendingIntent contentIntent = PendingIntent.getActivity(ctx, 0, myintent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(ctx)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(ctx.getResources().getString(R.string.app_name))
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(msg))
                        .setContentText(msg);

        mBuilder.setContentIntent(contentIntent);
        Uri nSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        mBuilder.setSound(nSound);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }

    /*private void sendImageNotification(String imgUrl, Context ctx) {
        mNotificationManager = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent myintent = new Intent(ctx, ExpertChat.class);
        myintent.putExtra("IMAGE_URL", imgUrl);
        PendingIntent contentIntent = PendingIntent.getActivity(ctx, 0,
                myintent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(ctx)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle("AgriApp")
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText("IMAGE"))
                        .setContentText("IMAGE");

        mBuilder.setContentIntent(contentIntent);
        Uri nSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        mBuilder.setSound(nSound);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }

    private void sendVideoNotification(String videoUrl, Context ctx) {
        mNotificationManager = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent myintent = new Intent(ctx, ExpertChat.class);
        myintent.putExtra("VIDEO_URL", videoUrl);
        PendingIntent contentIntent = PendingIntent.getActivity(ctx, 0,
                myintent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(ctx)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle("AgriApp")
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText("VIDEO"))
                        .setContentText("VIDEO");

        mBuilder.setContentIntent(contentIntent);
        Uri nSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        mBuilder.setSound(nSound);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }


    private void sendAudioNotification(String audioUrl, Context ctx) {
        mNotificationManager = (NotificationManager)
                ctx.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent myintent = new Intent(ctx, ExpertChat.class);
        myintent.putExtra("AUDIO_URL", audioUrl);
        PendingIntent contentIntent = PendingIntent.getActivity(ctx, 0,
                myintent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(ctx)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle("AgriApp")
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText("AUDIO"))
                        .setContentText("AUDIO");

        mBuilder.setContentIntent(contentIntent);
        Uri nSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        mBuilder.setSound(nSound);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }

    private void sendNewsNotification(String heading, String description, String imageName, Context ctx) {
        mNotificationManager = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);

        NewsDetail.isNewsDetailActive = true;
        Intent myintent = new Intent(ctx, NewsDetail.class);
        myintent.putExtra("NEWS_HEADING", heading);
        myintent.putExtra("NEWS_DESCRIPTION", description);
        myintent.putExtra("NEWS_IMAGE_NAME", imageName);
        PendingIntent contentIntent = PendingIntent.getActivity(ctx, 0, myintent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(ctx)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle("AgriApp")
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText("Latest News : " + heading))
                        .setContentText("Latest News : " + heading);

        mBuilder.setContentIntent(contentIntent);
        Uri nSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        mBuilder.setSound(nSound);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }*/
}
