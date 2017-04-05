package com.archide.hsb.view.model;

import com.archide.hsb.entity.FoodCategoryEntity;
import com.archide.hsb.entity.MenuEntity;
import com.archide.hsb.entity.PlacedOrderItemsEntity;
import com.archide.hsb.enumeration.FoodType;
import com.archide.hsb.enumeration.OrderStatus;

import java.util.Objects;

/**
 * Created by Nithish on 16/11/16.
 */

public class MenuItemsViewModel {
    private String uuid;
    private String name;
    private int count;
    private boolean isOrdered;
    private boolean isCategory;
    private double cost;
    private String itemCode;
    private OrderStatus orderStatus;
    private String description;
    private String tasteType;
    private FoodType foodType;
    private int availableCount;

    public MenuItemsViewModel(){

    }

    public MenuItemsViewModel(MenuEntity menuEntity){
        this.uuid = menuEntity.getMenuUUID();
        this.name = menuEntity.getName();
        this.cost = menuEntity.getPrice();
        this.itemCode = menuEntity.getMenuItemCode();
        this.description = menuEntity.getDescription();
        this.tasteType = menuEntity.getTasteType();
        this.foodType = menuEntity.getFoodType();
        this.availableCount = menuEntity.getAvailableCount();

    }

    public MenuItemsViewModel(FoodCategoryEntity foodCategoryEntity){
        this.name = foodCategoryEntity.getCategoryName();
        this.isCategory = true;
    }

    public MenuItemsViewModel(PlacedOrderItemsEntity placedOrderItemsEntity){
        this.itemCode = placedOrderItemsEntity.getItemCode();
        this.name = placedOrderItemsEntity.getName();
        this.count = placedOrderItemsEntity.getQuantity();
        this.cost = placedOrderItemsEntity.getCost();
        this.uuid = placedOrderItemsEntity.getMenuItem().getMenuUUID();
        this.tasteType = placedOrderItemsEntity.getMenuItem().getTasteType();
        this.orderStatus = placedOrderItemsEntity.getOrderStatus();
        this.isOrdered = true;
        this.foodType = placedOrderItemsEntity.getMenuItem().getFoodType();
        this.availableCount = placedOrderItemsEntity.getMenuItem().getAvailableCount();
    }

    public int getAvailableCount() {
        return availableCount;
    }

    public void setAvailableCount(int availableCount) {
        this.availableCount = availableCount;
    }

    public FoodType getFoodType() {
        return foodType;
    }

    public void setFoodType(FoodType foodType) {
        this.foodType = foodType;
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

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public boolean isOrdered() {
        return isOrdered;
    }

    public void setOrdered(boolean ordered) {
        isOrdered = ordered;
    }

    public boolean isCategory() {
        return isCategory;
    }

    public void setCategory(boolean category) {
        isCategory = category;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    @Override
    public String toString() {
        return "MenuItemsViewModel{" +
                "uuid='" + uuid + '\'' +
                ", name='" + name + '\'' +
                ", count=" + count +
                ", isOrdered=" + isOrdered +
                ", isCategory=" + isCategory +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MenuItemsViewModel that = (MenuItemsViewModel) o;
        return Objects.equals(itemCode, that.itemCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(itemCode);
    }
}
