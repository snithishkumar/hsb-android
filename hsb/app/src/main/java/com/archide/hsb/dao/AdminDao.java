package com.archide.hsb.dao;

import com.archide.hsb.entity.ConfigurationEntity;
import com.archide.hsb.entity.UsersEntity;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Nithish on 20/11/16.
 */

public interface AdminDao {


    ConfigurationEntity getAppType()throws SQLException;

    void createAppType(ConfigurationEntity configurationEntity)throws SQLException;


    List<UsersEntity> getUsersList()throws SQLException;

    boolean isMobileNumberPresent(String mobileNumber)throws SQLException;

    boolean isOrderCloseUser()throws SQLException;

    void removeClosedUser()throws SQLException;

    void createUsers(UsersEntity usersEntity)throws SQLException;
}
