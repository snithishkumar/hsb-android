package com.archide.hsb.view.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.archide.hsb.entity.KitchenMenuItemsEntity;
import com.archide.hsb.view.activities.ActivityUtil;
import com.archide.hsb.view.activities.KitchenMenusActivity;

import java.util.List;

import hsb.archide.com.hsb.R;

/**
 * Created by Nithish on 04/03/17.
 */

public class KitchenMenuListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<KitchenMenuItemsEntity> kitchenMenuItemsEntities = null;
    private KitchenMenusActivity kitchenMenusActivity = null;

    public KitchenMenuListAdapter(List<KitchenMenuItemsEntity> kitchenMenuItemsModels,KitchenMenusActivity kitchenMenusActivity){
        this.kitchenMenuItemsEntities = kitchenMenuItemsModels;
        this.kitchenMenusActivity = kitchenMenusActivity;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View  view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapt_kitchen_menu_list, parent, false);
        return new KitchenMenuListAdapter.KitchenMenuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, int position) {
        KitchenMenuItemsEntity kitchenMenuItemsEntity =  kitchenMenuItemsEntities.get(position);
        KitchenMenuListAdapter.KitchenMenuViewHolder kitchenMenuViewHolder = (KitchenMenuListAdapter.KitchenMenuViewHolder)viewHolder;
        kitchenMenuViewHolder.vMenuName.setText(kitchenMenuItemsEntity.getName());
        kitchenMenuViewHolder.vItemNumber.setText("Item Code:"+kitchenMenuItemsEntity.getMenuItemCode());
        kitchenMenuViewHolder.vCurrentCount.setText("Current Count:"+kitchenMenuItemsEntity.getCurrentCount());
        kitchenMenuViewHolder.vTotalCount.setText("Total Count:"+kitchenMenuItemsEntity.getMaxCount());
        kitchenMenuViewHolder.vRemainingCount.setText(String.valueOf(kitchenMenuItemsEntity.getRemainingCount() ));
    }

    public class  KitchenMenuViewHolder extends RecyclerView.ViewHolder{
        private TextView vMenuName;
        private TextView vCurrentCount;
        private TextView vTotalCount;
        private EditText vRemainingCount;
        private ImageView vMenuUpdates;
        private TextView vItemNumber;
        public KitchenMenuViewHolder(View view){
            super(view);
            vMenuName = (TextView)view.findViewById(R.id.menu_item_name);
            vCurrentCount = (TextView)view.findViewById(R.id.menus_ordered_count);
            vTotalCount = (TextView)view.findViewById(R.id.total_count);
            vItemNumber = (TextView)view.findViewById(R.id.menu_item_number);

            vRemainingCount = (EditText)view.findViewById(R.id.remaining_count);

            vMenuUpdates = (ImageView)view.findViewById(R.id.menu_updates_save);
            vMenuUpdates.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   int pos =  getAdapterPosition();
                    KitchenMenuItemsEntity kitchenMenuItemsEntity =  kitchenMenuItemsEntities.get(pos);
                    kitchenMenuItemsEntity.setEdited(true);
                    String val =  vRemainingCount.getText().toString();
                    if(val == null || val.trim().isEmpty()){
                        ActivityUtil.showDialog(kitchenMenusActivity,kitchenMenusActivity.getString(R.string.kitchen_menu_value_val_title),kitchenMenusActivity.getString(R.string.kitchen_menu_value_val));
                        return;
                    }
                    int count =   Integer.valueOf(val);
                    kitchenMenuItemsEntity.setMaxCount(kitchenMenuItemsEntity.getCurrentCount() + count);
                    kitchenMenusActivity.getKitchenService().updateKitchenMenuItemsEntity(kitchenMenuItemsEntity);

                    List<KitchenMenuItemsEntity> temp =  kitchenMenusActivity.getKitchenService().getKitchenMenuItemsModels(null);
                    kitchenMenuItemsEntities.clear();
                    kitchenMenuItemsEntities.addAll(temp);
                    notifyDataSetChanged();
                    ActivityUtil.toast(kitchenMenusActivity,kitchenMenusActivity.getString(R.string.kitchen_available_status_update));

                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return kitchenMenuItemsEntities.size();
    }
}
