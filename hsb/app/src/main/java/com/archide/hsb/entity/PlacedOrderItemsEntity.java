package com.archide.hsb.entity;


import com.archide.hsb.enumeration.OrderStatus;
import com.archide.hsb.sync.json.OrderedMenuItems;
import com.archide.hsb.view.model.MenuItemsViewModel;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "PlacedOrderItems")
public class PlacedOrderItemsEntity {
	
	public static final String PLACED_ORDER_ITEMS_ID = "PlacedOrderItemsId";
	public static final String PLACED_ORDER_ITEMS_UUID = "PlacedOrderItemsUUID";
	public static final String MENU_ITEM = "MenuItem";
	public static final String QUANTITY = "Quantity";
   // public static final String PLACED_ORDER_ENTITY = "PlacedOrders";
	public static final String MENU_COURSE_ENTITY = "MenuCourse";
	public static final String IS_CONFORM = "IsConfirm";
    public static final String ITEM_CODE = "ItemCode";
    public static final String NAME = "Name";
    public static final String COST = "Cost";
    public static final String ORDER_STATUS = "OrderStatus";
    public static final String SERVER_SYNC_TIME = "ServerSyncTime";
    public static final String IS_DELETED = "isDeleted";

    @DatabaseField(columnName = PLACED_ORDER_ITEMS_ID,generatedId = true)
	private int placedOrderItemsId;
    @DatabaseField(columnName = PLACED_ORDER_ITEMS_UUID)
	private String placedOrderItemsUUID;
    @DatabaseField(columnName = MENU_ITEM,foreign = true,foreignAutoRefresh =  true)
	private MenuEntity menuItem;
    @DatabaseField(columnName = QUANTITY)
	private int quantity;

    @DatabaseField(columnName = SERVER_SYNC_TIME)
    private long serverSyncTime;

    @DatabaseField(columnName = NAME)
    private String name;
	@DatabaseField(columnName = MENU_COURSE_ENTITY,foreign = true,foreignAutoRefresh =  true)
	private MenuCourseEntity menuCourseEntity;

    @DatabaseField(columnName = ITEM_CODE)
    private String itemCode;
	@DatabaseField(columnName = IS_CONFORM)
	private boolean isConform;
    @DatabaseField(columnName = COST)
    private double cost;
    @DatabaseField(columnName = ORDER_STATUS)
	private OrderStatus orderStatus;
    @DatabaseField(columnName = IS_DELETED)
    private boolean isDeleted;

	public PlacedOrderItemsEntity(){

	}



	public PlacedOrderItemsEntity(OrderedMenuItems orderedMenuItems){
        this.placedOrderItemsUUID = orderedMenuItems.getPlacedOrderItemsUUID();
        this.quantity = orderedMenuItems.getQuantity();
        this.name = orderedMenuItems.getName();
        this.itemCode = orderedMenuItems.getItemCode();
        this.orderStatus = orderedMenuItems.getOrderStatus();
        this.isConform = true;
        this.isDeleted = orderedMenuItems.isDeleted();

	}

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public long getServerSyncTime() {
        return serverSyncTime;
    }

    public void setServerSyncTime(long serverSyncTime) {
        this.serverSyncTime = serverSyncTime;
    }

    public MenuCourseEntity getMenuCourseEntity() {
        return menuCourseEntity;
    }

    public void setMenuCourseEntity(MenuCourseEntity menuCourseEntity) {
        this.menuCourseEntity = menuCourseEntity;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public boolean isConform() {
		return isConform;
	}

	public void setConform(boolean conform) {
		isConform = conform;
	}

	public int getPlacedOrderItemsId() {
		return placedOrderItemsId;
	}
	public void setPlacedOrderItemsId(int placedOrderItemsId) {
		this.placedOrderItemsId = placedOrderItemsId;
	}
	public String getPlacedOrderItemsUUID() {
		return placedOrderItemsUUID;
	}
	public void setPlacedOrderItemsUUID(String placedOrderItemsUUID) {
		this.placedOrderItemsUUID = placedOrderItemsUUID;
	}
	public MenuEntity getMenuItem() {
		return menuItem;
	}
	public void setMenuItem(MenuEntity menuItem) {
		this.menuItem = menuItem;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
	
/*
	public PlacedOrdersEntity getPlacedOrdersEntity() {
		return placedOrdersEntity;
	}
	public void setPlacedOrdersEntity(PlacedOrdersEntity placedOrdersEntity) {
		this.placedOrdersEntity = placedOrdersEntity;
	}*/

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    @Override
	public String toString() {
		return "PlacedOrderItemsEntity [placedOrderItemsId=" + placedOrderItemsId + ", placedOrderItemsUUID="
				+ placedOrderItemsUUID + ", menuItem=" + menuItem + ", quantity=" + quantity + "]";
	}

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }
}
