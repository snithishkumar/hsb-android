package com.archide.hsb.view.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
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
public class MobileFragment extends Fragment implements TextToSpeech.OnInitListener {

    private MainActivity mainActivity;
    private EditText userMobile;
    private ProgressDialog progressDialog;

    private LayoutInflater mInflater;
    private ViewGroup mContainer;


    private TextToSpeech engine;
    private String orderType;


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
        orderType = getArguments().getString("orderType");

        return initLayout();
    }


    private View initLayout(){
        View loginView =  mInflater.inflate(R.layout.fragment_mobile, mContainer, false);


        //mobile_fragment_layout

        RelativeLayout relativeLayout = (RelativeLayout)loginView.findViewById(R.id.mobile_fragment_layout);
        int orientation = getResources().getConfiguration().orientation;
        if(Configuration.ORIENTATION_LANDSCAPE == orientation){
            relativeLayout.setBackground(getResources().getDrawable(R.drawable.mobile_background_land));
        }else{
            relativeLayout.setBackground(getResources().getDrawable(R.drawable.mobile_background));
        }

        userMobile =  (EditText)loginView.findViewById(R.id.vUserMobileNumber);
        userMobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
              String userMobileText =    userMobile.getText().toString();
                if(userMobileText.length() >= 4){
                    getMenuList(userMobileText);
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        return loginView;

    }


    @Override
    public void onResume() {
        super.onResume();
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




    private void getMenuList(String userMobileText) {
        boolean isNetWorkConnected = Utilities.isNetworkConnected(mainActivity);
        if (isNetWorkConnected) {
            progressDialog = ActivityUtil.showProgress(getString(R.string.get_table_list_heading), getString(R.string.get_menu_items_message), mainActivity);
            mainActivity.getTableListService().createUsers(userMobileText, OrderType.valueOf(orderType));
            mainActivity.getTableListService().getMenuItems();
        } else {
            //showNoInterNet();
            ActivityUtil.showDialog(mainActivity, getString(R.string.no_network_heading), getString(R.string.no_network));
        }
    }

   /* private void showNoInterNet(){
        View newView = mInflater.inflate(R.layout.fragment_no_internet, mContainer, false);
        TextView tryNow = (TextView)newView.findViewById(R.id.try_now);
        tryNow.setOnClickListener(this);
        mContainer.removeAllViews();
        mContainer.addView(newView);
    }*/

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleServerSyncResponse(ResponseData responseData) {
       if(progressDialog != null){
           progressDialog.dismiss();
       }
        if(responseData.getStatusCode() == 404){
            mainActivity.success(MainActivity.TABLE_UNAVAILABLE,null);
            return;
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
