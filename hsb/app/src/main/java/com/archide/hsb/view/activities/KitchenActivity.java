package com.archide.hsb.view.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.archide.hsb.service.KitchenService;
import com.archide.hsb.service.impl.KitchenServiceImpl;
import com.archide.hsb.view.fragments.FragmentsUtil;
import com.archide.hsb.view.fragments.KitchenOrderListFragment;

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

    }

    private void init(){

      /*  Account account = HsbSyncAdapter.getSyncAccount(this);
        Bundle settingsBundle = new Bundle();
        settingsBundle.putInt("currentScreen", SyncEvent.GET_KITCHEN_ORDERS_DATA);

        ContentResolver.setIsSyncable(account, this.getString(R.string.content_authority), 1);
        ContentResolver.setSyncAutomatically(account, this.getString(R.string.content_authority), true);

        ContentResolver.addPeriodicSync(account, this.getString(R.string.auth_type), Bundle.EMPTY,30);*/

            kitchenService = new KitchenServiceImpl(this);

    }


    private void showFragment(){
        KitchenOrderListFragment kitchenOrderListFragment = new KitchenOrderListFragment();
        FragmentsUtil.addRemoveFragment(this,kitchenOrderListFragment, R.id.main_container);
       // FragmentsUtil.addFragment(this, kitchenOrderListFragment, R.id.main_container);
    }

    private void showOrderDetails(String orderId){

        Intent intent = new Intent(this, KitchenDetailsActivity.class);
        intent.putExtra("orderId",orderId);
        startActivity(intent);


       /* KitchenOrderedItemsFragment kitchenOrderedItemsFragment = new KitchenOrderedItemsFragment();

        Bundle purchaseIdArgs = new Bundle();
        purchaseIdArgs.putString("orderId",orderId);
        kitchenOrderedItemsFragment.setArguments(purchaseIdArgs);

        FragmentsUtil.replaceFragment(this,kitchenOrderedItemsFragment,R.id.main_container);*/
    }




    @Override
    protected void onResume() {
        showFragment();
        super.onResume();
    }

    @Override
    public void viewOrderDetails(String orderId) {
        showOrderDetails(orderId);
    }

    public KitchenService getKitchenService() {
        return kitchenService;
    }
}
