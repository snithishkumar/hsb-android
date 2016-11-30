package com.archide.hsb.sync.json;

import java.util.ArrayList;
import java.util.List;

public class KitchenOrderStatusSyncResponse {
	
	private String placedOrderUuid;
	private List<String> placedOrderItemsUuid = new ArrayList<>();
	public String getPlacedOrderUuid() {
		return placedOrderUuid;
	}
	public void setPlacedOrderUuid(String placedOrderUuid) {
		this.placedOrderUuid = placedOrderUuid;
	}
	public List<String> getPlacedOrderItemsUuid() {
		return placedOrderItemsUuid;
	}
	public void setPlacedOrderItemsUuid(List<String> placedOrderItemsUuid) {
		this.placedOrderItemsUuid = placedOrderItemsUuid;
	}
	@Override
	public String toString() {
		return "KitchenOrderStatusSyncResponse [placedOrderUuid=" + placedOrderUuid + ", placedOrderItemsUuid="
				+ placedOrderItemsUuid + "]";
	}
	
	
	

}
