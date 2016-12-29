package com.archide.hsb.view.activities;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.archide.hsb.service.OrderService;
import com.archide.hsb.service.impl.OrderServiceImpl;
import com.archide.hsb.view.fragments.BillingFragment;
import com.archide.hsb.view.fragments.FragmentsUtil;
import com.archide.hsb.view.fragments.KitchenOrderListFragment;
import com.archide.hsb.view.fragments.OrderPlaceFragment;
import com.archide.hsb.view.fragments.PlacedOrderHistoryFragment;

import hsb.archide.com.hsb.R;


/**
 * Created by Nithishkumar on 3/27/2016.
 */
public class NaviDrawerActivity extends AppCompatActivity {

    private OrderService orderService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navi_drawer);
        init();
        int options = getIntent().getIntExtra("options",0);
        showFragment(options);
    }

    private void init(){
        try{
            orderService = new OrderServiceImpl(this);
        }catch (Exception e){
            Log.e("Error","Error in init - NaviDrawerActivity",e);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void showFragment(int options){
        switch (options){
            case 1:
                Intent intent = new Intent(this, OrderActivity.class);
                startActivity(intent);
                finish();
                break;

            case 2:
                PlacedOrderHistoryFragment placedOrderHistoryFragment = new PlacedOrderHistoryFragment();
                FragmentsUtil.addFragment(this, placedOrderHistoryFragment, R.id.navi_drawer_container);
              //  KitchenOrderListFragment kitchenOrderListFragment = new KitchenOrderListFragment();
               // FragmentsUtil.addFragment(this, kitchenOrderListFragment, R.id.navi_drawer_container);
                break;

            case 3:
                BillingFragment billingFragment = new BillingFragment();
                FragmentsUtil.addFragment(this, billingFragment, R.id.navi_drawer_container);
                break;

            case 5:
                showPlayStore();
                break;


        }
    }


    private void showPlayStore(){
        Uri uri = Uri.parse("market://details?id=" + this.getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        // To count with Play market backstack, After pressing back button,
        // to taken back to our application, we need to add following flags to intent.
       /* goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                Intent.FLAG_ACTIVITY_NEW_DOCUMENT  |
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);*/
        try {
            startActivity(goToMarket);
            finish();
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + this.getPackageName())));
            finish();
        }
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
    public void onBackPressed() {
        super.onBackPressed();
    }





    public OrderService getOrderService() {
        return orderService;
    }



}
