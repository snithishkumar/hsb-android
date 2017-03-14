package com.archide.hsb.service;

import android.content.Context;

import com.archide.hsb.entity.KitchenMenuItemsEntity;
import com.archide.hsb.view.model.KitchenCommentsViewModel;
import com.archide.hsb.view.model.KitchenOrderDetailsViewModel;
import com.archide.hsb.view.model.KitchenOrderListViewModel;

import java.util.List;

/**
 * Created by Nithish on 26/11/16.
 */

public interface KitchenService {

    List<KitchenOrderListViewModel> getOrderList();

    List<KitchenOrderDetailsViewModel> getKitchenOrderDetails(String orderId);

    void updateKitchenOrderViewStatus(String orderId,List<KitchenOrderDetailsViewModel> detailsViewModels );

    void saveOrderStatus(List<KitchenOrderDetailsViewModel> detailsViewModels,String orderId ,Context context );

    List<KitchenCommentsViewModel> getKitchenCommentsViewModel(String orderId);

    List<KitchenMenuItemsEntity> getKitchenMenuItemsModels(String searchText);

    void updateKitchenMenuItemsEntity(KitchenMenuItemsEntity kitchenMenuItemsEntity);


}
