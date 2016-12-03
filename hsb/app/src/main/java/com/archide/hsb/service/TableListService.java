package com.archide.hsb.service;

import android.content.Context;

import com.archide.hsb.enumeration.AppType;

/**
 * Created by Nithish on 12/11/16.
 */

public interface TableListService {

    void getTableList();
    void getMenuItems(String tableNumber,String mobileNumber);
    void createAdmin(String tableNumber,String mPin,AppType selectedAppType);

    void updateUserMobile(String userMobile);

    boolean isTableConfigured();
}
