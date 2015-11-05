package com.artifact.events;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.artifact.R;
import com.artifact.common.Const;
import com.artifact.UXUtils.ImageLoader;
//import com.artifact.navigationdrawer.model.NavDrawerItem;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by girish.kulkarni on 9/24/2015.
 */
public class EventListAdapter extends BaseAdapter implements Filterable{
    private Context context;
    private List<EventData> mList;
    private ImageLoader imgLoader;
    private List<EventData> origEventList;
    private Filter planetFilter;

    public EventListAdapter(Context context, List<EventData> items){
        this.context = context;
        mList = items;
        imgLoader = new ImageLoader(this.context);
        origEventList = items;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public EventData getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        /*ViewHolder holder;
        View row = convertView;
        if (row == null) {
            LayoutInflater mInflater = (LayoutInflater)
                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            row = mInflater.inflate(R.layout.layout_events_row,null);
            holder = new ViewHolder();
            holder.eventHeading = (TextView)row.findViewById(R.id.event_heading);
            holder.eventDate = (TextView)row.findViewById(R.id.event_date);
            holder.eventTime = (TextView)row.findViewById(R.id.event_time);
            row.setTag(holder);
        } else {
            holder = (ViewHolder)row.getTag();
        }*/

        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater)
                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.layout_events_row,null);
        }
        TextView eventHeading = (TextView)convertView.findViewById(R.id.event_heading);
        TextView eventDate = (TextView)convertView.findViewById(R.id.event_date);
        TextView eventTime = (TextView)convertView.findViewById(R.id.event_time);
        ImageView eventIcon = (ImageView) convertView.findViewById(R.id.events_list_imageview);

        EventData data = mList.get(position);
        eventHeading.setText(data.getHeading());
        eventDate.setText(data.getDate());
        eventTime.setText(data.getTime());
        imgLoader.DisplayImage(Const.IMAGE_URL + File.separator + data.getImageURL(), eventIcon);

        /*EventData data = mList.get(position);
        holder.eventHeading.setText(data.getHeading());
        holder.eventDate.setText(data.getDate());
        holder.eventTime.setText(data.getTime());*/
       //imgLoader.DisplayImage(Const.BASE_URL + File.separator + data.getImageURL(), holder.eventImageView);
	    /*ImageView imgIcon = (ImageView) convertView.findViewById(R.id.icon);
        TextView txtTitle = (TextView) convertView.findViewById(R.id.title);

        imgIcon.setImageResource(navDrawerItems.get(position).getIcon());
        txtTitle.setText(navDrawerItems.get(position).getTitle());*/


        return convertView;
    }

    class ViewHolder{
        //ImageView eventImageView;
        TextView eventHeading;
        TextView eventDate;
        TextView eventTime;
    }


    /*
	 * We create our filter
	 */

    @Override
    public Filter getFilter() {
        if (planetFilter == null)
            planetFilter = new EventFilter();

        return planetFilter;
    }

    public void resetData() {
        mList = origEventList;
        notifyDataSetChanged();
    }

    private class EventFilter extends Filter {



        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            // We implement here the filter logic
            if (constraint == null || constraint.length() == 0) {
                // No filter implemented we return all the list
                results.values = origEventList;
                results.count = origEventList.size();
            }
            else {
                // We perform filtering operation
                List<EventData> nList = new ArrayList<>();

                for (EventData p : mList) {
                    if (p.getHeading().toUpperCase().startsWith(constraint.toString().toUpperCase()))
                        nList.add(p);
                }

                results.values = nList;
                results.count = nList.size();

            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint,
                                      Filter.FilterResults results) {

            // Now we have to inform the adapter about the new list filtered
            if (results.count == 0)
                notifyDataSetInvalidated();
            else {
                mList = (List<EventData>) results.values;
                notifyDataSetChanged();
            }

        }

    }
}
