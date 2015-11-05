package com.artifact.events;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.artifact.R;
import com.artifact.UXUtils.ImageLoader;
import com.artifact.common.Const;
import com.artifact.common.DialogUtils;
import com.artifact.common.UserPreferences;
import com.artifact.navigationdrawer.MainActivity;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by girish.kulkarni on 9/25/2015.
 */
public class EventsDetailActivity extends Activity implements View.OnClickListener{
    private ImageLoader imgLoader;
    Activity activity;
    ImageView eventImageView;
    Button rsvp;
    TextView eventDateTextView;
    TextView evenTimeTextView;
    TextView eventDescTextView;
    TextView eventPlaceTextView;
    TextView eventHeadingTextView;
    LinearLayout mapLayout;

    String eventId;
    String eventDesc;
    String eventPlace;
    String eventDate;
    String eventTime;
    String eventLatLong;
    String eventHeading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_details);
        //getActionBar().hide();
        activity = this;
        imgLoader = new ImageLoader(activity);
        eventImageView = (ImageView)findViewById(R.id.event_deatail_pic);
        rsvp = (Button)findViewById(R.id.rsvp);
        rsvp.setOnClickListener(this);
        Intent intent = getIntent();
        eventId = intent.getStringExtra("ID");
        eventDate = intent.getStringExtra("DATE");
        eventTime = intent.getStringExtra("TIME");
        eventPlace = intent.getStringExtra("PLACE");
        eventDesc = intent.getStringExtra("DESC");
        eventLatLong = intent.getStringExtra("LATLONG");
        eventHeading = intent.getStringExtra("HEADING");
        imgLoader.DisplayImage(Const.IMAGE_URL + File.separator + intent.getStringExtra("IMAGE"), eventImageView);

        eventDateTextView = (TextView)findViewById(R.id.event_date);
        evenTimeTextView = (TextView)findViewById(R.id.event_time);
        eventPlaceTextView = (TextView)findViewById(R.id.event_location);
        eventDescTextView = (TextView)findViewById(R.id.event_desc);
        eventHeadingTextView = (TextView)findViewById(R.id.event_heading);
        mapLayout = (LinearLayout)findViewById(R.id.find_map_layout);

        mapLayout.setOnClickListener(this);
        eventDescTextView.setText(eventDesc);
        eventPlaceTextView.setText(getResources().getString(R.string.locaton) + eventPlace);
        eventDateTextView.setText(getResources().getString(R.string.date) + eventDate);
        evenTimeTextView.setText(getResources().getString(R.string.time) + eventTime);
        eventHeadingTextView.setText(eventHeading);

        Button inviteButton = (Button)findViewById(R.id.invite);
        inviteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, getResources().getString(R.string.tell_friend_subject_text));
                shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, getResources().getString(R.string.tell_friend_body_text));

                Intent chooserIntent = Intent.createChooser(shareIntent, getResources().getString(R.string.share_text));
                chooserIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(chooserIntent);
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rsvp:
                new RSVPTask().execute();
                break;
            case R.id.find_map_layout:
                //String latlong[] = eventLatLong.replace(" ","").split(",");
                //double latitude = Double.valueOf(latlong[0]);
                //double longitude = Double.valueOf(latlong[1]);
                Intent mapIntent = new Intent(this, EventMapActivity.class);
                //mapIntent.putExtra("LATITUDE",latitude);
                ///mapIntent.putExtra("LONGITUDE", longitude);
                startActivity(mapIntent);
                break;
        }
    }


    private class RSVPTask extends AsyncTask<Void, Void, String> {
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

        /* FOR EVENT API ***************************************************************************************/

            try {
                SharedPreferences preferences = UserPreferences.getUserPreferences(activity);

                MultipartEntity entity = new MultipartEntity();
                String android_id = Settings.Secure.getString(activity.getContentResolver(), Settings.Secure.ANDROID_ID);

                entity.addPart("secretkey", new StringBody("nzyeU3RMt0OudAKtmX/O6v7gK6IXQ/LeqOls6nuysB6jbwj0yaWKRI6AFc5FYbdBsWRCiHWVzpUobbn94uFl/F6TgH9xGv3GceqfivgXYiGJIXikRRccDB0BfXKqHkBuJxnvNmnBHy3nER191I4dR91oxuQu+9DuGxPVK3H56U8="));
                entity.addPart("counter", new StringBody("rsvp"));
                entity.addPart("event", new StringBody(eventId));
                entity.addPart("user", new StringBody(preferences.getString(UserPreferences.USER_ID, null)));

                httppost.setEntity(entity);
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity resEntity = response.getEntity();
                if (resEntity != null) {
                    InputStream instream = resEntity.getContent();
                    responseData = convertStreamToString(instream);
                    instream.close();
                    Log.d("TAG", "RSVP Response : " + responseData);
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
                    if (resObject.getInt("RSVP") == 200 || resObject.getInt("RSVP") == 208 ) {
                    //if (result.contains("RSVP")) {
                        /*String rsvpSplit[] = result.split("\"Message\":");
                        Toast.makeText(getApplicationContext(),
                                rsvpSplit[1], Toast.LENGTH_LONG).show();*/

                        DialogUtils.showAlerDialog(activity, activity.getResources().getString(R.string.status), resObject.getString("Message"));
                    }

                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }


    }
}