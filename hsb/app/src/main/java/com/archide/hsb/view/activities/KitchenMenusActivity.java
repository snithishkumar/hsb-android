package com.archide.hsb.view.activities;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;

import com.archide.hsb.service.KitchenService;
import com.archide.hsb.service.impl.KitchenServiceImpl;
import com.archide.hsb.sync.json.ResponseData;
import com.archide.hsb.util.Utilities;
import com.archide.hsb.view.fragments.FragmentsUtil;
import com.archide.hsb.view.fragments.KitchenDataFromServer;
import com.archide.hsb.view.fragments.KitchenInternetLostFragment;
import com.archide.hsb.view.fragments.KitchenMenuItemsFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import hsb.archide.com.hsb.R;

/**
 * Created by Nithish on 05/03/17.
 */

public class KitchenMenusActivity extends AppCompatActivity {

    private KitchenService kitchenService;

    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kitchen_menu);
        init();
        showFragment();

    }

    private void init(){
        kitchenService = new KitchenServiceImpl(this);

    }

    private void showFragment(){
        boolean isNetWorkConnected = Utilities.isNetworkConnected(this);
        if(!isNetWorkConnected){
            showInternetLostFragment();
        }else{
            ActivityUtil.isKitchenMenuList = true;
            showKitchenDataFromServerFragment();
            progressDialog = ActivityUtil.showProgress(getString(R.string.get_table_list_heading), getString(R.string.get_menu_items_message), this);
        }

    }

    private void showInternetLostFragment(){
        KitchenInternetLostFragment kitchenInternetLostFragment = new KitchenInternetLostFragment();
        FragmentsUtil.addRemoveFragment(this,kitchenInternetLostFragment,R.id.main_container);
    }

    private void showKitchenDataFromServerFragment(){
        KitchenDataFromServer kitchenDataFromServer = new KitchenDataFromServer();
        FragmentsUtil.addRemoveFragment(this,kitchenDataFromServer,R.id.main_container);
    }


    private void showMenuListFragment(){
        KitchenMenuItemsFragment kitchenMenuItemsFragment = new KitchenMenuItemsFragment();
        FragmentsUtil.addFragment(this,kitchenMenuItemsFragment, R.id.main_container);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.kitchen_order_list, menu);





        return true;
    }





    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_order_list:
                Intent intent = new Intent(this, KitchenActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public KitchenService getKitchenService() {
        return kitchenService;
    }

    @Override
    public void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onPause() {
        EventBus.getDefault().unregister(this);
        super.onPause();
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleServerSyncResponse(ResponseData responseData) {

        if(responseData != null && responseData.getStatusCode() == 600 && !responseData.getSuccess()){
            showInternetLostFragment();
        }else  if(responseData.getStatusCode() == 2000){
            ActivityUtil.isKitchenMenuList = false;
            if(progressDialog != null){
                progressDialog.dismiss();
            }
            showMenuListFragment();
        }else if(responseData.getStatusCode() == 500){
            ActivityUtil.showDialog(this,"Error","Sorry for the Inconvenience. Please contact Admin.");
        }

    }






}
