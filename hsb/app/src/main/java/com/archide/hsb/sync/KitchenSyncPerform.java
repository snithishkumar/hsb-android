package com.archide.hsb.sync;

import android.content.Context;
import android.util.Log;

import com.archide.hsb.dao.KitchenDao;
import com.archide.hsb.dao.MenuItemsDao;
import com.archide.hsb.dao.impl.KitchenDaoImpl;
import com.archide.hsb.dao.impl.MenuItemsDaoImpl;
import com.archide.hsb.dao.impl.OrdersDaoImpl;
import com.archide.hsb.entity.FoodCategoryEntity;
import com.archide.hsb.entity.KitchenCookingCmntsEntity;
import com.archide.hsb.entity.KitchenMenuItemsEntity;
import com.archide.hsb.entity.KitchenOrderDetailsEntity;
import com.archide.hsb.entity.KitchenOrdersCategoryEntity;
import com.archide.hsb.entity.KitchenOrdersListEntity;
import com.archide.hsb.entity.MenuCourseEntity;
import com.archide.hsb.entity.MenuEntity;
import com.archide.hsb.enumeration.GsonAPI;
import com.archide.hsb.enumeration.Status;
import com.archide.hsb.enumeration.ViewStatus;
import com.archide.hsb.sync.json.FoodCategoryJson;
import com.archide.hsb.sync.json.GetKitchenOrders;
import com.archide.hsb.sync.json.GetMenuDetails;
import com.archide.hsb.sync.json.KitchenCookingComments;
import com.archide.hsb.sync.json.KitchenOrderListResponse;
import com.archide.hsb.sync.json.KitchenOrderStatusSyncResponse;
import com.archide.hsb.sync.json.MenuItemJson;
import com.archide.hsb.sync.json.MenuListJson;
import com.archide.hsb.sync.json.OrderedMenuItems;
import com.archide.hsb.sync.json.PlaceOrdersJson;
import com.archide.hsb.sync.json.ResponseData;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Nithish on 17/12/16.
 */

public class KitchenSyncPerform {

    private final String LOG_TAG = KitchenSyncPerform.class.getSimpleName();

    private KitchenDao kitchenDao;
    private HsbAPI hsbAPI;
    private Gson gson;
    private Context context;

    public KitchenSyncPerform(Context context){
        this.context = context;
        init();
    }

    private void init(){
        try{
            kitchenDao = new KitchenDaoImpl(context);
            gson = GsonAPI.INSTANCE.getGson();
        }catch (Exception e){
            Log.e(LOG_TAG,"Error in init",e);
        }
        hsbAPI = ServiceAPI.INSTANCE.getHsbAPI();
    }

    public ResponseData getKitchenOrders(){
        try{
            List<KitchenOrdersListEntity> kitchenOrdersListEntities =   kitchenDao.getKitchenOrdersList();
            List<GetKitchenOrders> getKitchenOrdersList = new ArrayList<>();
            for(KitchenOrdersListEntity kitchenOrdersListEntity : kitchenOrdersListEntities){
               long lastKitchenOrderDetails = kitchenDao.getLastKitchenOrderDetails(kitchenOrdersListEntity);
                GetKitchenOrders getKitchenOrders = new GetKitchenOrders(kitchenOrdersListEntity);
                getKitchenOrders.setServerDateTime(lastKitchenOrderDetails);
                getKitchenOrdersList.add(getKitchenOrders);
            }
            Call<ResponseData> kitchenResponse =   hsbAPI.getKitchenOrders(getKitchenOrdersList);

            Response<ResponseData> response = kitchenResponse.execute();
            if (response != null && response.isSuccessful()) {
                ResponseData responseData = response.body();

                String menuItemsJsonString = responseData.getData();

                KitchenOrderListResponse kitchenOrderListResponse =  gson.fromJson(menuItemsJsonString, KitchenOrderListResponse.class);
                processKitchenOrders(kitchenOrderListResponse.getPlaceOrdersJsonList());

                processCloseData(kitchenOrderListResponse.getClosedOrders());
            }
            ResponseData result = new ResponseData(200,null);
            return result;
        }catch (Exception e){
            e.printStackTrace();
        }
        return getErrorResponse();
    }


