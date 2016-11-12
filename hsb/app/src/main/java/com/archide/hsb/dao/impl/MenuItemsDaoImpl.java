package com.archide.hsb.dao.impl;

import android.content.Context;

import com.archide.hsb.dao.MenuItemsDao;
import com.archide.hsb.entity.FoodCategoryEntity;
import com.archide.hsb.entity.MenuCourseEntity;
import com.archide.hsb.entity.MenuEntity;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Nithish on 12/11/16.
 */

public class MenuItemsDaoImpl extends  BaseDaoImpl implements MenuItemsDao {

    Dao<MenuEntity,Integer> menuEntityDao = null;
    Dao<MenuCourseEntity,Integer> menuCourseDao = null;
    Dao<FoodCategoryEntity,Integer> foodCategoryDao = null;


    public MenuItemsDaoImpl(Context context) throws SQLException {
        super(context);
    }

    @Override
    protected void initDao() throws SQLException {

    }


    /**
     * Returns Most recent server time otherwise it will returns 0
     * @return
     * @throws SQLException
     */
    @Override
    public long getLastSyncTime()throws  SQLException{
        MenuEntity menuEntity =  menuEntityDao.queryBuilder().selectColumns(MenuEntity.SERVER_DATE_TIME).orderBy(MenuEntity.SERVER_DATE_TIME,false).queryForFirst();
        return menuEntity != null ? menuEntity.getServerDateTime() : 0;
    }


    /**
     * Returns MenuCourseEntity for an given Guid
     * @param menuCourseUUid
     * @return
     * @throws SQLException
     */
    public MenuCourseEntity getMenuCourseEntity(String menuCourseUUid)throws  SQLException{
       return menuCourseDao.queryBuilder().where().eq(MenuCourseEntity.MENU_COURSE_UUID,menuCourseUUid).queryForFirst();
    }


    /**
     * Create MenuCourseEntity
     * @param menuCourseEntity
     * @throws SQLException
     */
    public void createMenuCourseEntity(MenuCourseEntity menuCourseEntity)throws  SQLException{
        menuCourseDao.create(menuCourseEntity);
    }


    /**
     * Returns FoodCategoryEntity for an given Guid
     * @param foodCategoryUUid
     * @return
     * @throws SQLException
     */
    public FoodCategoryEntity getFoodCategoryEntity(String foodCategoryUUid)throws  SQLException{
        return foodCategoryDao.queryBuilder().where().eq(FoodCategoryEntity.FOOD_CATEGORY_UUID,foodCategoryUUid).queryForFirst();
    }


    /**
     * Create FoodCategoryEntity
     * @param foodCategoryEntity
     * @throws SQLException
     */
    public void createFoodCategoryEntity(FoodCategoryEntity foodCategoryEntity)throws  SQLException{
        foodCategoryDao.create(foodCategoryEntity);
    }



    /**
     * Returns MenuEntity for an given Guid
     * @param menuItemUUid
     * @return
     * @throws SQLException
     */
    public MenuEntity getMenuItemEntity(String menuItemUUid)throws  SQLException{
        return menuEntityDao.queryBuilder().where().eq(MenuEntity.MENU_UUID,menuItemUUid).queryForFirst();
    }


    /**
     * Create MenuEntity
     * @param menuEntity
     * @throws SQLException
     */
    public void createMenuEntity(MenuEntity menuEntity)throws  SQLException{
        menuEntityDao.create(menuEntity);
    }

    /**
     * Update MenuEntity
     * @param menuEntity
     * @throws SQLException
     */
    public void updateMenuEntity(MenuEntity menuEntity)throws  SQLException{
        menuEntityDao.update(menuEntity);
    }


    /**
     * Returns List of Menu Course
     * @return
     * @throws SQLException
     */
    public List<MenuCourseEntity> getMenuCourseEntity()throws SQLException{
       return menuCourseDao.queryForAll();
    }

    /**
     * Returns list of food category
     * @param menuCourseEntity
     * @return
     * @throws SQLException
     */
    public List<FoodCategoryEntity> getFoodCategoryEntity(MenuCourseEntity menuCourseEntity)throws SQLException{
       return foodCategoryDao.queryBuilder().where().eq(FoodCategoryEntity.MENU_COURSE,menuCourseEntity).query();
    }


    /**
     * Returns list of menu item
     * @param menuCourseEntity
     * @param foodCategoryEntity
     * @return
     * @throws SQLException
     */
    public List<MenuEntity> getMenuEntityList(MenuCourseEntity menuCourseEntity,FoodCategoryEntity foodCategoryEntity)throws SQLException{
       return menuEntityDao.queryBuilder().where().eq(MenuEntity.MENU_COURSE,menuCourseEntity).and().eq(MenuEntity.FOOD_CATEGORY,foodCategoryEntity).query();
    }

}
