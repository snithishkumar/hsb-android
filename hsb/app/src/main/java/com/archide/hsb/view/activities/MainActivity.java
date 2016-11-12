package com.archide.hsb.view.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.archide.hsb.service.TableListService;
import com.archide.hsb.service.impl.TableListServiceImpl;
import com.archide.hsb.view.fragments.FragmentsUtil;
import com.archide.hsb.view.fragments.LoginFragment;

import hsb.archide.com.hsb.R;

public class MainActivity extends AppCompatActivity {

    private LoginFragment loginFragment;
    private TableListService tableListService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        showFragment();
    }

    private void init(){
        tableListService = new TableListServiceImpl();
    }


    private void showFragment(){
        loginFragment = new LoginFragment();
        FragmentsUtil.addFragment(this, loginFragment, R.id.main_container);
    }

    public TableListService getTableListService() {
        return tableListService;
    }
}
