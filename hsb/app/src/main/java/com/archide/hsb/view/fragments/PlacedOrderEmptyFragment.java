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
 * Created by Nithish on 09/03/17.
 */

public class PlacedOrderEmptyFragment extends Fragment {


    private NaviDrawerActivity naviDrawerActivity;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_place_order_empty, container, false);
        initBackButton();
        return view;
    }

    private void initBackButton(){
        naviDrawerActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        naviDrawerActivity.getSupportActionBar().setHomeButtonEnabled(true);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.naviDrawerActivity = (NaviDrawerActivity)context;
    }
}
