package com.archide.hsb.view.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.archide.hsb.sync.json.ResponseData;
import com.archide.hsb.util.Utilities;
import com.archide.hsb.view.activities.ActivityUtil;
import com.archide.hsb.view.activities.MainActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import hsb.archide.com.hsb.R;

/**
 * Created by Nithish on 20/11/16.
 */

public class RegistrationFragment extends Fragment implements View.OnClickListener{

    private MainActivity mainActivity;
    ProgressDialog progressDialog = null;
    ArrayAdapter<String> adapter = null;

    private EditText vPasswordText;
    private EditText vReTypePasswordText;
    private Spinner spinner;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View registrationView =  inflater.inflate(R.layout.fragment_registration, container, false);

        vPasswordText = (EditText) registrationView.findViewById(R.id.vPassword);
        vReTypePasswordText = (EditText) registrationView.findViewById(R.id.vRetypePassword);

        FloatingActionButton button =  (FloatingActionButton)registrationView.findViewById(R.id.submit);
        button.setOnClickListener(this);

         spinner = (Spinner) registrationView.findViewById(R.id.vTableNumber);
        List<String> test  =new ArrayList<>();
        test.add("1");
        adapter = new ArrayAdapter<>(mainActivity,android.R.layout.simple_spinner_item,test);


        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        getTableList();
        return registrationView;
    }

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


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleServerSyncResponse(ResponseData responseData) {
        intiView(responseData);

        return;
    }

    private void intiView(ResponseData responseData){
        dismiss();
        if(responseData.getSuccess()){
          List<String> msg = (List<String>)responseData.getMessage();
            adapter.clear();
            adapter.addAll(msg);


            adapter.notifyDataSetChanged();

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

        String vTableNumber = spinner.getSelectedItem().toString();
        mainActivity.getTableListService().createAdmin(vTableNumber,mPin);
        mainActivity.success(1,null);
        return;
    }

    public interface MainActivityCallback{
        void success(int code,Object data);
    }
}
