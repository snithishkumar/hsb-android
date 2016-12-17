package com.archide.hsb.view.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.archide.hsb.entity.AppTypeEntity;
import com.archide.hsb.enumeration.AppType;
import com.archide.hsb.service.TableListService;
import com.archide.hsb.service.impl.TableListServiceImpl;
import com.archide.hsb.view.fragments.FragmentsUtil;
import com.archide.hsb.view.fragments.LoginFragment;
import com.archide.hsb.view.fragments.MobileFragment;
import com.archide.hsb.view.fragments.ConfigurationFragment;

import java.io.File;
import java.io.FileInputStream;

import hsb.archide.com.hsb.R;

public class MainActivity extends AppCompatActivity implements MobileFragment.MainActivityCallback, ConfigurationFragment.MainActivityCallback{

    private MobileFragment mobileFragment;
    private ConfigurationFragment configurationFragment;
    private TableListService tableListService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       // init();
        showFragment();
    }

    private void init(){
        tableListService = new TableListServiceImpl(this);
    }


    private void showFragment(){
        AppTypeEntity appTypeEntity = tableListService.getAppType();
        if(appTypeEntity == null){
            configurationFragment = new ConfigurationFragment();
            FragmentsUtil.addFragment(this, configurationFragment, R.id.main_container);
        }else{
            if(appTypeEntity.getAppType().toString().equals(AppType.Kitchen.toString())){
                LoginFragment loginFragment = new LoginFragment();
                FragmentsUtil.addFragment(this, loginFragment, R.id.main_container);
            }else{
                mobileFragment = new MobileFragment();
                FragmentsUtil.addFragment(this, mobileFragment, R.id.main_container);
                /*if(tableListService.isTableConfigured()){

                }else{

                }*/
            }
        }

    }

    @Override
    public void success(int code, Object data) {
        if(code == AppType.Kitchen.getAppType()){
            Intent intent = new Intent(this, KitchenActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
        else if(code == AppType.User.getAppType()){
            mobileFragment = new MobileFragment();
            FragmentsUtil.replaceFragmentNoStack(this, mobileFragment, R.id.main_container);
        }else{

            Intent intent = new Intent(this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }

    }

    public  void copyDataBase(){
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

    public TableListService getTableListService() {
        return tableListService;
    }
}
