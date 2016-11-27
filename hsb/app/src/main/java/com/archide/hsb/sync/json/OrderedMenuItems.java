package com.archide.hsb.sync.json;

import com.archide.hsb.entity.PlacedOrderItemsEntity;
import com.archide.hsb.enumeration.FoodType;
import com.archide.hsb.enumeration.OrderStatus;

public class OrderedMenuItems {
	
	private String name;
	private int quantity;
	private String placedOrderItemsUUID;
	private String menuUuid;
	private String itemCode;
	private String categoryName;
	private String categoryUuid;
    private FoodType foodType;
	private OrderStatus orderStatus;
    private int unAvailableCount;
	
	public OrderedMenuItems(){
		
	}

	public OrderedMenuItems(PlacedOrderItemsEntity placedOrderItemsEntity){
        this.name = placedOrderItemsEntity.getName();
        this.quantity = placedOrderItemsEntity.getQuantity();
        this.placedOrderItemsUUID = placedOrderItemsEntity.getPlacedOrderItemsUUID();
        this.menuUuid = placedOrderItemsEntity.getMenuItem().getMenuUUID();
        this.itemCode = placedOrderItemsEntity.getItemCode();
        this.orderStatus = placedOrderItemsEntity.getOrderStatus();



	}

    public int getUnAvailableCount() {
        return unAvailableCount;
    }

    public void setUnAvailableCount(int unAvailableCount) {
        this.unAvailableCount = unAvailableCount;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public FoodType getFoodType() {
        return foodType;
    }

    public void setFoodType(FoodType foodType) {
        this.foodType = foodType;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryUuid() {
        return categoryUuid;
    }

    public void setCategoryUuid(String categoryUuid) {
        this.categoryUuid = categoryUuid;
    }

    public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public String getPlacedOrderItemsUUID() {
		return placedOrderItemsUUID;
	}
	public void setPlacedOrderItemsUUID(String placedOrderItemsUUID) {
		this.placedOrderItemsUUID = placedOrderItemsUUID;
	}
	@Override
	public String toString() {
		return "OrderedMenuItems [name=" + name + ", quantity=" + quantity + ", placedOrderItemsUUID="
				+ placedOrderItemsUUID + "]";
	}

	public String getMenuUuid() {
		return menuUuid;
	}

	public void setMenuUuid(String menuUuid) {
		this.menuUuid = menuUuid;
	}
	
	

}
