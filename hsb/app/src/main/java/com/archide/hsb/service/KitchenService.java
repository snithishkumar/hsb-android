package com.archide.hsb.service;

import com.archide.hsb.view.model.KitchenOrderListViewModel;

import java.util.List;

/**
 * Created by Nithish on 26/11/16.
 */

public interface KitchenService {

    List<KitchenOrderListViewModel> getOrderList();


}
