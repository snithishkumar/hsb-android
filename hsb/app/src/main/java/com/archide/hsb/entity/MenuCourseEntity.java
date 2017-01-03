package com.archide.hsb.entity;

import com.archide.hsb.sync.json.MenuListJson;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "MenuCourseEntity")
public class MenuCourseEntity {
	public static final String MENU_COURSE_ID = "menuCourseId";
	public static final String MENU_COURSE_UUID = "menuCourseUUID";
	public static final String CATEGORY_NAME = "categoryName";
	public static final String DATE_TIME = "dateTime";
    public static final String DISPLAY_ORDER = "displayOrder";


    @DatabaseField(columnName = MENU_COURSE_ID,generatedId = true)
	private int menuCourseId;

    @DatabaseField(columnName = MENU_COURSE_UUID)
	private String menuCourseUUID;
    @DatabaseField(columnName = CATEGORY_NAME)
	private String categoryName;
    @DatabaseField(columnName = DATE_TIME)
	private long dateTime;

    @DatabaseField(columnName = DISPLAY_ORDER)
	private int displayOrder;

    public MenuCourseEntity(){

    }

	public MenuCourseEntity(MenuListJson menuListJson){
		this.menuCourseUUID = menuListJson.getMenuCourseUuid();
		this.categoryName = menuListJson.getCategoryName();
		this.dateTime = menuListJson.getDateTime();
        this.displayOrder = menuListJson.getDisplayOrder();
	}

	public int getMenuCourseId() {
		return menuCourseId;
	}
	public void setMenuCourseId(int menuCourseId) {
		this.menuCourseId = menuCourseId;
	}
	public String getMenuCourseUUID() {
		return menuCourseUUID;
	}
	public void setMenuCourseUUID(String menuCourseUUID) {
		this.menuCourseUUID = menuCourseUUID;
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
	@Override
	public String toString() {
		return "MenuCourse [menuCourseId=" + menuCourseId + ", menuCourseUUID=" + menuCourseUUID + ", categoryName="
				+ categoryName + ", dateTime=" + dateTime + "]";
	}
	
	

}
