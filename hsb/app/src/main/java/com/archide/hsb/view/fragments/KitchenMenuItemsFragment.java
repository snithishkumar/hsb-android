package com.archide.hsb.view.fragments;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.archide.hsb.entity.KitchenMenuItemsEntity;
import com.archide.hsb.sync.json.ResponseData;
import com.archide.hsb.view.activities.KitchenMenusActivity;
import com.archide.hsb.view.adapters.KitchenMenuListAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import hsb.archide.com.hsb.R;

/**
 * Created by Nithish on 04/03/17.
 */

public class KitchenMenuItemsFragment extends Fragment implements SearchView.OnQueryTextListener {


    private KitchenMenusActivity kitchenMenusActivity;
    private LinearLayoutManager  linearLayoutManager;
    private KitchenMenuListAdapter kitchenMenuListAdapter;

    private SearchView searchView;
    private MenuItem searchMenuItem;

    List<KitchenMenuItemsEntity> kitchenMenuItemsModels = new ArrayList<>();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Do something that differs the Activity's menu here
        super.onCreateOptionsMenu(menu, inflater);


        SearchManager searchManager = (SearchManager) kitchenMenusActivity.getSystemService(Context.SEARCH_SERVICE);
        searchMenuItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) searchMenuItem.getActionView();

        searchView.setSearchableInfo(searchManager.getSearchableInfo(kitchenMenusActivity.getComponentName()));
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(this);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View  view =   inflater.inflate(R.layout.fragment_kitchen_menu_list, container, false);
        init(view);

        return view;
    }




    private void init(View view){
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.kitchen_menu_items_list_fragment);
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
        List<KitchenMenuItemsEntity> temp =  kitchenMenusActivity.getKitchenService().getKitchenMenuItemsModels(null);
        kitchenMenuItemsModels.addAll(temp);
        kitchenMenuListAdapter = new KitchenMenuListAdapter(kitchenMenuItemsModels,kitchenMenusActivity);
        recyclerView.setAdapter(kitchenMenuListAdapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(kitchenMenusActivity,linearLayoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
    }






    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onQueryTextChange(String newText) {
        List<KitchenMenuItemsEntity> temp =  kitchenMenusActivity.getKitchenService().getKitchenMenuItemsModels(newText);
        kitchenMenuItemsModels.clear();
        kitchenMenuItemsModels.addAll(temp);
        kitchenMenuListAdapter.notifyDataSetChanged();
        return true;
    }






    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        kitchenMenusActivity = (KitchenMenusActivity)context;

    }


}
