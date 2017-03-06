package com.archide.hsb.sync.json;

import com.archide.hsb.enumeration.FoodType;
import com.archide.hsb.enumeration.Status;

public class MenuItemJson {
	
	private String menuUuid;
	private String menuItemCode;
	private String name;
	private double price;
	private Status status;
	private long dateTime;
	private long serverDateTime;

	private int displayOrder;
	private String description;
	private FoodType foodType;
	private String tasteType;
	private int availableCount;

	public int getAvailableCount() {
		return availableCount;
	}

	public void setAvailableCount(int availableCount) {
		this.availableCount = availableCount;
	}

	public int getDisplayOrder() {
		return displayOrder;
	}

	public void setDisplayOrder(int displayOrder) {
		this.displayOrder = displayOrder;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTasteType() {
		return tasteType;
	}

	public void setTasteType(String tasteType) {
		this.tasteType = tasteType;
	}

	public long getServerDateTime() {
		return serverDateTime;
	}

	public void setServerDateTime(long serverDateTime) {
		this.serverDateTime = serverDateTime;
	}

	public String getMenuUuid() {
		return menuUuid;
	}


	public void setMenuUuid(String menuUuid) {
		this.menuUuid = menuUuid;
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
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public Status getStatus() {
		return status;
	}
	public void setStatus(Status status) {
		this.status = status;
	}
	public long getDateTime() {
		return dateTime;
	}
	public void setDateTime(long dateTime) {
		this.dateTime = dateTime;
	}

    public FoodType getFoodType() {
        return foodType;
    }

    public void setFoodType(FoodType foodType) {
        this.foodType = foodType;
    }

    @Override
	public String toString() {
		return "MenuItemJson [menuUuid=" + menuUuid + ", menuItemCode=" + menuItemCode + ", name=" + name + ", price="
				+ price + ", status=" + status + ", dateTime=" + dateTime + "]";
	}
	
	

}
