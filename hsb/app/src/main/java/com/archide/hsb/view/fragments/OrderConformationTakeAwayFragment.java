package com.archide.hsb.view.fragments;

import android.content.Context;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.archide.hsb.entity.ConfigurationEntity;
import com.archide.hsb.enumeration.AppType;
import com.archide.hsb.view.activities.OrderActivity;

import java.util.Locale;

import hsb.archide.com.hsb.R;

/**
 * Created by Nithish on 17/03/17.
 */

public class OrderConformationTakeAwayFragment extends Fragment implements TextToSpeech.OnInitListener{

    private TextToSpeech engine;
    private OrderActivity orderActivity;
    private int statusCode;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        engine = new TextToSpeech(getContext(), this);
        statusCode = getArguments().getInt("statusCode");
        View view = inflater.inflate(R.layout.fragment_order_conformation_takeaway, container, false);
        TextView  textView  = (TextView)view.findViewById(R.id.vOrderConformation);
        if(statusCode == 201){
            textView.setText("Your Bill has been sent to MobilePay.");
        }else{
            textView.setText("Please Install MobilePay.");
        }
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        orderActivity = (OrderActivity)context;
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
            speech(getContext().getString(R.string.close_order_empty_voice));
        }

    }

    private void speech(String textToSpeech) {
        engine.speak(textToSpeech, TextToSpeech.QUEUE_FLUSH, null, null);
    }

}
