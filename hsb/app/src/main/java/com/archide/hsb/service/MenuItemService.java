package com.archide.hsb.service;

import com.archide.hsb.entity.MenuCourseEntity;
import com.archide.hsb.view.model.MenuItemsViewModel;
import com.archide.hsb.view.model.OrderDetailsViewModel;

import java.util.List;

/**
 * Created by Nithish on 16/11/16.
 */

public interface MenuItemService {

    List<MenuCourseEntity> getMenuCourseEntity();

    List<MenuItemsViewModel> getMenuItemsViewModel(String menuCourseUuid, OrderDetailsViewModel orderDetailsViewModel);

    void getCurrentOrdersCounts(OrderDetailsViewModel orderDetailsViewModel);

    boolean isAvailable(MenuCourseEntity menuCourseEntity);


}
