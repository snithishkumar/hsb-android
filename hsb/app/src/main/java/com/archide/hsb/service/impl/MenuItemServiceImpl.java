package com.archide.hsb.service.impl;

import android.content.Context;

import com.archide.hsb.dao.MenuItemsDao;
import com.archide.hsb.dao.OrdersDao;
import com.archide.hsb.dao.impl.MenuItemsDaoImpl;
import com.archide.hsb.dao.impl.OrdersDaoImpl;
import com.archide.hsb.entity.FoodCategoryEntity;
import com.archide.hsb.entity.MenuCourseEntity;
import com.archide.hsb.entity.MenuEntity;
import com.archide.hsb.entity.PlacedOrderItemsEntity;
import com.archide.hsb.service.MenuItemService;
import com.archide.hsb.view.model.MenuItemsViewModel;
import com.archide.hsb.view.model.OrderDetailsViewModel;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nithish on 16/11/16.
 */

public class MenuItemServiceImpl implements MenuItemService {

    private MenuItemsDao menuItemsDao;
    private OrdersDao ordersDao;

    public  MenuItemServiceImpl(Context context){
        try{
            menuItemsDao = new MenuItemsDaoImpl(context);
            ordersDao = new OrdersDaoImpl(context);
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


    public List<MenuItemsViewModel> getMenuItemsViewModel(String menuCourseUuid, OrderDetailsViewModel orderDetailsViewModel){
        List<MenuItemsViewModel> menuItemsViewModels = new ArrayList<>();
        try{

            MenuCourseEntity menuCourseEntity =  menuItemsDao.getMenuCourseEntity(menuCourseUuid);
            if(menuCourseEntity != null){
                List<FoodCategoryEntity>  foodCategoryList =  menuItemsDao.getFoodCategoryEntity(menuCourseEntity);
                for(FoodCategoryEntity foodCategoryEntity : foodCategoryList){
                    MenuItemsViewModel menuItemsViewModel = new MenuItemsViewModel(foodCategoryEntity) ;
                    menuItemsViewModels.add(menuItemsViewModel);
                    List<MenuEntity> menuEntityList =  menuItemsDao.getMenuEntityList(menuCourseEntity,foodCategoryEntity);
                    for(MenuEntity menuEntity : menuEntityList){
                        MenuItemsViewModel menuItems = new MenuItemsViewModel(menuEntity) ;
                        menuItemsViewModels.add(menuItems);
                    }
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
            orderDetailsViewModel.setTotalCost(orderDetailsViewModel.getTotalCost() + (menuItems.getCount() * menuItems.getCost()));
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
            }
            orderDetailsViewModel.setTotalCount(totalCount);
            orderDetailsViewModel.setTotalCost(totalCost);
        }catch (Exception e){
            e.printStackTrace();
        }
    }


}
