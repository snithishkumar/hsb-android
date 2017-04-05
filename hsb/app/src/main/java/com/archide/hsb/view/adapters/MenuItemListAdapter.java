package com.archide.hsb.view.adapters;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.archide.hsb.util.Utilities;
import com.archide.hsb.view.activities.ActivityUtil;
import com.archide.hsb.view.activities.HomeActivity;
import com.archide.hsb.view.fragments.MenuItemsFragment;
import com.archide.hsb.view.model.MenuItemsViewModel;
import com.archide.hsb.view.model.OrderDetailsViewModel;
import com.facebook.rebound.SimpleSpringListener;
import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringSystem;

import java.util.List;

import hsb.archide.com.hsb.R;

/**
 * Created by Nithish on 16/11/16.
 */

public class MenuItemListAdapter extends BaseAdapter {

    List<MenuItemsViewModel> menuItemsViewModels = null;

    MenuItemsFragment menuItemsFragment;
    private HomeActivity homeActivity;
    private OrderDetailsViewModel orderDetailsViewModel;
    MenuItemsViewHolder menuItemsViewHolder;

    public MenuItemListAdapter(List<MenuItemsViewModel> menuItemsViewModels, MenuItemsFragment menuItemsFragment, HomeActivity homeActivity,OrderDetailsViewModel orderDetailsViewModel){
        this.menuItemsViewModels = menuItemsViewModels;
        this.menuItemsFragment = menuItemsFragment;
        this.homeActivity = homeActivity;
        this.orderDetailsViewModel = orderDetailsViewModel;
    }

    @Override
    public int getCount() {
        return menuItemsViewModels.size() ;
    }



    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        MenuItemsViewHolder menuItemsViewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapt_unorder_menu_items, parent, false);
            menuItemsViewHolder = new MenuItemsViewHolder(convertView);
            convertView.setTag(menuItemsViewHolder);
        }else{
            menuItemsViewHolder = (MenuItemsViewHolder) convertView.getTag();
        }
        this.menuItemsViewHolder = menuItemsViewHolder;

       final MenuItemsViewModel menuItemsViewModel = menuItemsViewModels.get(position);
        menuItemsViewHolder.vFoodSubCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               RelativeLayout viewParent =  (RelativeLayout)view.getParent().getParent().getParent().getParent().getParent();
                bound(viewParent);
                if(menuItemsViewModel.getCount() == 1){
                    menuItemsViewModel.setOrdered(false);
                    menuItemsViewModel.setCount(0);
                    homeActivity.getOrderService().removeOrderItems(menuItemsViewModel);

                    orderDetailsViewModel.setTotalCount(orderDetailsViewModel.getTotalCount() - 1);
                    double val =  orderDetailsViewModel.getTotalCost() - menuItemsViewModel.getCost();
                    orderDetailsViewModel.setTotalCost( Utilities.roundOff(val));
                      menuItemsFragment.updateFooterBar();
                    notifyDataSetChanged();
                }else if(menuItemsViewModel.getCount() > 1){
                    menuItemsViewModel.setCount(menuItemsViewModel.getCount() - 1);
                    homeActivity.getOrderService().updateOrderItems(menuItemsViewModel);
                    orderDetailsViewModel.setTotalCount(orderDetailsViewModel.getTotalCount() - 1);
                    double val =  orderDetailsViewModel.getTotalCost() - menuItemsViewModel.getCost();
                    orderDetailsViewModel.setTotalCost( Utilities.roundOff(val));
                      menuItemsFragment.updateFooterBar();
                    notifyDataSetChanged();
                }else{
                    ActivityUtil.toast(homeActivity,"No items to remove");
                }

            }
        });


        menuItemsViewHolder.vFoodAddCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RelativeLayout viewParent =  (RelativeLayout)view.getParent().getParent().getParent().getParent().getParent();
                bound(viewParent);
                if(menuItemsViewModel.getCount() < menuItemsViewModel.getAvailableCount()){
                    menuItemsViewModel.setCount(menuItemsViewModel.getCount() + 1);
                    homeActivity.getOrderService().addOrderItems(menuItemsViewModel);
                    orderDetailsViewModel.setTotalCount(orderDetailsViewModel.getTotalCount() + 1);
                    double val =  orderDetailsViewModel.getTotalCost() + menuItemsViewModel.getCost();
                    orderDetailsViewModel.setTotalCost( Utilities.roundOff(val));
                     menuItemsFragment.updateFooterBar();
                }else{
                    ActivityUtil.toast(homeActivity,homeActivity.getString(R.string.un_available));
                }
                notifyDataSetChanged();
            }

        });


        populateData(menuItemsViewModel,menuItemsViewHolder);
        return convertView;

    }


    private void bound(final RelativeLayout viewParent){
        // Create a system to run the physics loop for a set of springs.
        SpringSystem springSystem = SpringSystem.create();

        // Add a spring to the system.
       final Spring spring = springSystem.createSpring();

       // Add a listener to observe the motion of the spring.
        spring.addListener(new SimpleSpringListener() {

            @Override
            public void onSpringUpdate(Spring spring) {
                // You can observe the updates in the spring
                // state by asking its current value in onSpringUpdate.
                float value = (float) spring.getCurrentValue();
                float scale = 1f - (value * 0.5f);
                viewParent.setScaleX(scale);
                viewParent.setScaleY(scale);
            }
        });

// Set the spring in motion; moving from 0 to 1
        spring.setEndValue(1);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                spring.setEndValue(0);
            }
        }, 100);


    }


    private void populateData(MenuItemsViewModel menuItemsViewModel,MenuItemsViewHolder menuItemsViewHolder){
        menuItemsViewHolder.vFoodName.setText(menuItemsViewModel.getName());
        menuItemsViewHolder.vFoodCost.setText(String.valueOf(menuItemsViewModel.getCost()));
        menuItemsViewHolder.vFoodDesc.setText(menuItemsViewModel.getDescription());
        if(menuItemsViewModel.getCount() > 0){
            menuItemsViewHolder.vFoodCartCount.setText(String.valueOf(menuItemsViewModel.getCount()));
        }else{
            menuItemsViewHolder.vFoodCartCount.setText(String.valueOf(""));
        }

    }




    public class MenuItemsViewHolder {
        protected TextView vFoodName;
        protected TextView vFoodCost;
        protected TextView vFoodDesc;
        protected Button vFoodSubCartButton;
        protected Button vFoodAddCartButton;
        protected TextView vFoodCartCount;
        protected RelativeLayout vAdaptUnOrderMenuLayout;

        public MenuItemsViewHolder(View view){
            //adapt_unorder_menu_layout
            vAdaptUnOrderMenuLayout =  (RelativeLayout)view.findViewById(R.id.adapt_unorder_menu_layout);
            vFoodName =  (TextView)view.findViewById(R.id.user_menu_item_name);
            vFoodDesc =  (TextView)view.findViewById(R.id.user_menu_item_desc);
            vFoodCost =  (TextView)view.findViewById(R.id.user_menu_item_rate);

            vFoodSubCartButton = (Button)view.findViewById(R.id.decrement);
            vFoodAddCartButton = (Button)view.findViewById(R.id.increment);
            vFoodCartCount = (TextView)view.findViewById(R.id.count_value);


        }


    }



}
