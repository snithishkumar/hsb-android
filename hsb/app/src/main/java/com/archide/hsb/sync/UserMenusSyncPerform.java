package com.archide.hsb.sync;

import android.content.Context;
import android.util.Log;

import com.archide.hsb.dao.KitchenDao;
import com.archide.hsb.dao.MenuItemsDao;
import com.archide.hsb.dao.OrdersDao;
import com.archide.hsb.dao.impl.KitchenDaoImpl;
import com.archide.hsb.dao.impl.MenuItemsDaoImpl;
import com.archide.hsb.dao.impl.OrdersDaoImpl;
import com.archide.hsb.entity.FoodCategoryEntity;
import com.archide.hsb.entity.KitchenOrderDetailsEntity;
import com.archide.hsb.entity.KitchenOrdersCategoryEntity;
import com.archide.hsb.entity.KitchenOrdersListEntity;
import com.archide.hsb.entity.MenuCourseEntity;
import com.archide.hsb.entity.MenuEntity;
import com.archide.hsb.entity.PlacedOrderItemsEntity;
import com.archide.hsb.entity.PlacedOrdersEntity;
import com.archide.hsb.enumeration.GsonAPI;
import com.archide.hsb.enumeration.Status;
import com.archide.hsb.enumeration.ViewStatus;
import com.archide.hsb.sync.json.FoodCategoryJson;
import com.archide.hsb.sync.json.GetKitchenOrders;
import com.archide.hsb.sync.json.GetMenuDetails;
import com.archide.hsb.sync.json.KitchenOrderListResponse;
import com.archide.hsb.sync.json.KitchenOrderStatusSyncResponse;
import com.archide.hsb.sync.json.MenuItemJson;
import com.archide.hsb.sync.json.MenuListJson;
import com.archide.hsb.sync.json.OrderedMenuItems;
import com.archide.hsb.sync.json.PlaceOrdersJson;
import com.archide.hsb.sync.json.ResponseData;
import com.archide.hsb.sync.json.TableListJson;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Nithish on 12/11/16.
 */

public class UserMenusSyncPerform {

    private final String LOG_TAG = UserMenusSyncPerform.class.getSimpleName();

    private Context context;
    private Gson gson;
    private HsbAPI hsbAPI;

    private MenuItemsDao menuItemsDao;
    private OrdersDao ordersDao;


    public UserMenusSyncPerform(){

    }

