package com.archide.hsb.view.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.archide.hsb.enumeration.AppType;
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

import hsb.archide.com.hsb.R;

/**
 * Created by Nithish on 20/11/16.
 */

public class ConfigurationFragment extends Fragment implements View.OnClickListener{

    private MainActivity mainActivity;
    ProgressDialog progressDialog = null;

    private EditText vPasswordText;
    private EditText vReTypePasswordText;
    private LabelledSpinner vType;
    private LabelledSpinner vTableNumber;
    private LinearLayout linearLayout;
    private AppType selectedAppType = AppType.Kitchen;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View registrationView =  inflater.inflate(R.layout.fragment_configuration, container, false);

        vPasswordText = (EditText) registrationView.findViewById(R.id.vPassword);
        vReTypePasswordText = (EditText) registrationView.findViewById(R.id.vRetypePassword);

        FloatingActionButton button =  (FloatingActionButton)registrationView.findViewById(R.id.submit);
        button.setOnClickListener(this);

        initType(registrationView);
        vTableNumber = (LabelledSpinner) registrationView.findViewById(R.id.vTableNumber);
        linearLayout = (LinearLayout) registrationView.findViewById(R.id.vTableDetailsLayout);

        return registrationView;
    }


    /**
     * Initialize type of User
     * @param registrationView
     */
    private void initType(View registrationView){
        vType = (LabelledSpinner) registrationView.findViewById(R.id.vType);
        final List<String> data  = new ArrayList<>();
        data.add(AppType.Kitchen.toString());
        data.add(AppType.User.toString());
        vType.setItemsArray(data);

        vType.setOnItemChosenListener(new LabelledSpinner.OnItemChosenListener() {
            @Override
            public void onItemChosen(View labelledSpinner, AdapterView<?> adapterView, View itemView, int position, long id) {
                if(position == 1){
                    getTableList();
                    selectedAppType = AppType.User;
                }else{
                    if(linearLayout.getVisibility() == View.VISIBLE){
                        linearLayout.setVisibility(View.GONE);
                    }
                    selectedAppType = AppType.Kitchen;
                }
            }

            @Override
            public void onNothingChosen(View labelledSpinner, AdapterView<?> adapterView) {

            }
        });

    }


    /**
     * Get Table List from the Server
     */
    private void getTableList(){
        boolean isNetWorkConnected =  Utilities.isNetworkConnected(mainActivity);
        if(isNetWorkConnected){

            progressDialog = ActivityUtil.showProgress(getString(R.string.get_table_list_heading), getString(R.string.get_table_list_message), mainActivity);
            mainActivity.getTableListService().getTableList();
        }else{
            ActivityUtil.showDialog(mainActivity, getString(R.string.no_network_heading), getString(R.string.no_network));
        }
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
    }

    /**
     * Process Table list response
     * @param responseData
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleServerSyncResponse(ResponseData responseData) {
        intiView(responseData);
        return;
    }

    /**
     * Process Table List Data and Show that component
     * @param responseData
     */
    private void intiView(ResponseData responseData){
        dismiss();
        if(responseData.getSuccess()){
            List<String> data  =  (List<String>)responseData.getMessage();
            linearLayout.setVisibility(View.VISIBLE);
            vTableNumber.setItemsArray(data);
        }

    }

    private void dismiss(){
        if(progressDialog != null){
            progressDialog.dismiss();
        }
    }

    @Override
    public void onClick(View view) {
       String mPin = vPasswordText.getText().toString();
        if(mPin.length() != 6){
            vPasswordText.setError("MPin must be 6 digits.");
        }
        String reTypeMPin = vReTypePasswordText.getText().toString();
        if(reTypeMPin.length() != 6){
            vReTypePasswordText.setError("MPin must be 6 digits.");
        }
        String vTableNumberText = null;
        int appType = AppType.Kitchen.getAppType();
        if(selectedAppType.equals(AppType.User)){
            vTableNumberText = vTableNumber.getSpinner().getSelectedItem().toString();
            appType = AppType.User.getAppType();
            ActivityUtil.TABLE_NUMBER = vTableNumberText;
        }

        mainActivity.getTableListService().createAdmin(vTableNumberText,mPin,selectedAppType);
        mainActivity.success(appType,null);
        return;
    }

    public interface MainActivityCallback{
        void success(int code,Object data);
    }
}
