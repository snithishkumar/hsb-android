package com.archide.hsb.sync.json;

import java.util.ArrayList;
import java.util.List;

public class UnAvailableMenuDetails {

	private List<String> unAvailableMenuDetails = new ArrayList<>();
	private List<MenuListJson> menuListJsonList = new ArrayList<MenuListJson>();

	public List<String> getUnAvailableMenuDetails() {
		return unAvailableMenuDetails;
	}

	public void setUnAvailableMenuDetails(List<String> unAvailableMenuDetails) {
		this.unAvailableMenuDetails = unAvailableMenuDetails;
	}

	public List<MenuListJson> getMenuListJsonList() {
		return menuListJsonList;
	}

	public void setMenuListJsonList(List<MenuListJson> menuListJsonList) {
		this.menuListJsonList = menuListJsonList;
	}

	@Override
	public String toString() {
		return "UnAvailableMenuDetails [unAvailableMenuDetails=" + unAvailableMenuDetails + ", menuListJsonList="
				+ menuListJsonList + "]";
	}

}
