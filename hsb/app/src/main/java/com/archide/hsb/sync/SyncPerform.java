package com.archide.hsb.sync;

import android.content.Context;
import android.util.Log;

import com.archide.hsb.dao.MenuItemsDao;
import com.archide.hsb.dao.OrdersDao;
import com.archide.hsb.dao.impl.MenuItemsDaoImpl;
import com.archide.hsb.dao.impl.OrdersDaoImpl;
import com.archide.hsb.entity.FoodCategoryEntity;
import com.archide.hsb.entity.MenuCourseEntity;
import com.archide.hsb.entity.MenuEntity;
import com.archide.hsb.entity.PlacedOrderItemsEntity;
import com.archide.hsb.entity.PlacedOrdersEntity;
import com.archide.hsb.enumeration.GsonAPI;
import com.archide.hsb.sync.json.FoodCategoryJson;
import com.archide.hsb.sync.json.GetMenuDetails;
import com.archide.hsb.sync.json.MenuItemJson;
import com.archide.hsb.sync.json.MenuListJson;
import com.archide.hsb.sync.json.OrderedMenuItems;
import com.archide.hsb.sync.json.PlaceOrdersJson;
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
    private OrdersDao ordersDao;

    public SyncPerform(){

    }

    public SyncPerform(Context context){
        this.context = context;
        init();
    }

    private void init(){
        try{
            menuItemsDao = new MenuItemsDaoImpl(context);
            ordersDao = new OrdersDaoImpl(context);
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
            Call<ResponseData> menuItemsResponse =  hsbAPI.getMenuItems(lastServerSyncTime,"1");
            Response<ResponseData> response =  menuItemsResponse.execute();
            if (response != null && response.isSuccessful()) {
                ResponseData responseData = response.body();
                String menuItemsJsonString =  (String)responseData.getData();

                GetMenuDetails  menuListJsonList =  gson.fromJson(menuItemsJsonString, GetMenuDetails.class);

                List<MenuListJson> menuListJsonsList =  menuListJsonList.getMenuListJsonList();

                for(MenuListJson menuListJson : menuListJsonsList){
                    processMenuDetails(menuListJson);
                }

                PlaceOrdersJson placeOrdersJson =    menuListJsonList.getPreviousOrder();
                if(placeOrdersJson != null){
                    processPreviousOrder(placeOrdersJson);
                }
                ResponseData result = new ResponseData(200,null);
                return result;

            }
        }catch (Exception e){
            e.printStackTrace();
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

    /**
     * Process Previous Order Details
     * @param placeOrdersJson
     */
    private void processPreviousOrder(PlaceOrdersJson placeOrdersJson){
        try{
            PlacedOrdersEntity placedOrdersEntity =   ordersDao.getPlacedOrdersEntity(placeOrdersJson.getPlaceOrderUuid());
            if(placedOrdersEntity == null){
                placedOrdersEntity = new PlacedOrdersEntity(placeOrdersJson);
                ordersDao.createPlacedOrdersEntity(placedOrdersEntity);
            }
            List<OrderedMenuItems>  menuItemsList =  placeOrdersJson.getMenuItems();
            for(OrderedMenuItems orderedMenuItems : menuItemsList){
                PlacedOrderItemsEntity placedOrderItemsEntity = ordersDao.getPlacedOrdersItemsEntity(orderedMenuItems.getPlacedOrderItemsUUID());
               if(placedOrderItemsEntity == null){
                   placedOrderItemsEntity = new PlacedOrderItemsEntity(orderedMenuItems);
                   ordersDao.createPlacedOrdersItemsEntity(placedOrderItemsEntity);
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


    public ResponseData sendOrderData(){
        try{
            PlacedOrdersEntity placedOrdersEntity =  ordersDao.getPlacedOrdersEntity();
            if(placedOrdersEntity != null){
                PlaceOrdersJson placeOrdersJson = new PlaceOrdersJson(placedOrdersEntity);
                List<PlacedOrderItemsEntity> itemsEntityList =  ordersDao.getPlacedOrderItemsEntity();
                for(PlacedOrderItemsEntity placedOrderItemsEntity : itemsEntityList){
                    OrderedMenuItems orderedMenuItems = new OrderedMenuItems(placedOrderItemsEntity);
                    placeOrdersJson.getMenuItems().add(orderedMenuItems);
                }
                Call<ResponseData> placeOrderResponse =  hsbAPI.placeAnOrder(placeOrdersJson);
                Response<ResponseData> response =   placeOrderResponse.execute();
                if (response != null && response.isSuccessful()) {
                    ResponseData responseData =  response.body();
                    if(responseData.getSuccess() && responseData.getStatusCode() == 200){
                        ordersDao.updateServerSyncTime(responseData.getData());
                        ordersDao.updatePlacedOrderItems();
                        ResponseData result = new ResponseData(200,null);
                        return result;
                    }else {
                        ResponseData result = new ResponseData(responseData.getStatusCode(),null);
                        return result;
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return getErrorResponse();
    }

}
