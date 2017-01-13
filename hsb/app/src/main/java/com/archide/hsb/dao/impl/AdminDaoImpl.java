package com.archide.hsb.dao.impl;

import android.content.Context;

import com.archide.hsb.dao.AdminDao;
import com.archide.hsb.entity.ConfigurationEntity;
import com.archide.hsb.entity.UsersEntity;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Nithish on 20/11/16.
 */

public class AdminDaoImpl extends BaseDaoImpl implements AdminDao {

    Dao<ConfigurationEntity,Integer> appTypeDao = null;
    Dao<UsersEntity,Integer> usersDao = null;



    public AdminDaoImpl(Context context) throws SQLException {
        super(context);
        initDao();
    }

    @Override
    protected void initDao() throws SQLException {
        appTypeDao = databaseHelper.getDao(ConfigurationEntity.class);
        usersDao = databaseHelper.getDao(UsersEntity.class);
    }

    @Override
    public void createUsers(UsersEntity usersEntity)throws SQLException{
        usersDao.create(usersEntity);
    }




    @Override
    public void createAppType(ConfigurationEntity configurationEntity)throws SQLException{
        appTypeDao.create(configurationEntity);
    }


    @Override
    public ConfigurationEntity getAppType()throws SQLException{
       return appTypeDao.queryBuilder().queryForFirst();
    }


    @Override
    public List<UsersEntity> getUsersList()throws SQLException{
       return usersDao.queryBuilder().where().eq(UsersEntity.IS_CLOSED,false).query();
    }


    @Override
    public boolean isMobileNumberPresent(String mobileNumber)throws SQLException{
        List<UsersEntity> users =  usersDao.queryBuilder().where().eq(UsersEntity.IS_CLOSED,false).and().eq(UsersEntity.USER_MOBILE_NUMBER,mobileNumber).query();
        return users.size() > 0;
    }


    @Override
    public boolean isOrderCloseUser()throws SQLException{
        List<UsersEntity> users =  usersDao.queryBuilder().where().eq(UsersEntity.IS_CLOSED,true).query();
        return users.size() > 0;
    }


    @Override
    public void removeClosedUser()throws SQLException{
      DeleteBuilder<UsersEntity,Integer> deleteBuilder = usersDao.deleteBuilder();
        deleteBuilder.where().eq(UsersEntity.IS_CLOSED,false);
        deleteBuilder.delete();
    }



}
