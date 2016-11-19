package com.archide.hsb.view.model;

/**
 * Created by Nithish on 19/11/16.
 */

public class OrderDetailsViewModel {

    private int totalCount;
    private double totalCost;

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }
}
