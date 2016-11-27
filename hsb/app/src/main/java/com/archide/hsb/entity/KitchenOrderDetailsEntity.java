package com.archide.hsb.entity;

import com.archide.hsb.enumeration.FoodType;
import com.archide.hsb.enumeration.OrderStatus;
import com.archide.hsb.enumeration.Status;
import com.archide.hsb.enumeration.ViewStatus;
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
    public static final String ORDER_STATUS = "OrderStatus";
    public static final String VIEW_STATUS = "ViewStatus";
    public static final String UN_AVAILABLE_COUNT = "UnAvailableCount";
    public static final String IS_SYNC = "IsSync";

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
    @DatabaseField(columnName = UN_AVAILABLE_COUNT)
    private int unAvailableCount;
    @DatabaseField(columnName = STATUS)
    private Status status;
    @DatabaseField(columnName =  FOOD_TYPE)
    private FoodType foodType;

    @DatabaseField(columnName = KITCHEN_ORDER_LIST,foreign = true)
    private KitchenOrdersListEntity kitchenOrdersList;

    @DatabaseField(columnName = KITCHEN_ORDER_CATEGORY,foreign = true)
    private KitchenOrdersCategoryEntity kitchenOrdersCategory;

    @DatabaseField(columnName =  ORDER_STATUS)
    private OrderStatus orderStatus;

    @DatabaseField(columnName =  VIEW_STATUS)
    private ViewStatus viewStatus;

    @DatabaseField(columnName =  IS_SYNC)
    private boolean isSync;

    public KitchenOrderDetailsEntity(){

    }

    public KitchenOrderDetailsEntity(OrderedMenuItems orderedMenuItems){
        this.menuUUID = orderedMenuItems.getMenuUuid();
        this.menuItemCode = orderedMenuItems.getItemCode();
        this.name = orderedMenuItems.getName();
        this.quantity = orderedMenuItems.getQuantity();
        this.foodType = orderedMenuItems.getFoodType();
        this.orderStatus = orderedMenuItems.getOrderStatus();
        this.viewStatus = ViewStatus.UN_VIEWED;
        this.unAvailableCount = orderedMenuItems.getUnAvailableCount();
    }

    public boolean isSync() {
        return isSync;
    }

    public void setSync(boolean sync) {
        isSync = sync;
    }

    public int getUnAvailableCount() {
        return unAvailableCount;
    }

    public void setUnAvailableCount(int unAvailableCount) {
        this.unAvailableCount = unAvailableCount;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
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

    public ViewStatus getViewStatus() {
        return viewStatus;
    }

    public void setViewStatus(ViewStatus viewStatus) {
        this.viewStatus = viewStatus;
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
