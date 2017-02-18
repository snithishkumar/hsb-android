package com.archide.hsb.view.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.archide.hsb.view.model.KitchenCommentsViewModel;

import java.util.ArrayList;
import java.util.List;

import hsb.archide.com.hsb.R;

/**
 * Created by Nithish on 18/02/17.
 */

public class KitchenOrderedCommentsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<KitchenCommentsViewModel> kitchenCommentsViewModels = null;

    public KitchenOrderedCommentsAdapter(List<KitchenCommentsViewModel> kitchenCommentsViewModels){
        this.kitchenCommentsViewModels = kitchenCommentsViewModels;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;

        view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapt_kitchen_cooking_comments, parent, false);
        return new KitchenOrderedCommentsAdapter.CookingCommentsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, int position) {
        KitchenCommentsViewModel kitchenCommentsViewModel =  kitchenCommentsViewModels.get(position);
        KitchenOrderedCommentsAdapter.CookingCommentsViewHolder kitchenOrderedMenusViewHolder = (KitchenOrderedCommentsAdapter.CookingCommentsViewHolder)viewHolder;
        kitchenOrderedMenusViewHolder.vFoodCookingComments.setText(kitchenCommentsViewModel.getComments());
        kitchenOrderedMenusViewHolder.vFoodCookingCommentsTime.setText(kitchenCommentsViewModel.getTime());
    }


    public class  CookingCommentsViewHolder extends RecyclerView.ViewHolder{
        private TextView vFoodCookingComments;
        private TextView vFoodCookingCommentsTime;
        public CookingCommentsViewHolder(View view){
            super(view);
            vFoodCookingComments = (TextView)view.findViewById(R.id.kitchen_cooking_comments);
            vFoodCookingCommentsTime = (TextView)view.findViewById(R.id.kitchen_cooking_comments_time);
        }
    }

    @Override
    public int getItemCount() {
        return kitchenCommentsViewModels.size();
    }
}
