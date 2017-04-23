package com.archide.hsb.view.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.archide.hsb.service.impl.PrinterServiceImpl;
import com.archide.hsb.view.activities.ActivityUtil;
import com.archide.hsb.view.activities.MainActivity;
import com.archide.hsb.view.activities.NaviDrawerActivity;
import com.archide.hsb.view.adapters.PlacedOrderHisMenuItemsAdapter;
import com.archide.hsb.view.model.CloseOrderViewModel;
import com.archide.hsb.view.model.MenuItemsViewModel;
import com.archide.hsb.view.model.PlaceAnOrderViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import hsb.archide.com.hsb.R;

/**
 * Created by Nithish on 23/11/16.
 */

public class BillingFragment extends Fragment implements View.OnClickListener,TextToSpeech.OnInitListener  {


    TextView vOrderId = null;
    TextView vTableNumber = null;
    TextView vTotalAmount = null;
    TextView vUserMobileNumber = null;
    //ImageView vOrderSyncStatus = null;

   // TextView vOrderSyncMsg = null;
    Button vOrderLogOut = null;
    Button vOrderSent = null;

    private NaviDrawerActivity naviDrawerActivity;


    TextView subTotalBeforeDiscount ;
    TextView discount ;
    TextView subTotal ;
    TextView serviceTax ;
    TextView totalAmount ;


    LinearLayoutManager linearLayoutManager = null;
    PlacedOrderHisMenuItemsAdapter orderedMenuItemsAdapter;
    List<MenuItemsViewModel> menuItemsViewModels = new ArrayList<>();


    PlaceAnOrderViewModel placeAnOrderViewModel = null;
    private TextToSpeech engine;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        engine = new TextToSpeech(getContext(), this);
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

        naviDrawerActivity.setLogOut(true);
        naviDrawerActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        naviDrawerActivity.getSupportActionBar().setHomeButtonEnabled(false);


        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.odr_his_order_data);
        recyclerView.setHasFixedSize(true);
        linearLayoutManager =  new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setNestedScrollingEnabled(false);
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
        loadData();
        setAdapters(recyclerView);
        populateData();
        return view;
    }


    private void loadData(){
        placeAnOrderViewModel =  naviDrawerActivity.getOrderService().getPlacedHistoryOrderViewModel();
        menuItemsViewModels.addAll(placeAnOrderViewModel.getMenuItemsViewModels());
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




    private void populateData(){
        CloseOrderViewModel closeOrderViewModel =  naviDrawerActivity.getOrderService().getBillingDetails();
        vOrderId.setText(closeOrderViewModel.getOrderId());
        vTableNumber.setText(closeOrderViewModel.getTableNumber());
        vTotalAmount.setText(getString(R.string.pound)+" "+closeOrderViewModel.getTotalAmount());
        vUserMobileNumber.setText(closeOrderViewModel.getUserMobileNumber());

        subTotalBeforeDiscount.setText(getString(R.string.pound)+" "+String.valueOf(placeAnOrderViewModel.getSubTotalBeforeDiscount()));
        discount.setText(getString(R.string.pound)+" "+String.valueOf(placeAnOrderViewModel.getDiscount()));
        subTotal.setText(getString(R.string.pound)+" "+String.valueOf(placeAnOrderViewModel.getSubTotal()));
        totalAmount.setText(getString(R.string.pound)+" "+String.valueOf(placeAnOrderViewModel.getTotalAmount()));

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
            //reSentBilling();
            // Resent
        }else if(R.id.not_closable == view.getId()){
            naviDrawerActivity.onBackPressed();
        }else if(R.id.place_an_order == view.getId()){
            naviDrawerActivity.onBackPressed();
        }
        //place_an_order
    }




    private void setAdapters(RecyclerView recyclerView){
        orderedMenuItemsAdapter = new PlacedOrderHisMenuItemsAdapter(menuItemsViewModels,naviDrawerActivity);
        recyclerView.setAdapter(orderedMenuItemsAdapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(naviDrawerActivity,linearLayoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
    }


    @Override
    public void onInit(int i) {
        if (i == TextToSpeech.SUCCESS) {
            engine.setLanguage(Locale.UK);
            speech(getContext().getString(R.string.close_order_voice));
        }

    }

    private void speech(String textToSpeech) {
        engine.speak(textToSpeech, TextToSpeech.QUEUE_FLUSH, null, null);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.kitchen_print_action, menu);

    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_print_job:
                PrinterServiceImpl printerService = new PrinterServiceImpl(naviDrawerActivity);
                printerService.printUserOrders(naviDrawerActivity);
                break;
        }
        return super.onOptionsItemSelected(item);
    }



}
