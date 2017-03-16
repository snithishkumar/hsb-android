package com.archide.hsb.view.fragments;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Locale;

import hsb.archide.com.hsb.R;

/**
 * Created by Nithish on 05/03/17.
 */

public class DataFromServer extends Fragment implements TextToSpeech.OnInitListener  {

    private TextToSpeech engine;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        engine = new TextToSpeech(getContext(), this);
        View view = inflater.inflate(R.layout.fragment_empty, container, false);
        return view;
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
            //speech(getContext().getString(R.string.data_from_server_voice));
        }

    }

    private void speech(String textToSpeech) {
        engine.speak(textToSpeech, TextToSpeech.QUEUE_FLUSH, null, null);
    }


}
