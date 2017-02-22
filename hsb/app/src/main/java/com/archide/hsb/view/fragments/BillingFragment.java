package com.archide.hsb.view.fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.archide.hsb.sync.json.ResponseData;
import com.archide.hsb.util.Utilities;
import com.archide.hsb.view.activities.ActivityUtil;
import com.archide.hsb.view.activities.HomeActivity;
import com.archide.hsb.view.activities.MainActivity;
import com.archide.hsb.view.activities.NaviDrawerActivity;
import com.archide.hsb.view.adapters.PlacedOrderHisMenuItemsAdapter;
import com.archide.hsb.view.model.CloseOrderViewModel;
import com.archide.hsb.view.model.MenuItemsViewModel;
import com.archide.hsb.view.model.PlaceAnOrderViewModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import hsb.archide.com.hsb.R;

/**
 * Created by Nithish on 23/11/16.
 */

public class BillingFragment extends Fragment implements View.OnClickListener {


    TextView vOrderId = null;
    TextView vTableNumber = null;
    TextView vTotalAmount = null;
    TextView vUserMobileNumber = null;
    //ImageView vOrderSyncStatus = null;

   // TextView vOrderSyncMsg = null;
    Button vOrderLogOut = null;
    Button vOrderSent = null;

    private ProgressDialog progressDialog;
    private NaviDrawerActivity naviDrawerActivity;

    private LayoutInflater mInflater;
    private ViewGroup mContainer;

    TextView subTotalBeforeDiscount ;
    TextView discount ;
    TextView subTotal ;
    TextView serviceTax ;
    TextView totalAmount ;


    LinearLayoutManager linearLayoutManager = null;
    PlacedOrderHisMenuItemsAdapter orderedMenuItemsAdapter;
    List<MenuItemsViewModel> menuItemsViewModels = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private void init(View view){
        vOrderId =  (TextView) view.findViewById(R.id.close_order_order_id);
        vTableNumber =  (TextView) view.findViewById(R.id.close_order_table_number);
        vTotalAmount =  (TextView) view.findViewById(R.id.close_order_total_amount);
        vUserMobileNumber =  (TextView) view.findViewById(R.id.close_order_mobile_number);

        subTotalBeforeDiscount =  (TextView)view.findViewById(R.id.odr_his_subtotal_before_discount);
        discount =  (TextView) view.findViewById(R.id.odr_his_discount);
        subTotal =  (TextView) view.findViewById(R.id.odr_his_subtotal);
        serviceTax =  (TextView) view.findViewById(R.id.odr_his_service_tax);
        totalAmount =  (TextView) view.findViewById(R.id.odr_his_total_amount);

       // vOrderSyncStatus =  (ImageView) view.findViewById(R.id.close_order_sync_status);
        //vOrderSyncMsg =  (TextView) view.findViewById(R.id.close_order_sync_msg);
        vOrderLogOut =  (Button) view.findViewById(R.id.close_order_logout);
        vOrderSent =  (Button) view.findViewById(R.id.close_order_send);

        vOrderLogOut.setOnClickListener(this);
        vOrderSent.setOnClickListener(this);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_order_close, container, false);

