package com.archide.hsb.view.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.archide.hsb.view.model.KitchenOrderListViewModel;

import java.util.ArrayList;
import java.util.List;

import hsb.archide.com.hsb.R;

/**
 * Created by Nithish on 25/11/16.
 *  http://www.pcsalt.com/android/listview-using-baseadapter-android/
 */

public class KitchenOrderListAdapter extends BaseAdapter {

    private static final int EMPTY_VIEW = -1;

    List<KitchenOrderListViewModel> kitchenOrderListViewModels = null;
    public KitchenOrderListAdapter(List<KitchenOrderListViewModel> kitchenOrderListViewModels){
        this.kitchenOrderListViewModels = kitchenOrderListViewModels;
    }

    @Override
    public int getCount() {
        return kitchenOrderListViewModels.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (kitchenOrderListViewModels.size() == 0) {
            return EMPTY_VIEW;
        }
        return super.getItemViewType(position);
    }



    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }


    public View getView(int position, View convertView, ViewGroup parent) {
        KitchenOrderListViewHolder kitchenOrderListViewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapt_kitchen_order_list, parent, false);
            kitchenOrderListViewHolder = new KitchenOrderListViewHolder(convertView);
            convertView.setTag(kitchenOrderListViewHolder);
        }else{
            kitchenOrderListViewHolder = (KitchenOrderListViewHolder) convertView.getTag();
        }
        KitchenOrderListViewModel kitchenOrderListViewModel = kitchenOrderListViewModels.get(position);
        populateKitchenOrderListViewHolder(kitchenOrderListViewModel,kitchenOrderListViewHolder);
        return convertView;
    }

    private void populateKitchenOrderListViewHolder(KitchenOrderListViewModel kitchenOrderListViewModel,KitchenOrderListViewHolder kitchenOrderListViewHolder){
        kitchenOrderListViewHolder.orderTime.setText(kitchenOrderListViewModel.getOrderTime());
        kitchenOrderListViewHolder.noOfVegCount.setText(kitchenOrderListViewModel.getVegCount());
       kitchenOrderListViewHolder.noOfNonVegCount.setText( kitchenOrderListViewModel.getNonVegCount());
        kitchenOrderListViewHolder.tableNumber.setText(kitchenOrderListViewModel.getTableNumber());
    }

    private class KitchenOrderListViewHolder{
        TextView orderTime;
        TextView noOfVegCount;
        TextView noOfNonVegCount;
        TextView tableNumber;

        public KitchenOrderListViewHolder(View item){
            orderTime =  (TextView)item.findViewById(R.id.kitchen_order_time);
            noOfVegCount =  (TextView) item.findViewById(R.id.kitchen_no_of_veg_count);
            noOfNonVegCount = (TextView)item.findViewById(R.id.kitchen_no_of_nonveg_count);
            tableNumber =  (TextView)item.findViewById(R.id.kitchen_table_number);

        }
    }
}
