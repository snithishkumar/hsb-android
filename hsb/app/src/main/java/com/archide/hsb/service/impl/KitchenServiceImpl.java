package com.archide.hsb.service.impl;

import android.accounts.Account;
import android.content.ContentResolver;
import android.content.Context;
import android.os.Bundle;

import com.archide.hsb.dao.KitchenDao;
import com.archide.hsb.dao.impl.KitchenDaoImpl;
import com.archide.hsb.entity.KitchenOrderDetailsEntity;
import com.archide.hsb.entity.KitchenOrdersCategoryEntity;
import com.archide.hsb.entity.KitchenOrdersListEntity;
import com.archide.hsb.enumeration.FoodType;
import com.archide.hsb.enumeration.ViewStatus;
import com.archide.hsb.service.KitchenService;
import com.archide.hsb.sync.HsbSyncAdapter;
import com.archide.hsb.sync.SyncEvent;
import com.archide.hsb.view.model.KitchenOrderDetailsViewModel;
import com.archide.hsb.view.model.KitchenOrderListViewModel;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import hsb.archide.com.hsb.R;

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
                String vegData = String.format("%02d",vegCount);

                kitchenOrderListViewModel.setVegCount(vegData);
                String nonVegData = String.format("%02d",nonVegCount);
                kitchenOrderListViewModel.setNonVegCount(nonVegData);

                kitchenOrderListViewModel.setOrderTime(getOrderTime(kitchenOrdersListEntity.getOrderDateTime()));
                kitchenOrderListViewModel.setLastOrderTime(getLastOrderTime(kitchenOrdersListEntity.getLastUpdateTime()));
                kitchenOrderListViewModels.add(kitchenOrderListViewModel);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return kitchenOrderListViewModels;
    }


    public List<KitchenOrderDetailsViewModel> getKitchenOrderDetails(String orderId){
        List<KitchenOrderDetailsViewModel> results = new ArrayList<>();
        try {
            KitchenOrdersListEntity kitchenOrdersListEntity = kitchenDao.getKitchenOrdersListEntity(orderId);
            List<KitchenOrdersCategoryEntity> ordersCategoryEntities =  kitchenDao.getKitchenOrdersCategory(kitchenOrdersListEntity);
            for(KitchenOrdersCategoryEntity kitchenOrdersCategoryEntity : ordersCategoryEntities){
               List<KitchenOrderDetailsEntity> kitchenOrderDetailsEntities =  kitchenDao.getKitchenOrderDetailsEntity(kitchenOrdersCategoryEntity);
               int i = 0;
                for(KitchenOrderDetailsEntity kitchenOrderDetailsEntity : kitchenOrderDetailsEntities){
                  if( i == 0){
                      KitchenOrderDetailsViewModel kitchenOrderDetailsViewModel = new KitchenOrderDetailsViewModel(kitchenOrdersCategoryEntity);
                      results.add(kitchenOrderDetailsViewModel);
                  }
                    i += 1;
                   KitchenOrderDetailsViewModel kitchenOrderDetailsViewModel = new KitchenOrderDetailsViewModel(kitchenOrderDetailsEntity);
                   results.add(kitchenOrderDetailsViewModel);
               }
           }

        }catch (Exception e){
            e.printStackTrace();
        }
        return results;
    }


    @Override
    public void updateKitchenOrderViewStatus(String orderId,List<KitchenOrderDetailsViewModel> detailsViewModels ){
        try {
            kitchenDao.updateKitchenOrderListViewStatus(orderId);
            for(KitchenOrderDetailsViewModel kitchenOrderDetailsViewModel : detailsViewModels){
                kitchenDao.updateKitchenOrderDetailsViewStatus(kitchenOrderDetailsViewModel.getId());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void saveOrderStatus(List<KitchenOrderDetailsViewModel> detailsViewModels,String orderId,Context context ){
        try{
            for(KitchenOrderDetailsViewModel kitchenOrderDetailsViewModel : detailsViewModels){
                kitchenDao.updateKitchenOrderDetailsViewStatus(kitchenOrderDetailsViewModel.getId(),kitchenOrderDetailsViewModel.getStatus(),kitchenOrderDetailsViewModel.getUnAvailableCount());
            }
            kitchenDao.updateKitchenOrderListViewSync(orderId);


        }catch (Exception e){
            e.printStackTrace();
        }
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
