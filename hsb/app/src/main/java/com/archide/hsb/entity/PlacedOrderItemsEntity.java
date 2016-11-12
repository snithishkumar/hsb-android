package com.archide.hsb.entity;

public class PlacedOrderItemsEntity {
	
	public static final String PLACED_ORDER_ITEMS_ID = "placedOrderItemsId";
	public static final String PLACED_ORDER_ITEMS_UUID = "placedOrderItemsUUID";
	public static final String MENU_ITEM = "menuItem";
	public static final String QUANTITY = "quantity";
	

	private int placedOrderItemsId;
	private String placedOrderItemsUUID;
	private MenuEntity menuItem;
	private float quantity;
	
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
