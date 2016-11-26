package com.archide.hsb.service.impl;

import android.accounts.Account;
import android.content.ContentResolver;
import android.content.Context;
import android.os.Bundle;

import com.archide.hsb.dao.MenuItemsDao;
import com.archide.hsb.dao.OrdersDao;
import com.archide.hsb.dao.impl.MenuItemsDaoImpl;
import com.archide.hsb.dao.impl.OrdersDaoImpl;
import com.archide.hsb.entity.MenuEntity;
import com.archide.hsb.entity.PlacedOrderItemsEntity;
import com.archide.hsb.entity.PlacedOrdersEntity;
import com.archide.hsb.service.OrderService;
import com.archide.hsb.sync.HsbSyncAdapter;
import com.archide.hsb.sync.SyncEvent;
import com.archide.hsb.view.activities.ActivityUtil;
import com.archide.hsb.view.model.MenuItemsViewModel;
import com.archide.hsb.view.model.OrderDetailsViewModel;
import com.archide.hsb.view.model.PlaceAnOrderViewModel;
import com.j256.ormlite.field.DatabaseField;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import hsb.archide.com.hsb.R;

/**
 * Created by Nithish on 19/11/16.
 */

public class OrderServiceImpl implements OrderService {

    private OrdersDao ordersDao;
    private MenuItemsDao menuItemsDao;

    private Bundle settingsBundle;
    private Account account ;


