package com.archide.hsb.service;

import com.archide.hsb.entity.ConfigurationEntity;
import com.archide.hsb.entity.UsersEntity;
import com.archide.hsb.enumeration.AppType;
import com.archide.hsb.enumeration.OrderType;

import java.util.List;

/**
 * Created by Nithish on 12/11/16.
 */

public interface TableListService {

    void getMenuItems();
    void createAdmin(String mPin,AppType selectedAppType);



  //  boolean isTableConfigured();

    ConfigurationEntity getAppType();

    int verifyLogin(String mPin);



    void changeTableNumber(String tableNumber);


    void removeAllData();


    void createUsers(String mobileNumber,OrderType orderType);

    void updateTableNumber(String tableNumber,String userMobileNumber,OrderType orderType);
}
