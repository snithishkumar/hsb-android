package com.archide.hsb.view.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.archide.hsb.service.OrderService;
import com.archide.hsb.service.impl.OrderServiceImpl;
import com.archide.hsb.sync.json.ResponseData;
import com.archide.hsb.util.Utilities;
import com.archide.hsb.view.fragments.AboutAsFragment;
import com.archide.hsb.view.fragments.BillingFragment;
import com.archide.hsb.view.fragments.DataFromServer;
import com.archide.hsb.view.fragments.FragmentsUtil;
import com.archide.hsb.view.fragments.OrderCloseEmptyFragment;
import com.archide.hsb.view.fragments.PlacedOrderEmptyFragment;
import com.archide.hsb.view.fragments.PlacedOrderHistoryFragment;
import com.archide.hsb.view.model.PlaceAnOrderViewModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import hsb.archide.com.hsb.R;


/**
 * Created by Nithishkumar on 3/27/2016.
 */
public class NaviDrawerActivity extends AppCompatActivity{

    private OrderService orderService;

    private boolean isLogOut;
    private ProgressDialog progressDialog;

    PlacedOrderHistoryFragment placedOrderHistoryFragment;

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



    private void showFragment(int options){
        switch (options){
            case 1:
                Intent intent = new Intent(this, OrderActivity.class);
                startActivity(intent);
                finish();
                break;

            case 2:
                showDataFromServerFragment(options);
                break;

            case 3:
                orderCloseConformation(options);

                break;

            case 4:
                AboutAsFragment aboutAsFragment = new AboutAsFragment();
                FragmentsUtil.addFragment(this, aboutAsFragment, R.id.navi_drawer_container);
                break;

            case 5:
                orderService.removeAllData();
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
            finish();
        }
    }

    private void clearData(){
        getOrderService().removeAllData();
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }


    private void orderCloseConformation(final int options){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        // Setting Dialog Title
        alertDialog.setTitle("Conform");

        // Setting Dialog Message
        alertDialog.setMessage("Are you sure want to close an order?");

        // Setting Positive "Yes" Button
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                dialog.cancel();
                showDataFromServerFragment(options);

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


    @Override
    public void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onPause() {
        EventBus.getDefault().unregister(this);
        super.onPause();
    }


    private void showDataFromServerFragment(int options){
        DataFromServer dataFromServer = new DataFromServer();
        FragmentsUtil.addRemoveFragment(this, dataFromServer,R.id.navi_drawer_container);

        boolean isNetWorkConnected = Utilities.isNetworkConnected(this);
        if (isNetWorkConnected) {
            progressDialog = ActivityUtil.showProgress(getString(R.string.get_table_list_heading), getString(R.string.get_previous_update), this);
            if(options == 2){
                orderService.getPreviousOrderFromServer(this);
            }else{
                orderService.closeAnOrder(this);
            }

        } else {
            ActivityUtil.showDialog(this, getString(R.string.no_network_heading), getString(R.string.no_network));
        }

    }

     /* History */
    private void processOrderHistory(){
        PlaceAnOrderViewModel placeAnOrderViewModel =  orderService.getPlacedHistoryOrderViewModel();
        if(placeAnOrderViewModel.getMenuItemsViewModels().size() < 1){
            showPlacedOrderEmptyFragment();
        }else{
            showPlacedOrderHistoryFragment();
        }
    }


    private void showPlacedOrderEmptyFragment(){
        PlacedOrderEmptyFragment placedOrderEmptyFragment = new PlacedOrderEmptyFragment();
        FragmentsUtil.replaceFragmentNoStack(this, placedOrderEmptyFragment,R.id.navi_drawer_container);
    }


    private void showPlacedOrderHistoryFragment(){
        placedOrderHistoryFragment = new PlacedOrderHistoryFragment();
        FragmentsUtil.replaceFragmentNoStack(this, placedOrderHistoryFragment,R.id.navi_drawer_container);
    }
     /* History End */


    private void showOrderCloseEmptyFragment(){
        OrderCloseEmptyFragment orderCloseEmptyFragment = new OrderCloseEmptyFragment();
        FragmentsUtil.replaceFragmentNoStack(this, orderCloseEmptyFragment,R.id.navi_drawer_container);
    }


    private void showBillingFragment(){
        BillingFragment billingFragment = new BillingFragment();
        FragmentsUtil.replaceFragmentNoStack(this, billingFragment,R.id.navi_drawer_container);
    }



    private void showMainFragment(){
        Intent intent = new Intent(this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }


    private void removeFragment(){
        Fragment fragment =  FragmentsUtil.getCurrentFragment(this,R.id.navi_drawer_container);
        if(fragment instanceof PlacedOrderHistoryFragment){
            placedOrderHistoryFragment.removeProgressDialog();
        }

    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleServerSyncResponse(ResponseData responseData) {

        if(progressDialog != null){
            progressDialog.dismiss();
        }
       switch (responseData.getStatusCode()){
           case 3000:
               processOrderHistory();
               break;
           case 200:
               placedOrderHistoryFragment.removeProgressDialog();
               showMainFragment();
               break;
           case 500:
               removeFragment();
               ActivityUtil.showDialog(this,"Error","Sorry for the Inconvenience. Please contact Admin.");
               break;

           case 400:
           case 405:
               showBillingFragment();
               break;
           case 403:
               showOrderCloseEmptyFragment();
               break;

           case 404:
               showPlacedOrderEmptyFragment();
               break;

       }

    }

    public void placeAnOrder(View view){
        finish();
    }



}
