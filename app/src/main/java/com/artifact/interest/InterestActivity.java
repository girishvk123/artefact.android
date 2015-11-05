package com.artifact.interest;

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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.artifact.R;
import com.artifact.common.Const;
import com.artifact.common.UserPreferences;
import com.artifact.navigationdrawer.MainActivity;
import com.artifact.navigationdrawer.MainActivity1;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by girish.kulkarni on 9/24/2015.
 */
public class InterestActivity extends ActionBarActivity {
    InterestAdapter dataAdapter = null;
    ArrayList<InterestData> InterestList = new ArrayList<InterestData>();
    Activity activity;
    private Toolbar mToolbar;
    android.support.v7.app.ActionBar mActionBar;
    public final static String FLAG_UPDATE_INTEREST = "flag_update_interest";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.interests);
        mToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        setSupportActionBar(mToolbar);

        mActionBar = getSupportActionBar();
        mActionBar .hide();

        activity = this;
        //Intent intent = getIntent();
        //boolean isUpdate = intent.getBooleanExtra(FLAG_UPDATE_INTEREST, false);
       // String updateString = intent.getStringExtra(FLAG_UPDATE_INTEREST);
        //Generate list View from ArrayList
        displayListView();

        checkButtonClick();
    }

    private void displayListView() {

        //Array of interest
        String array[] = getResources().getStringArray(R.array.interest_data_arrays);
        for (String str : array) {
            InterestData interestData = new InterestData(str, false);
            InterestList.add(interestData);
            //InterestList.add(interestData);
        }


        //create an ArrayAdaptar from the String Array
        dataAdapter = new InterestAdapter(this,
                R.layout.interest_info, InterestList);
        ListView listView = (ListView) findViewById(R.id.interest_listView1);
        // Assign adapter to ListView
        listView.setAdapter(dataAdapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // When clicked, show a toast with the TextView text
                /*InterestData data = (InterestData) parent.getItemAtPosition(position);
                Toast.makeText(getApplicationContext(),
                        "Clicked on Row: " + data.getName(),
                        Toast.LENGTH_LONG).show();*/
                dataAdapter.toggleChecked(position);
            }
        });

        /*InterestData interestData = new InterestData("Music",false);
        InterestList.add(interestData);
        interestData = new InterestData("Dance",false);
        InterestList.add(interestData);
        interestData = new InterestData("Arts",false);
        InterestList.add(interestData);
        interestData = new InterestData("Visual Arts",false);
        InterestList.add(interestData);*/





        // update interest
        SharedPreferences preferences = UserPreferences.getUserPreferences(activity);
        //Set intersetSet = preferences.getStringSet(UserPreferences.USER_INTEREST, null);
        String interestString = preferences.getString(UserPreferences.USER_INTEREST, null);
        if (interestString == null) {
            return;
        }
        String interestSplit[] = interestString.split(",");

        if (interestSplit != null) {
            HashMap<Integer, Boolean> myChecked = new HashMap<Integer, Boolean>();
            int j = 0;
            for (int i = 0 ; i< InterestList.size();i++) {
                if (j< interestSplit.length && interestSplit[i].equalsIgnoreCase(InterestList.get(i).getName())) {
                    myChecked.put(i,true);
                } else {
                    myChecked.put(i,false);
                }
                j++;

            }
            /*Iterator iterator = intersetSet.iterator();
            while (iterator.hasNext()) {
                myChecked.put(i, Boolean.valueOf(((String) iterator.next()).replace(i+"","")));
                i++;
            }*/
            InterestAdapter.myChecked = myChecked;
            dataAdapter.notifyDataSetChanged();
        }

    }

    private void checkButtonClick() {


        Button myButton = (Button) findViewById(R.id.submit_button);
        myButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                StringBuffer responseText = new StringBuffer();
                int index = 0;
                //getCheckedItems
                List<InterestData> resultList = dataAdapter.getCheckedItems();
                for(int i = 0; i < resultList.size(); i++){
                    InterestData data = resultList.get(i);
                    //result += String.valueOf(resultList.get(i)) + "\n";
                    if (index == 0) {
                        responseText.append(data.getName());
                    }else {
                        responseText.append("," + data.getName());
                    }
                    index++;
                }

                dataAdapter.getCheckedItemPositions().toString();
                /*Toast.makeText(
                        getApplicationContext(),
                        result,
                        Toast.LENGTH_LONG).show();*/

                //responseText.append("The following were selected...\n");
                /*int index = 0;
                for(int i=0;i< InterestList.size();i++){
                    InterestData data = InterestList.get(i);
                    if(data.isSelected()){
                        if (index == 0) {
                            responseText.append(data.getName());
                        }else {
                            responseText.append("," + data.getName());
                        }
                        index++;
                    }
                }*/

                if (!TextUtils.isEmpty(responseText.toString())) {
                    /*Toast.makeText(getApplicationContext(),
                            responseText, Toast.LENGTH_LONG).show();*/
                    new UpdateInterestTask(responseText.toString()).execute();
                }
            }
        });

    }


    private class UpdateInterestTask extends AsyncTask<Void, Void, String> {
        private ProgressDialog pDialog = null;
        private String descString;
        private String responseData = null;

        public UpdateInterestTask(String str){
            descString = str;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(activity);
            pDialog.setMessage(/*activity.getResources().getString(R.string.loading)*/"Loadng");
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
                entity.addPart("counter", new StringBody("interest"));
                entity.addPart("interest", new StringBody(descString));
                entity.addPart("cd", new StringBody("1"));
                entity.addPart("user", new StringBody(preferences.getString(UserPreferences.USER_ID, null)));

                httppost.setEntity(entity);
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity resEntity = response.getEntity();
                if (resEntity != null) {
                    InputStream instream = resEntity.getContent();
                    responseData = convertStreamToString(instream);
                    instream.close();
                    Log.d("TAG", "Profile Response : " + responseData);
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
                    //if (resObject.getInt("status") == 200) {
                    if (result.contains("Interest")) {
                       /* Set interestSet = new TreeSet<>();
                        for (int i = 0; i<InterestAdapter.myChecked.size();i++) {
                            interestSet.add(i+""+InterestAdapter.myChecked.get(i));
                        }*/
                        SharedPreferences preferences = UserPreferences.getUserPreferences(activity);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString(UserPreferences.USER_INTEREST, descString);
                        editor.commit();

                        Intent loginIntent = new Intent(InterestActivity.this, MainActivity1.class);
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
