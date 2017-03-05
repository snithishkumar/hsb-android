package com.archide.hsb.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Nithish on 03/03/17.
 */
@DatabaseTable(tableName = "KitchenMenuItems")
public class KitchenMenuItemsEntity {

    public static final String MENU_ID = "menuId";
    public static final String MENU_UUID= "menuUUID";
    public static final String MENU_ITEM_CODE= "menuItemCode";
    public static final String NAME= "name";
    public static final String FOOD_CATEGORY= "foodCategory";
    public static final String MENU_COURSE= "menuCourse";

    public static final String CURRENT_COUNT= "currentCount";
    public static final String MAX_COUNT= "maxCount";
    public static final String IS_EDITED= "isEdited";

    @DatabaseField(columnName = MENU_ID,generatedId = true)
    private int menuId;

    @DatabaseField(columnName = MENU_UUID)
    private String menuUUID;
    @DatabaseField(columnName = MENU_ITEM_CODE)
    private String menuItemCode;
    @DatabaseField(columnName = NAME)
    private String name;

    @DatabaseField(columnName = FOOD_CATEGORY)
    private String foodCategory;
    @DatabaseField(columnName = MENU_COURSE)
    private String menuCourse;
    @DatabaseField(columnName = CURRENT_COUNT)
    private int currentCount;
    @DatabaseField(columnName = MAX_COUNT)
    private int maxCount;
    @DatabaseField(columnName = IS_EDITED)
    private boolean isEdited;

    

    public int getMenuId() {
        return menuId;
    }

    public void setMenuId(int menuId) {
        this.menuId = menuId;
    }

    public String getMenuUUID() {
        return menuUUID;
    }

    public void setMenuUUID(String menuUUID) {
        this.menuUUID = menuUUID;
    }

    public String getMenuItemCode() {
        return menuItemCode;
    }

    public void setMenuItemCode(String menuItemCode) {
        this.menuItemCode = menuItemCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFoodCategory() {
        return foodCategory;
    }

    public void setFoodCategory(String foodCategory) {
        this.foodCategory = foodCategory;
    }

    public String getMenuCourse() {
        return menuCourse;
    }

    public void setMenuCourse(String menuCourse) {
        this.menuCourse = menuCourse;
    }

    public int getCurrentCount() {
        return currentCount;
    }

    public void setCurrentCount(int currentCount) {
        this.currentCount = currentCount;
    }

    public int getMaxCount() {
        return maxCount;
    }

    public void setMaxCount(int maxCount) {
        this.maxCount = maxCount;
    }

    public boolean isEdited() {
        return isEdited;
    }

    public void setEdited(boolean edited) {
        isEdited = edited;
    }

    @Override
    public String toString() {
        return "KitchenMenuItemsEntity{" +
                "menuId=" + menuId +
                ", menuUUID='" + menuUUID + '\'' +
                ", menuItemCode='" + menuItemCode + '\'' +
                ", name='" + name + '\'' +
                ", foodCategory='" + foodCategory + '\'' +
                ", menuCourse='" + menuCourse + '\'' +
                ", currentCount=" + currentCount +
                ", maxCount=" + maxCount +
                ", isEdited=" + isEdited +
                '}';
    }
}
