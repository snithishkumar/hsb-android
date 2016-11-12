package com.archide.hsb.dao;

import com.archide.hsb.entity.FoodCategoryEntity;
import com.archide.hsb.entity.MenuCourseEntity;
import com.archide.hsb.entity.MenuEntity;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Nithish on 13/11/16.
 */

public interface MenuItemsDao {

    long getLastSyncTime()throws SQLException;

    MenuCourseEntity getMenuCourseEntity(String menuCourseUUid)throws  SQLException;

    void createMenuCourseEntity(MenuCourseEntity menuCourseEntity)throws  SQLException;

    FoodCategoryEntity getFoodCategoryEntity(String foodCategoryUUid)throws  SQLException;

    void createFoodCategoryEntity(FoodCategoryEntity foodCategoryEntity)throws  SQLException;

    MenuEntity getMenuItemEntity(String menuItemUUid)throws  SQLException;

    void createMenuEntity(MenuEntity menuEntity)throws  SQLException;

    void updateMenuEntity(MenuEntity menuEntity)throws  SQLException;

    List<MenuCourseEntity> getMenuCourseEntity()throws SQLException;

    List<FoodCategoryEntity> getFoodCategoryEntity(MenuCourseEntity menuCourseEntity)throws SQLException;

    List<MenuEntity> getMenuEntityList(MenuCourseEntity menuCourseEntity,FoodCategoryEntity foodCategoryEntity)throws SQLException;
}
