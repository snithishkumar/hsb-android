package com.archide.hsb.view.activities;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
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
import com.archide.hsb.view.fragments.OrderModuleLoginFragment;
import com.archide.hsb.view.fragments.OrderPlaceFragment;
import com.archide.hsb.view.fragments.PlacedOrderHistoryFragment;
import com.archide.hsb.view.fragments.TableChangeFragment;

import hsb.archide.com.hsb.R;


/**
 * Created by Nithishkumar on 3/27/2016.
 */
public class NaviDrawerActivity extends AppCompatActivity{

    private OrderService orderService;

    private boolean isLogOut;

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

            case 4:

                break;

            case 5:
                orderService.removeAllData();
                orderService.closeOrder(ActivityUtil.USER_MOBILE);
                intent = new Intent(this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
                break;


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
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {

        if(isLogOut){
            logOut();
        }else{
            super.onBackPressed();
        }
    }

    private void clearData(){
        getOrderService().removeAllData();
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }


    private void logOut(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        // Setting Dialog Title
        alertDialog.setTitle("Conform");

        // Setting Dialog Message
        alertDialog.setMessage("Are you sure want to logout?");

        // Setting Positive "Yes" Button
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                dialog.cancel();
                clearData();

            }
        });

        // Setting Positive "Yes" Button
        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

   

    public void setLogOut(boolean logOut) {
        isLogOut = logOut;
    }

    public OrderService getOrderService() {
        return orderService;
    }



}
