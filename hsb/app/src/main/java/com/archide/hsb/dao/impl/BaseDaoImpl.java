package com.archide.hsb.dao.impl;

import android.content.Context;

import com.archide.hsb.db.DatabaseHelper;

import java.sql.SQLException;

/**
 * Created by Nithish on 24-01-2016.
 */
public abstract class BaseDaoImpl {
    DatabaseHelper databaseHelper = null;


    public BaseDaoImpl(Context context)throws SQLException {
        this.databaseHelper = DatabaseHelper.getInstance(context);
        initDao();

    }

    protected abstract void initDao()throws SQLException;


}