    public UserMenusSyncPerform(Context context){
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



    public ResponseData getMenuItems(String tableNumber,String mobileNumber){
        try{
          long lastServerSyncTime =  menuItemsDao.getLastSyncTime();
            Call<ResponseData> menuItemsResponse =  hsbAPI.getMenuItems(lastServerSyncTime,tableNumber,mobileNumber);
            Response<ResponseData> response =  menuItemsResponse.execute();
            if (response != null && response.isSuccessful()) {
                ResponseData responseData = response.body();
                String menuItemsJsonString =  responseData.getData();

                GetMenuDetails  menuListJsonList =  gson.fromJson(menuItemsJsonString, GetMenuDetails.class);

                List<MenuListJson> menuListJsonsList =  menuListJsonList.getMenuListJsonList();

                for(MenuListJson menuListJson : menuListJsonsList){
                    processMenuDetails(menuListJson);
                }

                PlaceOrdersJson placeOrdersJson =    menuListJsonList.getPreviousOrder();
                if(placeOrdersJson != null){
                    processPreviousOrder(placeOrdersJson,false);
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
    private void processPreviousOrder(PlaceOrdersJson placeOrdersJson,boolean isBilling){
        try{
            PlacedOrdersEntity placedOrdersEntity =   ordersDao.getPlacedOrdersEntity(placeOrdersJson.getPlaceOrderUuid());
            if(placedOrdersEntity == null){
                placedOrdersEntity = new PlacedOrdersEntity(placeOrdersJson);
                ordersDao.createPlacedOrdersEntity(placedOrdersEntity);
            }else if(isBilling){
                placedOrdersEntity.populateBilling(placeOrdersJson);
                ordersDao.updatePlacedOrdersEntity(placedOrdersEntity);
            }
            List<OrderedMenuItems>  menuItemsList =  placeOrdersJson.getMenuItems();
            for(OrderedMenuItems orderedMenuItems : menuItemsList){
                PlacedOrderItemsEntity placedOrderItemsEntity = ordersDao.getPlacedOrdersItemsEntity(orderedMenuItems.getPlacedOrderItemsUUID());
               if(placedOrderItemsEntity == null){
                   placedOrderItemsEntity = new PlacedOrderItemsEntity(orderedMenuItems);
                   MenuEntity menuEntity = menuItemsDao.getMenuItemEntity(orderedMenuItems.getMenuUuid());
                   placedOrderItemsEntity.setMenuItem(menuEntity);
                   placedOrderItemsEntity.setCost(menuEntity.getPrice());
                   placedOrderItemsEntity.setMenuCourseEntity(menuEntity.getMenuCourseEntity());
                   ordersDao.createPlacedOrdersItemsEntity(placedOrderItemsEntity);
               }else{
                   placedOrderItemsEntity.setOrderStatus(orderedMenuItems.getOrderStatus());
                   ordersDao.updatePlacedOrdersItemsEntity(placedOrderItemsEntity);
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
                        ordersDao.updatePlacedOrderItems(Long.valueOf(responseData.getData()));
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






    public ResponseData getPreviousOrderDetails(String tableNumber,String mobileNumber){
        try{
           long serverSyncTime =  ordersDao.getPreviousSyncHistoryData();
            JsonObject request = new JsonObject();
            request.addProperty("tableNumber", tableNumber);
            request.addProperty("mobileNumber", mobileNumber);
            request.addProperty("serverLastUdpateTime", serverSyncTime);
            Call<ResponseData> serverResponse =   hsbAPI.getPreviousOrder(request);
            Response<ResponseData> response = serverResponse.execute();
            if (response != null && response.isSuccessful()) {
              ResponseData responseData = response.body();
                if(responseData.getStatusCode() == 200){
                    String previousOrder = responseData.getData();
                    PlaceOrdersJson placeOrdersJson =  gson.fromJson(previousOrder,PlaceOrdersJson.class);
                    processPreviousOrder(placeOrdersJson,false);
                }
                ResponseData result = new ResponseData(3000,null);
                return result;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
       return getErrorResponse();
    }




    public ResponseData getUnAvailableOrders() {
        try{
            long lastServerSyncTime =  menuItemsDao.getLastSyncTime();
            Call<ResponseData> serverResponse =  hsbAPI.getUnAvailableMenuItems(lastServerSyncTime);
            Response<ResponseData> response = serverResponse.execute();
            if (response != null && response.isSuccessful()) {
                ResponseData responseData = response.body();
                if(responseData.getStatusCode() == 200){
                    String menuItems =  responseData.getData();
                    List<MenuItemJson> menuItemList = gson.fromJson(menuItems,
                            new TypeToken<List<MenuItemJson>>() {}.getType());
                    for(MenuItemJson menuItemJson : menuItemList){
                        menuItemsDao.updateMenuItemStatus(menuItemJson.getMenuUuid(),menuItemJson.getServerDateTime());
                    }
                }
            }
            ResponseData result = new ResponseData(2000,null);
            return result;

        }catch (Exception e){
            e.printStackTrace();
        }
        return getErrorResponse();
    }


    public ResponseData closeAnOrder(String tableNumber,String mobileNumber){
        try{
            String orderId = "";
            PlacedOrdersEntity placedOrdersEntity = ordersDao.getPlacedOrderHistoryByMobile(mobileNumber,tableNumber);
            if(placedOrdersEntity != null){
                orderId = placedOrdersEntity.getOrderId();
            }
            Call<ResponseData> serverResponse = hsbAPI.closeAnOrder(tableNumber,mobileNumber,orderId);
            Response<ResponseData> response = serverResponse.execute();
            if (response != null && response.isSuccessful()) {
                ResponseData responseData = response.body();
                if(responseData.getStatusCode() == 200){
                    String previousOrder = responseData.getData();
                    PlaceOrdersJson placeOrdersJson =  gson.fromJson(previousOrder,PlaceOrdersJson.class);
                    processPreviousOrder(placeOrdersJson,true);
                }
                ResponseData result = new ResponseData(2000,null);
                return result;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return getErrorResponse();
    }

}