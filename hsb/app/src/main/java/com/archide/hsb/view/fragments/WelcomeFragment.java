package com.archide.hsb.view.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.archide.hsb.entity.ConfigurationEntity;
import com.archide.hsb.entity.UsersEntity;
import com.archide.hsb.sync.json.ResponseData;
import com.archide.hsb.util.Utilities;
import com.archide.hsb.view.activities.ActivityUtil;
import com.archide.hsb.view.activities.MainActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import hsb.archide.com.hsb.R;

/**
 * Created by Nithish on 11/01/17.
 */
public class WelcomeFragment extends Fragment implements View.OnClickListener{

    Button button1 = null;
    Button button2 = null;
    Button button3 = null;
    Button button4 = null;
    MainActivity mainActivity = null;
    private ProgressDialog progressDialog;


    private LayoutInflater mInflater;
    private ViewGroup mContainer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mInflater = inflater;
        mContainer = container;
        return initLayout();
    }


    private View initLayout(){
        View view =  mInflater.inflate(R.layout.fragment_welcome, mContainer, false);

        button1 = (Button)view.findViewById(R.id.welcome_button1);
        button2 = (Button)view.findViewById(R.id.welcome_button2);
        button3 = (Button)view.findViewById(R.id.welcome_button3);
        button4 = (Button)view.findViewById(R.id.welcome_button4);
        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
        button4.setOnClickListener(this);

        ActivityUtil.USER_MOBILE = null;

        List<UsersEntity> usersEntities =  mainActivity.getTableListService().getUsers();
        ConfigurationEntity configurationEntity =   mainActivity.getTableListService().getAppType();
        ActivityUtil.TABLE_NUMBER = configurationEntity.getTableNumber();
        int size = usersEntities.size();
        switch (size){
            case 0:
                button1.setText("New Orders");
                button2.setVisibility(View.INVISIBLE);
                button3.setVisibility(View.INVISIBLE);
                button4.setVisibility(View.INVISIBLE);
                break;
            case 1:
                button1.setText(usersEntities.get(0).getUserMobileNumber());
                button2.setText("New Orders");
                button3.setVisibility(View.INVISIBLE);
                button4.setVisibility(View.INVISIBLE);
                break;
            case 2:
                button1.setText(usersEntities.get(0).getUserMobileNumber());
                button2.setText(usersEntities.get(1).getUserMobileNumber());
                button3.setText("New Orders");
                button4.setVisibility(View.INVISIBLE);
                break;
            case 3:
                button1.setText(usersEntities.get(0).getUserMobileNumber());
                button2.setText(usersEntities.get(1).getUserMobileNumber());
                button3.setText(usersEntities.get(2).getUserMobileNumber());
                button4.setText("New Orders");
                break;
            case 4:
                button1.setText(usersEntities.get(0).getUserMobileNumber());
                button2.setText(usersEntities.get(1).getUserMobileNumber());
                button3.setText(usersEntities.get(2).getUserMobileNumber());
                button4.setText(usersEntities.get(3).getUserMobileNumber());

                break;

        }
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_admin, menu);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.welcome_button1:
               String val = button1.getText().toString();
                showFragment(val);
                break;
            case R.id.welcome_button2:
                val = button2.getText().toString();
                showFragment(val);
                break;
            case R.id.welcome_button3:
                val = button3.getText().toString();
                showFragment(val);
                break;
            case R.id.welcome_button4:
                val = button4.getText().toString();
                showFragment(val);
                break;

            case R.id.try_now:
                boolean isNetWorkConnected = Utilities.isNetworkConnected(mainActivity);
                if(isNetWorkConnected){
                    mContainer.removeAllViews();
                    View layoutView = initLayout();
                    mContainer.addView(layoutView);
                }

                break;
        }
    }


    private void showFragment(String val){
        mainActivity.getTableListService().removePreviousData();
        if(val.equals("New Orders")){
            mainActivity.success(5001,null);
            return;
        }else{
            ActivityUtil.USER_MOBILE = val;
            getMenuList();
            return;
        }
    }

    private void getMenuList() {
        boolean isNetWorkConnected = Utilities.isNetworkConnected(mainActivity);
        if (isNetWorkConnected) {
            progressDialog = ActivityUtil.showProgress(getString(R.string.get_table_list_heading), getString(R.string.get_menu_items_message), mainActivity);
            mainActivity.getTableListService().getMenuItems(ActivityUtil.TABLE_NUMBER,ActivityUtil.USER_MOBILE,null);
        } else {
            showNoInterNet();
            //ActivityUtil.showDialog(mainActivity, getString(R.string.no_network_heading), getString(R.string.no_network));
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
            mainActivity.success(5002,null);
            return;
        }else{
            ActivityUtil.showDialog(mainActivity,"Error","Sorry for the Inconvenience. Please contact Admin.");
        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mainActivity = (MainActivity)context;

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

}
