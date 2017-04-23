package com.archide.hsb.sync.json;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nithish on 26/11/16.
 */

public class KitchenOrderListResponse {

    private List<PlaceOrdersJson> placeOrdersJsonList = new ArrayList<>();
    private List<String> closedOrders = new ArrayList<>();
    private int unAvailableCount;

    public int getUnAvailableCount() {
        return unAvailableCount;
    }

    public void setUnAvailableCount(int unAvailableCount) {
        this.unAvailableCount = unAvailableCount;
    }

    public List<PlaceOrdersJson> getPlaceOrdersJsonList() {
        return placeOrdersJsonList;
    }

    public void setPlaceOrdersJsonList(List<PlaceOrdersJson> placeOrdersJsonList) {
        this.placeOrdersJsonList = placeOrdersJsonList;
    }

    public List<String> getClosedOrders() {
        return closedOrders;
    }

    public void setClosedOrders(List<String> closedOrders) {
        this.closedOrders = closedOrders;
    }

    @Override
    public String toString() {
        return "KitchenOrderListResponse{" +
                "placeOrdersJsonList=" + placeOrdersJsonList +
                ", closedOrders=" + closedOrders +
                '}';
    }
}
