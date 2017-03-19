package com.archide.hsb.service;

import android.content.Context;

import com.archide.hsb.entity.ConfigurationEntity;
import com.archide.hsb.entity.PlacedOrderItemsEntity;
import com.archide.hsb.entity.UsersEntity;
import com.archide.hsb.view.model.CloseOrderViewModel;
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

    void conformOrder(String cookingComments,Context context);

    PlaceAnOrderViewModel getPlacedHistoryOrderViewModel();

    void checkAvailability(Context context);

    void removeUnAvailableOrders();

    void getPreviousOrderFromServer(Context context);

    void closeAnOrder(Context context);

    CloseOrderViewModel getBillingDetails();

    void resentBilling(Context context,String tableNumber,String mobileNumber);

    void removeAllData();

    void getMenuItems(Context context);


    void updateOrderItems(MenuItemsViewModel menuItemsViewModel);

    void logout(Context context,String tableNumber,String mobileNumber);

    ConfigurationEntity getAppType();

    UsersEntity getUsersEntity();

}
