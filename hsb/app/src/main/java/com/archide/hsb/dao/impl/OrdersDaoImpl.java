package com.archide.hsb.dao.impl;

import android.content.Context;

import com.archide.hsb.dao.OrdersDao;
import com.archide.hsb.entity.MenuCourseEntity;
import com.archide.hsb.entity.PlacedOrderItemsEntity;
import com.archide.hsb.entity.PlacedOrdersEntity;
import com.archide.hsb.enumeration.OrderStatus;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Nithish on 13/11/16.
 */

public class OrdersDaoImpl extends BaseDaoImpl implements OrdersDao {

    Dao<PlacedOrdersEntity,Integer> placedOrdersDao = null;
    Dao<PlacedOrderItemsEntity,Integer> placedOrderItemDao = null;

    public OrdersDaoImpl(Context context) throws SQLException {
        super(context);
        initDao();
    }

    @Override
    protected void initDao() throws SQLException {
        placedOrdersDao = databaseHelper.getDao(PlacedOrdersEntity.class);
        placedOrderItemDao = databaseHelper.getDao(PlacedOrderItemsEntity.class);
    }

    @Override
    public PlacedOrdersEntity getPlacedOrdersEntity(String placeOrderUuid) throws SQLException {
        return placedOrdersDao.queryBuilder().where().eq(PlacedOrdersEntity.PLACED_ORDERS_UUID,placeOrderUuid).queryForFirst();
    }

    public PlacedOrdersEntity getPlacedOrdersEntity()throws SQLException{
       return placedOrdersDao.queryBuilder().queryForFirst();
    }



    public void createPlacedOrdersEntity(PlacedOrdersEntity placedOrdersEntity)throws  SQLException{
        placedOrdersDao.create(placedOrdersEntity);
    }


    public void updatePlacedOrdersEntity(PlacedOrdersEntity placedOrdersEntity)throws  SQLException{
        placedOrdersDao.update(placedOrdersEntity);
    }

    public void updateServerSyncTime(String serverSyncTime)throws SQLException{
        placedOrdersDao.updateBuilder().updateColumnValue(PlacedOrdersEntity.SERVER_DATE_TIME,serverSyncTime).update();
    }


    public void updatePlacedOrderItems(long serverSyncTime)throws SQLException{
        UpdateBuilder<PlacedOrderItemsEntity,Integer> updateBuilder = placedOrderItemDao.updateBuilder();
        updateBuilder.updateColumnValue(PlacedOrderItemsEntity.IS_CONFORM,true).
                updateColumnValue(PlacedOrderItemsEntity.SERVER_SYNC_TIME,serverSyncTime).
        where().eq(PlacedOrderItemsEntity.IS_CONFORM,false);
        updateBuilder.update();
    }

    public PlacedOrderItemsEntity getPlacedOrdersItemsEntity(String orderUuid)throws SQLException{
        return placedOrderItemDao.queryBuilder().where().eq(PlacedOrderItemsEntity.PLACED_ORDER_ITEMS_UUID,orderUuid).queryForFirst();
    }

    public void createPlacedOrdersItemsEntity(PlacedOrderItemsEntity placedOrderItemsEntity)throws  SQLException{
        placedOrderItemDao.create(placedOrderItemsEntity);
    }

    public void updatePlacedOrdersItemsEntity(PlacedOrderItemsEntity placedOrderItemsEntity)throws  SQLException{
        placedOrderItemDao.update(placedOrderItemsEntity);
    }

    public void updateOrdersCount(String itemCode,int count)throws SQLException{
      UpdateBuilder<PlacedOrderItemsEntity,Integer> updateBuilder = placedOrderItemDao.updateBuilder();
        updateBuilder.updateColumnValue(PlacedOrderItemsEntity.QUANTITY,count);
        updateBuilder.where().eq(PlacedOrderItemsEntity.ITEM_CODE,itemCode).and().eq(PlacedOrderItemsEntity.IS_CONFORM,false);
        updateBuilder.update();
    }


