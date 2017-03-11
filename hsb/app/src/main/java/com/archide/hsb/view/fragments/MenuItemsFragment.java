package com.archide.hsb.view.fragments;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.archide.hsb.sync.json.ResponseData;
import com.archide.hsb.view.activities.ActivityUtil;
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


    private SwipeRefreshLayout refreshLayout;
    private LinearLayoutManager  linearLayoutManager;
    private HomeActivity homeActivity;
    private  MenuItemListAdapter  menuItemListAdapter;
    private  String menuCourseUuid ;
    OrderDetailsViewModel orderDetailsViewModel;
    private ImageView imageView;

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

        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.menu_items_swipe);


        orderDetailsViewModel = new OrderDetailsViewModel();


        layout =  (RelativeLayout)view.findViewById(R.id.textView1);
        layout.setVisibility(View.GONE);

        totalNoOfItems = (TextView) layout.findViewById(R.id.total_no_of_items);
        totalAmount =  (TextView) layout.findViewById(R.id.total_amount);
        imageView =  (ImageView)layout.findViewById(R.id.cart_item_fire_img);
        refreshLayout.setColorSchemeColors(Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                homeActivity.getOrderService().getMenuItems(ActivityUtil.TABLE_NUMBER,ActivityUtil.USER_MOBILE,homeActivity);

            }
        });


        //  refreshLayout.

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.menu_items_list_fragment);
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
                refreshLayout.setEnabled(linearLayoutManager.findFirstCompletelyVisibleItemPosition() == 0);
            }
        });

        setAdapters(recyclerView);
       // getData();
        return view;
    }

    private void setAdapters(RecyclerView recyclerView){
        menuItemListAdapter = new MenuItemListAdapter(menuItemsViewModels,MenuItemsFragment.this,homeActivity,orderDetailsViewModel);
        recyclerView.setAdapter(menuItemListAdapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(homeActivity,linearLayoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
    }

    private void getData(){
        List<MenuItemsViewModel> menuItemsViewModels =  homeActivity.getMenuItemService().getMenuItemsViewModel(menuCourseUuid,orderDetailsViewModel);
        this.menuItemsViewModels.clear();
        this.menuItemsViewModels.addAll(menuItemsViewModels);
        menuItemListAdapter.notifyDataSetChanged();
       // updateFooterBar();

    }

    @Override
    public void onResume() {
        super.onResume();
        getData();
        reLoadData();
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
        homeActivity.getMenuItemService().getCurrentOrdersCounts(orderDetailsViewModel);
        updateFooterBar();
    }

    public void updateFooterBar(){
        if(orderDetailsViewModel.getTotalCount() > 0){
            Animation bounce = AnimationUtils.loadAnimation(homeActivity,R.anim.bounce);
            imageView.startAnimation(bounce);
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
        stopRefreshing();
        getData();
        reLoadData();

    }


    private void stopRefreshing(){
        if(refreshLayout != null){
            refreshLayout.setRefreshing(false);
        }
    }

}
