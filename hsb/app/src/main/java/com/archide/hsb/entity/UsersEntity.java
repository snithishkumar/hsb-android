package com.archide.hsb.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Nithish on 12/01/17.
 */
@DatabaseTable(tableName = "UsersTable")
public class UsersEntity {

    public static final String USER_ID = "UserId";
    public static final String USER_MOBILE_NUMBER = "UserMobileNumber";
    public static final String IS_CLOSED = "IsClosed";

    @DatabaseField(columnName = USER_ID)
    private int userId;
    @DatabaseField(columnName = USER_MOBILE_NUMBER)
    private String userMobileNumber;
    @DatabaseField(columnName = IS_CLOSED)
    private boolean isClosed;

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

    public boolean isClosed() {
        return isClosed;
    }

    public void setClosed(boolean closed) {
        isClosed = closed;
    }

    @Override
    public String toString() {
        return "UsersEntity{" +
                "userId=" + userId +
                ", userMobileNumber='" + userMobileNumber + '\'' +
                ", isClosed=" + isClosed +
                '}';
    }
}