        mInflater = inflater;
        mContainer = container;
        naviDrawerActivity.setLogOut(true);
        naviDrawerActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        naviDrawerActivity.getSupportActionBar().setHomeButtonEnabled(false);


        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.odr_his_order_data);
        recyclerView.setHasFixedSize(true);
        linearLayoutManager =  new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });

        init(view);
        setAdapters(recyclerView);
        getDataFromServer();
        return view;
    }



    private void getDataFromServer(){
        boolean isNetWorkConnected = Utilities.isNetworkConnected(naviDrawerActivity);
        if (isNetWorkConnected) {
            progressDialog = ActivityUtil.showProgress(getString(R.string.get_table_list_heading), getString(R.string.get_table_list_message), naviDrawerActivity);
            naviDrawerActivity.getOrderService().closeAnOrder(naviDrawerActivity,ActivityUtil.TABLE_NUMBER,ActivityUtil.USER_MOBILE);
        } else {
            ActivityUtil.showDialog(naviDrawerActivity, getString(R.string.no_network_heading), getString(R.string.no_network));
        }

    }

    private void setError(){
        Drawable mDrawable = naviDrawerActivity.getResources().getDrawable(R.drawable.ic_error_white_48dp);
        mDrawable.setColorFilter(new
                PorterDuffColorFilter(Color.parseColor("#B71c1c"), PorterDuff.Mode.MULTIPLY));
        //vOrderSyncStatus.setImageDrawable(mDrawable);
        vOrderLogOut.setVisibility(View.GONE);
        vOrderSent.setVisibility(View.VISIBLE);
    }


    private void showSuccess(){
        Drawable mDrawable = naviDrawerActivity.getResources().getDrawable(R.drawable.ic_error_white_48dp);
        //vOrderSyncStatus.setImageDrawable(mDrawable);

        vOrderLogOut.setVisibility(View.VISIBLE);
        vOrderSent.setVisibility(View.GONE);
    }


    private void reSentBilling(){
        boolean isNetWorkConnected = Utilities.isNetworkConnected(naviDrawerActivity);
        if (isNetWorkConnected) {
            progressDialog = ActivityUtil.showProgress(getString(R.string.get_table_list_heading), getString(R.string.get_table_list_message), naviDrawerActivity);
            naviDrawerActivity.getOrderService().resentBilling(naviDrawerActivity,ActivityUtil.TABLE_NUMBER,ActivityUtil.USER_MOBILE);
        } else {
            ActivityUtil.showDialog(naviDrawerActivity, getString(R.string.no_network_heading), getString(R.string.no_network));
        }
    }





    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        naviDrawerActivity = (NaviDrawerActivity)context;

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }


    private void populateData(){
        CloseOrderViewModel closeOrderViewModel =  naviDrawerActivity.getOrderService().getBillingDetails();
        vOrderId.setText(closeOrderViewModel.getOrderId());
        vTableNumber.setText(closeOrderViewModel.getTableNumber());
        vTotalAmount.setText(getString(R.string.pound)+" "+closeOrderViewModel.getTotalAmount());
        vUserMobileNumber.setText(closeOrderViewModel.getUserMobileNumber());

    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.close_order_logout){
            naviDrawerActivity.getOrderService().logout(naviDrawerActivity,ActivityUtil.TABLE_NUMBER,ActivityUtil.USER_MOBILE);

            naviDrawerActivity.getOrderService().removeAllData();
            Intent intent = new Intent(naviDrawerActivity, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            naviDrawerActivity.finish();
             // LogOut
        }else if(view.getId() == R.id.close_order_send){
            reSentBilling();
            // Resent
        }else if(R.id.not_closable == view.getId()){
            naviDrawerActivity.onBackPressed();
        }else if(R.id.place_an_order == view.getId()){
            naviDrawerActivity.onBackPressed();
        }
        //place_an_order
    }

    private void showNoData(){
        naviDrawerActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        naviDrawerActivity.getSupportActionBar().setHomeButtonEnabled(true);
        naviDrawerActivity.setLogOut(false);
        View newView = mInflater.inflate(R.layout.fragment_place_order_empty, mContainer, false);
        mContainer.removeAllViews();
        mContainer.addView(newView);
        ImageView imageView =  (ImageView)newView.findViewById(R.id.place_an_order);
        imageView.setOnClickListener(this);
    }

    private void showNotCloseData(){
        naviDrawerActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        naviDrawerActivity.getSupportActionBar().setHomeButtonEnabled(true);
        naviDrawerActivity.setLogOut(false);
        View newView = mInflater.inflate(R.layout.fragment_order_close_empty, mContainer, false);
        mContainer.removeAllViews();
        mContainer.addView(newView);
        ImageView imageView =  (ImageView)newView.findViewById(R.id.not_closable);
        imageView.setOnClickListener(this);
        //unable_close_order
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleServerSyncResponse(ResponseData responseData) {
        if(progressDialog != null){
            progressDialog.dismiss();
        }
        switch (responseData.getStatusCode()){
            case 200:
            case 405:
            case 400:
                naviDrawerActivity.getOrderService().closeOrder(ActivityUtil.USER_MOBILE);
                populateData();
                populateOrderData();
                naviDrawerActivity.setLogOut(true);
                break;
          /*  case 405:
                populateData();
                setError();
                break;
            case 400:
                populateData();
                setError();
                break;*/

            case 403:
                showNotCloseData();
                break;

            case 404:
                showNoData();
                break;

            case 600:
                showSuccess();
                break;

            case 601:
                setError();
                break;

            case 602:
                setError();
                break;

            case 500:
                ActivityUtil.showDialog(naviDrawerActivity,"Error","Sorry for the Inconvenience. Please contact Admin.");
                break;

            default:
                Intent intent = new Intent(naviDrawerActivity, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                naviDrawerActivity.finish();
                break;
        }


    }


    private void setAdapters(RecyclerView recyclerView){
        orderedMenuItemsAdapter = new PlacedOrderHisMenuItemsAdapter(menuItemsViewModels,naviDrawerActivity);
        recyclerView.setAdapter(orderedMenuItemsAdapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(naviDrawerActivity,linearLayoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
    }

    private void populateOrderData(){
        PlaceAnOrderViewModel placeAnOrderViewModel =  naviDrawerActivity.getOrderService().getPlacedHistoryOrderViewModel();
        menuItemsViewModels.clear();
        menuItemsViewModels.addAll(placeAnOrderViewModel.getMenuItemsViewModels());
        orderedMenuItemsAdapter.notifyDataSetChanged();


        vOrderId.setText("Order Id:"+String.valueOf(placeAnOrderViewModel.getOrderId()));
        subTotalBeforeDiscount.setText(getString(R.string.pound)+" "+String.valueOf(placeAnOrderViewModel.getSubTotalBeforeDiscount()));
        discount.setText(getString(R.string.pound)+" "+String.valueOf(placeAnOrderViewModel.getDiscount()));
        subTotal.setText(getString(R.string.pound)+" "+String.valueOf(placeAnOrderViewModel.getSubTotal()));
        totalAmount.setText(getString(R.string.pound)+" "+String.valueOf(placeAnOrderViewModel.getTotalAmount()));
    }




}