    public void processKitchenOrders(List<PlaceOrdersJson> placeOrdersJsonList){
        try{
            for(PlaceOrdersJson placeOrdersJson : placeOrdersJsonList){
                List<OrderedMenuItems>  orderedMenuItemsList = placeOrdersJson.getMenuItems();
               if(orderedMenuItemsList.size() > 0){
                   boolean isCreate = false;
                   KitchenOrdersListEntity kitchenOrdersListEntity =  kitchenDao.getKitchenOrdersListEntity(placeOrdersJson.getOrderId());
                   if(kitchenOrdersListEntity == null){
                       kitchenOrdersListEntity = new KitchenOrdersListEntity(placeOrdersJson);
                       kitchenDao.createKitchenOrder(kitchenOrdersListEntity);
                       isCreate = true;
                   }

                   boolean flag = false;
                   for(OrderedMenuItems orderedMenuItems : orderedMenuItemsList){
                       KitchenOrdersCategoryEntity kitchenOrdersCategory = kitchenDao.getKitchenOrdersCategoryEntity(kitchenOrdersListEntity,orderedMenuItems.getCategoryUuid());
                       if(kitchenOrdersCategory == null){
                           kitchenOrdersCategory = new KitchenOrdersCategoryEntity();
                           kitchenOrdersCategory.setCategoryName(orderedMenuItems.getCategoryName());
                           kitchenOrdersCategory.setFoodCategoryUUID(orderedMenuItems.getCategoryUuid());
                           kitchenOrdersCategory.setKitchenOrdersList(kitchenOrdersListEntity);
                           kitchenOrdersCategory.setDateTime(System.currentTimeMillis());
                           kitchenDao.createKitchenOrderCategory(kitchenOrdersCategory);
                       }
                       KitchenOrderDetailsEntity kitchenOrderDetailsEntity =   kitchenDao.getKitchenOrderDetailsEntity(orderedMenuItems.getPlacedOrderItemsUUID());
                       if(kitchenOrderDetailsEntity == null){
                           kitchenOrderDetailsEntity = new KitchenOrderDetailsEntity(orderedMenuItems);
                           kitchenOrderDetailsEntity.setKitchenOrdersCategory(kitchenOrdersCategory);
                           kitchenOrderDetailsEntity.setKitchenOrdersList(kitchenOrdersListEntity);
                           kitchenDao.createKitchenOrderItems(kitchenOrderDetailsEntity);
                           flag = true;
                       }

                   }

                   if(flag && !isCreate){
                       kitchenOrdersListEntity.setLastUpdateTime(placeOrdersJson.getLastUpdatedDateTime());
                       kitchenOrdersListEntity.setServerDateTime(placeOrdersJson.getServerDateTime());
                       kitchenOrdersListEntity.setStatus(Status.OPEN);
                       kitchenOrdersListEntity.setViewStatus(ViewStatus.UPDATES);
                       kitchenDao.updateKitchenOrder(kitchenOrdersListEntity);
                   }


                   processCookingComments(placeOrdersJson,kitchenOrdersListEntity);
               }


            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    private void processCookingComments(PlaceOrdersJson placeOrdersJson,KitchenOrdersListEntity kitchenOrdersListEntity ){
        List<KitchenCookingComments> cookingCommentsList = placeOrdersJson.getCookingCommentsList();
        for(KitchenCookingComments kitchenCookingComments : cookingCommentsList){
            try {
                KitchenCookingCmntsEntity cookingCmntsEntity = kitchenDao.getKitchenCookingCmntsEntity(kitchenCookingComments.getKitchenCookingCommentsUUID());
                if(cookingCmntsEntity == null){
                    cookingCmntsEntity = new KitchenCookingCmntsEntity(kitchenCookingComments);
                    cookingCmntsEntity.setKitchenOrdersList(kitchenOrdersListEntity);
                    kitchenDao.saveKitchenCookingCmntsEntity(cookingCmntsEntity);
                }
            }catch (Exception e){
                e.printStackTrace();
                // test
            }

        }
    }

    private void processCloseData(List<String> closedOrders){
        for(String orderGuids : closedOrders){
            try{
                kitchenDao.closeOrders(orderGuids);
            }catch (Exception e){
                e.printStackTrace();
            }

        }

    }

    private ResponseData getErrorResponse(){
        ResponseData responseData =new ResponseData(500,null);
        responseData.setStatusCode(500);
        return responseData;
    }

    public void sendUnSyncedOrderStatus(){
        try {
            List<KitchenOrdersListEntity> kitchenOrdersList =  kitchenDao.getUnSyncedOrderList();
            List<PlaceOrdersJson> placeOrderList = new ArrayList<>();
            boolean dataFlag = false;
            for(KitchenOrdersListEntity kitchenOrdersListEntity : kitchenOrdersList){
                dataFlag = true;
                PlaceOrdersJson placeOrdersJson = new PlaceOrdersJson(kitchenOrdersListEntity);
                placeOrderList.add(placeOrdersJson);
                List<KitchenOrderDetailsEntity> kitchenOrderDetailsList = kitchenDao.getUnSyncedOrderDetails(kitchenOrdersListEntity);
                for(KitchenOrderDetailsEntity kitchenOrderDetailsEntity : kitchenOrderDetailsList){
                    OrderedMenuItems orderedMenuItems = new OrderedMenuItems(kitchenOrderDetailsEntity);
                    placeOrdersJson.getMenuItems().add(orderedMenuItems);
                }
            }
            if(dataFlag){
                Call<ResponseData> kitchenOrderSyncResponse =   hsbAPI.sendUnSyncedKitchenOrders(placeOrderList);
                Response<ResponseData> response =   kitchenOrderSyncResponse.execute();
                if (response != null && response.isSuccessful()) {
                    ResponseData responseData =  response.body();
                    if(responseData.getSuccess() && responseData.getStatusCode() == 200){
                        String orderedItemsUuids =  responseData.getData();
                        List<KitchenOrderStatusSyncResponse> placeOrdersList = gson.fromJson(orderedItemsUuids,
                                new TypeToken<List<KitchenOrderStatusSyncResponse>>() {}.getType());
                        for(KitchenOrderStatusSyncResponse kitchenOrderStatusSyncResponse : placeOrdersList){
                            List<String> placedOrderItemsUuids =  kitchenOrderStatusSyncResponse.getPlacedOrderItemsUuid();
                            kitchenDao.updateKitchenOrderListSyncStatus(kitchenOrderStatusSyncResponse.getPlacedOrderUuid());
                            for(String placedOrderItemsUuid : placedOrderItemsUuids){
                                kitchenDao.updateKitchenOrderDetailsSyncStatus(placedOrderItemsUuid);
                            }
                        }
                    }
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }


    /**
     * Get Menu Items
     * @return
     */
    public ResponseData getMenuItems(){
        try{
            Call<ResponseData> menuItemsResponse =  hsbAPI.getMenuItems();
            Response<ResponseData> response =  menuItemsResponse.execute();
            if (response != null && response.isSuccessful()) {

                ResponseData responseData = response.body();

                String kitchenMenuItems =  responseData.getData();

                List<KitchenMenuItemsEntity> menuItemList = gson.fromJson(kitchenMenuItems,
                        new TypeToken<List<KitchenMenuItemsEntity>>() {}.getType());

                if(menuItemList.size() > 0){
                    kitchenDao.clearKitchenMenuItems();
                    kitchenDao.saveKitchenMenuItems(menuItemList);

                }
                ResponseData result = new ResponseData(2000,null);
                return result;

            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return getErrorResponse();
    }


    public ResponseData sendKitchenMenuUpdates(){
        try{
            List<KitchenMenuItemsEntity> kitchenMenuItems =  kitchenDao.getEditedMenuItems();
            if(kitchenMenuItems.size() > 0){
                Call<ResponseData> sendKitchenMenuUpdates = hsbAPI.sendKitchenMenuUpdates(kitchenMenuItems);
                Response<ResponseData> response =  sendKitchenMenuUpdates.execute();
                if (response != null && response.isSuccessful()) {
                    ResponseData result = new ResponseData(200,null);
                    return result;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return getErrorResponse();
    }



}
