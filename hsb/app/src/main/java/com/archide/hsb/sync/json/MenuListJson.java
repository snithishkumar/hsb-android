package com.archide.hsb.sync.json;

import java.util.ArrayList;
import java.util.List;

public class MenuListJson {
	
	private String menuCourseUuid;
	private String categoryName;
	private long dateTime;
	private List<FoodCategoryJson> categoryJsons = new ArrayList<FoodCategoryJson>();

	public String getMenuCourseUuid() {
		return menuCourseUuid;
	}
	public void setMenuCourseUuid(String menuCourseUuid) {
		this.menuCourseUuid = menuCourseUuid;
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
	public List<FoodCategoryJson> getCategoryJsons() {
		return categoryJsons;
	}
	public void setCategoryJsons(List<FoodCategoryJson> categoryJsons) {
		this.categoryJsons = categoryJsons;
	}
	@Override
	public String toString() {
		return "MenuListJson [menuCourseUuid=" + menuCourseUuid + ", categoryName=" + categoryName + ", dateTime="
				+ dateTime + ", categoryJsons=" + categoryJsons + "]";
	}
	

}
