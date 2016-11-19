package com.archide.hsb.dao.impl;

import android.content.Context;

import com.archide.hsb.dao.OrdersDao;
import com.archide.hsb.entity.MenuEntity;
import com.archide.hsb.entity.PlacedOrderItemsEntity;
import com.archide.hsb.entity.PlacedOrdersEntity;
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

    public PlacedOrderItemsEntity getPlacedOrdersItemsEntity(String orderUuid)throws SQLException{
        return placedOrderItemDao.queryBuilder().where().eq(PlacedOrderItemsEntity.PLACED_ORDER_ITEMS_UUID,orderUuid).queryForFirst();
    }

    public void createPlacedOrdersItemsEntity(PlacedOrderItemsEntity placedOrderItemsEntity)throws  SQLException{
        placedOrderItemDao.create(placedOrderItemsEntity);
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


}
