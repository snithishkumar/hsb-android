package com.archide.hsb.view.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.archide.hsb.enumeration.OrderType;
import com.archide.hsb.enumeration.UserType;
import com.archide.hsb.sync.json.ResponseData;
import com.archide.hsb.util.Utilities;
import com.archide.hsb.view.activities.ActivityUtil;
import com.archide.hsb.view.activities.MainActivity;
import com.satsuware.usefulviews.LabelledSpinner;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import hsb.archide.com.hsb.R;

/**
 *
 */
public class CaptainMobileFragment extends Fragment implements View.OnClickListener,TextToSpeech.OnInitListener {

    private MainActivity mainActivity;
    private TextView tableNumber;
    private TextView mobileNumber;
    private ProgressDialog progressDialog;

    private LayoutInflater mInflater;
    private ViewGroup mContainer;


    private TextToSpeech engine;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        engine = new TextToSpeech(mainActivity, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        mInflater = inflater;
        mContainer = container;


        return initLayout();
    }


    private View initLayout(){
        View loginView =  mInflater.inflate(R.layout.fragment_captain_mobile, mContainer, false);
      //vCaptainTableNumber
        tableNumber =  (TextView)loginView.findViewById(R.id.vCaptainTableNumber);
        mobileNumber =  (TextView)loginView.findViewById(R.id.vCaptainUserMobileNumber);
        Button button =  (Button)loginView.findViewById(R.id.submit);
        button.setOnClickListener(this);



        return loginView;

    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mainActivity = (MainActivity)context;

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
        engine.shutdown();
    }

    public interface MainActivityCallback {
        void success(int code, Object data);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.submit:
              String tableNumberValue =  tableNumber.getText().toString();
              String mobileNumberValue =    mobileNumber.getText().toString();
                if (tableNumberValue != null && !tableNumberValue.trim().isEmpty()) {
                    getMenuList(tableNumberValue,mobileNumberValue);
                }else{
                    tableNumber.setError(getString(R.string.mobile_number_error));

                }
                break;

        }


    }

    private void getMenuList(String tableNumberValue,String mobileNumberValue) {
        boolean isNetWorkConnected = Utilities.isNetworkConnected(mainActivity);
        if (isNetWorkConnected) {
            progressDialog = ActivityUtil.showProgress(getString(R.string.get_table_list_heading), getString(R.string.get_menu_items_message), mainActivity);
            mainActivity.getTableListService().updateTableNumber(tableNumberValue,mobileNumberValue,OrderType.Dinning);
            mainActivity.getTableListService().getMenuItems();
        } else {
            showNoInterNet();
           // ActivityUtil.showDialog(mainActivity, getString(R.string.no_network_heading), getString(R.string.no_network));
        }
    }

    private void showNoInterNet(){
        View newView = mInflater.inflate(R.layout.fragment_no_internet, mContainer, false);
        TextView tryNow = (TextView)newView.findViewById(R.id.try_now);
        tryNow.setOnClickListener(this);
        mContainer.removeAllViews();
        mContainer.addView(newView);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleServerSyncResponse(ResponseData responseData) {
       if(progressDialog != null){
           progressDialog.dismiss();
       }

        if(responseData.getStatusCode() != 500){
            mainActivity.success(MainActivity.MENU_LIST_SUCCESS,null);
            return;
        }else{
            ActivityUtil.showDialog(mainActivity,"Error","Sorry for the Inconvenience. Please contact Admin.");
        }
    }

    @Override
    public void onInit(int i) {
        if (i == TextToSpeech.SUCCESS) {
            engine.setLanguage(Locale.UK);
            speech(mainActivity.getString(R.string.welcome_voice));
        }

    }

    private void speech(String textToSpeech) {
        engine.speak(textToSpeech, TextToSpeech.QUEUE_FLUSH, null, null);
    }


}
