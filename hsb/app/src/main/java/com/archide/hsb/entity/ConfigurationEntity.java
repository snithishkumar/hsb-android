package com.archide.hsb.entity;

import com.archide.hsb.enumeration.AppType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Nithish on 17/12/16.
 */
@DatabaseTable(tableName = "AppType")
public class ConfigurationEntity {

    public static final String APP_TYPE = "AppType";
    public static final String APP_TYPE_ID = "AppTypeId";
    public static final String TABLE_NUMBER = "TableNumber";
    public static final String MPIN = "MPin";

    @DatabaseField(columnName = APP_TYPE_ID,generatedId = true)
    private int appTypeId;

    @DatabaseField(columnName = APP_TYPE)
    private AppType appType;

    @DatabaseField(columnName = TABLE_NUMBER)
    private String tableNumber;

    @DatabaseField(columnName =  MPIN)
    private String mPin;

    public String getTableNumber() {
        return tableNumber;
    }

    public void setTableNumber(String tableNumber) {
        this.tableNumber = tableNumber;
    }

    public int getAppTypeId() {
        return appTypeId;
    }

    public void setAppTypeId(int appTypeId) {
        this.appTypeId = appTypeId;
    }

    public AppType getAppType() {
        return appType;
    }

    public void setAppType(AppType appType) {
        this.appType = appType;
    }

    public String getmPin() {
        return mPin;
    }

    public void setmPin(String mPin) {
        this.mPin = mPin;
    }

    @Override
    public String toString() {
        return "ConfigurationEntity{" +
                "appTypeId=" + appTypeId +
                ", appType=" + appType +
                '}';
    }
}
