package com.archide.hsb.view.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.archide.hsb.service.KitchenService;
import com.archide.hsb.service.impl.KitchenServiceImpl;
import com.archide.hsb.view.fragments.FragmentsUtil;
import com.archide.hsb.view.fragments.KitchenMenuItemsFragment;
import com.archide.hsb.view.fragments.KitchenOrderListFragment;
import com.mikepenz.actionitembadge.library.ActionItemBadge;

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

    }

    private void init(){

      //  getActionBar().setIcon(R.drawable.ic_media_play);
      /*  Account account = HsbSyncAdapter.getSyncAccount(this);
        Bundle settingsBundle = new Bundle();
        settingsBundle.putInt("currentScreen", SyncEvent.GET_KITCHEN_ORDERS_DATA);

        ContentResolver.setIsSyncable(account, this.getString(R.string.content_authority), 1);
        ContentResolver.setSyncAutomatically(account, this.getString(R.string.content_authority), true);

        ContentResolver.addPeriodicSync(account, this.getString(R.string.auth_type), Bundle.EMPTY,30);*/

      if(ActivityCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
          ActivityCompat.requestPermissions(this,
                  new String[]{Manifest.permission
                          .WRITE_EXTERNAL_STORAGE},
                  1);
      }

            kitchenService = new KitchenServiceImpl(this);

    }


    private void showFragment(){

         KitchenOrderListFragment kitchenOrderListFragment = new KitchenOrderListFragment();
        FragmentsUtil.addRemoveFragment(this,kitchenOrderListFragment, R.id.main_container);
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.kitchen_menu_list_action, menu);
        //  app:actionLayout="@layout/menu_action_item_badge"
      //  ActionItemBadge.update(this, menu.findItem(R.id.action_menu_list),getDrawable(R.drawable.ic_menu_white_48dp) , ActionItemBadge.BadgeStyles.BLUE, 0);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_menu_list:
                Intent intent = new Intent(this, KitchenMenusActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
