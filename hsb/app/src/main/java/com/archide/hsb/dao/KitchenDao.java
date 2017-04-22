package com.archide.hsb.dao;

import com.archide.hsb.entity.KitchenCookingCmntsEntity;
import com.archide.hsb.entity.KitchenMenuItemsEntity;
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

    void updateKitchenOrderDetailsViewStatus(int id, OrderStatus orderStatus, int unAvailableCount,int quantity)throws SQLException;

    void updateKitchenOrderListViewSync(String orderId)throws SQLException;

    List<KitchenOrdersListEntity> getUnSyncedOrderList()throws SQLException;

    List<KitchenOrderDetailsEntity> getUnSyncedOrderDetails(KitchenOrdersListEntity  kitchenOrdersListEntity)throws SQLException;

    void updateKitchenOrderDetailsSyncStatus(String placedOrderUuid)throws SQLException;

    void updateKitchenOrderListSyncStatus(String placedOrderUuid)throws SQLException;

    long getLastKitchenOrderDetails(KitchenOrdersListEntity kitchenOrdersListEntity)throws SQLException;

    KitchenCookingCmntsEntity getKitchenCookingCmntsEntity(String cookingCommentsUUID)throws SQLException;

    void saveKitchenCookingCmntsEntity(KitchenCookingCmntsEntity kitchenCookingCmntsEntity)throws SQLException;

    List<KitchenCookingCmntsEntity> getKitchenCookingCmntsEntity(KitchenOrdersListEntity kitchenOrdersListEntity)throws SQLException;

    void clearKitchenMenuItems()throws SQLException;

    void saveKitchenMenuItems(List<KitchenMenuItemsEntity> kitchenMenuItemsEntity)throws SQLException;

    List<KitchenMenuItemsEntity> getEditedMenuItems()throws SQLException;

    List<KitchenMenuItemsEntity> getKitchenMenuItems(String searchText)throws SQLException;

    void updateKitchenMenuItemsEntity(KitchenMenuItemsEntity kitchenMenuItemsEntity)throws SQLException;

    List<KitchenOrderDetailsEntity> getKitchenOrderDetails(KitchenOrdersListEntity  kitchenOrdersListEntity)throws SQLException;

    KitchenOrderDetailsEntity getKitchenOrderDetailsEntity(String placedOrderUUID) throws SQLException;
}
