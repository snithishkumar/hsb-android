package com.archide.hsb.view.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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
 *
 */
public class LoginFragment extends Fragment {

    private MainActivity mainActivity;
    ProgressDialog progressDialog = null;
    ArrayAdapter<String> adapter = null;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View loginView =  inflater.inflate(R.layout.fragment_login, container, false);
        Spinner spinner = (Spinner) loginView.findViewById(R.id.vTableNumber);
        List<String> test  =new ArrayList<>();
        test.add("1");
        adapter = new ArrayAdapter<>(mainActivity,android.R.layout.simple_spinner_item,test);


        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        getTableList();
        return loginView;
    }


    private void getTableList(){
      boolean isNetWorkConnected =  Utilities.isNetworkConnected(mainActivity);
        if(isNetWorkConnected){

            progressDialog = ActivityUtil.showProgress(getString(R.string.get_table_list_heading), getString(R.string.get_table_list_message), mainActivity);
            mainActivity.getTableListService().getTableList(mainActivity);
        }else{
            ActivityUtil.showDialog(mainActivity, getString(R.string.no_network_heading), getString(R.string.no_network));
        }
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
    }
}
