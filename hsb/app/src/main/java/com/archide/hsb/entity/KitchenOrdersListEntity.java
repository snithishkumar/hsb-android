package com.archide.hsb.entity;

import com.archide.hsb.enumeration.Status;
import com.archide.hsb.enumeration.ViewStatus;
import com.archide.hsb.sync.json.PlaceOrdersJson;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Nithish on 26/11/16.
 */
@DatabaseTable(tableName = "KitchenOrdersList")
public class KitchenOrdersListEntity {

    public static final String KITCHEN_ORDER_LIST_ID = "PlaceOrdersId";
    public static final String PLACED_ORDERS_UUID = "PlaceOrdersUUID";
    public static final String ORDER_ID = "OrderId";
    public static final String TABLE_NUMBER = "TableNumber";
    public static final String SERVER_DATE_TIME = "ServerDateTime";
    public static final String ORDER_DATE_TIME = "OrderDateTime";
    public static final String LAST_UPDATED_TIME = "LastUpdatedDateTime";
    public static final String IS_SYNCED = "IsSynced";
    public static final String VIEW_STATUS = "ViewStatus";
    public static final String STATUS = "Status";

    @DatabaseField(columnName = KITCHEN_ORDER_LIST_ID,generatedId = true)
    private int kitchenOrderListId;
    @DatabaseField(columnName = ORDER_ID)
    private String orderId;
    @DatabaseField(columnName = PLACED_ORDERS_UUID)
    private String placedOrderUuid;
    @DatabaseField(columnName = ORDER_DATE_TIME)
    private long orderDateTime;
    @DatabaseField(columnName = LAST_UPDATED_TIME)
    private long lastUpdateTime;
    @DatabaseField(columnName = TABLE_NUMBER)
    private String tableNumber;
    @DatabaseField(columnName = IS_SYNCED)
    private boolean isSynced;
    @DatabaseField(columnName = SERVER_DATE_TIME)
    private long serverDateTime;
    @DatabaseField(columnName = VIEW_STATUS)
    private ViewStatus viewStatus;
    @DatabaseField(columnName = STATUS)
    private Status status;

    public KitchenOrdersListEntity(){

    }

    public KitchenOrdersListEntity(PlaceOrdersJson placeOrdersJson){
        this.orderId = placeOrdersJson.getOrderId();
        this.placedOrderUuid = placeOrdersJson.getPlaceOrderUuid();
        this.orderDateTime = placeOrdersJson.getOrderDateTime();
        this.lastUpdateTime = placeOrdersJson.getLastUpdatedDateTime();
        this.tableNumber = placeOrdersJson.getTableNumber();
        this.viewStatus = ViewStatus.UN_VIEWED;

    }

    public int getKitchenOrderListId() {
        return kitchenOrderListId;
    }

    public void setKitchenOrderListId(int kitchenOrderListId) {
        this.kitchenOrderListId = kitchenOrderListId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getPlacedOrderUuid() {
        return placedOrderUuid;
    }

    public void setPlacedOrderUuid(String placedOrderUuid) {
        this.placedOrderUuid = placedOrderUuid;
    }

    public long getOrderDateTime() {
        return orderDateTime;
    }

    public void setOrderDateTime(long orderDateTime) {
        this.orderDateTime = orderDateTime;
    }

    public long getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(long lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public String getTableNumber() {
        return tableNumber;
    }

    public void setTableNumber(String tableNumber) {
        this.tableNumber = tableNumber;
    }

    public boolean isSynced() {
        return isSynced;
    }

    public void setSynced(boolean synced) {
        isSynced = synced;
    }

    public long getServerDateTime() {
        return serverDateTime;
    }

    public void setServerDateTime(long serverDateTime) {
        this.serverDateTime = serverDateTime;
    }

    public ViewStatus getViewStatus() {
        return viewStatus;
    }

    public void setViewStatus(ViewStatus viewStatus) {
        this.viewStatus = viewStatus;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "KitchenOrdersListEntity{" +
                "kitchenOrderListId=" + kitchenOrderListId +
                ", orderId='" + orderId + '\'' +
                ", placedOrderUuid='" + placedOrderUuid + '\'' +
                ", orderDateTime=" + orderDateTime +
                ", lastUpdateTime=" + lastUpdateTime +
                ", tableNumber='" + tableNumber + '\'' +
                ", isSynced=" + isSynced +
                ", serverDateTime=" + serverDateTime +
                '}';
    }
}
