package com.archide.hsb.view.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.archide.hsb.service.OrderService;
import com.archide.hsb.service.impl.OrderServiceImpl;
import com.archide.hsb.view.fragments.FragmentsUtil;
import com.archide.hsb.view.fragments.OrderPlaceFragment;

import hsb.archide.com.hsb.R;

public class OrderActivity extends AppCompatActivity {

    private OrderService orderService;
    OrderPlaceFragment orderPlaceFragment;

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
        orderPlaceFragment = new OrderPlaceFragment();
        FragmentsUtil.addFragment(this, orderPlaceFragment, R.id.main_container);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        orderPlaceFragment.setCookingComments();
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // go to previous activity
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public OrderService getOrderService() {
        return orderService;
    }
}
