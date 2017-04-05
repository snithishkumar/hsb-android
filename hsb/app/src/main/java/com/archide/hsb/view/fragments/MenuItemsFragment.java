package com.archide.hsb.view.fragments;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.archide.hsb.sync.json.ResponseData;
import com.archide.hsb.view.activities.HomeActivity;
import com.archide.hsb.view.adapters.MenuItemListAdapter;
import com.archide.hsb.view.model.MenuItemsViewModel;
import com.archide.hsb.view.model.OrderDetailsViewModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import hsb.archide.com.hsb.R;



public class MenuItemsFragment extends Fragment {


    private HomeActivity homeActivity;
    private  MenuItemListAdapter  menuItemListAdapter;
    private  String menuCourseUuid ;
    OrderDetailsViewModel orderDetailsViewModel;
    GridView userMenuList;
    TextView totalNoOfItems;
    TextView totalAmount;

    RelativeLayout layout;

            List<MenuItemsViewModel> menuItemsViewModels = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle purchaseIdArgs = getArguments();
        if(purchaseIdArgs != null){
          int  tabPosition =  purchaseIdArgs.getInt("tabPosition");
            menuCourseUuid =  purchaseIdArgs.getString("menuCourseUuid");

        }

        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_menu_items, container, false);

        userMenuList =  (GridView)view.findViewById(R.id.user_menu_item_grid);

        layout =  (RelativeLayout)view.findViewById(R.id.current_order_details);
        layout.setVisibility(View.GONE);

        totalNoOfItems = (TextView) layout.findViewById(R.id.total_no_of_items);
        totalAmount = (TextView) layout.findViewById(R.id.total_amount);


        int orientation = getResources().getConfiguration().orientation;
        if(Configuration.ORIENTATION_LANDSCAPE == orientation){
            userMenuList.setNumColumns(3);
        }else{
            userMenuList.setNumColumns(2);
        }


        orderDetailsViewModel = new OrderDetailsViewModel();


        List<MenuItemsViewModel> menuItemsViewModels =  homeActivity.getMenuItemService().getMenuItemsViewModel(menuCourseUuid,orderDetailsViewModel);
        this.menuItemsViewModels.clear();
        this.menuItemsViewModels.addAll(menuItemsViewModels);

        setAdapters();
        updateFooterBar();
        return view;
    }



    private void setAdapters(){
        menuItemListAdapter = new MenuItemListAdapter(menuItemsViewModels,MenuItemsFragment.this,homeActivity,orderDetailsViewModel);
        userMenuList.setAdapter(menuItemListAdapter);
    }



    @Override
    public void onResume() {
        super.onResume();
        //getData();
        //reLoadData();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        homeActivity = (HomeActivity)context;

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

    public void reLoadData(){
        if(homeActivity != null){
            homeActivity.getMenuItemService().getCurrentOrdersCounts(orderDetailsViewModel);
            updateFooterBar();
        }

    }

    public void updateFooterBar(){
        if(orderDetailsViewModel.getTotalCount() > 0){
            totalNoOfItems.setText(String.valueOf(orderDetailsViewModel.getTotalCount())+" items");
            totalAmount.setText(getString(R.string.pound)+" "+String.valueOf(orderDetailsViewModel.getTotalCost()));
            if(layout.getVisibility() != View.VISIBLE){
                layout.setVisibility(View.VISIBLE);
            }

        }else{
            if(layout.getVisibility() == View.VISIBLE){
                layout.setVisibility(View.GONE);
            }
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleServerSyncResponse(ResponseData responseData) {
       // getData();
       // reLoadData();

    }







}
