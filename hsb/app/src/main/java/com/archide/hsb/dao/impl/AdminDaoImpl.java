package com.archide.hsb.dao.impl;

import android.content.Context;

import com.archide.hsb.dao.AdminDao;
import com.archide.hsb.entity.AdminEntity;
import com.archide.hsb.view.activities.ActivityUtil;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;

/**
 * Created by Nithish on 20/11/16.
 */

public class AdminDaoImpl extends BaseDaoImpl implements AdminDao {

    Dao<AdminEntity,Integer> adminEntityDao = null;



    public AdminDaoImpl(Context context) throws SQLException {
        super(context);
        initDao();
    }

    @Override
    protected void initDao() throws SQLException {
        adminEntityDao = databaseHelper.getDao(AdminEntity.class);

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
    public boolean isMobilePresent(String mobileNumber) throws SQLException {
        AdminEntity adminEntity = adminEntityDao.queryBuilder().where().eq(AdminEntity.USER_MOBILE,mobileNumber).queryForFirst();
        return adminEntity != null;
    }

    @Override
    public boolean isTableConfigured() throws SQLException {
        AdminEntity adminEntity = adminEntityDao.queryBuilder().queryForFirst();
        ActivityUtil.TABLE_NUMBER = adminEntity.getTableNumber();
        return adminEntity != null;
    }
}
