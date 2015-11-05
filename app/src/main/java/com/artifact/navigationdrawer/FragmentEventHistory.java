package com.artifact.navigationdrawer;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.artifact.R;

public class FragmentEventHistory extends Fragment {
    ImageView menuImageView;
	public FragmentEventHistory(){}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.event_history_fragment, container, false);
        menuImageView = (ImageView)rootView.findViewById(R.id.menu_imageView);

        menuImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity1.hideMenu();
            }
        });

        return rootView;
    }
}

