package com.archide.hsb.service;

import android.content.Context;

import com.archide.hsb.entity.PlacedOrderItemsEntity;
import com.archide.hsb.view.model.MenuItemsViewModel;
import com.archide.hsb.view.model.PlaceAnOrderViewModel;

import java.util.List;

/**
 * Created by Nithish on 19/11/16.
 */

public interface OrderService {


    void addOrderItems(MenuItemsViewModel menuItemsViewModel);

    void removeOrderItems(MenuItemsViewModel menuItemsViewModel);

    PlaceAnOrderViewModel getCurrentOrderDetails();

    PlaceAnOrderViewModel calculateAmountDetails(List<PlacedOrderItemsEntity> placedOrderItemsEntityList);

    void calcAmount(PlaceAnOrderViewModel placeAnOrderViewModel);

    void conformOrder(PlaceAnOrderViewModel placeAnOrderViewModel,String mobileNumber,String tableNumber,Context context);

    PlaceAnOrderViewModel getPlacedHistoryOrderViewModel();

    void checkAvailability(Context context);

    void removeUnAvailableOrders();

    void getPreviousOrderFromServer(Context context,String tableNumber,String mobileNumber);

    void closeAnOrder(Context context,String tableNumber,String mobileNumber);

    PlaceAnOrderViewModel getBillingDetails();

}
