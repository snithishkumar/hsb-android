package com.archide.hsb.view.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.archide.hsb.entity.ConfigurationEntity;
import com.archide.hsb.enumeration.OrderType;
import com.archide.hsb.service.OrderService;
import com.archide.hsb.service.TableListService;
import com.archide.hsb.service.impl.OrderServiceImpl;
import com.archide.hsb.service.impl.TableListServiceImpl;
import com.archide.hsb.view.fragments.CaptainMobileFragment;
import com.archide.hsb.view.fragments.CaptainTableFragment;
import com.archide.hsb.view.fragments.ConfigurationFragment;
import com.archide.hsb.view.fragments.FragmentsUtil;
import com.archide.hsb.view.fragments.KitchenLoginFragment;
import com.archide.hsb.view.fragments.MobileFragment;
import com.archide.hsb.view.fragments.OrderModuleLoginFragment;
import com.archide.hsb.view.fragments.WelcomeFragment;

import java.io.File;
import java.io.FileInputStream;

import hsb.archide.com.hsb.R;

public class MainActivity extends AppCompatActivity implements MobileFragment.MainActivityCallback,
        ConfigurationFragment.MainActivityCallback, KitchenLoginFragment.MainActivityCallback,
        OrderModuleLoginFragment.MainActivityCallback{

    private WelcomeFragment welcomeFragment;
    private ConfigurationFragment configurationFragment;
    private TableListService tableListService;


    public static final int CONF_SUCCESS_KITCHEN = 1;
    public static final int CONF_SUCCESS_USER = 2;
    public static final int CONF_SUCCESS_CAPTAIN = 3;
    public static final int MENU_LIST_SUCCESS = 4;
    public static final int MENU_LIST_NO_MOBILE = 7;
    public static final int TABLE_UNAVAILABLE = 6;
    public static final int KITCHEN_LOGIN_SUCCESS = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        showFragment();

    }

    private void init(){
        tableListService = new TableListServiceImpl(this);
        tableListService.removeAllData();
    }


    private void showFragment(){
        ConfigurationEntity configurationEntity = tableListService.getAppType();
        if(configurationEntity == null){
            configurationFragment = new ConfigurationFragment();
            FragmentsUtil.addFragment(this, configurationFragment, R.id.main_container);
        }else{
            switch (configurationEntity.getAppType()){
                case User:
                    WelcomeFragment welcomeFragment = new WelcomeFragment();
                    FragmentsUtil.addFragment(this, welcomeFragment, R.id.main_container);
                    break;
                case Captain:
                    CaptainTableFragment captainTableFragment = new CaptainTableFragment();
                    FragmentsUtil.addFragment(this, captainTableFragment, R.id.main_container);
                    break;
                case Kitchen:
                    KitchenLoginFragment kitchenLoginFragment = new KitchenLoginFragment();
                    FragmentsUtil.addFragment(this, kitchenLoginFragment, R.id.main_container);
                    break;
            }

        }

    }

    private void showCaptainView(){
        CaptainTableFragment captainTableFragment = new CaptainTableFragment();
        FragmentsUtil.replaceFragmentNoStack(this, captainTableFragment, R.id.main_container);
    }

    private void showUserView(){
        WelcomeFragment welcomeFragment = new WelcomeFragment();
        FragmentsUtil.replaceFragmentNoStack(this, welcomeFragment, R.id.main_container);
    }

    private void showKitchenView(){
        KitchenLoginFragment kitchenLoginFragment = new KitchenLoginFragment();
        FragmentsUtil.replaceFragmentNoStack(this, kitchenLoginFragment, R.id.main_container);
    }


    private void showCaptainMobileView(){
        CaptainMobileFragment captainMobileFragment = new CaptainMobileFragment();
        FragmentsUtil.replaceFragmentNoStack(this, captainMobileFragment, R.id.main_container);
    }


    private void showUserMobileFragment(OrderType orderType){
        MobileFragment mobileFragment = new MobileFragment();
        Bundle bundle = new Bundle();
        bundle.putString("orderType", orderType.toString());
        mobileFragment.setArguments(bundle);
        FragmentsUtil.replaceFragmentNoStack(this, mobileFragment, R.id.main_container);
    }


    private void showWelcome(ConfigurationEntity configurationEntity){
        switch (configurationEntity.getAppType()){
            case User:
                WelcomeFragment welcomeFragment = new WelcomeFragment();
                FragmentsUtil.addFragment(this, welcomeFragment, R.id.main_container);
                break;
            case Captain:
                CaptainTableFragment captainTableFragment = new CaptainTableFragment();
                FragmentsUtil.addFragment(this, captainTableFragment, R.id.main_container);
                break;
            case Kitchen:
                KitchenLoginFragment kitchenLoginFragment = new KitchenLoginFragment();
                FragmentsUtil.addFragment(this, kitchenLoginFragment, R.id.main_container);
                break;
        }
    }


    private void showUnAvailable(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        // Setting Dialog Title
        alertDialog.setTitle("Sorry");

        // Setting Dialog Message
        alertDialog.setMessage(getString(R.string.table_unavailable));

        // Setting Positive "Yes" Button
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                ConfigurationEntity configurationEntity = tableListService.getAppType();
                tableListService.removeAllData();
                showWelcome(configurationEntity);

            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

    @Override
    public void success(int code, Object data) {
        switch (code){
            case TABLE_UNAVAILABLE:
                showUnAvailable();
                break;
            case CONF_SUCCESS_CAPTAIN:
                showCaptainView();
                break;
            case CONF_SUCCESS_KITCHEN:
                showKitchenView();
                break;
            case CONF_SUCCESS_USER:
                showUserView();
                break;
            case MENU_LIST_SUCCESS:
                Intent intent = new Intent(this, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
                break;

            case KITCHEN_LOGIN_SUCCESS:
                intent = new Intent(this, KitchenActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();

                break;

            case MENU_LIST_NO_MOBILE:
                showCaptainMobileView();
                break;
        }



    }

    private  void copyDataBase(){
        try{
            String fileName =  this.getApplicationInfo().dataDir + "/databases/hsbdatabase.db";
            String  resourcePath = FileUtils.getResourcePath(this, "hsbdatabase.db");
            File file = new File(fileName);
            if(file.exists()){
                FileInputStream fileInputStream = new FileInputStream(file);
                FileUtils.downloadStreamData(fileInputStream, resourcePath);
                fileInputStream.close();
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        Fragment fragment =  getSupportFragmentManager().findFragmentById(R.id.main_container);
        if(!(fragment instanceof WelcomeFragment)){
            super.onBackPressed();
        }else if(fragment instanceof CaptainMobileFragment){
            OrderService orderService = new OrderServiceImpl(this);
            ConfigurationEntity configurationEntity = tableListService.getAppType();
            orderService.removeReservedTable(this,configurationEntity.getTableNumber());
        }

    }



    public void onClick(View view){
        switch (view.getId()){
            case R.id.welcome_dinning:
                showUserMobileFragment(OrderType.Dinning);
                break;
            case R.id.welcome_take_away:
                showUserMobileFragment(OrderType.TakeAway);
                break;
        }

    }



    public TableListService getTableListService() {
        return tableListService;
    }
}
