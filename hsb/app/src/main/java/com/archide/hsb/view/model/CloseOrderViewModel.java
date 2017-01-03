package com.archide.hsb.view.model;

/**
 * Created by Nithish on 01/01/17.
 */

public class CloseOrderViewModel {
    private String orderId;
    private String tableNumber;
    private String totalAmount;
    private String userMobileNumber;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getTableNumber() {
        return tableNumber;
    }

    public void setTableNumber(String tableNumber) {
        this.tableNumber = tableNumber;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getUserMobileNumber() {
        return userMobileNumber;
    }

    public void setUserMobileNumber(String userMobileNumber) {
        this.userMobileNumber = userMobileNumber;
    }

    @Override
    public String toString() {
        return "CloseOrderViewModel{" +
                "orderId='" + orderId + '\'' +
                ", tableNumber='" + tableNumber + '\'' +
                ", totalAmount='" + totalAmount + '\'' +
                ", userMobileNumber='" + userMobileNumber + '\'' +
                '}';
    }
}
