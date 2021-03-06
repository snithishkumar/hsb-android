package com.archide.hsb.dao.impl;

import android.content.Context;

import com.archide.hsb.dao.KitchenDao;
import com.archide.hsb.entity.KitchenCookingCmntsEntity;
import com.archide.hsb.entity.KitchenMenuItemsEntity;
import com.archide.hsb.entity.KitchenOrderDetailsEntity;
import com.archide.hsb.entity.KitchenOrdersCategoryEntity;
import com.archide.hsb.entity.KitchenOrdersListEntity;
import com.archide.hsb.enumeration.FoodType;
import com.archide.hsb.enumeration.OrderStatus;
import com.archide.hsb.enumeration.Status;
import com.archide.hsb.enumeration.ViewStatus;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.GenericRawResults;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Nithish on 26/11/16.
 */

public class KitchenDaoImpl extends BaseDaoImpl implements KitchenDao{

    Dao<KitchenOrdersListEntity,Integer> kitchenOrderListDao = null;
    Dao<KitchenOrdersCategoryEntity,Integer> kitchenOrderCategorytDao = null;
    Dao<KitchenOrderDetailsEntity,Integer> kitchenOrderDetailsDao = null;
    Dao<KitchenCookingCmntsEntity,Integer> kitchenCookingCommentsDao = null;

    Dao<KitchenMenuItemsEntity,Integer> kitchenMenuItemsDao = null;


    public KitchenDaoImpl(Context context) throws SQLException {
        super(context);
        initDao();
    }

