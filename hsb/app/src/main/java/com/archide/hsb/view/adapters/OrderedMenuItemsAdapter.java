package com.archide.hsb.view.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.archide.hsb.enumeration.OrderStatus;
import com.archide.hsb.view.activities.OrderActivity;
import com.archide.hsb.view.fragments.OrderPlaceFragment;
import com.archide.hsb.view.model.MenuItemsViewModel;

import java.util.ArrayList;
import java.util.List;

import hsb.archide.com.hsb.R;

/**
 * Created by Nithish on 19/11/16.
 */

public class OrderedMenuItemsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {


    private OrderActivity orderActivity;
    private OrderPlaceFragment orderPlaceFragment;
    List<MenuItemsViewModel> menuItemsViewModels = new ArrayList<>();

    private final int AVAILABLE  = 1;
    private final int UN_AVAILABLE = 2;

    public OrderedMenuItemsAdapter(List<MenuItemsViewModel> menuItemsViewModels, OrderActivity orderActivity, OrderPlaceFragment orderPlaceFragment){
        this.menuItemsViewModels = menuItemsViewModels;
        this.orderActivity = orderActivity;
        this.orderPlaceFragment = orderPlaceFragment;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      if(viewType == AVAILABLE){
          View view = LayoutInflater.from(parent.getContext())
                  .inflate(R.layout.adapt_place_order_menus, parent, false);

          return new OrderedMenuItemsViewHolder(view);
      }else{
          View view = LayoutInflater.from(parent.getContext())
                  .inflate(R.layout.adapt_place_order_unavailable_menus, parent, false);

          return new OrderedMenuUnAvailableItemsViewHolder(view);
      }

    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, int position) {
        MenuItemsViewModel menuItemsViewModel = menuItemsViewModels.get(position);
        if(viewHolder instanceof OrderedMenuItemsViewHolder){
            OrderedMenuItemsViewHolder orderedMenuItemsViewHolder  = (OrderedMenuItemsViewHolder)viewHolder;
            //orderedMenuItemsViewHolder.vTotalCount.setText(String.valueOf(menuItemsViewModel.getCount()) +"x ");
//            orderedMenuItemsViewHolder.vTotalCount.setText("");
            orderedMenuItemsViewHolder.vTotalAmount.setText(orderActivity.getString(R.string.pound)+String.valueOf(menuItemsViewModel.getCost() * menuItemsViewModel.getCount()));
            orderedMenuItemsViewHolder.vOrderName.setText(String.valueOf(menuItemsViewModel.getName()));
            orderedMenuItemsViewHolder.vCountValue.setText(String.valueOf(menuItemsViewModel.getCount()));
        }else{
            OrderedMenuUnAvailableItemsViewHolder orderedMenuItemsViewHolder  = (OrderedMenuUnAvailableItemsViewHolder)viewHolder;
            orderedMenuItemsViewHolder.vTotalCount.setText(String.valueOf(menuItemsViewModel.getCount()) +"x ");
            //orderedMenuItemsViewHolder.vTotalAmount.setText(String.valueOf(menuItemsViewModel.getCost() * menuItemsViewModel.getCount()));
            orderedMenuItemsViewHolder.vOrderName.setText(String.valueOf(menuItemsViewModel.getName()));
        }

    }

    @Override
    public int getItemCount() {
        return menuItemsViewModels.size() ;
    }

    @Override
    public int getItemViewType(int position) {
        MenuItemsViewModel menuItemsViewModel = menuItemsViewModels.get(position);
        if(menuItemsViewModel.getOrderStatus().toString() == OrderStatus.UN_AVAILABLE.toString()){
            return UN_AVAILABLE;
        }else{
            return AVAILABLE;
        }

    }

    public class OrderedMenuItemsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView vTotalCount;
        TextView vTotalAmount;
        TextView vOrderName;
        TextView vDecrement;
        TextView vCountValue;
        TextView vIncrement;
        LinearLayout vRemovePlaceAnOrder;
        public OrderedMenuItemsViewHolder(View view) {
            super(view);
            vTotalCount = (TextView)view.findViewById(R.id.total_count);
            vTotalAmount = (TextView)view.findViewById(R.id.total_amount);
            vOrderName = (TextView)view.findViewById(R.id.order_name);
            vDecrement = (TextView)view.findViewById(R.id.decrement);
            vCountValue = (TextView)view.findViewById(R.id.count_value);
            vRemovePlaceAnOrder = (LinearLayout)view.findViewById(R.id.adapt_place_order_remove);

            vIncrement = (TextView)view.findViewById(R.id.increment);
            vDecrement.setOnClickListener(this);
            vIncrement.setOnClickListener(this);
            vRemovePlaceAnOrder.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            MenuItemsViewModel menuItemsViewModel =  menuItemsViewModels.get(getAdapterPosition());

            switch (view.getId()){

                case R.id.increment:
                    menuItemsViewModel.setCount(menuItemsViewModel.getCount() + 1);
                    orderActivity.getOrderService().addOrderItems(menuItemsViewModel);
                    orderPlaceFragment.calcAmountDetails();
                    break;
                case R.id.decrement:
                    if(menuItemsViewModel.getCount() == 1){
                        menuItemsViewModels.remove(getAdapterPosition());
                        orderActivity.getOrderService().removeOrderItems(menuItemsViewModel);
                        orderPlaceFragment.calcAmountDetails();
                    }else{
                        menuItemsViewModel.setCount(menuItemsViewModel.getCount() - 1);
                        orderActivity.getOrderService().updateOrderItems(menuItemsViewModel);
                        orderPlaceFragment.calcAmountDetails();
                    }

                    break;
                case R.id.adapt_place_order_remove:
                    orderActivity.getOrderService().removeOrderItems(menuItemsViewModel);
                    menuItemsViewModels.remove(getAdapterPosition());
                    if(menuItemsViewModels.size() > 0){
                        orderPlaceFragment.calcAmountDetails();
                    }
                    break;
            }
            if(menuItemsViewModels.size() > 0){
                notifyDataSetChanged();
            }else{
                orderPlaceFragment.showNoData();
                return;
            }

        }
    }


    public class OrderedMenuUnAvailableItemsViewHolder extends RecyclerView.ViewHolder {
        TextView vTotalCount;
        TextView vTotalAmount;
        TextView vOrderName;
        public OrderedMenuUnAvailableItemsViewHolder(View view) {
            super(view);
            vTotalCount = (TextView)view.findViewById(R.id.total_count);
            vTotalAmount = (TextView)view.findViewById(R.id.total_amount);
            vOrderName = (TextView)view.findViewById(R.id.order_name);
        }

    }

    public void setMenuItemsViewModels(List<MenuItemsViewModel> menuItemsViewModels) {
        this.menuItemsViewModels = menuItemsViewModels;
    }
}
