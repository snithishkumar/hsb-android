package com.archide.hsb.view.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.archide.hsb.service.impl.PrinterServiceImpl;
import com.archide.hsb.view.activities.KitchenActivity;
import com.archide.hsb.view.activities.KitchenDetailsActivity;
import com.archide.hsb.view.adapters.KitchenOrderedCommentsAdapter;
import com.archide.hsb.view.adapters.KitchenOrderedMenusAdapter;
import com.archide.hsb.view.model.KitchenCommentsViewModel;
import com.archide.hsb.view.model.KitchenOrderDetailsViewModel;

import java.util.ArrayList;
import java.util.List;

import hsb.archide.com.hsb.R;

/**
 * Created by Nithish on 26/11/16.
 */

public class KitchenOrderedItemsFragment extends Fragment implements View.OnClickListener{

    LinearLayoutManager linearLayoutManager = null;
    KitchenDetailsActivity kitchenDetailsActivity = null;
    private String orderId = null;
    KitchenOrderedMenusAdapter kitchenOrderedMenusAdapter = null;
    KitchenOrderedCommentsAdapter kitchenOrderedCommentsAdapter = null;
    private List<KitchenOrderDetailsViewModel> detailsViewModels = new ArrayList<>();
    private List<KitchenCommentsViewModel> kitchenCommentsViewModels = new ArrayList<>();
    FloatingActionButton floatingActionButton = null;

    Button button = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_kitchen_ordered_items, container, false);


        Bundle purchaseIdArgs = getArguments();
        if(purchaseIdArgs != null){
            orderId =  purchaseIdArgs.getString("orderId");

        }

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.kitchen_order_data);
        recyclerView.setHasFixedSize(true);

       // floatingActionButton = (FloatingActionButton)view.findViewById(R.id.saveOrderStatus);
        button = (Button)view.findViewById(R.id.saveOrderStatus);
        button.setOnClickListener(this);
        linearLayoutManager =  new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setNestedScrollingEnabled(false);
        setAdapters(recyclerView);

        setKitchenAdapters(view);


        loadData();
        updateViewStatus();


        kitchenDetailsActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        kitchenDetailsActivity.getSupportActionBar().setHomeButtonEnabled(true);



        return view;
    }

    private void setAdapters(RecyclerView recyclerView){
        kitchenOrderedMenusAdapter = new KitchenOrderedMenusAdapter(kitchenDetailsActivity,detailsViewModels,KitchenOrderedItemsFragment.this);

        recyclerView.setAdapter(kitchenOrderedMenusAdapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(kitchenDetailsActivity,linearLayoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
    }


    private void setKitchenAdapters(View view){
        RecyclerView cookingCommentsRecycle = (RecyclerView) view.findViewById(R.id.kitchen_order_comments);
        cookingCommentsRecycle.setHasFixedSize(true);
        cookingCommentsRecycle.setNestedScrollingEnabled(false);
       LinearLayoutManager linearLayoutManager =  new LinearLayoutManager(getActivity());
        cookingCommentsRecycle.setLayoutManager(linearLayoutManager);
        cookingCommentsRecycle.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }


            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
        cookingCommentsRecycle.setLayoutManager(linearLayoutManager);

        kitchenOrderedCommentsAdapter = new KitchenOrderedCommentsAdapter(kitchenCommentsViewModels);
        cookingCommentsRecycle.setAdapter(kitchenOrderedCommentsAdapter);
    }


    private void updateViewStatus(){
        kitchenDetailsActivity.getKitchenService().updateKitchenOrderViewStatus(orderId,detailsViewModels);
    }

    private void loadData(){
        List<KitchenOrderDetailsViewModel> temp = kitchenDetailsActivity.getKitchenService().getKitchenOrderDetails(orderId);
        detailsViewModels.clear();
        detailsViewModels.addAll(temp);
        kitchenOrderedMenusAdapter.notifyDataSetChanged();
        loadCookingComments();
    }

    private void loadCookingComments(){
        List<KitchenCommentsViewModel> kitchenCommentsList = kitchenDetailsActivity.getKitchenService().getKitchenCommentsViewModel(orderId);
        kitchenCommentsViewModels.clear();
        kitchenCommentsViewModels.addAll(kitchenCommentsList);
        kitchenOrderedCommentsAdapter.notifyDataSetChanged();

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        kitchenDetailsActivity = (KitchenDetailsActivity)context;

    }

    @Override
    public void onClick(View view) {
        kitchenDetailsActivity.getKitchenService().saveOrderStatus(detailsViewModels,orderId,kitchenDetailsActivity);
        kitchenDetailsActivity.finish();
        return;
    }

    public void enableSaveButton(){
       if(button != null && button.getVisibility() != View.VISIBLE){
           button.setVisibility(View.VISIBLE);
       }
    }
}
