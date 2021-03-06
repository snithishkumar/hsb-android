package com.archide.hsb.view.adapters;

import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.archide.hsb.enumeration.FoodType;
import com.archide.hsb.util.Utilities;
import com.archide.hsb.view.activities.ActivityUtil;
import com.archide.hsb.view.activities.HomeActivity;
import com.archide.hsb.view.fragments.MenuItemsFragment;
import com.archide.hsb.view.model.MenuItemsViewModel;
import com.archide.hsb.view.model.OrderDetailsViewModel;

import java.util.List;

import hsb.archide.com.hsb.R;

/**
 * Created by Nithish on 16/11/16.
 */

public class MenuItemListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {

    List<MenuItemsViewModel> menuItemsViewModels = null;

    private final int HEADER = 1;
    private final int MENU_ITEM = 2;
    private final int ORDERED_MENU_ITEN = 3;
    MenuItemsFragment menuItemsFragment;
    private HomeActivity homeActivity;
    private OrderDetailsViewModel orderDetailsViewModel;

    public MenuItemListAdapter(List<MenuItemsViewModel> menuItemsViewModels, MenuItemsFragment menuItemsFragment, HomeActivity homeActivity,OrderDetailsViewModel orderDetailsViewModel){
        this.menuItemsViewModels = menuItemsViewModels;
        this.menuItemsFragment = menuItemsFragment;
        this.homeActivity = homeActivity;
        this.orderDetailsViewModel = orderDetailsViewModel;
    }

    @Override
    public int getItemCount() {
        return menuItemsViewModels.size() ;
    }

