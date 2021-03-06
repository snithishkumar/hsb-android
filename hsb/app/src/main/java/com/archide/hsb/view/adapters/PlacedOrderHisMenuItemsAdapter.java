package com.archide.hsb.view.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.archide.hsb.view.activities.NaviDrawerActivity;
import com.archide.hsb.view.activities.OrderActivity;
import com.archide.hsb.view.fragments.OrderPlaceFragment;
import com.archide.hsb.view.model.MenuItemsViewModel;

import java.util.ArrayList;
import java.util.List;

import hsb.archide.com.hsb.R;

/**
 * Created by Nithish on 19/11/16.
 */

public class PlacedOrderHisMenuItemsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {


    List<MenuItemsViewModel> menuItemsViewModels = new ArrayList<>();
    private NaviDrawerActivity naviDrawerActivity;

    public PlacedOrderHisMenuItemsAdapter(List<MenuItemsViewModel> menuItemsViewModels,NaviDrawerActivity naviDrawerActivity){
        this.menuItemsViewModels = menuItemsViewModels;
        this.naviDrawerActivity = naviDrawerActivity;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapt_placed_ordered_his_item_details, parent, false);

        return new OrderedMenuItemsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, int position) {
        MenuItemsViewModel menuItemsViewModel = menuItemsViewModels.get(position);
        OrderedMenuItemsViewHolder orderedMenuItemsViewHolder  = (OrderedMenuItemsViewHolder)viewHolder;
        orderedMenuItemsViewHolder.vTotalCount.setText(String.valueOf(menuItemsViewModel.getCount()) +"x ");
        orderedMenuItemsViewHolder.vTotalAmount.setText(naviDrawerActivity.getString(R.string.pound)+" "+String.valueOf(menuItemsViewModel.getCost() * menuItemsViewModel.getCount()));
        orderedMenuItemsViewHolder.vOrderName.setText(String.valueOf(menuItemsViewModel.getName()));
    }

    @Override
    public int getItemCount() {
        return menuItemsViewModels.size() ;
    }

    public class OrderedMenuItemsViewHolder extends RecyclerView.ViewHolder {
        TextView vTotalCount;
        TextView vTotalAmount;
        TextView vOrderName;


        public OrderedMenuItemsViewHolder(View view) {
            super(view);
            vTotalCount = (TextView)view.findViewById(R.id.total_count);
            vTotalAmount = (TextView)view.findViewById(R.id.total_amount);
            vOrderName = (TextView)view.findViewById(R.id.order_name);
        }


    }

}
