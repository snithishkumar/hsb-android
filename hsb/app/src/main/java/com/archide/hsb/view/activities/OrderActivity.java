package com.archide.hsb.view.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.archide.hsb.service.OrderService;
import com.archide.hsb.service.impl.OrderServiceImpl;
import com.archide.hsb.view.fragments.FragmentsUtil;
import com.archide.hsb.view.fragments.OrderPlaceFragment;

import hsb.archide.com.hsb.R;

public class OrderActivity extends AppCompatActivity {

    private OrderService orderService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        init();
        showFragment();
    }

    private void init(){
        orderService = new OrderServiceImpl(this);
    }


    private void showFragment(){
        OrderPlaceFragment orderPlaceFragment = new OrderPlaceFragment();
        FragmentsUtil.addFragment(this, orderPlaceFragment, R.id.main_container);
    }

    public OrderService getOrderService() {
        return orderService;
    }
}
