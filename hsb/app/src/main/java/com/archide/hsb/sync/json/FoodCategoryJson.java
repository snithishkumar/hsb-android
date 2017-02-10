package com.archide.hsb.sync.json;

import java.util.ArrayList;
import java.util.List;

public class FoodCategoryJson {
	
	private String foodCategoryUuid;
	private String categoryName;
	private long dateTime;
	private int displayOrder;
	private List<MenuItemJson> menuItems = new ArrayList<>();
	

	
	public String getFoodCategoryUuid() {
		return foodCategoryUuid;
	}

	public void setFoodCategoryUuid(String foodCategoryUuid) {
		this.foodCategoryUuid = foodCategoryUuid;
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
	
	
	public List<MenuItemJson> getMenuItems() {
		return menuItems;
	}

	public void setMenuItems(List<MenuItemJson> menuItems) {
		this.menuItems = menuItems;
	}

	public int getDisplayOrder() {
		return displayOrder;
	}

	public void setDisplayOrder(int displayOrder) {
		this.displayOrder = displayOrder;
	}

	@Override
	public String toString() {
		return "FoodCategoryJson [foodCategoryUuid=" + foodCategoryUuid + ", categoryName=" + categoryName
				+ ", dateTime=" + dateTime + "]";
	}
	
	

}
