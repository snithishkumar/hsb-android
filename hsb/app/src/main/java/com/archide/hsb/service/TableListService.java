package com.archide.hsb.service;

import android.content.Context;

/**
 * Created by Nithish on 12/11/16.
 */

public interface TableListService {

    void getTableList();
    void getMenuItems(String tableNumber);
    void createAdmin(String tableNumber,String mPin);

    void updateUserMobile(String userMobile);

    boolean isTableConfigured();
}
