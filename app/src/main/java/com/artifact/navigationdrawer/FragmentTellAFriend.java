package com.artifact.navigationdrawer;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.artifact.R;

public class FragmentTellAFriend extends Fragment {
	// this Fragment will be called from MainActivity
	public FragmentTellAFriend(){}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.tell_a_friend_fragment, container, false);
         
        return rootView;
    }
}