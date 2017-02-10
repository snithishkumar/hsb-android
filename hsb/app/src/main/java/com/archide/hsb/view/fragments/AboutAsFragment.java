package com.archide.hsb.view.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.archide.hsb.view.activities.NaviDrawerActivity;

import hsb.archide.com.hsb.R;


/**
 * Created by Nithishkumar on 6/17/2016.
 */
public class AboutAsFragment extends Fragment {

    private NaviDrawerActivity naviDrawerActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about_us, container, false);
        naviDrawerActivity.getSupportActionBar().setTitle("About Us");
        initBackButton();
        return view;
    }


    private void initBackButton(){
        naviDrawerActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        naviDrawerActivity.getSupportActionBar().setHomeButtonEnabled(false);

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.naviDrawerActivity = (NaviDrawerActivity)context;
    }

}
