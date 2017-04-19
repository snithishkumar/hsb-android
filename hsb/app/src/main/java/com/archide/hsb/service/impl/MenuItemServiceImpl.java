package com.archide.hsb.service.impl;

import android.accounts.Account;
import android.content.ContentResolver;
import android.content.Context;
import android.os.Bundle;

import com.archide.hsb.dao.MenuItemsDao;
import com.archide.hsb.dao.OrdersDao;
import com.archide.hsb.dao.impl.MenuItemsDaoImpl;
import com.archide.hsb.dao.impl.OrdersDaoImpl;
import com.archide.hsb.entity.FoodCategoryEntity;
import com.archide.hsb.entity.MenuCourseEntity;
import com.archide.hsb.entity.MenuEntity;
import com.archide.hsb.entity.PlacedOrderItemsEntity;
import com.archide.hsb.service.MenuItemService;
import com.archide.hsb.sync.HsbSyncAdapter;
import com.archide.hsb.sync.SyncEvent;
import com.archide.hsb.util.Utilities;
import com.archide.hsb.view.model.MenuItemsViewModel;
import com.archide.hsb.view.model.OrderDetailsViewModel;

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import hsb.archide.com.hsb.R;

/**
 * Created by Nithish on 16/11/16.
 */

public class MenuItemServiceImpl implements MenuItemService {

    private MenuItemsDao menuItemsDao;
    private OrdersDao ordersDao;
    private Context context;

    public  MenuItemServiceImpl(Context context){
        try{
            menuItemsDao = new MenuItemsDaoImpl(context);
            ordersDao = new OrdersDaoImpl(context);
            this.context = context;
        }catch (Exception e){

        }

    }

    @Override
    public List<MenuCourseEntity> getMenuCourseEntity(){
     try {
         List<MenuCourseEntity> menuCourseEntityList =  menuItemsDao.getMenuCourseEntity();
         return menuCourseEntityList;
     }catch (Exception e){
         e.printStackTrace();
     }
        return new ArrayList<>();
    }


    public boolean isAvailable(MenuCourseEntity menuCourseEntity){
        try{
           return menuItemsDao.getAvailableMenuEntity(menuCourseEntity);
        }catch (Exception e){
            e.printStackTrace();
        }
        return true;
    }


    public List<MenuItemsViewModel> getMenuItemsViewModel(String menuCourseUuid, OrderDetailsViewModel orderDetailsViewModel){
        List<MenuItemsViewModel> menuItemsViewModels = new ArrayList<>();
        try{

            MenuCourseEntity menuCourseEntity =  menuItemsDao.getMenuCourseEntity(menuCourseUuid);
            if(menuCourseEntity != null){
                List<MenuEntity> menuEntityList =  menuItemsDao.getMenuEntityList(menuCourseEntity);
                for(MenuEntity menuEntity : menuEntityList){
                    MenuItemsViewModel menuItems = new MenuItemsViewModel(menuEntity) ;
                    menuItemsViewModels.add(menuItems);
                }
                getCurrentOrders(menuCourseEntity,menuItemsViewModels,orderDetailsViewModel);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return menuItemsViewModels;
    }



    public void getCurrentOrders(MenuCourseEntity menuCourseEntity, List<MenuItemsViewModel> menuItemsViewModels,OrderDetailsViewModel orderDetailsViewModel)throws SQLException{
        List<PlacedOrderItemsEntity> placedOrderItemsEntities =  ordersDao.getPlacedOrderItemsEntity();
        for(PlacedOrderItemsEntity placedOrderItemsEntity : placedOrderItemsEntities){
            MenuItemsViewModel menuItems = new MenuItemsViewModel(placedOrderItemsEntity) ;
           int pos = menuItemsViewModels.indexOf(menuItems);
            if(pos != -1){
                // TODO Need to check what will happen if only one element present in the list
                menuItemsViewModels.remove(pos);
                menuItemsViewModels.add(pos,menuItems);
            }

            orderDetailsViewModel.setTotalCount(orderDetailsViewModel.getTotalCount() + menuItems.getCount());
            double totalCost =  orderDetailsViewModel.getTotalCost() + (menuItems.getCount() * menuItems.getCost());
            totalCost = Utilities.roundOff(totalCost);
            orderDetailsViewModel.setTotalCost(totalCost);
        }

    }

    public void getCurrentOrdersCounts(OrderDetailsViewModel orderDetailsViewModel){
        try{
            List<PlacedOrderItemsEntity> placedOrderItemsEntities =  ordersDao.getPlacedOrderItemsEntity();
            int totalCount = 0;
            double totalCost = 0;
            for(PlacedOrderItemsEntity placedOrderItemsEntity : placedOrderItemsEntities){
                totalCount = totalCount + placedOrderItemsEntity.getQuantity();
                totalCost = totalCost + (placedOrderItemsEntity.getQuantity() * placedOrderItemsEntity.getCost());
                totalCost = Utilities.roundOff(totalCost);
            }
            orderDetailsViewModel.setTotalCount(totalCount);
            orderDetailsViewModel.setTotalCost(totalCost);
        }catch (Exception e){
            e.printStackTrace();
        }
    }





}
