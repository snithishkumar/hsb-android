package com.archide.hsb.view.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import com.archide.hsb.sync.json.ResponseData;
import com.archide.hsb.view.activities.HomeActivity;
import com.archide.hsb.view.activities.KitchenActivity;
import com.archide.hsb.view.adapters.KitchenOrderListAdapter;
import com.archide.hsb.view.model.KitchenOrderListViewModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import hsb.archide.com.hsb.R;

/**
 * Created by Nithish on 25/11/16.
 * http://www.pcsalt.com/android/listview-using-baseadapter-android/
 * http://androidlift.info/2015/12/24/android-recyclerview-cardview-example/
 */

public class KitchenOrderListFragment extends Fragment {

    GridView kitchenOrderList;
    private KitchenActivity kitchenActivity;
    private KitchenOrderListAdapter kitchenOrderListAdapter;
    List<KitchenOrderListViewModel> kitchenOrderListViewModels =new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_kitchen_order_list, container, false);
        kitchenOrderList =  (GridView)view.findViewById(R.id.gridview);
        kitchenOrderListAdapter =  new KitchenOrderListAdapter(kitchenOrderListViewModels);
        kitchenOrderList.setAdapter(kitchenOrderListAdapter);
        kitchenOrderList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Toast.makeText(getActivity(), "" + position,
                        Toast.LENGTH_SHORT).show();
                kitchenActivity.viewOrderDetails(position);
                return;
            }
        });
        loadData();
        return view;
    }

    private void loadData(){
        List<KitchenOrderListViewModel> temp = kitchenActivity.getKitchenService().getOrderList();
        kitchenOrderListViewModels.clear();
        kitchenOrderListViewModels.addAll(temp);
        kitchenOrderListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        kitchenActivity = (KitchenActivity)context;

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleServerSyncResponse(ResponseData responseData) {
        loadData();
    }

   public interface ViewOrderDetails{
        void viewOrderDetails(int id);
    }
}