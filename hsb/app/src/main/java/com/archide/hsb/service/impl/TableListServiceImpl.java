package com.archide.hsb.service.impl;

import android.accounts.Account;
import android.content.ContentResolver;
import android.content.Context;
import android.os.Bundle;

import com.archide.hsb.dao.AdminDao;
import com.archide.hsb.dao.OrdersDao;
import com.archide.hsb.dao.impl.AdminDaoImpl;
import com.archide.hsb.dao.impl.OrdersDaoImpl;
import com.archide.hsb.entity.ConfigurationEntity;
import com.archide.hsb.entity.UsersEntity;
import com.archide.hsb.enumeration.AppType;
import com.archide.hsb.service.TableListService;
import com.archide.hsb.sync.HsbSyncAdapter;
import com.archide.hsb.sync.SyncEvent;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import hsb.archide.com.hsb.R;

/**
 * Created by Nithish on 12/11/16.
 */

public class TableListServiceImpl implements TableListService {

    private Bundle settingsBundle;
    private Account account ;
    private AdminDao adminDao;
    private Context context;

    public TableListServiceImpl(Context context){
        this.context = context;
       try{
           init();
       }catch (Exception e){
           e.printStackTrace();
       }

    }

    @Override
    public void getTableList() {
        account = HsbSyncAdapter.getSyncAccount(context);
        settingsBundle.putInt("currentScreen", SyncEvent.GET_TABLE_LIST);
        ContentResolver.requestSync(account, context.getString(R.string.auth_type), settingsBundle);
    }


    @Override
    public void getMenuItems(String tableNumber,String mobileNumber,String vUserTypeText){
        account = HsbSyncAdapter.getSyncAccount(context);
        settingsBundle.putInt("currentScreen", SyncEvent.GET_MENU_LIST);
        settingsBundle.putString("tableNumber", tableNumber);
        settingsBundle.putString("mobileNumber", mobileNumber);
        if(vUserTypeText != null){
            settingsBundle.putString("userType", vUserTypeText);
        }

        ContentResolver.requestSync(account, context.getString(R.string.auth_type), settingsBundle);
    }




    private void init()throws SQLException{
        adminDao = new AdminDaoImpl(context);
        if(settingsBundle == null){
            settingsBundle = new Bundle();
            settingsBundle.putBoolean(
                    ContentResolver.SYNC_EXTRAS_MANUAL, true);
            settingsBundle.putBoolean(
                    ContentResolver.SYNC_EXTRAS_EXPEDITED, true);

        }

    }


    @Override
    public void createAdmin(String tableNumber, String mPin,AppType selectedAppType) {
        try {
            // Create an App Type
            ConfigurationEntity configurationEntity = new ConfigurationEntity();
            configurationEntity.setAppType(selectedAppType);
            configurationEntity.setTableNumber(tableNumber);
            configurationEntity.setmPin(mPin);
            adminDao.createAppType(configurationEntity);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateUserMobile(String userMobile) {
        try{
           UsersEntity usersEntity =  adminDao.getUsersEntity(userMobile);
            if(usersEntity == null){
                usersEntity = new UsersEntity();
                usersEntity.setUserMobileNumber(userMobile);
                usersEntity.setClosed(false);
                adminDao.createUsers(usersEntity);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }



    @Override
    public ConfigurationEntity getAppType(){
        try {
          return   adminDao.getAppType();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public int verifyLogin(String mPin){
        try{
            ConfigurationEntity configurationEntity = adminDao.getAppType();
            if(configurationEntity == null){
                return 1;
            }else  if(configurationEntity.getmPin().equals(mPin)){
                return 2;
            }
            return 3;
        }catch (Exception e){
            e.printStackTrace();
        }
        return 1;
    }


    @Override
    public List<UsersEntity> getUsers(){
        try {
            return adminDao.getUsersList();
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ArrayList<>();
    }


    @Override
    public void removePreviousData(){
        try{
            OrdersDao ordersDao = new OrdersDaoImpl(context);
            ordersDao.removeAllData();
            if(adminDao.isOrderCloseUser()){
                adminDao.removeClosedUser();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    @Override
    public void removeAllData(){
        try{
            OrdersDao ordersDao = new OrdersDaoImpl(context);
            ordersDao.removeAllData();
            adminDao.removeAllUsers();
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    @Override
    public void removeUsers(String userMobileNumber){
        try{
            adminDao.removeUser(userMobileNumber);
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    @Override
    public void changeTableNumber(String tableNumber){
        try {
            AdminDao adminDao = new AdminDaoImpl(context);
            adminDao.changeTableNumber(tableNumber);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public boolean isOrderOpen(){
        try{
            return   adminDao.isOrderCloseUser();
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean isUnClosedUser(){
        try{
           return adminDao.isUnClosedUser();
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }



}
