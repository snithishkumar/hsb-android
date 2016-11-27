package com.archide.hsb.dao;

import com.archide.hsb.entity.KitchenOrderDetailsEntity;
import com.archide.hsb.entity.KitchenOrdersCategoryEntity;
import com.archide.hsb.entity.KitchenOrdersListEntity;
import com.archide.hsb.enumeration.FoodType;
import com.archide.hsb.enumeration.OrderStatus;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Nithish on 26/11/16.
 */

public interface KitchenDao {

    void createKitchenOrder(KitchenOrdersListEntity kitchenOrdersListEntity)throws SQLException;

    void updateKitchenOrder(KitchenOrdersListEntity kitchenOrdersListEntity)throws SQLException;

    void createKitchenOrderCategory(KitchenOrdersCategoryEntity kitchenOrdersCategoryEntity)throws SQLException;

    KitchenOrdersCategoryEntity getKitchenOrdersCategoryEntity(KitchenOrdersListEntity kitchenOrdersListEntity,String categoryUuid)throws SQLException;

    KitchenOrdersListEntity getKitchenOrdersListEntity(String orderId)throws SQLException;

    void createKitchenOrderItems(KitchenOrderDetailsEntity kitchenOrderDetailsEntity)throws SQLException;

    List<KitchenOrdersListEntity> getKitchenOrdersList()throws SQLException;

    void closeOrders(String orderGuid)throws SQLException;

    List<KitchenOrdersListEntity> getUnClosedKitchenOrdersList()throws SQLException;

    long getCountOf(FoodType foodType, KitchenOrdersListEntity kitchenOrdersListEntity)throws SQLException;

    List<KitchenOrdersCategoryEntity> getKitchenOrdersCategory(KitchenOrdersListEntity kitchenOrdersListEntity)throws SQLException;

    List<KitchenOrderDetailsEntity> getKitchenOrderDetailsEntity(KitchenOrdersCategoryEntity kitchenOrdersCategoryEntity)throws SQLException;

    void updateKitchenOrderListViewStatus(String orderId)throws SQLException;

    void updateKitchenOrderDetailsViewStatus(int id)throws SQLException;

    void updateKitchenOrderDetailsViewStatus(int id, OrderStatus orderStatus, int unAvailableCount)throws SQLException;

    void updateKitchenOrderListViewSync(String orderId)throws SQLException;
}
