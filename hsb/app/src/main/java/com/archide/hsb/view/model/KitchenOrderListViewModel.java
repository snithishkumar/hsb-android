package com.archide.hsb.view.model;

import com.archide.hsb.enumeration.FoodType;
import com.archide.hsb.enumeration.OrderStatus;
import com.archide.hsb.enumeration.ViewStatus;

/**
 * Created by Nithish on 26/11/16.
 */

public class KitchenOrderListViewModel {

    private String orderId;
    private String orderTime;
    private String lastOrderTime;
    private String tableNumber;
    private ViewStatus viewStatus;
    private FoodType foodType;
    private String vegCount;
    private String nonVegCount;
    //private OrderStatus orderStatus;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public String getLastOrderTime() {
        return lastOrderTime;
    }

    public void setLastOrderTime(String lastOrderTime) {
        this.lastOrderTime = lastOrderTime;
    }

    public String getTableNumber() {
        return tableNumber;
    }

    public void setTableNumber(String tableNumber) {
        this.tableNumber = tableNumber;
    }

    public ViewStatus getViewStatus() {
        return viewStatus;
    }

    public void setViewStatus(ViewStatus viewStatus) {
        this.viewStatus = viewStatus;
    }

    public FoodType getFoodType() {
        return foodType;
    }

    public void setFoodType(FoodType foodType) {
        this.foodType = foodType;
    }

    public String getVegCount() {
        return vegCount;
    }

    public void setVegCount(String vegCount) {
        this.vegCount = vegCount;
    }

    public String getNonVegCount() {
        return nonVegCount;
    }

    public void setNonVegCount(String nonVegCount) {
        this.nonVegCount = nonVegCount;
    }

   /* public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }*/

    @Override
    public String toString() {
        return "KitchenOrderListViewModel{" +
                "orderId='" + orderId + '\'' +
                ", orderTime='" + orderTime + '\'' +
                ", lastOrderTime='" + lastOrderTime + '\'' +
                ", tableNumber='" + tableNumber + '\'' +
                ", viewStatus=" + viewStatus +
                ", foodType=" + foodType +
                ", vegCount='" + vegCount + '\'' +
                ", nonVegCount='" + nonVegCount + '\'' +
                '}';
    }
}
