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


    UsersEntity getUsers()throws SQLException;


    void createUsers(UsersEntity usersEntity)throws SQLException;

    UsersEntity getUsersEntity(String mobileNumber)throws SQLException;


    void changeTableNumber(String tableNumber)throws SQLException;



    void removeAllUsers()throws SQLException;

    void removeUser(String mobileNumber)throws SQLException;

    void updateUsers(UsersEntity usersEntity)throws SQLException;

    void changeUserMobileNumber(String userMobileNumber)throws SQLException;
}
