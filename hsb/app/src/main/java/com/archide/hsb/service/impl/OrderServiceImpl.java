package com.archide.hsb.service.impl;

import android.content.Context;

import com.archide.hsb.dao.MenuItemsDao;
import com.archide.hsb.dao.OrdersDao;
import com.archide.hsb.dao.impl.MenuItemsDaoImpl;
import com.archide.hsb.dao.impl.OrdersDaoImpl;
import com.archide.hsb.entity.MenuEntity;
import com.archide.hsb.entity.PlacedOrderItemsEntity;
import com.archide.hsb.entity.PlacedOrdersEntity;
import com.archide.hsb.service.OrderService;
import com.archide.hsb.view.model.MenuItemsViewModel;
import com.archide.hsb.view.model.PlaceAnOrderViewModel;

import java.util.Calendar;
import java.util.List;
import java.util.Random;
import java.util.UUID;

/**
 * Created by Nithish on 19/11/16.
 */

public class OrderServiceImpl implements OrderService {

    private OrdersDao ordersDao;
    private MenuItemsDao menuItemsDao;

    public OrderServiceImpl(Context context){
        try{
            ordersDao = new OrdersDaoImpl(context);
            menuItemsDao = new MenuItemsDaoImpl(context);
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
                MenuEntity menuEntity = menuItemsDao.getMenuItemEntity(menuItemsViewModel.getUuid());
                PlacedOrderItemsEntity placedOrderItemsEntity = new PlacedOrderItemsEntity();
                placedOrderItemsEntity.setConform(false);
                placedOrderItemsEntity.setMenuItem(menuEntity);
                placedOrderItemsEntity.setItemCode(menuItemsViewModel.getItemCode());
                placedOrderItemsEntity.setName(menuItemsViewModel.getName());
                placedOrderItemsEntity.setPlacedOrderItemsUUID(UUID.randomUUID().toString());
                placedOrderItemsEntity.setQuantity(menuItemsViewModel.getCount());
                placedOrderItemsEntity.setCost(menuItemsViewModel.getCost());
                ordersDao.createPlacedOrdersItemsEntity(placedOrderItemsEntity);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

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


    public void placeOrder(){
        try{
          PlacedOrdersEntity placedOrdersEntity =  ordersDao.getPlacedOrdersEntity();
            if(placedOrdersEntity == null){
                placedOrdersEntity = new PlacedOrdersEntity();
                placedOrdersEntity.setOrderDateTime(System.currentTimeMillis());
                placedOrdersEntity.setOrderId(generateOrderId());
                placedOrdersEntity.setDateTime(System.currentTimeMillis());
                placedOrdersEntity.setPlaceOrdersUUID(UUID.randomUUID().toString());
                ordersDao.createPlacedOrdersEntity(placedOrdersEntity);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
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


}
