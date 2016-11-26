package com.archide.hsb.db;

/**
 * Created by Nithish on 22-01-2016.
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.archide.hsb.entity.AdminEntity;
import com.archide.hsb.entity.FoodCategoryEntity;
import com.archide.hsb.entity.KitchenOrderDetailsEntity;
import com.archide.hsb.entity.KitchenOrdersCategoryEntity;
import com.archide.hsb.entity.KitchenOrdersListEntity;
import com.archide.hsb.entity.MenuCourseEntity;
import com.archide.hsb.entity.MenuEntity;
import com.archide.hsb.entity.PlacedOrderItemsEntity;
import com.archide.hsb.entity.PlacedOrdersEntity;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

/**
 * Database helper class used to manage the creation and upgrading of your database. This class also usually provides
 * the DAOs used by the other classes.
 */
public class DatabaseHelper<T,T1> extends OrmLiteSqliteOpenHelper {

    // name of the database file for your application -- change to something appropriate for your app
    private static final String DATABASE_NAME = "hsbdatabase.db";
    // any time you make changes to your database objects, you may have to increase the database version
    private static final int DATABASE_VERSION = 1;


    private static DatabaseHelper databaseHelper = null;
    Context context  = null;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    /**
     * This is called when the database is first created. Usually you should call createTable statements here to create
     * the tables that will store your data.
     */
    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
        try {
            Log.i(DatabaseHelper.class.getName(), "onCreate");
            TableUtils.createTableIfNotExists(connectionSource, AdminEntity.class);
            TableUtils.createTableIfNotExists(connectionSource, MenuCourseEntity.class);
            TableUtils.createTableIfNotExists(connectionSource, FoodCategoryEntity.class);
            TableUtils.createTableIfNotExists(connectionSource, MenuEntity.class);
            TableUtils.createTableIfNotExists(connectionSource, PlacedOrdersEntity.class);
            TableUtils.createTableIfNotExists(connectionSource, PlacedOrderItemsEntity.class);

            TableUtils.createTableIfNotExists(connectionSource, KitchenOrdersListEntity.class);
            TableUtils.createTableIfNotExists(connectionSource, KitchenOrdersCategoryEntity.class);
            TableUtils.createTableIfNotExists(connectionSource, KitchenOrderDetailsEntity.class);

        } catch (Exception e) {
            Log.e(DatabaseHelper.class.getName(), "Can't create database", e);
            throw new RuntimeException(e);
        }

    }

    /**
     * This is called when your application is upgraded and it has a higher version number. This allows you to adjust
     * the various data to match the new version number.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            Log.i(DatabaseHelper.class.getName(), "onUpgrade");


        } catch (Exception e) {
            e.printStackTrace();
            Log.e(DatabaseHelper.class.getName(), "Can't Upgrade databases", e);
            throw new RuntimeException(e);
        }
    }


    /**
     * Close the database connections and clear any cached DAOs.
     */
    @Override
    public void close() {
        super.close();

    }

    public static  DatabaseHelper getInstance(Context context) {
        if(databaseHelper == null){
            databaseHelper =  OpenHelperManager.getHelper(context.getApplicationContext(), DatabaseHelper.class);
        }
        return  databaseHelper;
    }


    public static void removeInstance(){
        if(databaseHelper.isOpen()){
            databaseHelper.close();

        }
        OpenHelperManager.releaseHelper();
        databaseHelper = null;
    }
}