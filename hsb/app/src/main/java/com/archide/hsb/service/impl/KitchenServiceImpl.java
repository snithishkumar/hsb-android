package com.archide.hsb.service.impl;

import android.content.Context;

import com.archide.hsb.dao.KitchenDao;
import com.archide.hsb.dao.impl.KitchenDaoImpl;
import com.archide.hsb.entity.KitchenOrdersListEntity;
import com.archide.hsb.enumeration.FoodType;
import com.archide.hsb.enumeration.ViewStatus;
import com.archide.hsb.service.KitchenService;
import com.archide.hsb.view.model.KitchenOrderListViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Nithish on 26/11/16.
 */

public class KitchenServiceImpl implements KitchenService {

    private KitchenDao kitchenDao;

    public KitchenServiceImpl(Context context){
        try{
            kitchenDao = new KitchenDaoImpl(context);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public List<KitchenOrderListViewModel> getOrderList(){
        List<KitchenOrderListViewModel> kitchenOrderListViewModels = new ArrayList<>();
        try{
            List<KitchenOrdersListEntity> kitchenOrdersList =  kitchenDao.getUnClosedKitchenOrdersList();
            for(KitchenOrdersListEntity kitchenOrdersListEntity : kitchenOrdersList){
                KitchenOrderListViewModel kitchenOrderListViewModel = new KitchenOrderListViewModel();
                kitchenOrderListViewModel.setOrderId(kitchenOrdersListEntity.getOrderId());
                kitchenOrderListViewModel.setTableNumber(kitchenOrdersListEntity.getTableNumber());
                kitchenOrderListViewModel.setViewStatus(kitchenOrdersListEntity.getViewStatus());

                long vegCount = kitchenDao.getCountOf(FoodType.VEG,kitchenOrdersListEntity);
                long nonVegCount = kitchenDao.getCountOf(FoodType.NONVEG,kitchenOrdersListEntity);
                kitchenOrderListViewModel.setVegCount(String.valueOf(vegCount));
                kitchenOrderListViewModel.setNonVegCount(String.valueOf(nonVegCount));
                if(vegCount > 0 && nonVegCount > 0){
                    kitchenOrderListViewModel.setFoodType(FoodType.BOTH);
                }else if(vegCount > 0 ){
                    kitchenOrderListViewModel.setFoodType(FoodType.VEG);
                }else {
                    kitchenOrderListViewModel.setFoodType(FoodType.NONVEG);
                }
                kitchenOrderListViewModel.setOrderTime(getOrderTime(kitchenOrdersListEntity.getOrderDateTime()));
                kitchenOrderListViewModel.setLastOrderTime(getLastOrderTime(kitchenOrdersListEntity.getLastUpdateTime()));
                kitchenOrderListViewModels.add(kitchenOrderListViewModel);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return kitchenOrderListViewModels;
    }


    private String getOrderTime(long orderDateTime){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm a");
        return  simpleDateFormat.format(new Date(orderDateTime));
    }


    private String getLastOrderTime(long lastUpdatedDatTime){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
        return  simpleDateFormat.format(new Date(lastUpdatedDatTime));
    }
}
