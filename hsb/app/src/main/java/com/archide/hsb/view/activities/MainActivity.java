package com.archide.hsb.view.activities;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;

import com.archide.hsb.entity.ConfigurationEntity;
import com.archide.hsb.enumeration.AppType;
import com.archide.hsb.service.TableListService;
import com.archide.hsb.service.impl.TableListServiceImpl;
import com.archide.hsb.view.fragments.FragmentsUtil;
import com.archide.hsb.view.fragments.KitchenLoginFragment;
import com.archide.hsb.view.fragments.MobileFragment;
import com.archide.hsb.view.fragments.ConfigurationFragment;
import com.archide.hsb.view.fragments.WelcomeFragment;

import java.io.File;
import java.io.FileInputStream;

import hsb.archide.com.hsb.R;

public class MainActivity extends AppCompatActivity implements MobileFragment.MainActivityCallback,
        ConfigurationFragment.MainActivityCallback, KitchenLoginFragment.MainActivityCallback,WelcomeFragment.MainActivityCallback{

   // private MobileFragment mobileFragment;
    private WelcomeFragment welcomeFragment;
    private ConfigurationFragment configurationFragment;
    private TableListService tableListService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        showFragment();
    }

    private void init(){
        tableListService = new TableListServiceImpl(this);
    }


    private void showFragment(){
        ConfigurationEntity configurationEntity = tableListService.getAppType();
        if(configurationEntity == null){
            configurationFragment = new ConfigurationFragment();
            FragmentsUtil.addFragment(this, configurationFragment, R.id.main_container);
        }else{
            if(configurationEntity.getAppType().toString().equals(AppType.Kitchen.toString())){
                KitchenLoginFragment kitchenLoginFragment = new KitchenLoginFragment();
                FragmentsUtil.addFragment(this, kitchenLoginFragment, R.id.main_container);
            }else{
                welcomeFragment = new WelcomeFragment();
                FragmentsUtil.addFragment(this, welcomeFragment, R.id.main_container);
                ActivityUtil.TABLE_NUMBER = configurationEntity.getTableNumber();
              /*  mobileFragment = new MobileFragment();
                FragmentsUtil.addFragment(this, mobileFragment, R.id.main_container);
                ActivityUtil.TABLE_NUMBER = configurationEntity.getTableNumber();*/
            }
        }

    }

    @Override
    public void success(int code, Object data) {
        if(code == AppType.Kitchen.getAppType()){
            KitchenLoginFragment kitchenLoginFragment = new KitchenLoginFragment();
            FragmentsUtil.addFragment(this, kitchenLoginFragment, R.id.main_container);

           /* Intent intent = new Intent(this, KitchenActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();*/
        }
        else if(code == AppType.User.getAppType()){
            welcomeFragment = new WelcomeFragment();
            FragmentsUtil.replaceFragmentNoStack(this, welcomeFragment, R.id.main_container);

        }
        else if(code == 5001){
            MobileFragment mobileFragment = new MobileFragment();
            FragmentsUtil.replaceFragment(this,mobileFragment,R.id.main_container);
        }else if(code == 5002){
            Intent intent = new Intent(this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
        else if(code == 5000){
            Intent intent = new Intent(this, KitchenActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }else{

            Intent intent = new Intent(this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
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
        }

    }

    public TableListService getTableListService() {
        return tableListService;
    }
}
