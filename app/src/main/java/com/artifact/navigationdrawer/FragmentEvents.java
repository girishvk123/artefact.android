package com.artifact.navigationdrawer;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;

import com.artifact.R;
import com.artifact.common.Const;
import com.artifact.common.UserPreferences;
import com.artifact.events.EventData;
import com.artifact.events.EventListAdapter;
import com.artifact.events.EventsDetailActivity;
import com.artifact.signup.OTPActivity;
import com.artifact.user.Profile;

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
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class FragmentEvents extends Fragment {

    public FragmentEvents() {
    }

    private List<EventData> eventDataList;

    Activity conActivity;
    ListView eventListView;
    ImageView menuImageView;
    EditText searchEditText;
    EventListAdapter eventListAdapter;
    Spinner eventSpinner;
    ImageView menuFilterImage;
    String array[];
    ArrayAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        conActivity = this.getActivity();
        View rootView = inflater.inflate(R.layout.events_fragment, container, false);
        eventListView = (ListView) rootView.findViewById(R.id.events_listView);
        searchEditText = (EditText) rootView.findViewById(R.id.event_search);
        eventSpinner = (Spinner)rootView.findViewById(R.id.menu_spinner);
        menuFilterImage = (ImageView)rootView.findViewById(R.id.menu_filter_button);

        // Declaring an Adapter and initializing it to the data pump
        array = getResources().getStringArray(R.array.event_filter_arrays);
        adapter = new ArrayAdapter(
                conActivity, android.R.layout.simple_list_item_1, array);

        // Setting Adapter to the Spinner
        eventSpinner.setAdapter(adapter);

        menuImageView = (ImageView) rootView.findViewById(R.id.menu_imageView);
        eventListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (eventDataList != null && eventDataList.size() > 0) {
                    EventData data = (EventData)parent.getItemAtPosition(position);//eventDataList.get(position);
                    Intent intent = new Intent(conActivity, EventsDetailActivity.class);
                    intent.putExtra("IMAGE", data.getImageURL());
                    intent.putExtra("DESC", data.getDescription());
                    intent.putExtra("HEADING", data.getHeading());
                    intent.putExtra("DATE", data.getDate());
                    intent.putExtra("TIME", data.getTime());
                    intent.putExtra("PLACE", data.getPlace());
                    intent.putExtra("LATLONG", data.getLatlong());
                    intent.putExtra("ID", data.getID());
                    startActivity(intent);
                }
            }
        });

        eventListView.setTextFilterEnabled(true);
        menuImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity1.hideMenu();
            }
        });

        menuFilterImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //eventSpinner.setVisibility(View.VISIBLE);
                //eventSpinner.performClick();
                showFilter();
            }
        });

        searchEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                System.out.println("Text [" + s + "] - Start [" + start + "] - Before [" + before + "] - Count [" + count + "]");
                if (count < before) {
                    // We're deleting char so we need to reset the adapter data
                    eventListAdapter.resetData();
                    //eventListAdapter.notifyDataSetChanged();
                }
                if (s.toString() != null && s.toString().length() > 0 && eventDataList != null && eventDataList.size() > 0) {

                    eventListAdapter.getFilter().filter(s.toString());

                }

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        new EventsTask().execute();
        return rootView;
    }


    private void showFilter() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(conActivity);
        //builder.setTitle("whatever title");
        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                switch (item) {
                    case 0:
                        groupByDate();
                        break;
                    case 1:
                        groupByPlace();
                        break;
                    case 2:
                        //setEventListAdapter(eventDataList);
                        groupByHeading();
                        break;
                    default:
                        break;
                }

            }
        });
        AlertDialog dialog = builder.create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams wmlp = dialog.getWindow().getAttributes();

        wmlp.gravity = Gravity.TOP | Gravity.RIGHT;
        //wmlp.x = 100;   //x position
        //wmlp.y = 100;   //y position
        dialog.getWindow().setAttributes(wmlp);
        dialog.show();
    }


    private class EventsTask extends AsyncTask<Void, Void, String> {
        private ProgressDialog pDialog = null;
        private String responseData = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(conActivity);
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
                SharedPreferences preferences = UserPreferences.getUserPreferences(conActivity);

                MultipartEntity entity = new MultipartEntity();
                String android_id = Settings.Secure.getString(conActivity.getContentResolver(), Settings.Secure.ANDROID_ID);

                entity.addPart("secretkey", new StringBody("nzyeU3RMt0OudAKtmX/O6v7gK6IXQ/LeqOls6nuysB6jbwj0yaWKRI6AFc5FYbdBsWRCiHWVzpUobbn94uFl/F6TgH9xGv3GceqfivgXYiGJIXikRRccDB0BfXKqHkBuJxnvNmnBHy3nER191I4dR91oxuQu+9DuGxPVK3H56U8="));
                entity.addPart("counter", new StringBody("event"));
                entity.addPart("loc", new StringBody("bangalore"));
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

            } catch (Exception e) {
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
            if (pDialog != null && pDialog.isShowing()) {
                pDialog.dismiss();
            }
            if (result != null) {
                try {
                    JSONObject resObject = new JSONObject(result);
                    //if (resObject.getInt("status") == 200) {
                    if (result.contains("event")) {
                        // if (resObject.has("events")) {
                        JSONArray eventArray = resObject.getJSONArray("event");
                        if (eventArray != null && eventArray.length() > 0) {
                            eventDataList = new ArrayList<>(0);
                            for (int i = 0; i < eventArray.length(); i++) {
                                JSONObject eventObject = eventArray.getJSONObject(i);
                                EventData data = new EventData();
                                data.setID(eventObject.getString("ID"));
                                data.setHeading(eventObject.getString("heading"));
                                data.setDescription(eventObject.getString("desc"));
                                data.setImageURL(eventObject.getString("image"));
                                data.setDate(eventObject.getString("date"));
                                data.setTime(eventObject.getString("time"));
                                data.setPlace(eventObject.getString("place"));
                                data.setLatlong(eventObject.getString("latlong"));
                                eventDataList.add(data);
                            }
                        }
                        // }
                    }

                    setEventListAdapter(eventDataList);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


        }


    }

    private void setEventListAdapter(List<EventData> list) {
        if (list != null && list.size() > 0) {
            eventListAdapter = new EventListAdapter(conActivity, list);
            eventListView.setAdapter(eventListAdapter);
        }

    }

    private void groupByDate() {
        List<EventData> list = new ArrayList<>(0);
        list.addAll(eventDataList);
        Collections.sort(list, new Comparator<EventData>() {
            @Override
            public int compare(EventData lhs, EventData rhs) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                Date d1 = null;
                Date d2 = null;
                try {
                    d1 = sdf.parse(lhs.getDate());
                    d2 = sdf.parse(rhs.getDate());
                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                return (d1.getTime() > d2.getTime() ? -1 : 1);     //descending
                //  return (d1.getTime() > d2.getTime() ? 1 : -1);     //ascending
            }

        });
        //eventListAdapter.notifyDataSetChanged();
        setEventListAdapter(list);
    }

    private void groupByPlace() {
        List<EventData> list = new ArrayList<>(0);
        list.addAll(eventDataList);
        Collections.sort(list, new Comparator<EventData>() {
            @Override
            public int compare(EventData lhs, EventData rhs) {
                if (lhs.getPlace().equalsIgnoreCase(rhs.getPlace())) {
                    return lhs.getPlace().compareTo(rhs.getPlace());
                }
                return lhs.getPlace().compareTo(rhs.getPlace());
            }

        });
        //eventListAdapter.notifyDataSetChanged();
        setEventListAdapter(list);
    }

    private void groupByHeading() {
        List<EventData> list = new ArrayList<>(0);
        list.addAll(eventDataList);
        Collections.sort(list, new Comparator<EventData>() {
            @Override
            public int compare(EventData lhs, EventData rhs) {
                if (lhs.getHeading().equalsIgnoreCase(rhs.getHeading())) {
                    return lhs.getHeading().compareTo(rhs.getHeading());
                }
                return lhs.getHeading().compareTo(rhs.getHeading());
            }

        });
        //eventListAdapter.notifyDataSetChanged();
        setEventListAdapter(list);
    }
}
