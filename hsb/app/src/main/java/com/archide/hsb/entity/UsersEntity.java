package com.archide.hsb.entity;

import com.archide.hsb.enumeration.OrderType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Nithish on 12/01/17.
 */
@DatabaseTable(tableName = "UsersTable")
public class UsersEntity {

    public static final String USER_ID = "UserId";
    public static final String USER_MOBILE_NUMBER = "UserMobileNumber";
    public static final String ORDER_TYPE = "OrderType";

    @DatabaseField(columnName = USER_ID)
    private int userId;
    @DatabaseField(columnName = USER_MOBILE_NUMBER)
    private String userMobileNumber;


    @DatabaseField(columnName =  ORDER_TYPE)
    private OrderType orderType;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserMobileNumber() {
        return userMobileNumber;
    }

    public void setUserMobileNumber(String userMobileNumber) {
        this.userMobileNumber = userMobileNumber;
    }

    public OrderType getOrderType() {
        return orderType;
    }

    public void setOrderType(OrderType orderType) {
        this.orderType = orderType;
    }

    @Override
    public String toString() {
        return "UsersEntity{" +
                "userId=" + userId +
                ", userMobileNumber='" + userMobileNumber + '\'' +
                ", orderType=" + orderType +
                '}';
    }
}
