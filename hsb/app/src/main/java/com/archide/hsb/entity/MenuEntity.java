package com.archide.hsb.entity;

import com.archide.hsb.enumeration.FoodType;
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

    public static final String DISPLAY_ORDER = "displayOrder";
    public static final String DESCRIPTION = "description";
    public static final String TASTE_TYPE = "tasteType";
	public static final String FOOD_TYPE = "foodType";
	public static final String AVAILABLE_COUNT = "availableCount";


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

    @DatabaseField(columnName = DISPLAY_ORDER)
	private int displayOrder;
    @DatabaseField(columnName = DESCRIPTION)
	private String description;
    @DatabaseField(columnName = TASTE_TYPE)
	private String tasteType;
	@DatabaseField(columnName = FOOD_TYPE)
	private FoodType foodType;

	@DatabaseField(columnName = AVAILABLE_COUNT)
	private int availableCount;

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
        this.displayOrder = menuItemJson.getDisplayOrder();
        this.description = menuItemJson.getDescription();
        this.tasteType = menuItemJson.getTasteType();
        this.foodType = menuItemJson.getFoodType();
		this.availableCount = menuItemJson.getAvailableCount();
    }

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

    public FoodType getFoodType() {
        return foodType;
    }

    public void setFoodType(FoodType foodType) {
        this.foodType = foodType;
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
