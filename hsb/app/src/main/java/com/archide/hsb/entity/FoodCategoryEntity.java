package com.archide.hsb.entity;

import com.archide.hsb.sync.json.FoodCategoryJson;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "FoodCategoryEntity")
public class FoodCategoryEntity {
	
	public static final String FOOD_CATEGORY_ID = "foodCategoryId";
	public static final String FOOD_CATEGORY_UUID = "foodCategoryUUID";
	public static final String CATEGORY_NAME = "categoryName";
	public static final String DATE_TIME = "dateTime";
	public static final String MENU_COURSE = "menuCourse";

    @DatabaseField(columnName = FOOD_CATEGORY_ID,generatedId = true)
	private int foodCategoryId;
    @DatabaseField(columnName = FOOD_CATEGORY_UUID)
	private String foodCategoryUUID;
    @DatabaseField(columnName = CATEGORY_NAME)
	private String categoryName;
    @DatabaseField(columnName = DATE_TIME)
	private long dateTime;

    @DatabaseField(columnName = MENU_COURSE,foreign = true,foreignAutoRefresh =  true)
	private MenuCourseEntity menuCourseEntity;

    public FoodCategoryEntity(){

    }

	public FoodCategoryEntity(FoodCategoryJson foodCategoryJson){
		this.foodCategoryUUID = foodCategoryJson.getFoodCategoryUuid();
		this.categoryName = foodCategoryJson.getCategoryName();
		this.dateTime = foodCategoryJson.getDateTime();

	}
	
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
