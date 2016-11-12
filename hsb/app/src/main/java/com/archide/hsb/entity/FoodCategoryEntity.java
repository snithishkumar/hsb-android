package com.archide.hsb.entity;

public class FoodCategoryEntity {
	
	public static final String FOOD_CATEGORY_ID = "foodCategoryId";
	public static final String FOOD_CATEGORY_UUID = "foodCategoryUUID";
	public static final String CATEGORY_NAME = "categoryName";
	public static final String DATE_TIME = "dateTime";
	public static final String MENU_COURSE = "menuCourse";
	
	private int foodCategoryId;
	private String foodCategoryUUID;
	private String categoryName;
	private long dateTime;
	
	private MenuCourseEntity menuCourseEntity;
	
	public int getFoodCategoryId() {
		return foodCategoryId;
	}
	public void setFoodCategoryId(int foodCategoryId) {
		this.foodCategoryId = foodCategoryId;
	}
	public String getFoodCategoryUUID() {
		return foodCategoryUUID;
	}
	public void setFoodCategoryUUID(String foodCategoryUUID) {
		this.foodCategoryUUID = foodCategoryUUID;
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

	public MenuCourseEntity getMenuCourseEntity() {
		return menuCourseEntity;
	}

	public void setMenuCourseEntity(MenuCourseEntity menuCourseEntity) {
		this.menuCourseEntity = menuCourseEntity;
	}

	@Override
	public String toString() {
		return "FoodCategory [foodCategoryId=" + foodCategoryId + ", foodCategoryUUID=" + foodCategoryUUID
				+ ", categoryName=" + categoryName + ", dateTime=" + dateTime + "]";
	}
	
	

}
