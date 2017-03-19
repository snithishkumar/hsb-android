package com.archide.hsb.sync.json;

import java.util.ArrayList;
import java.util.List;

public class GetMenuDetails {
	
	private List<MenuListJson> menuListJsonList = new ArrayList<>();
	private PlaceOrdersJson previousOrder;
	private String tableNumber;

	public String getTableNumber() {
		return tableNumber;
	}

	public void setTableNumber(String tableNumber) {
		this.tableNumber = tableNumber;
	}

	public List<MenuListJson> getMenuListJsonList() {
		return menuListJsonList;
	}
	public void setMenuListJsonList(List<MenuListJson> menuListJsonList) {
		this.menuListJsonList = menuListJsonList;
	}
	public PlaceOrdersJson getPreviousOrder() {
		return previousOrder;
	}
	public void setPreviousOrder(PlaceOrdersJson previousOrder) {
		this.previousOrder = previousOrder;
	}
	@Override
	public String toString() {
		return "GetMenuDetails [menuListJsonList=" + menuListJsonList + ", previousOrder=" + previousOrder + "]";
	}
	
	

}