    public void removeOrderByItemCode(String itemCode)throws SQLException{
        DeleteBuilder<PlacedOrderItemsEntity,Integer> deleteBuilder = placedOrderItemDao.deleteBuilder();
        deleteBuilder.where().eq(PlacedOrderItemsEntity.ITEM_CODE,itemCode).and().eq(PlacedOrderItemsEntity.IS_CONFORM,false);
        deleteBuilder.delete();
    }


    public List<PlacedOrderItemsEntity> getPlacedOrderItemsEntity()throws SQLException{
      return   placedOrderItemDao.queryBuilder().where().eq(PlacedOrderItemsEntity.IS_CONFORM,false).query();
    }

    public List<PlacedOrderItemsEntity> getPlacedOrderItemsEntityTest()throws SQLException{
        return   placedOrderItemDao.queryForAll();
    }

    public List<PlacedOrderItemsEntity> getPlacedOrderItemsEntity(MenuCourseEntity menuCourseEntity)throws SQLException{
        return   placedOrderItemDao.queryBuilder().where().eq(PlacedOrderItemsEntity.IS_CONFORM,false).and().eq(PlacedOrderItemsEntity.MENU_COURSE_ENTITY,menuCourseEntity).query();
    }

    public void removeCurrentOrder()throws SQLException{
        DeleteBuilder<PlacedOrderItemsEntity,Integer> deleteBuilder = placedOrderItemDao.deleteBuilder();
        deleteBuilder.where().eq(PlacedOrderItemsEntity.IS_CONFORM,false);
        deleteBuilder.delete();
    }


    public void removeAllData()throws SQLException{
        placedOrderItemDao.deleteBuilder().delete();
        placedOrdersDao.deleteBuilder().delete();
    }

    @Override
    public PlacedOrdersEntity getPlacedOrderHistoryByMobile(String userMobileNumber,String tableNumber) throws SQLException {
      // List<PlacedOrdersEntity> list =  placedOrdersDao.queryForAll();
        PlacedOrdersEntity placedOrdersEntity =  placedOrdersDao.queryBuilder().where().eq(PlacedOrdersEntity.USER_MOBILE_NUMBER,userMobileNumber)
                .and().eq(PlacedOrdersEntity.TABLE_NUMBER,tableNumber)
                .queryForFirst();
        return placedOrdersEntity;
    }


    @Override
    public List<PlacedOrderItemsEntity> getPlacedOrderHistoryItems(PlacedOrdersEntity placedOrdersEntity)throws SQLException{
        return   placedOrderItemDao.queryBuilder().where().eq(PlacedOrderItemsEntity.IS_CONFORM,true).and().ne(PlacedOrderItemsEntity.ORDER_STATUS,OrderStatus.UN_AVAILABLE).query();
    }


    @Override
    public long getPreviousSyncHistoryData()throws SQLException{
        PlacedOrderItemsEntity placedOrderItemsEntity =  placedOrderItemDao.queryBuilder().selectColumns(PlacedOrderItemsEntity.SERVER_SYNC_TIME).orderBy(PlacedOrderItemsEntity.SERVER_SYNC_TIME,false).where().ne(PlacedOrderItemsEntity.ORDER_STATUS, OrderStatus.ORDERED).and().eq(PlacedOrderItemsEntity.IS_CONFORM,true).queryForFirst();
        return placedOrderItemsEntity != null ? placedOrderItemsEntity.getServerSyncTime() : 0;
    }


    @Override
    public void removeUnAvailablePlacedOrders()throws SQLException{
       DeleteBuilder<PlacedOrderItemsEntity,Integer> deleteBuilder = placedOrderItemDao.deleteBuilder();
       deleteBuilder.where().eq(PlacedOrderItemsEntity.ORDER_STATUS,OrderStatus.UN_AVAILABLE).and().eq(PlacedOrderItemsEntity.IS_CONFORM,false);
       deleteBuilder.delete();
    }



    @Override
    public void reSetPlacedOrderItems() throws SQLException {
        placedOrderItemDao.deleteBuilder().delete();
        placedOrdersDao.deleteBuilder().delete();
    }


}
