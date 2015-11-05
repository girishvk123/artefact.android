package com.artifact.navigationdrawer;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.artifact.R;
import com.artifact.UXUtils.CircleImageView;
import com.artifact.UXUtils.ImageLoader;
import com.artifact.common.UserPreferences;
import com.artifact.interest.InterestActivity;
import com.artifact.navigationdrawer.adapter.NavDrawerListAdapter;
import com.artifact.navigationdrawer.model.NavDrawerItem;

import java.util.ArrayList;


public class MainActivity1 extends ActionBarActivity {

    private Toolbar mToolbar;

    public static DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private LinearLayout menuLayout;
    private ActionBarDrawerToggle mDrawerToggle;

    // NavigationDrawer title "Nasdaq" in this example
    private CharSequence mDrawerTitle;

    //  App title "Navigation Drawer" in this example
    private CharSequence mTitle;

    // slider menu items details
    private String[] navMenuTitles;
    private TypedArray navMenuIcons;

    private ArrayList<NavDrawerItem> navDrawerItems;
    private NavDrawerListAdapter adapter;

    private CircleImageView userPicImageView;
    private TextView userNameTextView;

    // action bar varaibles
    private boolean mDrawerState;
    TextView mTitleTextView;
    ActionBar mActionBar;
    private ImageLoader imgLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        setSupportActionBar(mToolbar);
        //getSupportActionBar().setDisplayShowHomeEnabled(true);

        imgLoader = new ImageLoader(this);
        ///////
        mActionBar = getSupportActionBar();
        mActionBar.setDisplayShowHomeEnabled(true);
        mActionBar.setDisplayShowTitleEnabled(false);
        LayoutInflater mInflater = LayoutInflater.from(this);

        View mCustomView = mInflater.inflate(R.layout.custom_actionbar, null);
        mTitleTextView = (TextView) mCustomView.findViewById(R.id.menu_title_text);
        mTitleTextView.setText(getResources().getString(R.string.events));

