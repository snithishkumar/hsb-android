package com.archide.hsb.entity;

import com.archide.hsb.enumeration.AppType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Nithish on 20/11/16.
 */
@DatabaseTable(tableName = "AdminTable")
public class AdminEntity {

    public static final String ADMIN_ID = "AdminId";
   // public static final String TABLE_NUMBER = "TableNumber";
    public static final String M_PIN = "MPin";
    public static final String USER_MOBILE = "UserMobile";
    @DatabaseField(columnName = ADMIN_ID,generatedId = true)
    private int adminId;
   // @DatabaseField(columnName = TABLE_NUMBER)
   // private String tableNumber;
    @DatabaseField(columnName = M_PIN)
    private String mPin;
    @DatabaseField(columnName = USER_MOBILE)
    private String userMobile;

    public int getAdminId() {
        return adminId;
    }

    public void setAdminId(int adminId) {
        this.adminId = adminId;
    }



    public String getmPin() {
        return mPin;
    }

    public void setmPin(String mPin) {
        this.mPin = mPin;
    }

    public String getUserMobile() {
        return userMobile;
    }

    public void setUserMobile(String userMobile) {
        this.userMobile = userMobile;
    }


    @Override
    public String toString() {
        return "AdminEntity{" +
                "adminId=" + adminId +
                ", mPin='" + mPin + '\'' +
                ", userMobile='" + userMobile + '\'' +
                '}';
    }
}
