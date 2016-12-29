package com.archide.hsb.view.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.archide.hsb.service.TableListService;
import com.archide.hsb.service.impl.TableListServiceImpl;
import com.archide.hsb.sync.json.ResponseData;
import com.archide.hsb.util.Utilities;
import com.archide.hsb.view.activities.ActivityUtil;
import com.archide.hsb.view.activities.HomeActivity;
import com.archide.hsb.view.activities.NaviDrawerActivity;
import com.archide.hsb.view.adapters.PlacedOrderHisMenuItemsAdapter;
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

public class PlacedOrderHistoryFragment extends Fragment implements View.OnClickListener {

    LinearLayoutManager linearLayoutManager = null;

    EditText cookingComments ;
    TextView subTotalBeforeDiscount ;
    TextView discount ;
    TextView subTotal ;
    TextView serviceTax ;
    TextView totalAmount ;
    FloatingActionButton placeAnOrder ;


    private ProgressDialog progressDialog;
    private NaviDrawerActivity naviDrawerActivity;
    PlacedOrderHisMenuItemsAdapter orderedMenuItemsAdapter;
    List<MenuItemsViewModel> menuItemsViewModels = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private void init(View view){
        subTotalBeforeDiscount =  (TextView)view.findViewById(R.id.odr_his_subtotal_before_discount);
        discount =  (TextView) view.findViewById(R.id.odr_his_discount);
        subTotal =  (TextView) view.findViewById(R.id.odr_his_subtotal);
        serviceTax =  (TextView) view.findViewById(R.id.odr_his_service_tax);
        totalAmount =  (TextView) view.findViewById(R.id.odr_his_total_amount);
        placeAnOrder =  (FloatingActionButton) view.findViewById(R.id.order_history_add_menu_items);
        placeAnOrder.setOnClickListener(this);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_placed_orders_history, container, false);

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

        naviDrawerActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        naviDrawerActivity.getSupportActionBar().setHomeButtonEnabled(true);



        setAdapters(recyclerView);
        getDataFromServer();
        return view;
    }

    private void getDataFromServer(){
        boolean isNetWorkConnected = Utilities.isNetworkConnected(naviDrawerActivity);
        if (isNetWorkConnected) {
            progressDialog = ActivityUtil.showProgress(getString(R.string.get_table_list_heading), getString(R.string.get_table_list_message), naviDrawerActivity);
            naviDrawerActivity.getOrderService().getPreviousOrderFromServer(naviDrawerActivity,ActivityUtil.TABLE_NUMBER,ActivityUtil.USER_MOBILE);
        } else {
            ActivityUtil.showDialog(naviDrawerActivity, getString(R.string.no_network_heading), getString(R.string.no_network));
        }

    }

    private void setAdapters(RecyclerView recyclerView){
        orderedMenuItemsAdapter = new PlacedOrderHisMenuItemsAdapter(menuItemsViewModels,naviDrawerActivity);
        recyclerView.setAdapter(orderedMenuItemsAdapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(naviDrawerActivity,linearLayoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
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
        PlaceAnOrderViewModel placeAnOrderViewModel =  naviDrawerActivity.getOrderService().getPlacedHistoryOrderViewModel();
        menuItemsViewModels.clear();
        menuItemsViewModels.addAll(placeAnOrderViewModel.getMenuItemsViewModels());
        orderedMenuItemsAdapter.notifyDataSetChanged();
        populateAmountDetails(placeAnOrderViewModel);
    }

    public void populateAmountDetails(PlaceAnOrderViewModel placeAnOrderViewModel){
       if(placeAnOrderViewModel.getCookingComments() != null){
           cookingComments.setText(placeAnOrderViewModel.getCookingComments());
       }


        subTotalBeforeDiscount.setText(getString(R.string.pound)+" "+String.valueOf(placeAnOrderViewModel.getSubTotalBeforeDiscount()));
        discount.setText(getString(R.string.pound)+" "+String.valueOf(placeAnOrderViewModel.getDiscount()));
        subTotal.setText(getString(R.string.pound)+" "+String.valueOf(placeAnOrderViewModel.getSubTotal()));
        totalAmount.setText(getString(R.string.pound)+" "+String.valueOf(placeAnOrderViewModel.getTotalAmount()));
       // serviceTax.setText(getString(R.string.pound)+" "+String.valueOf(placeAnOrderViewModel.getServiceTax()));


    }

    private void getMenuList() {
        boolean isNetWorkConnected = Utilities.isNetworkConnected(naviDrawerActivity);
        if (isNetWorkConnected) {
            TableListService tableListService = new TableListServiceImpl(naviDrawerActivity);
            progressDialog = ActivityUtil.showProgress(getString(R.string.get_table_list_heading), getString(R.string.get_table_list_message), naviDrawerActivity);
            tableListService.getMenuItems(ActivityUtil.TABLE_NUMBER,ActivityUtil.USER_MOBILE);
        } else {
            ActivityUtil.showDialog(naviDrawerActivity, getString(R.string.no_network_heading), getString(R.string.no_network));
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleServerSyncResponse(ResponseData responseData) {
        if(progressDialog != null){
            progressDialog.dismiss();
        }
        // 3000
        if(responseData.getStatusCode() == 3000){
            populateData();
        }else if(responseData.getStatusCode() != 500){
            Intent intent = new Intent(naviDrawerActivity, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            naviDrawerActivity.finish();
            return;
        }else{
            ActivityUtil.showDialog(naviDrawerActivity,"Error","Sorry for the Inconvenience. Please contact Admin.");
        }
    }


    @Override
    public void onClick(View view) {
        getMenuList();

    }

}
