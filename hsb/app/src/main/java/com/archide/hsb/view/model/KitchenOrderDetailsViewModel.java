package com.archide.hsb.view.model;

import com.archide.hsb.entity.KitchenCookingCmntsEntity;
import com.archide.hsb.entity.KitchenOrderDetailsEntity;
import com.archide.hsb.entity.KitchenOrdersCategoryEntity;
import com.archide.hsb.enumeration.OrderStatus;
import com.archide.hsb.enumeration.Status;
import com.archide.hsb.enumeration.ViewStatus;

/**
 * Created by Nithish on 27/11/16.
 */

public class KitchenOrderDetailsViewModel {

    private String name;
    private String quantity;
    private int id;
    private OrderStatus status;
    private boolean isCategory;
    private boolean isComments;
    private String comments;
    private ViewStatus viewStatus;
    private int unAvailableCount;
    private boolean isEdited;

    public KitchenOrderDetailsViewModel(){

    }

    public KitchenOrderDetailsViewModel(KitchenOrderDetailsEntity kitchenOrderDetailsEntity){
        this.name = kitchenOrderDetailsEntity.getName();
        this.quantity = String.valueOf(kitchenOrderDetailsEntity.getQuantity());
        this.viewStatus = kitchenOrderDetailsEntity.getViewStatus();
        this.id = kitchenOrderDetailsEntity.getMenuId();
        this.status = kitchenOrderDetailsEntity.getOrderStatus();

    }

    public KitchenOrderDetailsViewModel(KitchenOrdersCategoryEntity kitchenOrdersCategoryEntity){
        this.name = kitchenOrdersCategoryEntity.getCategoryName();
        this.isCategory = true;
    }

    public KitchenOrderDetailsViewModel(KitchenCookingCmntsEntity kitchenCookingCmntsEntity){
        this.comments = kitchenCookingCmntsEntity.getCookingComments();
        this.isComments = true;
    }

    public boolean isEdited() {
        return isEdited;
    }

    public void setEdited(boolean edited) {
        isEdited = edited;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ViewStatus getViewStatus() {
        return viewStatus;
    }

    public void setViewStatus(ViewStatus viewStatus) {
        this.viewStatus = viewStatus;
    }

    public boolean isCategory() {
        return isCategory;
    }

    public void setCategory(boolean category) {
        isCategory = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public boolean isComments() {
        return isComments;
    }

    public void setComments(boolean comments) {
        isComments = comments;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public int getUnAvailableCount() {
        return unAvailableCount;
    }

    public void setUnAvailableCount(int unAvailableCount) {
        this.unAvailableCount = unAvailableCount;
    }

    @Override
    public String toString() {
        return "KitchenOrderDetailsViewModel{" +
                "name='" + name + '\'' +
                ", quantity='" + quantity + '\'' +
                ", status=" + status +
                '}';
    }
}
