package com.archide.hsb.view.model;

import com.archide.hsb.entity.KitchenOrderDetailsEntity;
import com.archide.hsb.entity.KitchenOrdersCategoryEntity;
import com.archide.hsb.enumeration.Status;

/**
 * Created by Nithish on 27/11/16.
 */

public class KitchenOrderDetailsViewModel {

    private String name;
    private String quantity;
    private Status status;
    private boolean isCategory;
    private boolean isComments;
    private String comments;

    public KitchenOrderDetailsViewModel(){

    }

    public KitchenOrderDetailsViewModel(KitchenOrderDetailsEntity kitchenOrderDetailsEntity){
        this.name = kitchenOrderDetailsEntity.getName();
        this.quantity = String.valueOf(kitchenOrderDetailsEntity.getQuantity());

    }

    public KitchenOrderDetailsViewModel(KitchenOrdersCategoryEntity kitchenOrdersCategoryEntity){
            this.name = kitchenOrdersCategoryEntity.getCategoryName();
        this.isCategory = true;
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

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
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

    @Override
    public String toString() {
        return "KitchenOrderDetailsViewModel{" +
                "name='" + name + '\'' +
                ", quantity='" + quantity + '\'' +
                ", status=" + status +
                '}';
    }
}
