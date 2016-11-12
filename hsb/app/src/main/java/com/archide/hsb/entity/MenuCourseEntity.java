package com.archide.hsb.entity;

public class MenuCourseEntity {
	public static final String MENU_COURSE_ID = "menuCourseId";
	public static final String MENU_COURSE_UUID = "menuCourseUUID";
	public static final String CATEGORY_NAME = "categoryName";
	public static final String DATE_TIME = "dateTime";
	

	private int menuCourseId;

	private String menuCourseUUID;
	private String categoryName;
	private long dateTime;
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
