package com.archide.hsb.view.fragments;

import android.accounts.Account;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import com.archide.hsb.service.impl.KitchenServiceImpl;
import com.archide.hsb.sync.HsbSyncAdapter;
import com.archide.hsb.sync.SyncEvent;
import com.archide.hsb.sync.json.ResponseData;
import com.archide.hsb.view.activities.HomeActivity;
import com.archide.hsb.view.activities.KitchenActivity;
import com.archide.hsb.view.adapters.KitchenOrderListAdapter;
import com.archide.hsb.view.model.KitchenOrderListViewModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import hsb.archide.com.hsb.R;

/**
 * Created by Nithish on 25/11/16.
 * http://www.pcsalt.com/android/listview-using-baseadapter-android/
 * http://androidlift.info/2015/12/24/android-recyclerview-cardview-example/
 */

public class KitchenOrderListFragment extends Fragment {

    GridView kitchenOrderList;
    private KitchenActivity kitchenActivity;
    private KitchenOrderListAdapter kitchenOrderListAdapter;
    List<KitchenOrderListViewModel> kitchenOrderListViewModels = new ArrayList<>();
    //ProgressDialog progressDialog = null;

    private LayoutInflater mInflater;
    private ViewGroup mContainer;
    private boolean isFlag = false;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_kitchen_order_list, container, false);

        mInflater = inflater;
        mContainer = container;

        kitchenOrderList =  (GridView)view.findViewById(R.id.gridview);
       int orientation = getResources().getConfiguration().orientation;
        if(Configuration.ORIENTATION_LANDSCAPE == orientation){
            kitchenOrderList.setNumColumns(2);
        }else{
            kitchenOrderList.setNumColumns(1);
        }
        kitchenOrderListAdapter =  new KitchenOrderListAdapter(kitchenOrderListViewModels,kitchenActivity);

        kitchenOrderList.setAdapter(kitchenOrderListAdapter);
       /* kitchenOrderList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                int pos = position;
                KitchenOrderListViewModel kitchenOrderListViewModel =  kitchenOrderListViewModels.get(position);
                kitchenActivity.viewOrderDetails(kitchenOrderListViewModel.getOrderId());
                return;
            }
        });*/
        init();
        loadData();

        kitchenOrderList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                KitchenOrderListViewModel kitchenOrderListViewModel =  kitchenOrderListViewModels.get(position);
              int nonVegCount = Integer.valueOf(kitchenOrderListViewModel.getNonVegCount());
                int vegCount = Integer.valueOf(kitchenOrderListViewModel.getVegCount());
                if(nonVegCount < 1 && vegCount < 1){
                    Toast.makeText(kitchenActivity,getString(R.string.kitchen_no_data),Toast.LENGTH_LONG).show();
                    return;
                }
                kitchenActivity.viewOrderDetails(kitchenOrderListViewModel.getOrderId());
                return;
            }
        });

        return view;
    }


    private void showView(View newView){
        kitchenOrderList =  (GridView)newView.findViewById(R.id.gridview);
        int orientation = getResources().getConfiguration().orientation;
        if(Configuration.ORIENTATION_LANDSCAPE == orientation){
            kitchenOrderList.setNumColumns(2);
        }else{
            kitchenOrderList.setNumColumns(1);
        }
        kitchenOrderListAdapter =  new KitchenOrderListAdapter(kitchenOrderListViewModels,kitchenActivity);

        kitchenOrderList.setAdapter(kitchenOrderListAdapter);

        kitchenOrderList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                KitchenOrderListViewModel kitchenOrderListViewModel =  kitchenOrderListViewModels.get(position);
                int nonVegCount = Integer.valueOf(kitchenOrderListViewModel.getNonVegCount());
                int vegCount = Integer.valueOf(kitchenOrderListViewModel.getVegCount());
                if(nonVegCount < 1 && vegCount < 1){
                    Toast.makeText(kitchenActivity,getString(R.string.kitchen_no_data),Toast.LENGTH_LONG).show();
                    return;
                }
                kitchenActivity.viewOrderDetails(kitchenOrderListViewModel.getOrderId());
                return;
            }
        });
    }

    private void init(){

        kitchenActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        kitchenActivity.getSupportActionBar().setHomeButtonEnabled(true);

    }



    private void loadData(){
        List<KitchenOrderListViewModel> temp = kitchenActivity.getKitchenService().getOrderList();
        kitchenOrderListViewModels.clear();
        if(temp.size() < 1){
            View newView = mInflater.inflate(R.layout.fragment_kitchen_empty_list, mContainer, false);
            mContainer.removeAllViews();
            mContainer.addView(newView);
            isFlag = true;
        }else{
            if(isFlag){
                View newView = mInflater.inflate(R.layout.fragment_kitchen_order_list, mContainer, false);
                mContainer.removeAllViews();
                mContainer.addView(newView);
                showView(newView);
            }
            isFlag = false;
            kitchenOrderListViewModels.addAll(temp);
            kitchenOrderListAdapter.notifyDataSetChanged();
        }


    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        kitchenActivity = (KitchenActivity)context;

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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleServerSyncResponse(ResponseData responseData) {
        loadData();
    }

   public interface ViewOrderDetails{
        void viewOrderDetails(String orderId);
    }
}
