package com.artifact.navigationdrawer.adapter;



import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.artifact.R;
import com.artifact.navigationdrawer.model.NavDrawerItem;

public class NavDrawerListAdapter extends BaseAdapter {
	
	private Context context;
	private ArrayList<NavDrawerItem> navDrawerItems;
	private ArrayList<NavDrawerItem> navClickedDrawerItems;
	public static int clickPosition = -1;
	TypedArray navMenuClickedIcons;

	public NavDrawerListAdapter(Context context, ArrayList<NavDrawerItem> navDrawerItems){
		this.context = context;
		this.navDrawerItems = navDrawerItems;
		navClickedDrawerItems = new ArrayList<>(0);

		// getting Navigation drawer icons from res
		navMenuClickedIcons = context.getResources()
				.obtainTypedArray(R.array.nav_drawer_clicked_icons_artifacts);

		/*// list item in slider at 1 Home Nasdaq details
		navClickedDrawerItems.add(new NavDrawerItem(navMenuTitles[0], navMenuClickedIcons.getResourceId(0, -1)));
		// list item in slider at 2 Facebook details
		navClickedDrawerItems.add(new NavDrawerItem(navMenuTitles[1], navMenuIcons.getResourceId(1, -1)));
		// list item in slider at 3 Google details
		navClickedDrawerItems.add(new NavDrawerItem(navMenuTitles[2], navMenuIcons.getResourceId(2, -1)));
		// list item in slider at 4 Apple details
		navClickedDrawerItems.add(new NavDrawerItem(navMenuTitles[3], navMenuIcons.getResourceId(3, -1)));
		// list item in slider at 5 Microsoft details
		navClickedDrawerItems.add(new NavDrawerItem(navMenuTitles[4], navMenuIcons.getResourceId(4, -1)));
		// list item in slider at 5 Microsoft details
		navClickedDrawerItems.add(new NavDrawerItem(navMenuTitles[5], navMenuIcons.getResourceId(5, -1)));
		*/
	}

	@Override
	public int getCount() {
		return navDrawerItems.size();
	}

	@Override
	public Object getItem(int position) {		
		return navDrawerItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater)
                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.drawer_list_item,null);
        }
         
        ImageView imgIcon = (ImageView) convertView.findViewById(R.id.icon);
        TextView txtTitle = (TextView) convertView.findViewById(R.id.title);
        
        imgIcon.setImageResource(navDrawerItems.get(position).getIcon());        
        txtTitle.setText(navDrawerItems.get(position).getTitle());

		if (clickPosition >= 0 && position == clickPosition)
			imgIcon.setImageResource(navMenuClickedIcons.getResourceId(clickPosition, -1));

      	/*switch (clickPosition) {
			case 0:
				break;
		}*/

        return convertView;
	}

	/*private void updateViewIcons(int adapterPosition) {

	}*/

}
