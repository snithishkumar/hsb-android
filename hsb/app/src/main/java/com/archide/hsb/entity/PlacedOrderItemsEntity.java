package com.archide.hsb.entity;


import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "PlacedOrderItemsEntity")
public class PlacedOrderItemsEntity {
	
	public static final String PLACED_ORDER_ITEMS_ID = "placedOrderItemsId";
	public static final String PLACED_ORDER_ITEMS_UUID = "placedOrderItemsUUID";
	public static final String MENU_ITEM = "menuItem";
	public static final String QUANTITY = "quantity";
    public static final String PLACED_ORDER_ENTITY = "placedOrdersEntity";

    @DatabaseField(columnName = PLACED_ORDER_ITEMS_ID,generatedId = true)
	private int placedOrderItemsId;
    @DatabaseField(columnName = PLACED_ORDER_ITEMS_UUID)
	private String placedOrderItemsUUID;
    @DatabaseField(columnName = MENU_ITEM,foreign = true,foreignAutoRefresh =  true)
	private MenuEntity menuItem;
    @DatabaseField(columnName = QUANTITY)
	private float quantity;
    @DatabaseField(columnName = PLACED_ORDER_ENTITY,foreign = true,foreignAutoRefresh =  true)
	private PlacedOrdersEntity placedOrdersEntity;
	
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
	public float getQuantity() {
		return quantity;
	}
	public void setQuantity(float quantity) {
		this.quantity = quantity;
	}
	
	
	
	public PlacedOrdersEntity getPlacedOrdersEntity() {
		return placedOrdersEntity;
	}
	public void setPlacedOrdersEntity(PlacedOrdersEntity placedOrdersEntity) {
		this.placedOrdersEntity = placedOrdersEntity;
	}
	@Override
	public String toString() {
		return "PlacedOrderItemsEntity [placedOrderItemsId=" + placedOrderItemsId + ", placedOrderItemsUUID="
				+ placedOrderItemsUUID + ", menuItem=" + menuItem + ", quantity=" + quantity + "]";
	}
	
	

}