    @Override
    protected void initDao() throws SQLException {
        kitchenOrderListDao = databaseHelper.getDao(KitchenOrdersListEntity.class);
        kitchenOrderCategorytDao =  databaseHelper.getDao(KitchenOrdersCategoryEntity.class);
        kitchenOrderDetailsDao = databaseHelper.getDao(KitchenOrderDetailsEntity.class);
        kitchenCookingCommentsDao = databaseHelper.getDao(KitchenCookingCmntsEntity.class);
        kitchenMenuItemsDao = databaseHelper.getDao(KitchenMenuItemsEntity.class);
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

    public long getLastKitchenOrderDetails(KitchenOrdersListEntity kitchenOrdersListEntity)throws SQLException{
        KitchenOrderDetailsEntity kitchenOrderDetailsEntity =  kitchenOrderDetailsDao.queryBuilder().selectColumns(KitchenOrderDetailsEntity.SERVER_DATE_TIME).orderBy(KitchenOrderDetailsEntity.SERVER_DATE_TIME,false).where().eq(KitchenOrderDetailsEntity.KITCHEN_ORDER_LIST,kitchenOrdersListEntity).queryForFirst();
        return kitchenOrderDetailsEntity != null ? kitchenOrderDetailsEntity.getServerDateTime() : 0;

    }

    @Override
    public void closeOrders(String orderGuid)throws SQLException{ // TODO Need to delete data from table
      UpdateBuilder<KitchenOrdersListEntity,Integer> updateBuilder =  kitchenOrderListDao.updateBuilder();
        updateBuilder.updateColumnValue(KitchenOrdersListEntity.STATUS,Status.CLOSE).where().eq(KitchenOrdersListEntity.ORDER_ID,orderGuid);
        updateBuilder.update();
    }

    @Override
    public List<KitchenOrdersListEntity> getUnClosedKitchenOrdersList()throws SQLException{
       return kitchenOrderListDao.queryBuilder().orderBy(KitchenOrdersListEntity.LAST_UPDATED_TIME,false).where().ne(KitchenOrdersListEntity.STATUS,Status.CLOSE).query();

    }

    @Override
    public List<KitchenOrdersCategoryEntity> getKitchenOrdersCategory(KitchenOrdersListEntity kitchenOrdersListEntity)throws SQLException{
       return kitchenOrderCategorytDao.queryBuilder().where().eq(KitchenOrdersCategoryEntity.KITCHEN_ORDER_LIST,kitchenOrdersListEntity).query();
    }

    @Override
    public List<KitchenOrderDetailsEntity> getKitchenOrderDetailsEntity(KitchenOrdersCategoryEntity kitchenOrdersCategoryEntity)throws SQLException{
     /* return   kitchenOrderDetailsDao.queryBuilder().orderBy(KitchenOrderDetailsEntity.MENU_ID,false).where().
              eq(KitchenOrderDetailsEntity.KITCHEN_ORDER_CATEGORY,kitchenOrdersCategoryEntity).
              and().ne(KitchenOrderDetailsEntity.ORDER_STATUS, OrderStatus.DELIVERED).
              and().ne(KitchenOrderDetailsEntity.ORDER_STATUS,OrderStatus.UN_AVAILABLE)
             .query();*/

        return   kitchenOrderDetailsDao.queryBuilder().orderBy(KitchenOrderDetailsEntity.MENU_ID,false).where().
                eq(KitchenOrderDetailsEntity.KITCHEN_ORDER_CATEGORY,kitchenOrdersCategoryEntity).
                and().eq(KitchenOrderDetailsEntity.ORDER_STATUS, OrderStatus.ORDERED)
                .query();
    }

    @Override
    public long getCountOf(FoodType foodType,KitchenOrdersListEntity kitchenOrdersListEntity)throws SQLException{
        QueryBuilder<KitchenOrderDetailsEntity, Integer> qb = kitchenOrderDetailsDao.queryBuilder();
        qb.selectRaw("Sum("+KitchenOrderDetailsEntity.QUANTITY+")");
        qb.where().
                eq(KitchenOrderDetailsEntity.FOOD_TYPE,foodType).and().
                eq(KitchenOrderDetailsEntity.ORDER_STATUS, OrderStatus.ORDERED).and().
                eq(KitchenOrderDetailsEntity.KITCHEN_ORDER_LIST,kitchenOrdersListEntity);
        GenericRawResults<String[]> rawResults =  kitchenOrderDetailsDao.queryRaw(qb.prepareStatementString());
        String[] values = rawResults.getFirstResult();
        return values[0] != null ? Long.valueOf(values[0]) : 0;
       /* QueryBuilder<KitchenOrderDetailsEntity, Integer> qb = kitchenOrderDetailsDao.queryBuilder();
        qb.selectRaw("Sum("+KitchenOrderDetailsEntity.QUANTITY+")");
        qb.where().
                eq(KitchenOrderDetailsEntity.FOOD_TYPE,foodType).and().
                ne(KitchenOrderDetailsEntity.ORDER_STATUS,OrderStatus.DELIVERED).and().
                ne(KitchenOrderDetailsEntity.ORDER_STATUS,OrderStatus.UN_AVAILABLE).and().
                eq(KitchenOrderDetailsEntity.KITCHEN_ORDER_LIST,kitchenOrdersListEntity);
        GenericRawResults<String[]> rawResults =  kitchenOrderDetailsDao.queryRaw(qb.prepareStatementString());
        String[] values = rawResults.getFirstResult();
        return values[0] != null ? Long.valueOf(values[0]) : 0;*/


    }



    @Override
    public void updateKitchenOrderListViewStatus(String orderId)throws SQLException{
      UpdateBuilder<KitchenOrdersListEntity,Integer> updateBuilder =  kitchenOrderListDao.updateBuilder();
      updateBuilder.updateColumnValue(KitchenOrdersListEntity.VIEW_STATUS, ViewStatus.VIEWED).where().eq(KitchenOrdersListEntity.ORDER_ID,orderId);
      updateBuilder.update();
    }

    @Override
    public void updateKitchenOrderDetailsViewStatus(int id)throws SQLException{
        UpdateBuilder<KitchenOrderDetailsEntity,Integer> updateBuilder =  kitchenOrderDetailsDao.updateBuilder();
        updateBuilder.updateColumnValue(KitchenOrderDetailsEntity.VIEW_STATUS, ViewStatus.VIEWED).where().eq(KitchenOrderDetailsEntity.MENU_ID,id);
        updateBuilder.update();
    }


    @Override
    public void updateKitchenOrderDetailsViewStatus(int id,OrderStatus orderStatus,int unAvailableCount,int quantity)throws SQLException{
        UpdateBuilder<KitchenOrderDetailsEntity,Integer> updateBuilder =  kitchenOrderDetailsDao.updateBuilder();
        updateBuilder.updateColumnValue(KitchenOrderDetailsEntity.ORDER_STATUS, orderStatus).
                updateColumnValue(KitchenOrderDetailsEntity.UN_AVAILABLE_COUNT, unAvailableCount).
                updateColumnValue(KitchenOrderDetailsEntity.QUANTITY, quantity).
                updateColumnValue(KitchenOrderDetailsEntity.IS_SYNC, false).
                where().eq(KitchenOrderDetailsEntity.MENU_ID,id);
        updateBuilder.update();
    }


    @Override
    public void updateKitchenOrderListViewSync(String orderId)throws SQLException{
        UpdateBuilder<KitchenOrdersListEntity,Integer> updateBuilder =  kitchenOrderListDao.updateBuilder();
        updateBuilder.updateColumnValue(KitchenOrdersListEntity.IS_SYNCED, false).where().eq(KitchenOrdersListEntity.ORDER_ID,orderId);
        updateBuilder.update();
    }

    @Override
    public List<KitchenOrdersListEntity> getUnSyncedOrderList()throws SQLException{
       return kitchenOrderListDao.queryBuilder().where().eq(KitchenOrdersListEntity.IS_SYNCED,false).query();
    }


    @Override
    public List<KitchenOrderDetailsEntity> getUnSyncedOrderDetails(KitchenOrdersListEntity  kitchenOrdersListEntity)throws SQLException{
        return kitchenOrderDetailsDao.queryBuilder().where().eq(KitchenOrderDetailsEntity.IS_SYNC,false).and().eq(KitchenOrderDetailsEntity.KITCHEN_ORDER_LIST,kitchenOrdersListEntity).query();
    }

    @Override
    public void updateKitchenOrderDetailsSyncStatus(String placedOrderUuid)throws SQLException{
        UpdateBuilder<KitchenOrderDetailsEntity,Integer> updateBuilder =  kitchenOrderDetailsDao.updateBuilder();
        updateBuilder.updateColumnValue(KitchenOrderDetailsEntity.IS_SYNC, true).where().eq(KitchenOrderDetailsEntity.MENU_UUID,placedOrderUuid);
        updateBuilder.update();
    }

    @Override
    public void updateKitchenOrderListSyncStatus(String placedOrderUuid)throws SQLException{
        UpdateBuilder<KitchenOrdersListEntity,Integer> updateBuilder =  kitchenOrderListDao.updateBuilder();
        updateBuilder.updateColumnValue(KitchenOrdersListEntity.IS_SYNCED, true).where().eq(KitchenOrdersListEntity.PLACED_ORDERS_UUID,placedOrderUuid);
        updateBuilder.update();
    }

    @Override
    public KitchenCookingCmntsEntity getKitchenCookingCmntsEntity(String cookingCommentsUUID)throws SQLException{
      return   kitchenCookingCommentsDao.queryBuilder().where().eq(KitchenCookingCmntsEntity.COOKING_COMMENTS_UUID,cookingCommentsUUID).queryForFirst();
    }

    @Override
    public List<KitchenCookingCmntsEntity> getKitchenCookingCmntsEntity(KitchenOrdersListEntity kitchenOrdersListEntity)throws SQLException{
        //List<KitchenCookingCmntsEntity> res = kitchenCookingCommentsDao.queryForAll();
        return   kitchenCookingCommentsDao.queryBuilder().orderBy(KitchenCookingCmntsEntity.DATE_TIME,false).where().eq(KitchenCookingCmntsEntity.KITCHEN_ORDER_LIST,kitchenOrdersListEntity).query();
    }

    @Override
    public void saveKitchenCookingCmntsEntity(KitchenCookingCmntsEntity kitchenCookingCmntsEntity)throws SQLException{
        kitchenCookingCommentsDao.create(kitchenCookingCmntsEntity);
    }


    @Override
    public void clearKitchenMenuItems()throws SQLException{
        kitchenMenuItemsDao.deleteBuilder().delete();
    }


    @Override
    public void saveKitchenMenuItems(List<KitchenMenuItemsEntity> kitchenMenuItemsEntity)throws SQLException{
        kitchenMenuItemsDao.create(kitchenMenuItemsEntity);
    }


    @Override
    public List<KitchenMenuItemsEntity> getEditedMenuItems()throws SQLException{
       return kitchenMenuItemsDao.queryBuilder().where().eq(KitchenMenuItemsEntity.IS_EDITED,true).query();
    }

    @Override
    public List<KitchenMenuItemsEntity> getKitchenMenuItems(String searchText)throws SQLException{
        if(searchText != null && !searchText.trim().isEmpty()){
          QueryBuilder<KitchenMenuItemsEntity,Integer> queryBuilder =  kitchenMenuItemsDao.queryBuilder();
            queryBuilder.where().like(KitchenMenuItemsEntity.NAME,'%'+searchText+"%").or().like(KitchenMenuItemsEntity.MENU_ITEM_CODE,'%'+searchText+"%");
            return queryBuilder.query();
        }
        return kitchenMenuItemsDao.queryForAll();
    }


    @Override
    public void updateKitchenMenuItemsEntity(KitchenMenuItemsEntity kitchenMenuItemsEntity)throws SQLException{
        kitchenMenuItemsDao.update(kitchenMenuItemsEntity);
    }

}
