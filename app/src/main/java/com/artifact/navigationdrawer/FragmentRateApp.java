package com.artifact.navigationdrawer;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.artifact.R;

public class FragmentRateApp extends Fragment {
	
	public FragmentRateApp(){}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.rate_app_fragment, container, false);
         
        return rootView;
    }
}
