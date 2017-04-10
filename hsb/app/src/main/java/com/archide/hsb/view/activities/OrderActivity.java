package com.archide.hsb.view.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.archide.hsb.entity.ConfigurationEntity;
import com.archide.hsb.entity.UsersEntity;
import com.archide.hsb.enumeration.AppType;
import com.archide.hsb.enumeration.OrderType;
import com.archide.hsb.service.OrderService;
import com.archide.hsb.service.impl.OrderServiceImpl;
import com.archide.hsb.sync.json.ResponseData;
import com.archide.hsb.util.Utilities;
import com.archide.hsb.view.fragments.DataFromServer;
import com.archide.hsb.view.fragments.FragmentsUtil;
import com.archide.hsb.view.fragments.OrderConformationTakeAwayFragment;
import com.archide.hsb.view.fragments.OrderConformationUserFragment;
import com.archide.hsb.view.fragments.OrderPlaceFragment;
import com.archide.hsb.view.fragments.PlacedOrderEmptyFragment;
import com.archide.hsb.view.model.PlaceAnOrderViewModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import hsb.archide.com.hsb.R;

public class OrderActivity extends AppCompatActivity {

    private OrderService orderService;
    OrderPlaceFragment orderPlaceFragment;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        init();
        showFragment();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    private void init(){
        orderService = new OrderServiceImpl(this);
    }


    private void showFragment(){
        PlaceAnOrderViewModel placeAnOrderViewModel =  orderService.getCurrentOrderDetails();
        if(placeAnOrderViewModel.getMenuItemsViewModels().size() > 0){
            showGetDataFromServerFragment();
        }else{
            showPlacedOrderEmptyFragment();
        }


    }


    private void showPlacedOrderEmptyFragment(){
        PlacedOrderEmptyFragment placedOrderEmptyFragment = new PlacedOrderEmptyFragment();
        FragmentsUtil.addFragment(this, placedOrderEmptyFragment,R.id.navi_drawer_container);
    }


    public void showNoPlacedOrderFragment(){
        PlacedOrderEmptyFragment placedOrderEmptyFragment = new PlacedOrderEmptyFragment();
        FragmentsUtil.replaceFragmentNoStack(this, placedOrderEmptyFragment,R.id.main_container);
    }

    private void showOrderPlaceFragment(){
        orderPlaceFragment = new OrderPlaceFragment();
        FragmentsUtil.replaceFragmentNoStack(this, orderPlaceFragment, R.id.main_container);
    }

    private void showOrderConformationFragment(int statusCode){
        final int SPLASH_TIME_OUT = 10000;
        UsersEntity usersEntity = getOrderService().getUsersEntity();
        if(usersEntity.getOrderType().toString().equals(OrderType.Dinning.toString())){

            OrderConformationUserFragment orderConformationUserFragment = new OrderConformationUserFragment();
            FragmentsUtil.replaceFragmentNoStack(this, orderConformationUserFragment, R.id.main_container);
        }else{

            OrderConformationTakeAwayFragment orderConformationTakeAwayFragment = new OrderConformationTakeAwayFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("statusCode", statusCode);
            orderConformationTakeAwayFragment.setArguments(bundle);

            FragmentsUtil.replaceFragmentNoStack(this, orderConformationTakeAwayFragment, R.id.main_container);
        }


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                orderService.removeAllData();
                showWelcomeScreen();
            }
        }, SPLASH_TIME_OUT);

    }


    private void showGetDataFromServerFragment(){
        DataFromServer dataFromServer = new DataFromServer();
        FragmentsUtil.addFragment(this, dataFromServer, R.id.main_container);
        boolean isNetWorkConnected = Utilities.isNetworkConnected(this);
        if (isNetWorkConnected) {
            progressDialog = ActivityUtil.showProgress(getString(R.string.get_table_list_heading), getString(R.string.check_availability), this);
            orderService.checkAvailability(this);
        }else {
            ActivityUtil.showDialog(this, getString(R.string.no_network_heading), getString(R.string.no_network));
        }
    }

    public void placeAnOrder(View view){
        onClick(view);
    }

    public void onClick(View view){
    switch (view.getId()){
        case R.id.edit_order:
        case R.id.place_an_order:
            orderService.removeUnAvailableOrders();
            Intent intent = new Intent(this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
            return;

        case R.id.place_an_order_submit:
            boolean isNetWorkConnected =  Utilities.isNetworkConnected(this);
            if(isNetWorkConnected){
                progressDialog = ActivityUtil.showProgress(getString(R.string.get_table_list_heading), getString(R.string.order_placing), this);
                String cookingComments = orderPlaceFragment.getCookingCommentsValue();
                orderService.conformOrder(cookingComments,this);
            }else{
                ActivityUtil.showDialog(this, getString(R.string.no_network_heading), getString(R.string.no_network));
            }
            break;
    }
    }

    private void showWelcomeScreen(){
        orderService.removeAllData();
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }



    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleServerSyncResponse(ResponseData responseData) {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
        switch (responseData.getStatusCode()){
            case 2000:
            case 405:
                showOrderPlaceFragment();
                break;

            case 200:
            case 201:
            case 400:
                showOrderConformationFragment(responseData.getStatusCode());
                return;


            default:
                //
                /*speech(orderActivity.getString(R.string.internal_server_error_voice));
                Toast.makeText(orderActivity,getString(R.string.internal_error),Toast.LENGTH_LONG).show();
                intent = new Intent(orderActivity, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                orderActivity.finish();*/
                return;
        }

    }

    @Override
    public void onBackPressed() {
        Fragment fragment =  ActivityUtil.getCurrentFragment(this,R.id.main_container);
        if(fragment instanceof OrderConformationUserFragment || fragment instanceof OrderConformationTakeAwayFragment){
            removeComments();
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }else{
            orderPlaceFragment.setCookingComments();
            Intent intent = new Intent(this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }


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

    private void removeComments(){
        String key =  "cookingComments";
        SharedPreferences sharedpreferences = this.getSharedPreferences("mobilepayhsb",
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.remove(key);
        editor.commit();
    }




    public OrderService getOrderService() {
        return orderService;
    }
}
