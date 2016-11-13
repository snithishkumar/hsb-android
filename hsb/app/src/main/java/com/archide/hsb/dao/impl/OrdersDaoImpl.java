package com.archide.hsb.dao.impl;

import android.content.Context;

import com.archide.hsb.dao.OrdersDao;
import com.archide.hsb.entity.MenuEntity;
import com.archide.hsb.entity.PlacedOrderItemsEntity;
import com.archide.hsb.entity.PlacedOrdersEntity;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;

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

    public PlacedOrdersEntity getPlacedOrdersEntity(String orderUuid)throws SQLException{
       return placedOrdersDao.queryBuilder().where().eq(PlacedOrdersEntity.PLACED_ORDERS_UUID,orderUuid).queryForFirst();
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


}
