package com.archide.hsb.sync.json;

import com.archide.hsb.entity.PlacedOrderItemsEntity;

public class OrderedMenuItems {
	
	private String name;
	private int quantity;
	private String placedOrderItemsUUID;
	private String menuUuid;
	private String itemCode;
	
	public OrderedMenuItems(){
		
	}

	public OrderedMenuItems(PlacedOrderItemsEntity placedOrderItemsEntity){
        this.name = placedOrderItemsEntity.getName();
        this.quantity = placedOrderItemsEntity.getQuantity();
        this.placedOrderItemsUUID = placedOrderItemsEntity.getPlacedOrderItemsUUID();
        this.menuUuid = placedOrderItemsEntity.getMenuItem().getMenuUUID();
        this.itemCode = placedOrderItemsEntity.getItemCode();

	}

	
	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
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
	public String getPlacedOrderItemsUUID() {
		return placedOrderItemsUUID;
	}
	public void setPlacedOrderItemsUUID(String placedOrderItemsUUID) {
		this.placedOrderItemsUUID = placedOrderItemsUUID;
	}
	@Override
	public String toString() {
		return "OrderedMenuItems [name=" + name + ", quantity=" + quantity + ", placedOrderItemsUUID="
				+ placedOrderItemsUUID + "]";
	}

	public String getMenuUuid() {
		return menuUuid;
	}

	public void setMenuUuid(String menuUuid) {
		this.menuUuid = menuUuid;
	}
	
	

}
