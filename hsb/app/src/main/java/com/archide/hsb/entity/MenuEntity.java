package com.archide.hsb.entity;

import com.archide.hsb.enumeration.Status;
import com.archide.hsb.sync.json.MenuItemJson;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;


@DatabaseTable(tableName = "MenuEntity")
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
	public static final String SERVER_DATE_TIME = "serverDateTime";


    @DatabaseField(columnName = MENU_ID,generatedId = true)
	private int menuId;
    @DatabaseField(columnName = MENU_UUID)
	private String menuUUID;
    @DatabaseField(columnName = MENU_ITEM_CODE)
	private String menuItemCode;
    @DatabaseField(columnName = NAME)
	private String name;
    @DatabaseField(columnName = FOOD_CATEGORY,foreign = true,foreignAutoRefresh =  true)
	private FoodCategoryEntity foodCategoryEntity;
    @DatabaseField(columnName = MENU_COURSE,foreign = true,foreignAutoRefresh =  true)
	private MenuCourseEntity menuCourseEntity;
    @DatabaseField(columnName = PRICE)
	private double price;
    @DatabaseField(columnName = STATUS)
	private Status status;
    @DatabaseField(columnName = DATE_TIME)
	private long dateTime;
    @DatabaseField(columnName = SERVER_DATE_TIME)
	private long serverDateTime;

    public MenuEntity(){

    }

	public MenuEntity(MenuItemJson menuItemJson){
        clone(menuItemJson);
	}

    public void clone(MenuItemJson menuItemJson){
        this.menuUUID = menuItemJson.getMenuUuid();
        this.menuItemCode = menuItemJson.getMenuItemCode();
        this.name = menuItemJson.getName();
        this.price = menuItemJson.getPrice();
        this.status = menuItemJson.getStatus();
        this.dateTime = menuItemJson.getDateTime();
        this.serverDateTime = menuItemJson.getServerDateTime();
    }

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

	public long getServerDateTime() {
		return serverDateTime;
	}

	public void setServerDateTime(long serverDateTime) {
		this.serverDateTime = serverDateTime;
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
