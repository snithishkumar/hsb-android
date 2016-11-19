package com.archide.hsb.service.impl;

import android.content.Context;

import com.archide.hsb.dao.MenuItemsDao;
import com.archide.hsb.dao.impl.MenuItemsDaoImpl;
import com.archide.hsb.entity.FoodCategoryEntity;
import com.archide.hsb.entity.MenuCourseEntity;
import com.archide.hsb.entity.MenuEntity;
import com.archide.hsb.service.MenuItemService;
import com.archide.hsb.view.model.MenuItemsViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nithish on 16/11/16.
 */

public class MenuItemServiceImpl implements MenuItemService {

    private MenuItemsDao menuItemsDao;

    public  MenuItemServiceImpl(Context context){
        try{
            menuItemsDao = new MenuItemsDaoImpl(context);

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


    public List<MenuItemsViewModel> getMenuItemsViewModel(String menuCourseUuid){
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
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return menuItemsViewModels;
    }



}
