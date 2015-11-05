package com.artifact.interest;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.TextView;
import android.widget.Toast;

import com.artifact.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by girish.kulkarni on 10/1/2015.
 */
public class InterestAdapter extends ArrayAdapter<InterestData> {
    public static HashMap<Integer, Boolean> myChecked = new HashMap<Integer, Boolean>();
    private ArrayList<InterestData> mList;
    Context mContext;
    public InterestAdapter(Context context, int textViewResourceId,
                           ArrayList<InterestData> cList) {
        super(context, textViewResourceId, cList);
        this.mList = new ArrayList<InterestData>();
        this.mList.addAll(cList);
        mContext = context;
        for(int i = 0; i < cList.size(); i++){
            myChecked.put(i, false);
        }
    }

    public void toggleChecked(int position){
        if(myChecked.get(position)){
            myChecked.put(position, false);
        }else{
            myChecked.put(position, true);
        }

        notifyDataSetChanged();
    }

    public List<Integer> getCheckedItemPositions(){
        List<Integer> checkedItemPositions = new ArrayList<Integer>();

        for(int i = 0; i < myChecked.size(); i++){
            if (myChecked.get(i)){
                (checkedItemPositions).add(i);
            }
        }

        return checkedItemPositions;
    }

    public List<InterestData> getCheckedItems(){
        List<InterestData> checkedItems = new ArrayList<>();

        for(int i = 0; i < myChecked.size(); i++){
            if (myChecked.get(i)){
                (checkedItems).add(mList.get(i));
            }
        }

        return checkedItems;
    }


    private class ViewHolder {
        //TextView code;
        //CheckBox name;
        CheckedTextView name;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        Log.v("ConvertView", String.valueOf(position));

        if (convertView == null) {
            LayoutInflater vi = (LayoutInflater)mContext.getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.interest_info, null);

            holder = new ViewHolder();
            //holder.code = (TextView) convertView.findViewById(R.id.code);
            holder.name = (CheckedTextView) convertView.findViewById(R.id.checkBox1);
            convertView.setTag(holder);


            /*holder.name.setOnClickListener( new View.OnClickListener() {
                public void onClick(View v) {
                    CheckedTextView cb = (CheckedTextView) v ;
                    InterestData data = (InterestData) cb.getTag();
                    *//*Toast.makeText(mContext,
                            "Clicked on Checkbox: " + cb.getText() +
                                    " is " + cb.isChecked(),
                            Toast.LENGTH_LONG).show();
                    data.setIsSelected(cb.isChecked());*//*
                }
            });*/
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        InterestData data = mList.get(position);
        holder.name.setText(data.getName());
        holder.name.setChecked(data.isSelected());
        holder.name.setTag(data);

        Boolean checked = myChecked.get(position);
        if (checked != null) {
            holder.name.setChecked(checked);
        }

        return convertView;

    }

}