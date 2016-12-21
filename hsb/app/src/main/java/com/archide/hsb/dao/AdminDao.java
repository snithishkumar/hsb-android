package com.archide.hsb.dao;

import com.archide.hsb.entity.AdminEntity;
import com.archide.hsb.entity.ConfigurationEntity;

import java.sql.SQLException;

/**
 * Created by Nithish on 20/11/16.
 */

public interface AdminDao {

    void createAdminEntity(AdminEntity adminEntity)throws SQLException;

    void updateUserMobile(String mobileNumber) throws SQLException;

    boolean isMobilePresent(String mobileNumber)throws SQLException;

    //boolean isTableConfigured()throws SQLException;

    ConfigurationEntity getAppType()throws SQLException;

    void createAppType(ConfigurationEntity configurationEntity)throws SQLException;
}
