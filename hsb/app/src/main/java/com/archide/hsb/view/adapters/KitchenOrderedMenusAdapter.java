package com.archide.hsb.view.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;


import hsb.archide.com.hsb.R;

/**
 * Created by Nithish on 26/11/16.
 */

public class KitchenOrderedMenusAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>   {



    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapt_kitchen_ordered_items, parent, false);

        return new KitchenOrderedMenusViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, int position) {

    }

    @Override
    public int getItemCount() {
        return 3;
    }

    private class KitchenOrderedMenusViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView vTotalCount;

        TextView vOrderName;
        ImageButton vMenuList;

        public KitchenOrderedMenusViewHolder(View view) {
            super(view);
            vTotalCount = (TextView)view.findViewById(R.id.adapt_kitchen_total_count);
            vOrderName = (TextView)view.findViewById(R.id.adapt_kitchen_order_name);
            vMenuList = (ImageButton)view.findViewById(R.id.adapt_kitchen_settings);
            vMenuList.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
           // MenuItemsViewModel menuItemsViewModel =  menuItemsViewModels.get(getAdapterPosition());



            notifyDataSetChanged();
        }
    }
}
