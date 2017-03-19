package com.archide.hsb.view.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.archide.hsb.service.KitchenService;
import com.archide.hsb.service.impl.KitchenServiceImpl;
import com.archide.hsb.util.Utilities;
import com.archide.hsb.view.fragments.FragmentsUtil;
import com.archide.hsb.view.fragments.KitchenMenuItemsFragment;
import com.archide.hsb.view.fragments.KitchenOrderListFragment;

import hsb.archide.com.hsb.R;

/**
 * Created by Nithish on 26/11/16.
 */

public class KitchenActivity extends AppCompatActivity implements KitchenOrderListFragment.ViewOrderDetails {

    private KitchenService kitchenService;
    private int mNotificationsCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kitchen);
      //  init();
       // new FetchCountTask().execute();
    }

    private void init() {

        //  getActionBar().setIcon(R.drawable.ic_media_play);
      /*  Account account = HsbSyncAdapter.getSyncAccount(this);
        Bundle settingsBundle = new Bundle();
        settingsBundle.putInt("currentScreen", SyncEvent.GET_KITCHEN_ORDERS_DATA);

        ContentResolver.setIsSyncable(account, this.getString(R.string.content_authority), 1);
        ContentResolver.setSyncAutomatically(account, this.getString(R.string.content_authority), true);

        ContentResolver.addPeriodicSync(account, this.getString(R.string.auth_type), Bundle.EMPTY,30);*/

        kitchenService = new KitchenServiceImpl(this);
    }
    private void showFragment() {
        KitchenOrderListFragment kitchenOrderListFragment = new KitchenOrderListFragment();
        FragmentsUtil.addRemoveFragment(this, kitchenOrderListFragment, R.id.main_container);
    }
    private void showOrderDetails(String orderId) {

        Intent intent = new Intent(this, KitchenDetailsActivity.class);
        intent.putExtra("orderId", orderId);
        startActivity(intent);


       /* KitchenOrderedItemsFragment kitchenOrderedItemsFragment = new KitchenOrderedItemsFragment();

        Bundle purchaseIdArgs = new Bundle();
        purchaseIdArgs.putString("orderId",orderId);
        kitchenOrderedItemsFragment.setArguments(purchaseIdArgs);

        FragmentsUtil.replaceFragment(this,kitchenOrderedItemsFragment,R.id.main_container);*/
    }


    @Override
    protected void onResume() {
//        showFragment();
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
        // writeTextOnDrawable(R.drawable.info,"75");
        MenuItem item = menu.findItem(R.id.notification_view);
        LayerDrawable icon = (LayerDrawable) item.getIcon();
        Utilities.setBadgeCount(this, icon, 8);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (item.getItemId() == R.id.notification_view) {
            return true;
        }
        switch (id) {
            case R.id.action_menu_list:
                Intent intent = new Intent(this, KitchenMenusActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    class FetchCountTask extends AsyncTask<Void, Void, Integer> {

        @Override
        protected Integer doInBackground(Void... params) {
            // example count. This is where you'd
            // query your data store for the actual count.
            return 5;
        }

        @Override
        public void onPostExecute(Integer count) {
            updateNotificationsBadge(count);
        }
    }

    private void updateNotificationsBadge(int count) {
        mNotificationsCount = count;

        // force the ActionBar to relayout its MenuItems.
        // onCreateOptionsMenu(Menu) will be called again.
        invalidateOptionsMenu();
    }

}
