package com.archide.hsb.service.impl;

import android.accounts.Account;
import android.content.ContentResolver;
import android.content.Context;
import android.os.Bundle;

import com.archide.hsb.dao.AdminDao;
import com.archide.hsb.dao.MenuItemsDao;
import com.archide.hsb.dao.OrdersDao;
import com.archide.hsb.dao.impl.AdminDaoImpl;
import com.archide.hsb.dao.impl.MenuItemsDaoImpl;
import com.archide.hsb.dao.impl.OrdersDaoImpl;
import com.archide.hsb.entity.MenuEntity;
import com.archide.hsb.entity.PlacedOrderItemsEntity;
import com.archide.hsb.entity.PlacedOrdersEntity;
import com.archide.hsb.enumeration.OrderStatus;
import com.archide.hsb.enumeration.Status;
import com.archide.hsb.service.OrderService;
import com.archide.hsb.sync.HsbSyncAdapter;
import com.archide.hsb.sync.SyncEvent;
import com.archide.hsb.util.Utilities;
import com.archide.hsb.view.activities.ActivityUtil;
import com.archide.hsb.view.model.CloseOrderViewModel;
import com.archide.hsb.view.model.MenuItemsViewModel;
import com.archide.hsb.view.model.PlaceAnOrderViewModel;

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
    private Context context;


    public OrderServiceImpl(Context context){
        try{
            ordersDao = new OrdersDaoImpl(context);
            menuItemsDao = new MenuItemsDaoImpl(context);
            this.context = context;
            init();
        }catch (Exception e){
            e.printStackTrace();
        }

    }


    @Override
    public void updateOrderItems(MenuItemsViewModel menuItemsViewModel){
        try{
            ordersDao.updateOrdersCount(menuItemsViewModel.getItemCode(),menuItemsViewModel.getCount());
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
        placedOrderItemsEntity.setOrderStatus(OrderStatus.ORDERED);
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


    public void removeUnAvailableOrders(){
        try {
            ordersDao.removeUnAvailablePlacedOrders();
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public void calcAmount(PlaceAnOrderViewModel placeAnOrderViewModel){
        double subTotal = 0;
        List<MenuItemsViewModel> menuItemsViewModels = placeAnOrderViewModel.getMenuItemsViewModels();
        for(MenuItemsViewModel menuItemsViewModel : menuItemsViewModels){
           double cost =  Utilities.roundOff(menuItemsViewModel.getCost() * menuItemsViewModel.getCount());
            subTotal = subTotal + cost;
        }
        subTotal = Utilities.roundOff(subTotal);
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
            String status =  orderItemsEntity.getOrderStatus().toString();
            if(status.equals(Status.UN_AVAILABLE.toString())){
                placeAnOrderViewModel.setUnAvailable(true);
            }else{
                subTotal = subTotal + Utilities.roundOff((orderItemsEntity.getCost() * orderItemsEntity.getQuantity()));
            }

            MenuItemsViewModel menuItemsViewModel = new MenuItemsViewModel(orderItemsEntity);
            OrderStatus orderStatus = OrderStatus.valueOf(status);
            menuItemsViewModel.setOrderStatus(orderStatus);
            placeAnOrderViewModel.getMenuItemsViewModels().add(menuItemsViewModel);
        }
        subTotal = Utilities.roundOff(subTotal);
        placeAnOrderViewModel.setSubTotalBeforeDiscount(subTotal);
        placeAnOrderViewModel.setSubTotal(subTotal);
        placeAnOrderViewModel.setDiscount(0);
        placeAnOrderViewModel.setServiceTax(0);
        placeAnOrderViewModel.setServiceVat(0);
        placeAnOrderViewModel.setTotalAmount(subTotal);
        return placeAnOrderViewModel;
    }

    private void createPlacedOrders(PlacedOrdersEntity placedOrdersEntity,PlaceAnOrderViewModel placeAnOrderViewModel,String mobileNumber,String tableNumber)throws SQLException{

        placedOrdersEntity.setPlaceOrdersUUID(UUID.randomUUID().toString());
        placedOrdersEntity.setOrderId(generateOrderId());
        placedOrdersEntity.setTableNumber(tableNumber);
        placedOrdersEntity.setUserMobileNumber(mobileNumber);
        placedOrdersEntity.setOrderDateTime(System.currentTimeMillis());
        placedOrdersEntity.setPrice(placeAnOrderViewModel.getSubTotal());
        placedOrdersEntity.setTaxAmount(placeAnOrderViewModel.getServiceTax());
        placedOrdersEntity.setDiscount(placeAnOrderViewModel.getDiscount());
        placedOrdersEntity.setTotalPrice(placeAnOrderViewModel.getTotalAmount());
        placedOrdersEntity.setDateTime(System.currentTimeMillis());
        placedOrdersEntity.setComments(placeAnOrderViewModel.getCookingComments());
        ordersDao.createPlacedOrdersEntity(placedOrdersEntity);
    }





    public void conformOrder(PlaceAnOrderViewModel placeAnOrderViewModel,String mobileNumber,String tableNumber,Context context){
        try{
            PlacedOrdersEntity placedOrdersEntity =  ordersDao.getPlacedOrdersEntity();
            if(placedOrdersEntity != null && placedOrdersEntity.isClosed()){
                ordersDao.removePlacedOrder(placedOrdersEntity);
                AdminDao adminDao = new AdminDaoImpl(context);
                adminDao.updateUser(ActivityUtil.USER_MOBILE);
                placedOrdersEntity = new PlacedOrdersEntity();
                createPlacedOrders(placedOrdersEntity,placeAnOrderViewModel,mobileNumber,tableNumber);
            }
            if(placedOrdersEntity == null ){
                placedOrdersEntity = new PlacedOrdersEntity();
                createPlacedOrders(placedOrdersEntity,placeAnOrderViewModel,mobileNumber,tableNumber);
            }else{
                placedOrdersEntity.setTotalPrice(placedOrdersEntity.getTotalPrice() + placeAnOrderViewModel.getTotalAmount());
                placedOrdersEntity.setComments(placeAnOrderViewModel.getCookingComments());
                ordersDao.updatePlacedOrdersEntity(placedOrdersEntity);
            }
           // List<PlacedOrderItemsEntity> itemsEntities = ordersDao.getPlacedOrderItemsEntity();
            ordersDao.removeCurrentOrder();
           // List<PlacedOrderItemsEntity> itemsEntities1 = ordersDao.getPlacedOrderItemsEntity();
            List<MenuItemsViewModel> menuItemsViewModels =  placeAnOrderViewModel.getMenuItemsViewModels();
            for(MenuItemsViewModel menuItemsViewModel : menuItemsViewModels){
                if(!menuItemsViewModel.getOrderStatus().toString().equals(OrderStatus.UN_AVAILABLE.toString())){
                    createOrderItems(menuItemsViewModel);
                }

            }

        }catch (Exception e){
            e.printStackTrace();
        }

        account = HsbSyncAdapter.getSyncAccount(context);
        settingsBundle.putInt("currentScreen", SyncEvent.PLACE_AN_ORDER);
        settingsBundle.putString("tableNumber", tableNumber);
        ContentResolver.requestSync(account, context.getString(R.string.auth_type), settingsBundle);
    }


    public void checkAvailability(Context context){
        account = HsbSyncAdapter.getSyncAccount(context);
        settingsBundle.putInt("currentScreen", SyncEvent.CHECK_AVAILABILITY);
        ContentResolver.requestSync(account, context.getString(R.string.auth_type), settingsBundle);
    }

    @Override
    public void getPreviousOrderFromServer(Context context,String tableNumber,String mobileNumber){
        account = HsbSyncAdapter.getSyncAccount(context);
        settingsBundle.putInt("currentScreen", SyncEvent.GET_PREVIOUS_ORDER);
        settingsBundle.putString("tableNumber", tableNumber);
        settingsBundle.putString("mobileNumber", mobileNumber);
        ContentResolver.requestSync(account, context.getString(R.string.auth_type), settingsBundle);
    }


    private String generateOrderId(){
        StringBuilder stringBuilder = new StringBuilder("T");
        stringBuilder.append(ActivityUtil.TABLE_NUMBER);
        stringBuilder.append("-");
        Calendar calendar = Calendar.getInstance();
        stringBuilder.append(calendar.get(Calendar.HOUR_OF_DAY));
        stringBuilder.append(calendar.get(Calendar.MINUTE));
        stringBuilder.append(calendar.get(Calendar.SECOND));
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
    public CloseOrderViewModel getBillingDetails(){

        try{

            PlacedOrdersEntity placedOrdersEntity =   ordersDao.getPlacedOrderHistoryByMobile(ActivityUtil.USER_MOBILE,ActivityUtil.TABLE_NUMBER);
            CloseOrderViewModel closeOrderViewModel = new CloseOrderViewModel();
            if(placedOrdersEntity != null){
                closeOrderViewModel.setOrderId(placedOrdersEntity.getOrderId());
                closeOrderViewModel.setTableNumber(placedOrdersEntity.getTableNumber());
                closeOrderViewModel.setUserMobileNumber(placedOrdersEntity.getUserMobileNumber());
                closeOrderViewModel.setTotalAmount(String.valueOf(Utilities.roundOff(placedOrdersEntity.getTotalPrice())));
            }


            return closeOrderViewModel;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public PlaceAnOrderViewModel getPlacedHistoryOrderViewModel(){
        PlaceAnOrderViewModel placeAnOrderViewModel = new PlaceAnOrderViewModel();
        try{

            PlacedOrdersEntity placedOrdersEntity =   ordersDao.getPlacedOrderHistoryByMobile(ActivityUtil.USER_MOBILE,ActivityUtil.TABLE_NUMBER);
            if(placedOrdersEntity != null){
                placeAnOrderViewModel.setOrderId(placedOrdersEntity.getOrderId());
                List<PlacedOrderItemsEntity> placedOrderItemsEntityList =  ordersDao.getPlacedOrderHistoryItems(placedOrdersEntity);
                for(PlacedOrderItemsEntity orderItemsEntity : placedOrderItemsEntityList){
                    MenuItemsViewModel menuItemsViewModel = new MenuItemsViewModel(orderItemsEntity);
                    placeAnOrderViewModel.getMenuItemsViewModels().add(menuItemsViewModel);
                }
            }
            calcAmount(placeAnOrderViewModel);

            return placeAnOrderViewModel;
        }catch (Exception e){
            e.printStackTrace();
        }
        return placeAnOrderViewModel;
    }


    @Override
    public void closeAnOrder(Context context,String tableNumber,String mobileNumber){
        try{
            account = HsbSyncAdapter.getSyncAccount(context);
            settingsBundle.putInt("currentScreen", SyncEvent.CLOSE_AN_ORDER);
            settingsBundle.putString("tableNumber", tableNumber);
            settingsBundle.putString("mobileNumber", mobileNumber);
            ContentResolver.requestSync(account, context.getString(R.string.auth_type), settingsBundle);
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    @Override
    public void resentBilling(Context context,String tableNumber,String mobileNumber){
        try{
            account = HsbSyncAdapter.getSyncAccount(context);
            settingsBundle.putInt("currentScreen", SyncEvent.RESENT_BILLING);
            settingsBundle.putString("tableNumber", tableNumber);
            settingsBundle.putString("mobileNumber", mobileNumber);
            ContentResolver.requestSync(account, context.getString(R.string.auth_type), settingsBundle);
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    @Override
    public void logout(Context context,String tableNumber,String mobileNumber){
        try{
            account = HsbSyncAdapter.getSyncAccount(context);
            settingsBundle.putInt("currentScreen", SyncEvent.LOG_OUT);
            settingsBundle.putString("tableNumber", tableNumber);
            settingsBundle.putString("mobileNumber", mobileNumber);
            ContentResolver.requestSync(account, context.getString(R.string.auth_type), settingsBundle);
        }catch (Exception e){
            e.printStackTrace();
        }

    }


    public void removeAllData(){
        try{
            ordersDao.removeAllData();
            if(ActivityUtil.USER_MOBILE != null){
                AdminDao adminDao = new AdminDaoImpl(context);
                adminDao.removeUser(ActivityUtil.USER_MOBILE);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void getMenuItems(String tableNumber,String mobileNumber,Context context){
        Account account = HsbSyncAdapter.getSyncAccount(context);
        Bundle settingsBundle = new Bundle();
        settingsBundle.putBoolean(
                ContentResolver.SYNC_EXTRAS_MANUAL, true);
        settingsBundle.putBoolean(
                ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        settingsBundle.putInt("currentScreen", SyncEvent.GET_MENU_LIST);
        settingsBundle.putString("tableNumber", tableNumber);
        settingsBundle.putString("mobileNumber", mobileNumber);
        ContentResolver.requestSync(account, context.getString(R.string.auth_type), settingsBundle);
    }


    @Override
    public void closeOrder(String mobileNumber){
        try{
            AdminDao adminDao = new AdminDaoImpl(context);
            adminDao.closeOrder(mobileNumber);
        }catch (Exception e){
            e.printStackTrace();
        }
    }





}
