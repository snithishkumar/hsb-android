package com.archide.hsb.entity;

import com.archide.hsb.enumeration.Status;



public class MenuEntity {
	
	public static final String MENU_ID = "menuId";
	public static final String MENU_UUID= "menuUUID";
	public static final String MENU_ITEM_CODE= "menuItemCode";
	public static final String NAME= "name";
	public static final String FOOD_CATEGORY= "foodCategory";
	public static final String MENU_COURSE= "menuCourse";
	public static final String PRICE= "price";
	public static final String STATUS= "status";
	public static final String DATE_TIME= "dateTime";
	
	

	private int menuId;
	private String menuUUID;
	private String menuItemCode;
	private String name;
	private FoodCategoryEntity foodCategoryEntity;
	private MenuCourseEntity menuCourseEntity;
	private double price;

	private Status status;
	
	private long dateTime;

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

	public FoodCategoryEntity getFoodCategoryEntity() {
		return foodCategoryEntity;
	}

	public void setFoodCategoryEntity(FoodCategoryEntity foodCategoryEntity) {
		this.foodCategoryEntity = foodCategoryEntity;
	}

	public MenuCourseEntity getMenuCourseEntity() {
		return menuCourseEntity;
	}

	public void setMenuCourseEntity(MenuCourseEntity menuCourseEntity) {
		this.menuCourseEntity = menuCourseEntity;
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

	@Override
	public String toString() {
		return "MenuEntity{" +
				"menuId=" + menuId +
				", menuUUID='" + menuUUID + '\'' +
				", menuItemCode='" + menuItemCode + '\'' +
				", name='" + name + '\'' +
				", foodCategoryEntity=" + foodCategoryEntity +
				", menuCourseEntity=" + menuCourseEntity +
				", price=" + price +
				", status=" + status +
				", dateTime=" + dateTime +
				'}';
	}
}
