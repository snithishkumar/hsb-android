package com.archide.hsb.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Nithish on 26/11/16.
 */
@DatabaseTable(tableName = "KitchenOrdersCategory")
public class KitchenOrdersCategoryEntity {

    public static final String FOOD_CATEGORY_ID = "foodCategoryId";
    public static final String FOOD_CATEGORY_UUID = "foodCategoryUUID";
    public static final String CATEGORY_NAME = "categoryName";
    public static final String DATE_TIME = "dateTime";
    public static final String KITCHEN_ORDER_LIST = "KitchenOrdersList";

    @DatabaseField(columnName = FOOD_CATEGORY_ID,generatedId = true)
    private int foodCategoryId;
    @DatabaseField(columnName = FOOD_CATEGORY_UUID)
    private String foodCategoryUUID;
    @DatabaseField(columnName = CATEGORY_NAME)
    private String categoryName;
    @DatabaseField(columnName = DATE_TIME)
    private long dateTime;
    @DatabaseField(columnName = KITCHEN_ORDER_LIST,foreign = true)
    private KitchenOrdersListEntity kitchenOrdersList;

    public int getFoodCategoryId() {
        return foodCategoryId;
    }

    public void setFoodCategoryId(int foodCategoryId) {
        this.foodCategoryId = foodCategoryId;
    }

    public String getFoodCategoryUUID() {
        return foodCategoryUUID;
    }

    public void setFoodCategoryUUID(String foodCategoryUUID) {
        this.foodCategoryUUID = foodCategoryUUID;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public long getDateTime() {
        return dateTime;
    }

    public void setDateTime(long dateTime) {
        this.dateTime = dateTime;
    }

    public KitchenOrdersListEntity getKitchenOrdersList() {
        return kitchenOrdersList;
    }

    public void setKitchenOrdersList(KitchenOrdersListEntity kitchenOrdersList) {
        this.kitchenOrdersList = kitchenOrdersList;
    }

    @Override
    public String toString() {
        return "KitchenOrdersCategoryEntity{" +
                "foodCategoryId=" + foodCategoryId +
                ", foodCategoryUUID='" + foodCategoryUUID + '\'' +
                ", categoryName='" + categoryName + '\'' +
                ", dateTime=" + dateTime +
                ", kitchenOrdersList=" + kitchenOrdersList +
                '}';
    }
}
