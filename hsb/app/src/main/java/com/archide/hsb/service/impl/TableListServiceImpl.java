package com.archide.hsb.service.impl;

import android.accounts.Account;
import android.content.ContentResolver;
import android.content.Context;
import android.os.Bundle;

import com.archide.hsb.dao.AdminDao;
import com.archide.hsb.dao.OrdersDao;
import com.archide.hsb.dao.impl.AdminDaoImpl;
import com.archide.hsb.dao.impl.OrdersDaoImpl;
import com.archide.hsb.entity.AdminEntity;
import com.archide.hsb.enumeration.AppType;
import com.archide.hsb.service.TableListService;
import com.archide.hsb.sync.HsbSyncAdapter;
import com.archide.hsb.sync.SyncEvent;

import java.sql.SQLException;

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
    public void getMenuItems(String tableNumber,String mobileNumber){
        account = HsbSyncAdapter.getSyncAccount(context);
        settingsBundle.putInt("currentScreen", SyncEvent.GET_MENU_LIST);
        settingsBundle.putString("tableNumber", tableNumber);
        settingsBundle.putString("mobileNumber", mobileNumber);
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
            AdminEntity adminEntity = new AdminEntity();
            adminEntity.setTableNumber(tableNumber);
            adminEntity.setmPin(mPin);
            adminEntity.setAppType(selectedAppType);
            adminDao.createAdminEntity(adminEntity);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateUserMobile(String userMobile) {
        try{
            boolean isMobileSame =  adminDao.isMobilePresent(userMobile);
            if(!isMobileSame){
                OrdersDao ordersDao = new OrdersDaoImpl(context);
                ordersDao.removeCurrentOrder();
                adminDao.updateUserMobile(userMobile);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public boolean isTableConfigured() {
        try{
            return adminDao.isTableConfigured();
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }


}
