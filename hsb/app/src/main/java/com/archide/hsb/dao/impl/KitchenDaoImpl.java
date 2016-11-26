package com.archide.hsb.dao.impl;

import android.content.Context;

import com.archide.hsb.dao.KitchenDao;
import com.archide.hsb.entity.KitchenOrderDetailsEntity;
import com.archide.hsb.entity.KitchenOrdersCategoryEntity;
import com.archide.hsb.entity.KitchenOrdersListEntity;
import com.archide.hsb.enumeration.FoodType;
import com.archide.hsb.enumeration.Status;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Nithish on 26/11/16.
 */

public class KitchenDaoImpl extends BaseDaoImpl implements KitchenDao{

    Dao<KitchenOrdersListEntity,Integer> kitchenOrderListDao = null;
    Dao<KitchenOrdersCategoryEntity,Integer> kitchenOrderCategorytDao = null;
    Dao<KitchenOrderDetailsEntity,Integer> kitchenOrderDetailsDao = null;


    public KitchenDaoImpl(Context context) throws SQLException {
        super(context);
        initDao();
    }

    @Override
    protected void initDao() throws SQLException {
        kitchenOrderListDao = databaseHelper.getDao(KitchenOrdersListEntity.class);
        kitchenOrderCategorytDao =  databaseHelper.getDao(KitchenOrdersCategoryEntity.class);
        kitchenOrderDetailsDao = databaseHelper.getDao(KitchenOrderDetailsEntity.class);
    }

    @Override
    public void createKitchenOrder(KitchenOrdersListEntity kitchenOrdersListEntity) throws SQLException {
        kitchenOrderListDao.create(kitchenOrdersListEntity);
    }

    @Override
    public void updateKitchenOrder(KitchenOrdersListEntity kitchenOrdersListEntity) throws SQLException {
        kitchenOrderListDao.update(kitchenOrdersListEntity);
    }

    @Override
    public void createKitchenOrderCategory(KitchenOrdersCategoryEntity kitchenOrdersCategoryEntity) throws SQLException {
        kitchenOrderCategorytDao.create(kitchenOrdersCategoryEntity);
    }

    @Override
    public KitchenOrdersCategoryEntity getKitchenOrdersCategoryEntity(KitchenOrdersListEntity kitchenOrdersListEntity, String categoryUuid) throws SQLException {
       return kitchenOrderCategorytDao.queryBuilder().where().eq(KitchenOrdersCategoryEntity.FOOD_CATEGORY_UUID,categoryUuid).and().eq(KitchenOrdersCategoryEntity.KITCHEN_ORDER_LIST,kitchenOrdersListEntity).queryForFirst();
    }

    @Override
    public void createKitchenOrderItems(KitchenOrderDetailsEntity kitchenOrderDetailsEntity) throws SQLException {
        kitchenOrderDetailsDao.create(kitchenOrderDetailsEntity);
    }

    @Override
    public KitchenOrdersListEntity getKitchenOrdersListEntity(String orderId) throws SQLException {
       return kitchenOrderListDao.queryBuilder().where().eq(KitchenOrdersListEntity.ORDER_ID,orderId).queryForFirst();
    }

    @Override
    public List<KitchenOrdersListEntity> getKitchenOrdersList()throws SQLException{
       return kitchenOrderListDao.queryBuilder().selectColumns(KitchenOrdersListEntity.ORDER_ID,KitchenOrdersListEntity.PLACED_ORDERS_UUID,KitchenOrdersListEntity.SERVER_DATE_TIME).where().ne(KitchenOrdersListEntity.STATUS, Status.CLOSE).query();
    }

    @Override
    public void closeOrders(String orderGuid)throws SQLException{
        kitchenOrderListDao.updateBuilder().updateColumnValue(KitchenOrdersListEntity.STATUS,Status.CLOSE).where().eq(KitchenOrdersListEntity.PLACED_ORDERS_UUID,orderGuid);
    }

    @Override
    public List<KitchenOrdersListEntity> getUnClosedKitchenOrdersList()throws SQLException{
      // return kitchenOrderListDao.queryBuilder().where().ne(KitchenOrdersListEntity.STATUS,Status.CLOSE).query();
       return kitchenOrderListDao.queryForAll();
    }

    @Override
    public long getCountOf(FoodType foodType,KitchenOrdersListEntity kitchenOrdersListEntity)throws SQLException{
       return kitchenOrderDetailsDao.queryBuilder().where().
                eq(KitchenOrderDetailsEntity.FOOD_TYPE,foodType).and().
                eq(KitchenOrderDetailsEntity.KITCHEN_ORDER_LIST,kitchenOrdersListEntity)
                .countOf();
    }


}
