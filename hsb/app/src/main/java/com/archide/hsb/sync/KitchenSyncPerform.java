package com.archide.hsb.sync;

import android.content.Context;
import android.util.Log;

import com.archide.hsb.dao.KitchenDao;
import com.archide.hsb.dao.impl.KitchenDaoImpl;
import com.archide.hsb.dao.impl.MenuItemsDaoImpl;
import com.archide.hsb.dao.impl.OrdersDaoImpl;
import com.archide.hsb.entity.KitchenOrderDetailsEntity;
import com.archide.hsb.entity.KitchenOrdersCategoryEntity;
import com.archide.hsb.entity.KitchenOrdersListEntity;
import com.archide.hsb.enumeration.GsonAPI;
import com.archide.hsb.enumeration.Status;
import com.archide.hsb.enumeration.ViewStatus;
import com.archide.hsb.sync.json.GetKitchenOrders;
import com.archide.hsb.sync.json.KitchenOrderListResponse;
import com.archide.hsb.sync.json.KitchenOrderStatusSyncResponse;
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
                GetKitchenOrders getKitchenOrders = new GetKitchenOrders(kitchenOrdersListEntity);
                getKitchenOrdersList.add(getKitchenOrders);
            }
            Call<ResponseData> kitchenResponse =   hsbAPI.getKitchenOrders(getKitchenOrdersList);

            Response<ResponseData> response = kitchenResponse.execute();
            if (response != null && response.isSuccessful()) {
                ResponseData responseData = response.body();

                String menuItemsJsonString =  (String)responseData.getData();

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
                KitchenOrdersListEntity kitchenOrdersListEntity =  kitchenDao.getKitchenOrdersListEntity(placeOrdersJson.getOrderId());
                if(kitchenOrdersListEntity == null){
                    kitchenOrdersListEntity = new KitchenOrdersListEntity(placeOrdersJson);
                    kitchenDao.createKitchenOrder(kitchenOrdersListEntity);
                }else{
                    kitchenOrdersListEntity.setLastUpdateTime(placeOrdersJson.getLastUpdatedDateTime());
                    kitchenOrdersListEntity.setServerDateTime(placeOrdersJson.getServerDateTime());
                    kitchenOrdersListEntity.setStatus(Status.OPEN);
                    kitchenOrdersListEntity.setViewStatus(ViewStatus.UN_VIEWED);
                    kitchenDao.updateKitchenOrder(kitchenOrdersListEntity);
                }

                List<OrderedMenuItems>  orderedMenuItemsList = placeOrdersJson.getMenuItems();
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
                    KitchenOrderDetailsEntity kitchenOrderDetailsEntity = new KitchenOrderDetailsEntity(orderedMenuItems);
                    kitchenOrderDetailsEntity.setKitchenOrdersCategory(kitchenOrdersCategory);
                    kitchenOrderDetailsEntity.setKitchenOrdersList(kitchenOrdersListEntity);
                    kitchenDao.createKitchenOrderItems(kitchenOrderDetailsEntity);
                }

            }
        }catch (Exception e){
            e.printStackTrace();
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
            for(KitchenOrdersListEntity kitchenOrdersListEntity : kitchenOrdersList){
                PlaceOrdersJson placeOrdersJson = new PlaceOrdersJson(kitchenOrdersListEntity);
                placeOrderList.add(placeOrdersJson);
                List<KitchenOrderDetailsEntity> kitchenOrderDetailsList = kitchenDao.getUnSyncedOrderDetails(kitchenOrdersListEntity);
                for(KitchenOrderDetailsEntity kitchenOrderDetailsEntity : kitchenOrderDetailsList){
                    OrderedMenuItems orderedMenuItems = new OrderedMenuItems(kitchenOrderDetailsEntity);
                    placeOrdersJson.getMenuItems().add(orderedMenuItems);
                }
            }
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
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
