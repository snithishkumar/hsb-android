package com.archide.hsb.entity;

import com.archide.hsb.enumeration.AppType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Nithish on 17/12/16.
 */
@DatabaseTable(tableName = "AppType")
public class AppTypeEntity {

    public static final String APP_TYPE = "AppType";
    public static final String APP_TYPE_ID = "AppTypeId";

    @DatabaseField(columnName = APP_TYPE_ID,generatedId = true)
    private int appTypeId;

    @DatabaseField(columnName = APP_TYPE)
    private AppType appType;

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

    @Override
    public String toString() {
        return "AppTypeEntity{" +
                "appTypeId=" + appTypeId +
                ", appType=" + appType +
                '}';
    }
}
