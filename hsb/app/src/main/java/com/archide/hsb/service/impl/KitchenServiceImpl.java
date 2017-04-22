package com.archide.hsb.service.impl;

import android.content.Context;

import com.archide.hsb.dao.KitchenDao;
import com.archide.hsb.dao.impl.KitchenDaoImpl;
import com.archide.hsb.entity.KitchenCookingCmntsEntity;
import com.archide.hsb.entity.KitchenMenuItemsEntity;
import com.archide.hsb.entity.KitchenOrderDetailsEntity;
import com.archide.hsb.entity.KitchenOrdersCategoryEntity;
import com.archide.hsb.entity.KitchenOrdersListEntity;
import com.archide.hsb.enumeration.FoodType;
import com.archide.hsb.enumeration.OrderStatus;
import com.archide.hsb.enumeration.OrderType;
import com.archide.hsb.service.KitchenService;
import com.archide.hsb.view.model.KitchenCommentsViewModel;
import com.archide.hsb.view.model.KitchenOrderDetailsViewModel;
import com.archide.hsb.view.model.KitchenOrderListViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by Nithish on 26/11/16.
 */

public class KitchenServiceImpl implements KitchenService {

    private KitchenDao kitchenDao;
    private Context context;

    public KitchenServiceImpl(Context context){
        try{
            kitchenDao = new KitchenDaoImpl(context);
            this.context = context;
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
    public List<KitchenCommentsViewModel> getKitchenCommentsViewModel(String orderId){
        List<KitchenCommentsViewModel> results = new ArrayList<>();
        try{
            KitchenOrdersListEntity kitchenOrdersListEntity = kitchenDao.getKitchenOrdersListEntity(orderId);
            List<KitchenCookingCmntsEntity> kitchenCookingCmntsList =  kitchenDao.getKitchenCookingCmntsEntity(kitchenOrdersListEntity);
            for(KitchenCookingCmntsEntity kitchenCookingCmntsEntity : kitchenCookingCmntsList){
                KitchenCommentsViewModel kitchenCommentsViewModel = new KitchenCommentsViewModel(kitchenCookingCmntsEntity);
                results.add(kitchenCommentsViewModel);
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
            int notDelivered = 0;
            for(KitchenOrderDetailsViewModel kitchenOrderDetailsViewModel : detailsViewModels){
                if(!kitchenOrderDetailsViewModel.isCategory() && kitchenOrderDetailsViewModel.isEdited()){
                    kitchenDao.updateKitchenOrderDetailsViewStatus(kitchenOrderDetailsViewModel.getId(),
                            kitchenOrderDetailsViewModel.getStatus(),kitchenOrderDetailsViewModel.getUnAvailableCount(),
                            Integer.valueOf(kitchenOrderDetailsViewModel.getQuantity()));

                    if(!kitchenOrderDetailsViewModel.getStatus().toString().equals(OrderStatus.DELIVERED.toString())){
                        notDelivered += 1;
                    }
                }

            }
            OrderType orderType = getOrderType(orderId);
            if(notDelivered == 0 && orderType.toString().equals(OrderType.TakeAway.toString())){
                kitchenDao.closeOrders(orderId);
            }
            kitchenDao.updateKitchenOrderListViewSync(orderId);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

  /*  private void syncData(){
        Account account = HsbSyncAdapter.getSyncAccount(context);
        Bundle settingsBundle = new Bundle();
        settingsBundle.putBoolean(
                ContentResolver.SYNC_EXTRAS_MANUAL, true);
        settingsBundle.putBoolean(
                ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        settingsBundle.putInt("currentScreen", SyncEvent.GET_KITCHEN_ORDERS_DATA);
        ContentResolver.requestSync(account, context.getString(R.string.auth_type), settingsBundle);
    }*/





    private String getOrderTime(long orderDateTime){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm a");
        return  simpleDateFormat.format(new Date(orderDateTime));
    }


    private String getLastOrderTime(long lastUpdatedDatTime){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
        return  simpleDateFormat.format(new Date(lastUpdatedDatTime));
    }


    public List<KitchenMenuItemsEntity> getKitchenMenuItemsModels(String searchText){
        try{
            List<KitchenMenuItemsEntity> kitchenMenuItemsEntities =   kitchenDao.getKitchenMenuItems(searchText);
            for(KitchenMenuItemsEntity kitchenMenuItemsEntity : kitchenMenuItemsEntities){
                kitchenMenuItemsEntity.setRemainingCount(kitchenMenuItemsEntity.getMaxCount() - kitchenMenuItemsEntity.getCurrentCount());
            }
            Collections.sort(kitchenMenuItemsEntities);
            return kitchenMenuItemsEntities;
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public void updateKitchenMenuItemsEntity(KitchenMenuItemsEntity kitchenMenuItemsEntity){
       try{
           kitchenDao.updateKitchenMenuItemsEntity(kitchenMenuItemsEntity);

       }catch (Exception e){
           e.printStackTrace();
       }

    }


    @Override
    public OrderType getOrderType(String orderId){
        try{
            KitchenOrdersListEntity kitchenOrdersListEntity =  kitchenDao.getKitchenOrdersListEntity(orderId);
           return kitchenOrdersListEntity.getOrderType();
        }catch (Exception e){
e.printStackTrace();
        }
        return OrderType.TakeAway;
    }
}
