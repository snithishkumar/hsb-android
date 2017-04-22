package com.archide.hsb.view.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.archide.hsb.enumeration.OrderType;
import com.archide.hsb.service.KitchenService;
import com.archide.hsb.service.impl.KitchenServiceImpl;
import com.archide.hsb.service.impl.PrinterServiceImpl;
import com.archide.hsb.view.fragments.FragmentsUtil;
import com.archide.hsb.view.fragments.KitchenOrderListFragment;
import com.archide.hsb.view.fragments.KitchenOrderedItemsFragment;

import hsb.archide.com.hsb.R;

/**
 * Created by Nithish on 20/02/17.
 */

public class KitchenDetailsActivity extends AppCompatActivity {

    private KitchenService kitchenService;
    private String orderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kitchen);
        init();
        this.orderId = getIntent().getStringExtra("orderId");
        showFragment(orderId);
    }

    private void init(){

        kitchenService = new KitchenServiceImpl(this);

    }

    private void showFragment(String orderId){
        KitchenOrderedItemsFragment kitchenOrderedItemsFragment = new KitchenOrderedItemsFragment();

        Bundle purchaseIdArgs = new Bundle();
        purchaseIdArgs.putString("orderId",orderId);
        kitchenOrderedItemsFragment.setArguments(purchaseIdArgs);
        FragmentsUtil.addFragment(this, kitchenOrderedItemsFragment, R.id.main_container);
    }


    public KitchenService getKitchenService() {
        return kitchenService;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
       OrderType orderType =  kitchenService.getOrderType(orderId);
        if(orderType.toString().equals(OrderType.Dinning.toString())){
            getMenuInflater().inflate(R.menu.menu_main, menu);
        }else{
            getMenuInflater().inflate(R.menu.kitchen_print_action, menu);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // go to previous activity
                onBackPressed();
                return true;

            case R.id.action_print_job:
                PrinterServiceImpl printerService = new PrinterServiceImpl(this,true);
                printerService.printKitchenOrders(orderId,this);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
