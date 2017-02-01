package com.archide.hsb.view.fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.archide.hsb.sync.json.ResponseData;
import com.archide.hsb.util.Utilities;
import com.archide.hsb.view.activities.ActivityUtil;
import com.archide.hsb.view.activities.HomeActivity;
import com.archide.hsb.view.activities.MainActivity;
import com.archide.hsb.view.activities.OrderActivity;
import com.archide.hsb.view.adapters.OrderedMenuItemsAdapter;
import com.archide.hsb.view.model.MenuItemsViewModel;
import com.archide.hsb.view.model.PlaceAnOrderViewModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import hsb.archide.com.hsb.R;

/**
 * Created by Nithish on 19/11/16.
 */

public class OrderPlaceFragment extends Fragment implements View.OnClickListener{

    LinearLayoutManager linearLayoutManager = null;

    EditText cookingComments ;
    TextView subTotalBeforeDiscount ;
    TextView discount ;
    TextView subTotal ;
    TextView serviceTax ;
    TextView serviceVat ;
    TextView totalAmount ;
    TextView addMoreItems ;
    Button placeAnOrder ;

    RelativeLayout relativeLayout;

    private LayoutInflater mInflater;
    private ViewGroup mContainer;


    private OrderActivity orderActivity;
    private ProgressDialog progressDialog;
    private PlaceAnOrderViewModel placeAnOrderViewModel;
    OrderedMenuItemsAdapter orderedMenuItemsAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private void init(View view){
         cookingComments =  (EditText)view.findViewById(R.id.cooking_comments);
         subTotalBeforeDiscount =  (TextView)view.findViewById(R.id.subtotal_before_discount);
         discount =  (TextView) view.findViewById(R.id.discount);
         subTotal =  (TextView) view.findViewById(R.id.subtotal);
         serviceTax =  (TextView) view.findViewById(R.id.service_tax);
          serviceVat =  (TextView) view.findViewById(R.id.service_vat);
          addMoreItems =  (TextView)view.findViewById(R.id.edit_order);
          totalAmount =  (TextView) view.findViewById(R.id.total_amount);
          placeAnOrder =  (Button) view.findViewById(R.id.place_an_order);
          placeAnOrder.setOnClickListener(this);
         addMoreItems.setOnClickListener(this);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mInflater = inflater;
        mContainer = container;

        placeAnOrderViewModel =  orderActivity.getOrderService().getCurrentOrderDetails();

        orderActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        orderActivity.getSupportActionBar().setHomeButtonEnabled(true);

        if(placeAnOrderViewModel.getMenuItemsViewModels().size() > 0){
            // Inflate the layout for this fragment
            View view =  inflater.inflate(R.layout.fragment_place_order, container, false);



            //order_unavailable_layout
            relativeLayout =  (RelativeLayout)view.findViewById(R.id.order_unavailable_layout);
            RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.ordered_data);
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
            setAdapters(recyclerView);


            init(view);
            checkAvailability();
            return view;
        }else{
            View view =  inflater.inflate(R.layout.fragment_place_order_empty, container, false);
            return view;
        }


    }

    private void setAdapters(RecyclerView recyclerView){
        orderedMenuItemsAdapter = new OrderedMenuItemsAdapter(placeAnOrderViewModel.getMenuItemsViewModels(),orderActivity,OrderPlaceFragment.this);

        recyclerView.setAdapter(orderedMenuItemsAdapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(orderActivity,linearLayoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
    }

    private void loadData(){
        placeAnOrderViewModel =  orderActivity.getOrderService().getCurrentOrderDetails();
        if(placeAnOrderViewModel.isUnAvailable()){
            relativeLayout.setVisibility(View.VISIBLE);
        }
        orderedMenuItemsAdapter.setMenuItemsViewModels(placeAnOrderViewModel.getMenuItemsViewModels());
        orderedMenuItemsAdapter.notifyDataSetChanged();
        populateAmountDetails();
    }

    private void checkAvailability(){
        boolean isNetWorkConnected =  Utilities.isNetworkConnected(orderActivity);
        if(isNetWorkConnected){
            progressDialog = ActivityUtil.showProgress(getString(R.string.get_table_list_heading), getString(R.string.get_table_list_message), orderActivity);
            orderActivity.getOrderService().checkAvailability(orderActivity);
        }else{
            ActivityUtil.showDialog(orderActivity, getString(R.string.no_network_heading), getString(R.string.no_network));
        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        orderActivity = (OrderActivity)context;

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


    public void calcAmountDetails(){
        orderActivity.getOrderService().calcAmount(placeAnOrderViewModel);
        populateAmountDetails();
    }

    public void populateAmountDetails(){
        String text = cookingComments.getText().toString();
        if(text != null && text.trim().length() > 0){
            cookingComments.setText(text);
            cookingComments.setFocusable(false);
            cookingComments.setFocusableInTouchMode(true);
        }else{
            cookingComments.setText(placeAnOrderViewModel.getCookingComments());
            cookingComments.setFocusable(false);
            cookingComments.setFocusableInTouchMode(true);
        }

        subTotalBeforeDiscount.setText(getString(R.string.pound)+" "+String.valueOf(placeAnOrderViewModel.getSubTotalBeforeDiscount()));
        discount.setText(getString(R.string.pound)+" "+String.valueOf(placeAnOrderViewModel.getDiscount()));
        subTotal.setText(getString(R.string.pound)+" "+String.valueOf(placeAnOrderViewModel.getSubTotal()));
        serviceTax.setText(getString(R.string.pound)+" "+String.valueOf(placeAnOrderViewModel.getServiceTax()));
        serviceVat.setText(getString(R.string.pound)+" "+String.valueOf(placeAnOrderViewModel.getServiceVat()));
        totalAmount.setText(getString(R.string.pound)+" "+String.valueOf(placeAnOrderViewModel.getTotalAmount()));

    }

    public void addMoreOrders(){
        orderActivity.getOrderService().removeUnAvailableOrders();
        Intent intent = new Intent(orderActivity, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        orderActivity.finish();
        return;
    }

    @Override
    public void onClick(View view) {
       if(view.getId() == R.id.edit_order){
           addMoreOrders();
       }else{
           if(placeAnOrderViewModel.getTotalAmount() > 0){
               boolean isNetWorkConnected =  Utilities.isNetworkConnected(orderActivity);
               if(isNetWorkConnected){
                   placeAnOrderViewModel.setCookingComments(cookingComments.getText().toString());
                   progressDialog = ActivityUtil.showProgress(getString(R.string.get_table_list_heading), getString(R.string.get_table_list_message), orderActivity);
                   orderActivity.getOrderService().conformOrder(placeAnOrderViewModel,ActivityUtil.USER_MOBILE,ActivityUtil.TABLE_NUMBER,orderActivity);
               }else{
                   ActivityUtil.showDialog(orderActivity, getString(R.string.no_network_heading), getString(R.string.no_network));
               }
           }else{
               showNoItemToPlaceOrder();
           }


       }


    }

    private void showNoItemToPlaceOrder(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(orderActivity);
        // Setting Dialog Title
        alertDialog.setTitle("Sorry");

        // Setting Dialog Message
        alertDialog.setMessage("No items to order. Please add other items");

        // Setting Positive "Yes" Button
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                dialog.cancel();
                addMoreOrders();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleServerSyncResponse(ResponseData responseData) {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
        if(responseData.getStatusCode() == 2000){
            loadData();
        }else if(responseData.getStatusCode() == 200){
            Toast.makeText(orderActivity,getString(R.string.order_conformation),Toast.LENGTH_LONG).show();
            Intent intent = new Intent(orderActivity, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            orderActivity.finish();
            return;
        }else{
            Toast.makeText(orderActivity,getString(R.string.internal_error),Toast.LENGTH_LONG).show();
            Intent intent = new Intent(orderActivity, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            orderActivity.finish();
            return;
        }

    }


    public void showNoData(){
        View newView = mInflater.inflate(R.layout.fragment_place_order_empty, mContainer, false);
        mContainer.removeAllViews();
        mContainer.addView(newView);
    }
}
