package com.archide.hsb.view.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.archide.hsb.view.model.CloseOrderViewModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import hsb.archide.com.hsb.R;

/**
 * Created by Nithish on 23/11/16.
 */

public class BillingFragment extends Fragment implements View.OnClickListener {


    TextView vOrderId = null;
    TextView vTableNumber = null;
    TextView vTotalAmount = null;
    TextView vUserMobileNumber = null;
    ImageView vOrderSyncStatus = null;

    TextView vOrderSyncMsg = null;
    Button vOrderLogOut = null;
    Button vOrderSent = null;

    private ProgressDialog progressDialog;
    private NaviDrawerActivity naviDrawerActivity;

    private LayoutInflater mInflater;
    private ViewGroup mContainer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private void init(View view){
        vOrderId =  (TextView) view.findViewById(R.id.close_order_order_id);
        vTableNumber =  (TextView) view.findViewById(R.id.close_order_table_number);
        vTotalAmount =  (TextView) view.findViewById(R.id.close_order_total_amount);
        vUserMobileNumber =  (TextView) view.findViewById(R.id.close_order_mobile_number);

        vOrderSyncStatus =  (ImageView) view.findViewById(R.id.close_order_sync_status);
        vOrderSyncMsg =  (TextView) view.findViewById(R.id.close_order_sync_msg);
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

        naviDrawerActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        naviDrawerActivity.getSupportActionBar().setHomeButtonEnabled(false);


        init(view);

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
        vOrderSyncStatus.setImageDrawable(mDrawable);
        vOrderLogOut.setVisibility(View.GONE);
        vOrderSent.setVisibility(View.VISIBLE);
    }


    private void showSuccess(){
        Drawable mDrawable = naviDrawerActivity.getResources().getDrawable(R.drawable.ic_error_white_48dp);
        vOrderSyncStatus.setImageDrawable(mDrawable);

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
        vTotalAmount.setText(closeOrderViewModel.getTotalAmount());
        vUserMobileNumber.setText(closeOrderViewModel.getUserMobileNumber());

    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.close_order_logout){
            naviDrawerActivity.getOrderService().removeAllData();
            Intent intent = new Intent(naviDrawerActivity, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            naviDrawerActivity.finish();
             // LogOut
        }else if(view.getId() == R.id.close_order_send){
            reSentBilling();
            // Resent
        }
    }

    private void showNoData(){
        View newView = mInflater.inflate(R.layout.fragment_order_close_empty, mContainer, false);
        mContainer.removeAllViews();
        mContainer.addView(newView);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleServerSyncResponse(ResponseData responseData) {
        if(progressDialog != null){
            progressDialog.dismiss();
        }
        switch (responseData.getStatusCode()){
            case 200:
                naviDrawerActivity.getOrderService().closeOrder(ActivityUtil.USER_MOBILE);
                populateData();
                naviDrawerActivity.setLogOut(true);
                break;
            case 405:
                populateData();
                setError();
                break;
            case 400:
                populateData();
                setError();
                break;

            case 403:
                showNoData();
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


}
