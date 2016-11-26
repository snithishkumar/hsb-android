package com.archide.hsb.sync.json;

import com.archide.hsb.entity.KitchenOrdersListEntity;

/**
 * Created by Nithish on 26/11/16.
 */

public class GetKitchenOrders {

    private String orderId;
    private String placedOrdersUuid;
    private long serverDateTime;

    public GetKitchenOrders(){

    }

    public GetKitchenOrders(KitchenOrdersListEntity ordersListEntity){
        this.orderId = ordersListEntity.getOrderId();
        this.placedOrdersUuid = ordersListEntity.getPlacedOrderUuid();
        this.serverDateTime = ordersListEntity.getServerDateTime();
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getPlacedOrdersUuid() {
        return placedOrdersUuid;
    }

    public void setPlacedOrdersUuid(String placedOrdersUuid) {
        this.placedOrdersUuid = placedOrdersUuid;
    }

    public long getServerDateTime() {
        return serverDateTime;
    }

    public void setServerDateTime(long serverDateTime) {
        this.serverDateTime = serverDateTime;
    }
}