        ImageView menuImageView = (ImageView) mCustomView
                .findViewById(R.id.menu_imageView);
        menuImageView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // make drawer open and close
                if (!mDrawerState) {
                    mActionBar.hide();
                    mDrawerLayout.openDrawer(Gravity.LEFT);
                    mDrawerState = true;
                } else {
                    mActionBar.show();
                    mDrawerState = false;
                    mDrawerLayout.closeDrawer(Gravity.LEFT);
                }
            }
        });

        mActionBar.setCustomView(mCustomView);
        mActionBar.setDisplayShowCustomEnabled(true);
        mActionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.blue)));
        //////




        mActionBar.hide();
        mTitle = mDrawerTitle = getTitle();

        menuLayout = (LinearLayout)findViewById(R.id.menu_layout);

        userNameTextView = (TextView)findViewById(R.id.user_name);
        userPicImageView = (CircleImageView)findViewById(R.id.user_pic);

        // getting items of slider from array
        navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items_artifacts);

        // getting Navigation drawer icons from res
        navMenuIcons = getResources()
                .obtainTypedArray(R.array.nav_drawer_icons_artifacts);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.list_slidermenu);

        navDrawerItems = new ArrayList<NavDrawerItem>();


        // list item in slider at 1 Home Nasdaq details
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[0], navMenuIcons.getResourceId(0, -1)));
        // list item in slider at 2 Facebook details
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[1], navMenuIcons.getResourceId(1, -1)));
        // list item in slider at 3 Google details
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[2], navMenuIcons.getResourceId(2, -1)));
        // list item in slider at 4 Apple details
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[3], navMenuIcons.getResourceId(3, -1)));
        // list item in slider at 5 Microsoft details
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[4], navMenuIcons.getResourceId(4, -1)));
        // list item in slider at 5 Microsoft details
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[5], navMenuIcons.getResourceId(5, -1)));


        // Recycle array
        navMenuIcons.recycle();

        mDrawerList.setOnItemClickListener(new SlideMenuClickListener());

        // setting list adapter for Navigation Drawer
        adapter = new NavDrawerListAdapter(getApplicationContext(),
                navDrawerItems);

        NavDrawerListAdapter.clickPosition = 0;
        mDrawerList.setAdapter(adapter);

        // Enable action bar icon_luncher as toggle Home Button
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.mipmap.menu_92, 0, 0) {

            public void onDrawerClosed(View view) {
                //getSupportActionBar().setTitle(mTitle);
               // mTitleTextView.setText(mTitle.toString().toUpperCase());
               // invalidateOptionsMenu(); //Setting, Refresh and Rate App
                //getSupportActionBar().show();
               // mActionBar.show();
            }

            public void onDrawerOpened(View drawerView) {
                //getSupportActionBar().setTitle(mDrawerTitle);
               // mTitleTextView.setText(mTitle.toString().toUpperCase());
                //invalidateOptionsMenu();
                //getSupportActionBar().hide();
                //mActionBar.hide();
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        if (savedInstanceState == null) {
            displayView(0);
        }

        // update user details
        updateUserDetails();

        //mNavigationDrawerFragment = (NavigationDrawerFragment) getFragmentManager().findFragmentById(R.id.fragment_drawer);
        //mNavigationDrawerFragment.setup(R.id.fragment_drawer, (DrawerLayout) findViewById(R.id.drawer), mToolbar);
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }*/

    /*@Override
    public void onNavigationDrawerItemSelected(int position) {
        Toast.makeText(this, "Menu item selected -> " + position, Toast.LENGTH_SHORT).show();
    }*/

    /*@Override
    public void onBackPressed() {
        if (mNavigationDrawerFragment.isDrawerOpen())
            mNavigationDrawerFragment.closeDrawer();
        else
            super.onBackPressed();
    }*/


    private void updateUserDetails() {
        SharedPreferences preferences = UserPreferences.getUserPreferences(this);
        userNameTextView.setText(preferences.getString(UserPreferences.USER_NAME, null));
        String selectedImagePath = preferences.getString(UserPreferences.PROFILE_PICTURE, null);
        if (selectedImagePath != null) {
            Bitmap bm;
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(selectedImagePath, options);
            final int REQUIRED_SIZE = 60;
            int scale = 1;
            while (options.outWidth / scale / 2 >= REQUIRED_SIZE
                    && options.outHeight / scale / 2 >= REQUIRED_SIZE)
                scale *= 2;
            options.inSampleSize = scale;
            options.inJustDecodeBounds = false;
            bm = BitmapFactory.decodeFile(selectedImagePath, options);

            //roundedImage = new RoundImage(bm);
            //picImageView.setImageDrawable(roundedImage);
            userPicImageView.setImageBitmap(bm);
        } else {
            String picUrl = preferences.getString(UserPreferences.PROFILE_PICTURE_URL, null);
            if (picUrl != null) {
                imgLoader.DisplayImage(picUrl, userPicImageView);
            }
        }
    }

    /**
     * Slider menu item click listener
     * */
    private class SlideMenuClickListener implements
            ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            // display view for selected item
            displayView(position);
            NavDrawerListAdapter.clickPosition = position;
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //  title/icon
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle action bar actions click
        switch (item.getItemId()) {
		/*case R.id.action_settings:
			return true;*/
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //called when invalidateOptionsMenu() invoke

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // if Navigation drawer is opened, hide the action items
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(menuLayout);
        //menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    private void displayView(int position) {
        // update the main content with called Fragment
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new FragmentEvents();
                break;
            case 1:
                //fragment = new Fragment1UpdateInterest();
                Intent intent = new Intent(this, InterestActivity.class);
                startActivity(intent);
                finish();
                break;
            case 2:
                //fragment = new FragmentTellAFriend();
                Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, getResources().getString(R.string.tell_friend_subject_text));
                shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, getResources().getString(R.string.tell_friend_body_text));

                Intent chooserIntent = Intent.createChooser(shareIntent, getResources().getString(R.string.share_text));
                chooserIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(chooserIntent);
                break;
            case 3:
                //fragment = new FragmentContactUs();
                Intent cIntent = new Intent(Intent.ACTION_VIEW);
                Uri data = Uri.parse("mailto:?subject=" + getResources().getString(R.string.contact_us) + "&body=" + getResources().getString(R.string.contact_us_mail_id));
                cIntent.setData(data);
                startActivity(cIntent);
                break;
            case 4:
                //fragment = new FragmentRateApp();
                Uri uri = Uri.parse("market://details?id=" + this.getPackageName());
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                // To count with Play market backstack, After pressing back button,
                // to taken back to our application, we need to add following flags to intent.
                goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                        Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET |
                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                try {
                    startActivity(goToMarket);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://play.google.com/store/apps/details?id=" + this.getPackageName())));
                }
                break;
            case 5:
                fragment = new FragmentEventHistory();
                break;

            default:
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit();
            mDrawerList.setItemChecked(position, true);
            mDrawerList.setSelection(position);
            setTitle(navMenuTitles[position]);
            mDrawerLayout.closeDrawer(menuLayout);
        } else {

            Log.e("this is mainActivity", "Error in else case");
        }
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    public static void hideMenu() {
        /*if (!mDrawerState) {
            //mActionBar.hide();
            mDrawerLayout.openDrawer(Gravity.LEFT);
            mDrawerState = true;
        } else {
        */    //mActionBar.show();
            //mDrawerState = false;
            mDrawerLayout.openDrawer(Gravity.LEFT);
        //}
    }


}
