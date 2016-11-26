package com.archide.hsb.view.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.archide.hsb.service.KitchenService;
import com.archide.hsb.service.impl.KitchenServiceImpl;
import com.archide.hsb.service.impl.OrderServiceImpl;
import com.archide.hsb.view.fragments.FragmentsUtil;
import com.archide.hsb.view.fragments.KitchenOrderListFragment;
import com.archide.hsb.view.fragments.KitchenOrderedItemsFragment;
import com.archide.hsb.view.fragments.OrderPlaceFragment;

import hsb.archide.com.hsb.R;

/**
 * Created by Nithish on 26/11/16.
 */

public class KitchenActivity extends AppCompatActivity implements KitchenOrderListFragment.ViewOrderDetails{

    private KitchenService kitchenService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kitchen);
        init();
        showFragment();
    }

    private void init(){
        kitchenService = new KitchenServiceImpl(this);
       // orderService = new OrderServiceImpl(this);
    }


    private void showFragment(){
        KitchenOrderListFragment kitchenOrderListFragment = new KitchenOrderListFragment();
        FragmentsUtil.addFragment(this, kitchenOrderListFragment, R.id.main_container);
    }

    private void showOrderDetails(){
        KitchenOrderedItemsFragment kitchenOrderedItemsFragment = new KitchenOrderedItemsFragment();
        FragmentsUtil.replaceFragment(this,kitchenOrderedItemsFragment,R.id.main_container);
    }

    @Override
    public void viewOrderDetails(int id) {
        showOrderDetails();
    }

    public KitchenService getKitchenService() {
        return kitchenService;
    }
}
