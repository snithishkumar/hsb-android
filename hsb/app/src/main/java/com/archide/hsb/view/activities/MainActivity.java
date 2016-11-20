package com.archide.hsb.view.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.archide.hsb.service.TableListService;
import com.archide.hsb.service.impl.TableListServiceImpl;
import com.archide.hsb.view.fragments.FragmentsUtil;
import com.archide.hsb.view.fragments.LoginFragment;
import com.archide.hsb.view.fragments.RegistrationFragment;

import java.io.File;
import java.io.FileInputStream;

import hsb.archide.com.hsb.R;

public class MainActivity extends AppCompatActivity implements LoginFragment.MainActivityCallback, RegistrationFragment.MainActivityCallback{

    private LoginFragment loginFragment;
    private RegistrationFragment registrationFragment;
    private TableListService tableListService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       // copyDataBase();
        init();
        showFragment();
    }

    private void init(){
        tableListService = new TableListServiceImpl(this);
    }


    private void showFragment(){
        if(tableListService.isTableConfigured()){
            loginFragment = new LoginFragment();
            FragmentsUtil.addFragment(this, loginFragment, R.id.main_container);
        }else{
            registrationFragment = new RegistrationFragment();
            FragmentsUtil.addFragment(this, registrationFragment, R.id.main_container);
        }

    }

    @Override
    public void success(int code, Object data) {
        if(code == 2){
            Intent intent = new Intent(this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }else{
            loginFragment = new LoginFragment();
            FragmentsUtil.replaceFragmentNoStack(this,loginFragment, R.id.main_container);
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
