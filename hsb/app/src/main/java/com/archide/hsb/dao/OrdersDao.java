package com.archide.hsb.dao;

import com.archide.hsb.entity.MenuCourseEntity;
import com.archide.hsb.entity.PlacedOrderItemsEntity;
import com.archide.hsb.entity.PlacedOrdersEntity;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Nithish on 13/11/16.
 */

public interface OrdersDao {

    PlacedOrdersEntity getPlacedOrdersEntity()throws SQLException;

    PlacedOrdersEntity getPlacedOrdersEntity(String placeOrderUuid)throws SQLException;

    void createPlacedOrdersEntity(PlacedOrdersEntity placedOrdersEntity)throws  SQLException;

    PlacedOrderItemsEntity getPlacedOrdersItemsEntity(String orderUuid)throws SQLException;

    void createPlacedOrdersItemsEntity(PlacedOrderItemsEntity placedOrderItemsEntity)throws  SQLException;

    void updateOrdersCount(String itemCode,int count)throws SQLException;

    void removeOrderByItemCode(String itemCode)throws SQLException;

    List<PlacedOrderItemsEntity> getPlacedOrderItemsEntity()throws SQLException;

    List<PlacedOrderItemsEntity> getPlacedOrderItemsEntity(MenuCourseEntity menuCourseEntity)throws SQLException;

    void removeCurrentOrder()throws SQLException;


    void updateServerSyncTime(String serverSyncTime)throws SQLException;

    void updatePlacedOrderItems()throws SQLException;
}
