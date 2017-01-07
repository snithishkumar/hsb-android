package com.archide.hsb.view.activities;

import android.accounts.Account;
import android.content.ContentResolver;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.archide.hsb.service.KitchenService;
import com.archide.hsb.service.impl.KitchenServiceImpl;
import com.archide.hsb.service.impl.OrderServiceImpl;
import com.archide.hsb.sync.HsbSyncAdapter;
import com.archide.hsb.sync.SyncEvent;
import com.archide.hsb.view.fragments.FragmentsUtil;
import com.archide.hsb.view.fragments.KitchenOrderListFragment;
import com.archide.hsb.view.fragments.KitchenOrderedItemsFragment;
import com.archide.hsb.view.fragments.OrderPlaceFragment;

import hsb.archide.com.hsb.R;

import static android.provider.CallLog.AUTHORITY;

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

        Account account = HsbSyncAdapter.getSyncAccount(this);
        Bundle settingsBundle = new Bundle();
        settingsBundle.putInt("currentScreen", SyncEvent.GET_KITCHEN_ORDERS_DATA);

        ContentResolver.setIsSyncable(account, this.getString(R.string.content_authority), 1);
        ContentResolver.setSyncAutomatically(account, this.getString(R.string.content_authority), true);

        ContentResolver.addPeriodicSync(account, this.getString(R.string.auth_type), Bundle.EMPTY,5);

            kitchenService = new KitchenServiceImpl(this);

    }


    private void showFragment(){
        KitchenOrderListFragment kitchenOrderListFragment = new KitchenOrderListFragment();
        FragmentsUtil.addFragment(this, kitchenOrderListFragment, R.id.main_container);
    }

    private void showOrderDetails(String orderId){
        KitchenOrderedItemsFragment kitchenOrderedItemsFragment = new KitchenOrderedItemsFragment();

        Bundle purchaseIdArgs = new Bundle();
        purchaseIdArgs.putString("orderId",orderId);
        kitchenOrderedItemsFragment.setArguments(purchaseIdArgs);

        FragmentsUtil.replaceFragment(this,kitchenOrderedItemsFragment,R.id.main_container);
    }

    public void backPress(){
        FragmentsUtil.backPressed(this);
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

    @Override
    public void viewOrderDetails(String orderId) {
        showOrderDetails(orderId);
    }

    public KitchenService getKitchenService() {
        return kitchenService;
    }
}
