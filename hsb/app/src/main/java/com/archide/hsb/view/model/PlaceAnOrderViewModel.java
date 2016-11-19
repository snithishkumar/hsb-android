package com.archide.hsb.view.model;

import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nithish on 19/11/16.
 */

public class PlaceAnOrderViewModel {

    private String  cookingComments ;
    private double subTotalBeforeDiscount ;
    private double discount ;
    private double subTotal ;
    private double serviceTax ;
    private double serviceVat ;
    private double totalAmount ;

    private List<MenuItemsViewModel> menuItemsViewModels = new ArrayList<>();

    public String getCookingComments() {
        return cookingComments;
    }

    public void setCookingComments(String cookingComments) {
        this.cookingComments = cookingComments;
    }

    public double getSubTotalBeforeDiscount() {
        return subTotalBeforeDiscount;
    }

    public void setSubTotalBeforeDiscount(double subTotalBeforeDiscount) {
        this.subTotalBeforeDiscount = subTotalBeforeDiscount;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public double getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(double subTotal) {
        this.subTotal = subTotal;
    }

    public double getServiceTax() {
        return serviceTax;
    }

    public void setServiceTax(double serviceTax) {
        this.serviceTax = serviceTax;
    }

    public double getServiceVat() {
        return serviceVat;
    }

    public void setServiceVat(double serviceVat) {
        this.serviceVat = serviceVat;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public List<MenuItemsViewModel> getMenuItemsViewModels() {
        return menuItemsViewModels;
    }

    public void setMenuItemsViewModels(List<MenuItemsViewModel> menuItemsViewModels) {
        this.menuItemsViewModels = menuItemsViewModels;
    }

    @Override
    public String toString() {
        return "PlaceAnOrderViewModel{" +
                "cookingComments='" + cookingComments + '\'' +
                ", subTotalBeforeDiscount=" + subTotalBeforeDiscount +
                ", discount=" + discount +
                ", subTotal=" + subTotal +
                ", serviceTax=" + serviceTax +
                ", serviceVat=" + serviceVat +
                ", totalAmount=" + totalAmount +
                ", menuItemsViewModels=" + menuItemsViewModels +
                '}';
    }
}
