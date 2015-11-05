package com.artifact.navigationdrawer;


import java.util.ArrayList;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.artifact.R;
import com.artifact.UXUtils.CircleImageView;
import com.artifact.common.UserPreferences;
import com.artifact.navigationdrawer.adapter.NavDrawerListAdapter;
import com.artifact.navigationdrawer.model.NavDrawerItem;

public class MainActivity extends AppCompatActivity {
	private DrawerLayout mDrawerLayout;
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//getWindow().requestFeature(Window.FEATURE_OPTIONS_PANEL);
		setContentView(R.layout.activity_main1);

		//getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		//getActionBar().setCustomView(R.layout.actionbar_layout);

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
		getActionBar().setHomeButtonEnabled(true);

		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.mipmap.menu_92, 0, 0) {
			
					public void onDrawerClosed(View view) {
						getActionBar().setTitle(mTitle);
						invalidateOptionsMenu(); //Setting, Refresh and Rate App
			}

			public void onDrawerOpened(View drawerView) {
				getActionBar().setTitle(mDrawerTitle);
					invalidateOptionsMenu();
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		if (savedInstanceState == null) {
			  displayView(0);
		}

        // update user details
        updateUserDetails();
	}

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
			fragment = new Fragment1UpdateInterest();
			break;
		case 2:
			//fragment = new FragmentTellAFriend();
            Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Try Artifact App for Android!");
            shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, "I'm using Artifact for Android and I recommend it. Click here: http://www.yourdomain.com");

            Intent chooserIntent = Intent.createChooser(shareIntent, "Share with");
            chooserIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(chooserIntent);
			break;
		case 3:
			//fragment = new FragmentContactUs();
			Intent intent = new Intent(Intent.ACTION_VIEW);
			Uri data = Uri.parse("mailto:?subject=" + "Contact us" + "&body=" + "contact us @ artifact@mail.in");
			intent.setData(data);
			startActivity(intent);
			break;
		case 4:
			fragment = new FragmentRateApp();
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
		getActionBar().setTitle(mTitle);
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

}
