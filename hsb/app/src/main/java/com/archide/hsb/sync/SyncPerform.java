package com.archide.hsb.sync;

import android.content.Context;
import android.util.Log;

import com.archide.hsb.dao.MenuItemsDao;
import com.archide.hsb.entity.FoodCategoryEntity;
import com.archide.hsb.entity.MenuCourseEntity;
import com.archide.hsb.entity.MenuEntity;
import com.archide.hsb.enumeration.GsonAPI;
import com.archide.hsb.sync.json.FoodCategoryJson;
import com.archide.hsb.sync.json.MenuItemJson;
import com.archide.hsb.sync.json.MenuListJson;
import com.archide.hsb.sync.json.ResponseData;
import com.archide.hsb.sync.json.TableListJson;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Nithish on 12/11/16.
 */

public class SyncPerform {

    private final String LOG_TAG = SyncPerform.class.getSimpleName();

    private Context context;
    private Gson gson;
    private HsbAPI hsbAPI;

    private MenuItemsDao menuItemsDao;

    public SyncPerform(){

    }

    public SyncPerform(Context context){
        this.context = context;
        init();
    }

    private void init(){
        try{

            gson = GsonAPI.INSTANCE.getGson();
        }catch (Exception e){
            Log.e(LOG_TAG,"Error in init",e);
        }
        hsbAPI = ServiceAPI.INSTANCE.getHsbAPI();
    }

    /**
     * Get Table list from server and send back to UI
     * @return
     */
    public ResponseData getTableDetails() {
        try {
            Call<ResponseData> tableListResponse = hsbAPI.getTableList();
            Response<ResponseData> response = tableListResponse.execute();
            if (response != null && response.isSuccessful()) {
                ResponseData responseData = response.body();
               // ResponseData responseData =  gson.fromJson(stringResponse,ResponseData.class);
                String tableListJsonString =  (String)responseData.getData();

                List<TableListJson>  tableListJson =  gson.fromJson(tableListJsonString, new TypeToken<List<TableListJson>>() {
                }.getType());

                List<String> results = new ArrayList<>();
                for(TableListJson tempTableList : tableListJson){
                    results.add(tempTableList.getTableNumber());
                }
                responseData.setData(null);
                responseData.setMessage(results);
                return responseData;
            }else{
                return getErrorResponse();
            }
        } catch (Exception e) {
            Log.e("Error","Error in  getTableDetails",e);
        }
        return getErrorResponse();
    }

    public ResponseData getMenuItems(){
        try{
          long lastServerSyncTime =  menuItemsDao.getLastSyncTime();
            Call<ResponseData> menuItemsResponse =  hsbAPI.getMenuItems(lastServerSyncTime);
            Response<ResponseData> response =  menuItemsResponse.execute();
            if (response != null && response.isSuccessful()) {
                ResponseData responseData = response.body();
                String menuItemsJsonString =  (String)responseData.getData();

                List<MenuListJson>  menuListJsonList =  gson.fromJson(menuItemsJsonString, new TypeToken<List<MenuListJson>>() {
                }.getType());

                for(MenuListJson menuListJson : menuListJsonList){
                    processMenuDetails(menuListJson);
                }

            }
        }catch (Exception e){

        }

        return getErrorResponse();
    }

    /**
     * Process Menu Details
     * @param menuListJson
     */
    private void processMenuDetails(MenuListJson menuListJson){
        try{
            MenuCourseEntity menuCourseEntity =  menuItemsDao.getMenuCourseEntity(menuListJson.getMenuCourseUuid());
            if(menuCourseEntity == null){
                menuCourseEntity = new MenuCourseEntity(menuListJson);
                menuItemsDao.createMenuCourseEntity(menuCourseEntity);
            }
            List<FoodCategoryJson> foodCategoryJsons = menuListJson.getCategoryJsons();
            for(FoodCategoryJson foodCategoryJson : foodCategoryJsons){
                FoodCategoryEntity foodCategoryEntity =  menuItemsDao.getFoodCategoryEntity(foodCategoryJson.getFoodCategoryUuid());
                if(foodCategoryEntity == null){

                    foodCategoryEntity = new FoodCategoryEntity(foodCategoryJson);
                    foodCategoryEntity.setMenuCourseEntity(menuCourseEntity);
                    menuItemsDao.createFoodCategoryEntity(foodCategoryEntity);
                }
                List<MenuItemJson> menuItemJsonList = foodCategoryJson.getMenuItems();
                for(MenuItemJson menuItemJson : menuItemJsonList){
                    MenuEntity menuEntity =  menuItemsDao.getMenuItemEntity(menuItemJson.getMenuUuid());
                    if(menuEntity == null){
                        menuEntity = new MenuEntity(menuItemJson);
                        menuEntity.setMenuCourseEntity(menuCourseEntity);
                        menuEntity.setFoodCategoryEntity(foodCategoryEntity);
                        menuItemsDao.createMenuEntity(menuEntity);
                    }else if(menuEntity.getServerDateTime() < menuItemJson.getServerDateTime()){
                        menuEntity.clone(menuItemJson);
                        menuItemsDao.updateMenuEntity(menuEntity);
                    }
                }

            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private ResponseData getErrorResponse(){
        ResponseData responseData =new ResponseData(500,null);
        responseData.setStatusCode(500);
        return responseData;
    }

}