    public OrderServiceImpl(Context context){
        try{
            ordersDao = new OrdersDaoImpl(context);
            menuItemsDao = new MenuItemsDaoImpl(context);
            init();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void addOrderItems(MenuItemsViewModel menuItemsViewModel) {
        try{
            if(menuItemsViewModel.getCount() > 1){
                ordersDao.updateOrdersCount(menuItemsViewModel.getItemCode(),menuItemsViewModel.getCount());
            }else{
                createOrderItems(menuItemsViewModel);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void createOrderItems(MenuItemsViewModel menuItemsViewModel)throws SQLException{
        MenuEntity menuEntity = menuItemsDao.getMenuItemEntity(menuItemsViewModel.getUuid());
        PlacedOrderItemsEntity placedOrderItemsEntity = new PlacedOrderItemsEntity();
        placedOrderItemsEntity.setConform(false);
        placedOrderItemsEntity.setMenuItem(menuEntity);
        placedOrderItemsEntity.setMenuCourseEntity(menuEntity.getMenuCourseEntity());
        placedOrderItemsEntity.setItemCode(menuItemsViewModel.getItemCode());
        placedOrderItemsEntity.setName(menuItemsViewModel.getName());
        placedOrderItemsEntity.setPlacedOrderItemsUUID(UUID.randomUUID().toString());
        placedOrderItemsEntity.setQuantity(menuItemsViewModel.getCount());
        placedOrderItemsEntity.setCost(menuItemsViewModel.getCost());
        ordersDao.createPlacedOrdersItemsEntity(placedOrderItemsEntity);
    }

    @Override
    public void removeOrderItems(MenuItemsViewModel menuItemsViewModel) {
        try{
            ordersDao.removeOrderByItemCode(menuItemsViewModel.getItemCode());
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public PlaceAnOrderViewModel getCurrentOrderDetails(){
        try{
            List<PlacedOrderItemsEntity> placedOrderItemsEntityList = ordersDao.getPlacedOrderItemsEntity();
           return calculateAmountDetails(placedOrderItemsEntityList);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }


    public void calcAmount(PlaceAnOrderViewModel placeAnOrderViewModel){
        double subTotal = 0;
        List<MenuItemsViewModel> menuItemsViewModels = placeAnOrderViewModel.getMenuItemsViewModels();
        for(MenuItemsViewModel menuItemsViewModel : menuItemsViewModels){
            subTotal = subTotal + ( menuItemsViewModel.getCost() * menuItemsViewModel.getCount());
        }
        placeAnOrderViewModel.setSubTotalBeforeDiscount(subTotal);
        placeAnOrderViewModel.setSubTotal(subTotal);
        placeAnOrderViewModel.setDiscount(0);
        placeAnOrderViewModel.setServiceTax(0);
        placeAnOrderViewModel.setServiceVat(0);
        placeAnOrderViewModel.setTotalAmount(subTotal);
    }

    public PlaceAnOrderViewModel calculateAmountDetails(List<PlacedOrderItemsEntity> placedOrderItemsEntityList){
        PlaceAnOrderViewModel placeAnOrderViewModel = new PlaceAnOrderViewModel();
        double subTotal = 0;
        for(PlacedOrderItemsEntity orderItemsEntity : placedOrderItemsEntityList){
            subTotal = subTotal + orderItemsEntity.getCost() * orderItemsEntity.getQuantity();
            MenuItemsViewModel menuItemsViewModel = new MenuItemsViewModel(orderItemsEntity);
            placeAnOrderViewModel.getMenuItemsViewModels().add(menuItemsViewModel);
        }
        placeAnOrderViewModel.setSubTotalBeforeDiscount(subTotal);
        placeAnOrderViewModel.setSubTotal(subTotal);
        placeAnOrderViewModel.setDiscount(0);
        placeAnOrderViewModel.setServiceTax(0);
        placeAnOrderViewModel.setServiceVat(0);
        placeAnOrderViewModel.setTotalAmount(subTotal);
        return placeAnOrderViewModel;
    }


    public void conformOrder(PlaceAnOrderViewModel placeAnOrderViewModel,String tableNumber,Context context){
        try{
          PlacedOrdersEntity placedOrdersEntity =  ordersDao.getPlacedOrdersEntity();
            if(placedOrdersEntity == null){
                placedOrdersEntity = new PlacedOrdersEntity();
                placedOrdersEntity.setPlaceOrdersUUID(UUID.randomUUID().toString());
                placedOrdersEntity.setOrderId(generateOrderId());
                placedOrdersEntity.setTableNumber(tableNumber);
                placedOrdersEntity.setOrderDateTime(System.currentTimeMillis());
                placedOrdersEntity.setPrice(placeAnOrderViewModel.getSubTotal());
                placedOrdersEntity.setTaxAmount(placeAnOrderViewModel.getServiceTax());
                placedOrdersEntity.setDiscount(placeAnOrderViewModel.getDiscount());
                placedOrdersEntity.setTotalPrice(placeAnOrderViewModel.getTotalAmount());
                placedOrdersEntity.setDateTime(System.currentTimeMillis());

                ordersDao.createPlacedOrdersEntity(placedOrdersEntity);
            }
            ordersDao.removeCurrentOrder();
            List<MenuItemsViewModel> menuItemsViewModels =  placeAnOrderViewModel.getMenuItemsViewModels();
            for(MenuItemsViewModel menuItemsViewModel : menuItemsViewModels){
                createOrderItems(menuItemsViewModel);
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        account = HsbSyncAdapter.getSyncAccount(context);
        settingsBundle.putInt("currentScreen", SyncEvent.PLACE_AN_ORDER);
        settingsBundle.putString("tableNumber", tableNumber);
        ContentResolver.requestSync(account, context.getString(R.string.auth_type), settingsBundle);
    }


    private String generateOrderId(){
        StringBuilder stringBuilder = new StringBuilder("ARC-");
        Calendar calendar = Calendar.getInstance();
        stringBuilder.append(calendar.get(Calendar.HOUR_OF_DAY));
        stringBuilder.append(calendar.get(Calendar.MINUTE));
        stringBuilder.append(calendar.get(Calendar.SECOND));
        stringBuilder.append(calendar.get(Calendar.MILLISECOND));
        Random random = new Random();
        int ran = random.nextInt();
        stringBuilder.append(ran);
       return stringBuilder.toString();
    }


    private void init(){
        if(settingsBundle == null){
            settingsBundle = new Bundle();
            settingsBundle.putBoolean(
                    ContentResolver.SYNC_EXTRAS_MANUAL, true);
            settingsBundle.putBoolean(
                    ContentResolver.SYNC_EXTRAS_EXPEDITED, true);

        }

    }


    @Override
    public PlaceAnOrderViewModel getPlacedHistoryOrderViewModel(){
        PlaceAnOrderViewModel placeAnOrderViewModel = new PlaceAnOrderViewModel();
        try{

            PlacedOrdersEntity placedOrdersEntity =   ordersDao.getPlacedOrderHistoryByMobile(ActivityUtil.USER_MOBILE);
            if(placedOrdersEntity != null){
                List<PlacedOrderItemsEntity> placedOrderItemsEntityList =  ordersDao.getPlacedOrderHistoryItems(placedOrdersEntity);
                for(PlacedOrderItemsEntity orderItemsEntity : placedOrderItemsEntityList){
                    MenuItemsViewModel menuItemsViewModel = new MenuItemsViewModel(orderItemsEntity);
                    placeAnOrderViewModel.getMenuItemsViewModels().add(menuItemsViewModel);
                }
            }

            placeAnOrderViewModel.setSubTotalBeforeDiscount(placedOrdersEntity.getPrice());
            placeAnOrderViewModel.setSubTotal(placedOrdersEntity.getPrice());
            placeAnOrderViewModel.setDiscount(0);
            placeAnOrderViewModel.setServiceTax(0);
            placeAnOrderViewModel.setServiceVat(0);
            placeAnOrderViewModel.setTotalAmount(placedOrdersEntity.getTotalPrice());
            return placeAnOrderViewModel;
        }catch (Exception e){
            e.printStackTrace();
        }
        return placeAnOrderViewModel;
    }


}
