package com.archide.hsb.view.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.archide.hsb.view.activities.KitchenActivity;
import com.archide.hsb.view.adapters.KitchenOrderedMenusAdapter;
import com.archide.hsb.view.model.KitchenOrderDetailsViewModel;

import java.util.ArrayList;
import java.util.List;

import hsb.archide.com.hsb.R;

/**
 * Created by Nithish on 26/11/16.
 */

public class KitchenOrderedItemsFragment extends Fragment {

    LinearLayoutManager linearLayoutManager = null;
    KitchenActivity kitchenActivity = null;
    private String orderId = null;
    KitchenOrderedMenusAdapter kitchenOrderedMenusAdapter = null;
    private List<KitchenOrderDetailsViewModel> detailsViewModels = new ArrayList<>();
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
            }
        });
        setAdapters(recyclerView);
        loadData();
        return view;
    }

    private void setAdapters(RecyclerView recyclerView){
        kitchenOrderedMenusAdapter = new KitchenOrderedMenusAdapter(kitchenActivity,detailsViewModels);

        recyclerView.setAdapter(kitchenOrderedMenusAdapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(kitchenActivity,linearLayoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
    }

    private void loadData(){
        List<KitchenOrderDetailsViewModel> temp = kitchenActivity.getKitchenService().getKitchenOrderDetails(orderId);
        detailsViewModels.clear();
        detailsViewModels.addAll(temp);
        kitchenOrderedMenusAdapter.notifyDataSetChanged();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        kitchenActivity = (KitchenActivity)context;

    }

}
