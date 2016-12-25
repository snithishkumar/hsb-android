package com.archide.hsb.dao.impl;

import android.content.Context;

import com.archide.hsb.dao.AdminDao;
import com.archide.hsb.entity.AdminEntity;
import com.archide.hsb.entity.ConfigurationEntity;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Nithish on 20/11/16.
 */

public class AdminDaoImpl extends BaseDaoImpl implements AdminDao {

    Dao<AdminEntity,Integer> adminEntityDao = null;
    Dao<ConfigurationEntity,Integer> appTypeDao = null;



    public AdminDaoImpl(Context context) throws SQLException {
        super(context);
        initDao();
    }

    @Override
    protected void initDao() throws SQLException {
        adminEntityDao = databaseHelper.getDao(AdminEntity.class);
        appTypeDao = databaseHelper.getDao(ConfigurationEntity.class);
    }



    @Override
    public void updateUserMobile(String mobileNumber) throws SQLException{
        adminEntityDao.updateBuilder().updateColumnValue(AdminEntity.USER_MOBILE,mobileNumber).update();
    }

    @Override
    public void createAdminEntity(AdminEntity adminEntity) throws SQLException{
        adminEntityDao.create(adminEntity);
    }

    @Override
    public void createAppType(ConfigurationEntity configurationEntity)throws SQLException{
        appTypeDao.create(configurationEntity);
    }

    @Override
    public boolean isMobilePresent(String mobileNumber) throws SQLException {
        AdminEntity adminEntity = adminEntityDao.queryBuilder().where().eq(AdminEntity.USER_MOBILE,mobileNumber).queryForFirst();
        return adminEntity != null;
    }

  /*  @Override
    public boolean isTableConfigured() throws SQLException {
        AdminEntity adminEntity = adminEntityDao.queryBuilder().queryForFirst();
        if(adminEntity != null){
            ActivityUtil.TABLE_NUMBER = adminEntity.getTableNumber();
        }

        return adminEntity != null;
    }*/


    public ConfigurationEntity getAppType()throws SQLException{
       return appTypeDao.queryBuilder().queryForFirst();
    }

    @Override
    public AdminEntity getAdminEntity()throws SQLException{
       List<AdminEntity> adminEntityList = adminEntityDao.queryForAll();
        if(adminEntityList.size() > 0){
           return adminEntityList.get(0);
        }
        return null;
    }
}
