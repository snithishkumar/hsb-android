package com.archide.hsb.view.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.archide.hsb.service.KitchenService;
import com.archide.hsb.service.impl.KitchenServiceImpl;
import com.archide.hsb.view.fragments.FragmentsUtil;
import com.archide.hsb.view.fragments.KitchenOrderListFragment;
import com.archide.hsb.view.fragments.KitchenOrderedItemsFragment;

import hsb.archide.com.hsb.R;

/**
 * Created by Nithish on 20/02/17.
 */

public class KitchenDetailsActivity extends AppCompatActivity {

    private KitchenService kitchenService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kitchen);
        init();
        String orderId = getIntent().getStringExtra("orderId");
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
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
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
}
