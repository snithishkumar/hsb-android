package com.archide.hsb.view.fragments;

import android.content.Context;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.archide.hsb.view.activities.NaviDrawerActivity;

import java.util.Locale;

import hsb.archide.com.hsb.R;

/**
 * Created by Nithish on 09/03/17.
 */

public class PlacedOrderEmptyFragment extends Fragment implements TextToSpeech.OnInitListener {


    private NaviDrawerActivity naviDrawerActivity;
    private TextToSpeech engine;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        engine = new TextToSpeech(getContext(), this);
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

    @Override
    public void onStop() {
        super.onStop();
        engine.shutdown();
    }

    @Override
    public void onInit(int i) {
        if (i == TextToSpeech.SUCCESS) {
            engine.setLanguage(Locale.UK);
            speech(getContext().getString(R.string.history_empty_voice));
        }

    }

    private void speech(String textToSpeech) {
        engine.speak(textToSpeech, TextToSpeech.QUEUE_FLUSH, null, null);
    }
}
