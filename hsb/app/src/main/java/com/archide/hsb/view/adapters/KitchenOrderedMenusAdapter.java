package com.archide.hsb.view.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.TextView;


import com.archide.hsb.enumeration.OrderStatus;
import com.archide.hsb.enumeration.ViewStatus;
import com.archide.hsb.view.activities.KitchenActivity;
import com.archide.hsb.view.fragments.KitchenOrderedItemsFragment;
import com.archide.hsb.view.model.KitchenOrderDetailsViewModel;

import java.util.ArrayList;
import java.util.List;

import hsb.archide.com.hsb.R;

/**
 * Created by Nithish on 26/11/16.
 */

public class KitchenOrderedMenusAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>   {

    private final int HEADER = 1;
    private final int MENU_ITEM = 2;
  //  private final int COMMENTS = 3;

    KitchenActivity kitchenActivity;
    List<KitchenOrderDetailsViewModel> detailsViewModels = new ArrayList<>();
    KitchenOrderedItemsFragment kitchenOrderedItemsFragment = null;

    public KitchenOrderedMenusAdapter(KitchenActivity kitchenActivity, List<KitchenOrderDetailsViewModel> detailsViewModels, KitchenOrderedItemsFragment kitchenOrderedItemsFragment){
        this.kitchenActivity = kitchenActivity;
        this.detailsViewModels = detailsViewModels;
        this.kitchenOrderedItemsFragment = kitchenOrderedItemsFragment;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;
        if(viewType == HEADER){
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.adapt_order_menu_category, parent, false);
            return new FoodCategoryViewHolder(view);
        }else {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.adapt_kitchen_ordered_items, parent, false);
            return new KitchenOrderedMenusViewHolder(view);
        }/*else{
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.adapt_kitchen_cooking_comments, parent, false);
            return new CookingCommentsViewHolder(view);
        }*/


    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, int position) {

        KitchenOrderDetailsViewModel kitchenOrderDetailsViewModel =  detailsViewModels.get(position);
        if(viewHolder instanceof KitchenOrderedMenusViewHolder){

            KitchenOrderedMenusViewHolder kitchenOrderedMenusViewHolder = (KitchenOrderedMenusViewHolder)viewHolder;
            kitchenOrderedMenusViewHolder.vOrderName.setText(kitchenOrderDetailsViewModel.getName());
            if(kitchenOrderDetailsViewModel.getViewStatus().toString().equals(ViewStatus.UN_VIEWED.toString())){
                kitchenOrderedMenusViewHolder.vOrderViewStatus.setVisibility(View.VISIBLE);
            }else{
                kitchenOrderedMenusViewHolder.vOrderViewStatus.setVisibility(View.GONE);
            }
            kitchenOrderedMenusViewHolder.vTotalCount.setText(kitchenOrderDetailsViewModel.getQuantity()+"x");

            switch (kitchenOrderDetailsViewModel.getStatus()){
                case DELIVERED:
                    kitchenOrderedMenusViewHolder.vOrderStatus.setText("Delivered");
                    kitchenOrderedMenusViewHolder.vOrderStatus.setVisibility(View.VISIBLE);
                    break;
                case UN_AVAILABLE:
                    kitchenOrderedMenusViewHolder.vOrderStatus.setText("Totally UnAvailable");
                    kitchenOrderedMenusViewHolder.vOrderStatus.setVisibility(View.VISIBLE);
                    break;
                case PARTIAL:
                    kitchenOrderedMenusViewHolder.vOrderStatus.setText(kitchenOrderDetailsViewModel.getUnAvailableCount()+" UnAvailable");
                    kitchenOrderedMenusViewHolder.vOrderStatus.setVisibility(View.VISIBLE);
                    break;

                default:
                    kitchenOrderedMenusViewHolder.vOrderStatus.setVisibility(View.GONE);
                    break;
            }

        }else if(viewHolder instanceof FoodCategoryViewHolder){
            FoodCategoryViewHolder foodCategoryViewHolder = (FoodCategoryViewHolder)viewHolder;
            foodCategoryViewHolder.vFoodCategory.setText(kitchenOrderDetailsViewModel.getName());
        }
    }

    @Override
    public int getItemViewType(int position) {
        KitchenOrderDetailsViewModel menuItemsViewModel =  detailsViewModels.get(position);
        if(menuItemsViewModel.isCategory()){
            return HEADER;
        }
        else{
            return MENU_ITEM;
        }

    }

    @Override
    public int getItemCount() {
        return detailsViewModels.size();
    }

    public class  FoodCategoryViewHolder extends RecyclerView.ViewHolder{
        private TextView vFoodCategory;
        public FoodCategoryViewHolder(View view){
            super(view);
            vFoodCategory = (TextView)view.findViewById(R.id.adapt_food_category);
        }
    }


   /* public class  CookingCommentsViewHolder extends RecyclerView.ViewHolder{
        private TextView vFoodCookingComments;
        public CookingCommentsViewHolder(View view){
            super(view);
            vFoodCookingComments = (TextView)view.findViewById(R.id.kitchen_cooking_comments);
        }
    }*/

    private class KitchenOrderedMenusViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,PopupMenu.OnMenuItemClickListener{
        TextView vTotalCount;

        TextView vOrderName;
        TextView vOrderStatus;
        ImageButton vMenuList;
        Button vOrderViewStatus;

        public KitchenOrderedMenusViewHolder(View view) {
            super(view);
            vTotalCount = (TextView)view.findViewById(R.id.adapt_kitchen_total_count);
            vOrderName = (TextView)view.findViewById(R.id.adapt_kitchen_order_name);
            vOrderStatus = (TextView)view.findViewById(R.id.order_status);
            vMenuList = (ImageButton)view.findViewById(R.id.adapt_kitchen_settings);
            vOrderViewStatus = (Button)view.findViewById(R.id.adapt_kitchen_order_tag);
            vMenuList.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            PopupMenu popupMenu = new PopupMenu(kitchenActivity, view);
            popupMenu.setOnMenuItemClickListener(this);
            popupMenu.inflate(R.menu.popup_menu);
            popupMenu.show();

        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
           int adapterPos = getAdapterPosition();
            final KitchenOrderDetailsViewModel kitchenOrderDetailsViewModel = detailsViewModels.get(adapterPos);
            kitchenOrderDetailsViewModel.setEdited(true);
            final int totalCount = Integer.valueOf(kitchenOrderDetailsViewModel.getQuantity());
           switch (item.getItemId()){
               case R.id.item_delivered:
                   kitchenOrderDetailsViewModel.setStatus(OrderStatus.DELIVERED);
                    break;
              /* case R.id.item_unavailable:
                   kitchenOrderDetailsViewModel.setStatus(OrderStatus.UN_AVAILABLE);
                   kitchenOrderDetailsViewModel.setUnAvailableCount(totalCount);
                   break;
               case R.id.item_custom:
                   LayoutInflater inflater = (LayoutInflater)
                           kitchenActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                   View npView = inflater.inflate(R.layout.number_picker_dialog_layout, null);
                   final NumberPicker np = (NumberPicker) npView.findViewById(R.id.numberPicker);

                   np.setMaxValue(totalCount);
                   np.setMinValue(1);
                   np.setWrapSelectorWheel(false);

                   AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(kitchenActivity)
                           .setTitle("Available Count:")
                           .setView(npView)
                           .setPositiveButton(R.string.dialog_ok,
                                   new DialogInterface.OnClickListener() {
                                       public void onClick(DialogInterface dialog, int whichButton) {
                                          int val =  np.getValue();
                                           kitchenOrderDetailsViewModel.setUnAvailableCount(totalCount - val);
                                           kitchenOrderDetailsViewModel.setQuantity(String.valueOf(val));
                                           kitchenOrderDetailsViewModel.setStatus(OrderStatus.PARTIAL);
                                           notifyDataSetChanged();
                                       }
                                   })
                           .setNegativeButton(R.string.dialog_cancel,
                                   new DialogInterface.OnClickListener() {
                                       public void onClick(DialogInterface dialog, int whichButton) {
                                       }
                                   });
                   alertDialogBuilder.show();
                   break;*/
           }
            kitchenOrderedItemsFragment.enableSaveButton();
            notifyDataSetChanged();
            return false;
        }
    }
}
