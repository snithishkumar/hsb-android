package com.archide.hsb.view.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import hsb.archide.com.hsb.R;

/**
 * Created by Nithish on 04/03/17.
 */

public class DesignTestFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.adapt_kitchen_menu_list, container, false);

        return view;
    }
}
