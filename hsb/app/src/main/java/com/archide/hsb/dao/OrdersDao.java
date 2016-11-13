package com.archide.hsb.dao;

import com.archide.hsb.entity.PlacedOrderItemsEntity;
import com.archide.hsb.entity.PlacedOrdersEntity;

import java.sql.SQLException;

/**
 * Created by Nithish on 13/11/16.
 */

public interface OrdersDao {

    PlacedOrdersEntity getPlacedOrdersEntity(String orderUuid)throws SQLException;

    void createPlacedOrdersEntity(PlacedOrdersEntity placedOrdersEntity)throws  SQLException;

    PlacedOrderItemsEntity getPlacedOrdersItemsEntity(String orderUuid)throws SQLException;

    void createPlacedOrdersItemsEntity(PlacedOrderItemsEntity placedOrderItemsEntity)throws  SQLException;
}