    @Override
    public int getItemViewType(int position) {
        MenuItemsViewModel menuItemsViewModel =  menuItemsViewModels.get(position);
        if(menuItemsViewModel.isCategory()){
            return HEADER;
        }else if(menuItemsViewModel.isOrdered()){
            return ORDERED_MENU_ITEN;
        }else{
           return MENU_ITEM;
        }

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if(viewType == HEADER){
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.adapt_order_menu_category, parent, false);
            return new FoodCategoryViewHolder(view);
        }else if(viewType == MENU_ITEM){
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.adapt_unorder_menu_items, parent, false);
            return new MenuItemsViewHolder(view);
        }else{
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.adapt_order_menu_items, parent, false);
            return new OrderItemsViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, int position) {
        MenuItemsViewModel menuItemsViewModel = menuItemsViewModels.get(position);
        if(viewHolder instanceof FoodCategoryViewHolder){
            FoodCategoryViewHolder foodCategoryViewHolder  = (FoodCategoryViewHolder)viewHolder;

            foodCategoryViewHolder.vFoodCategory.setText(menuItemsViewModel.getName());
        }else if(viewHolder instanceof OrderItemsViewHolder){
            OrderItemsViewHolder orderItemsViewHolder = (OrderItemsViewHolder)viewHolder;
            orderItemsViewHolder.vFoodName.setText(menuItemsViewModel.getName());
            orderItemsViewHolder.vFoodCost.setText(homeActivity.getString(R.string.pound)+String.valueOf(menuItemsViewModel.getCost()));
            orderItemsViewHolder.vFoodCartCount.setText(String.valueOf(menuItemsViewModel.getCount()));
            orderItemsViewHolder.vFoodTasteType.setText(menuItemsViewModel.getTasteType());
            orderItemsViewHolder.vFoodDesc.setText(menuItemsViewModel.getDescription());
            int availableCount = menuItemsViewModel.getAvailableCount() - menuItemsViewModel.getCount();
            if(availableCount < 10){
                orderItemsViewHolder.vFoodAvailable.setVisibility(View.VISIBLE);
                orderItemsViewHolder.vFoodAvailable.setText("Available Count :"+availableCount);
            }else{
                orderItemsViewHolder.vFoodAvailable.setVisibility(View.INVISIBLE);
            }

            setFoodType(orderItemsViewHolder.vFoodType,menuItemsViewModel.getFoodType());
        }else{

            MenuItemsViewHolder menuItemsViewHolder = (MenuItemsViewHolder)viewHolder;
            setFoodType(menuItemsViewHolder.vFoodType,menuItemsViewModel.getFoodType());
            setValues(menuItemsViewHolder,menuItemsViewModel);
        }
    }


    private void setFoodType(ImageView imageView, FoodType foodType){
        if(foodType.toString().equals(FoodType.NONVEG.toString())){
            imageView.setImageDrawable(homeActivity.getDrawable(R.drawable.ic_nonveg));
        }else{
            imageView.setImageDrawable(homeActivity.getDrawable(R.drawable.ic_veg));
        }
    }

    private void setValues(MenuItemsViewHolder menuItemsViewHolder,MenuItemsViewModel menuItemsViewModel ){
        menuItemsViewHolder.vFoodName.setText(menuItemsViewModel.getName());
        menuItemsViewHolder.vFoodCost.setText(homeActivity.getString(R.string.pound)+String.valueOf(menuItemsViewModel.getCost()));
        menuItemsViewHolder.vFoodTasteType.setText(menuItemsViewModel.getTasteType());
        menuItemsViewHolder.vFoodDesc.setText(menuItemsViewModel.getDescription());
        if(menuItemsViewModel.getAvailableCount() < 10){
            menuItemsViewHolder.vFoodAvailable.setVisibility(View.VISIBLE);
            menuItemsViewHolder.vFoodAvailable.setText("Available Count :"+menuItemsViewModel.getAvailableCount());
        }else{
            menuItemsViewHolder.vFoodAvailable.setVisibility(View.INVISIBLE);
        }
    }

    public class  FoodCategoryViewHolder extends RecyclerView.ViewHolder{
       private TextView vFoodCategory;
        public FoodCategoryViewHolder(View view){
            super(view);
            vFoodCategory = (TextView)view.findViewById(R.id.adapt_food_category);
        }
    }

    public class MenuItemsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        protected TextView vFoodName;
        protected TextView vFoodCost;
        protected TextView vFoodDesc;
        protected TextView vFoodAddCart;
        protected  Button vFoodTasteType;
        private ImageView vFoodType;
        protected TextView vFoodAvailable;

        public MenuItemsViewHolder(View view){
            super(view);
            vFoodName = (TextView)view.findViewById(R.id.adapt_food_name);
            vFoodCost = (TextView)view.findViewById(R.id.adapt_food_cost);
            vFoodDesc = (TextView)view.findViewById(R.id.adapt_food_desc);
            vFoodTasteType = (Button)view.findViewById(R.id.adapt_food_taste_type);
            vFoodAddCart = (TextView)view.findViewById(R.id.adapt_food_add_cart);
            vFoodType = (ImageView)view.findViewById(R.id.veg);
            vFoodAddCart.setOnClickListener(this);
            vFoodAvailable = (TextView)view.findViewById(R.id.adapt_food_available);


        }

        @Override
        public void onClick(View view) {
            MenuItemsViewModel menuItemsViewModel =  menuItemsViewModels.get(getAdapterPosition());
            menuItemsViewModel.setOrdered(true);
            menuItemsViewModel.setCount(menuItemsViewModel.getCount() + 1);
            homeActivity.getOrderService().addOrderItems(menuItemsViewModel);
            orderDetailsViewModel.setTotalCount(orderDetailsViewModel.getTotalCount() + 1);

            double val =  orderDetailsViewModel.getTotalCost() + menuItemsViewModel.getCost();
            orderDetailsViewModel.setTotalCost( Utilities.roundOff(val));

            menuItemsFragment.updateFooterBar();
            notifyDataSetChanged();
        }
    }

    public class OrderItemsViewHolder  extends RecyclerView.ViewHolder implements View.OnClickListener {
        protected TextView vFoodCartCount;
        protected Button vFoodSubCartButton;
        protected Button vFoodAddCartButton;
        protected TextView vFoodName;
        protected TextView vFoodCost;
        protected  Button vFoodTasteType;
        protected TextView vFoodDesc;
        protected TextView vFoodAvailable;
        private ImageView vFoodType;
        public OrderItemsViewHolder(View view){
            super(view);
            vFoodName = (TextView)view.findViewById(R.id.adapt_food_name);
            vFoodCost = (TextView)view.findViewById(R.id.adapt_food_cost);
            vFoodDesc = (TextView)view.findViewById(R.id.adapt_food_desc);
            vFoodCartCount = (TextView)view.findViewById(R.id.count_value);
            vFoodAvailable = (TextView)view.findViewById(R.id.adapt_food_available);
            vFoodTasteType = (Button)view.findViewById(R.id.adapt_food_taste_type);
            vFoodSubCartButton = (Button)view.findViewById(R.id.decrement);
            vFoodAddCartButton = (Button)view.findViewById(R.id.increment);
            //adapt_food_available
            vFoodType = (ImageView)view.findViewById(R.id.veg);
            vFoodSubCartButton.setOnClickListener(this);
            vFoodAddCartButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            MenuItemsViewModel menuItemsViewModel =  menuItemsViewModels.get(getAdapterPosition());

            switch (view.getId()){

                case R.id.increment:
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

                    break;
                case R.id.decrement:
                    if(menuItemsViewModel.getCount() == 1){
                        menuItemsViewModel.setOrdered(false);
                        menuItemsViewModel.setCount(0);
                        homeActivity.getOrderService().removeOrderItems(menuItemsViewModel);

                        orderDetailsViewModel.setTotalCount(orderDetailsViewModel.getTotalCount() - 1);
                        double val =  orderDetailsViewModel.getTotalCost() - menuItemsViewModel.getCost();
                        orderDetailsViewModel.setTotalCost( Utilities.roundOff(val));
                        menuItemsFragment.updateFooterBar();
                    }else{
                        menuItemsViewModel.setCount(menuItemsViewModel.getCount() - 1);
                        homeActivity.getOrderService().updateOrderItems(menuItemsViewModel);
                        orderDetailsViewModel.setTotalCount(orderDetailsViewModel.getTotalCount() - 1);
                        double val =  orderDetailsViewModel.getTotalCost() - menuItemsViewModel.getCost();
                        orderDetailsViewModel.setTotalCost( Utilities.roundOff(val));
                        menuItemsFragment.updateFooterBar();
                    }

                    break;
            }

            notifyDataSetChanged();
        }
    }

}
