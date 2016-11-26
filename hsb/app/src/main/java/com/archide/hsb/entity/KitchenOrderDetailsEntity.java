package com.archide.hsb.entity;

import com.archide.hsb.enumeration.FoodType;
import com.archide.hsb.enumeration.Status;
import com.archide.hsb.sync.json.OrderedMenuItems;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import static com.archide.hsb.entity.KitchenOrdersCategoryEntity.KITCHEN_ORDER_LIST;

/**
 * Created by Nithish on 26/11/16.
 */
@DatabaseTable(tableName = "KitchenOrdersDetails")
public class KitchenOrderDetailsEntity {

    public static final String MENU_ID = "MenuId";
    public static final String MENU_UUID = "MenuUuid";
    public static final String MENU_ITEM_CODE = "MenuItemCode";
    public static final String NAME = "Name";
    public static final String QUANTITY = "Quantity";
    public static final String STATUS = "Status";
    public static final String FOOD_TYPE = "FoodType";
    public static final String KITCHEN_ORDER_LIST = "KitchenOrdersList";
    public static final String KITCHEN_ORDER_CATEGORY = "KitchenOrderCategory";

    @DatabaseField(columnName = MENU_ID,generatedId = true)
    private int menuId;
    @DatabaseField(columnName = MENU_UUID)
    private String menuUUID;
    @DatabaseField(columnName = MENU_ITEM_CODE)
    private String menuItemCode;
    @DatabaseField(columnName = NAME)
    private String name;
    @DatabaseField(columnName = QUANTITY)
    private int quantity;
    @DatabaseField(columnName = STATUS)
    private Status status;
    @DatabaseField(columnName =  FOOD_TYPE)
    private FoodType foodType;

    @DatabaseField(columnName = KITCHEN_ORDER_LIST,foreign = true)
    private KitchenOrdersListEntity kitchenOrdersList;

    @DatabaseField(columnName = KITCHEN_ORDER_CATEGORY,foreign = true)
    private KitchenOrdersCategoryEntity kitchenOrdersCategory;

    public KitchenOrderDetailsEntity(){

    }

    public KitchenOrderDetailsEntity(OrderedMenuItems orderedMenuItems){
        this.menuUUID = orderedMenuItems.getMenuUuid();
        this.menuItemCode = orderedMenuItems.getItemCode();
        this.name = orderedMenuItems.getName();
        this.quantity = orderedMenuItems.getQuantity();
        this.foodType = orderedMenuItems.getFoodType();

    }

    public FoodType getFoodType() {
        return foodType;
    }

    public void setFoodType(FoodType foodType) {
        this.foodType = foodType;
    }

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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public KitchenOrdersListEntity getKitchenOrdersList() {
        return kitchenOrdersList;
    }

    public void setKitchenOrdersList(KitchenOrdersListEntity kitchenOrdersList) {
        this.kitchenOrdersList = kitchenOrdersList;
    }

    public KitchenOrdersCategoryEntity getKitchenOrdersCategory() {
        return kitchenOrdersCategory;
    }

    public void setKitchenOrdersCategory(KitchenOrdersCategoryEntity kitchenOrdersCategory) {
        this.kitchenOrdersCategory = kitchenOrdersCategory;
    }

    @Override
    public String toString() {
        return "KitchenOrderDetailsEntity{" +
                "menuId=" + menuId +
                ", menuUUID='" + menuUUID + '\'' +
                ", menuItemCode='" + menuItemCode + '\'' +
                ", name='" + name + '\'' +
                ", quantity=" + quantity +
                ", status=" + status +
                '}';
    }
}
