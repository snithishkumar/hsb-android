package com.archide.hsb.view.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.archide.hsb.sync.json.ResponseData;
import com.archide.hsb.util.Utilities;
import com.archide.hsb.view.activities.ActivityUtil;
import com.archide.hsb.view.activities.MainActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import hsb.archide.com.hsb.R;

/**
 *
 */
public class MobileFragment extends Fragment implements View.OnClickListener{

    private MainActivity mainActivity;
    private TextView userMobile;
    private ProgressDialog progressDialog;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View loginView =  inflater.inflate(R.layout.fragment_mobile, container, false);
        userMobile =  (TextView)loginView.findViewById(R.id.vUserMobileNumber);
        FloatingActionButton button =  (FloatingActionButton)loginView.findViewById(R.id.submit);
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
    }

    public interface MainActivityCallback {
        void success(int code, Object data);
    }


    @Override
    public void onClick(View view) {
        String userMobileText = userMobile.getText().toString();
        if (userMobileText != null && !userMobileText.trim().isEmpty()) {
            mainActivity.getTableListService().updateUserMobile(userMobileText);
            ActivityUtil.USER_MOBILE = userMobileText;
        }
        getMenuList();
    }

    private void getMenuList() {
        boolean isNetWorkConnected = Utilities.isNetworkConnected(mainActivity);
        if (isNetWorkConnected) {

            progressDialog = ActivityUtil.showProgress(getString(R.string.get_table_list_heading), getString(R.string.get_table_list_message), mainActivity);
            mainActivity.getTableListService().getMenuItems(ActivityUtil.TABLE_NUMBER,ActivityUtil.USER_MOBILE);
        } else {
            ActivityUtil.showDialog(mainActivity, getString(R.string.no_network_heading), getString(R.string.no_network));
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleServerSyncResponse(ResponseData responseData) {
       if(progressDialog != null){
           progressDialog.dismiss();
       }
        if(responseData.getStatusCode() != 500){
            mainActivity.success(3,null);
            return;
        }else{
            ActivityUtil.showDialog(mainActivity,"Error","Sorry for the Inconvenience. Please contact Admin.");
        }
    }


}
