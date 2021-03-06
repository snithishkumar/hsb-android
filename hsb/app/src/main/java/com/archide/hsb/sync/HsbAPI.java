package com.archide.hsb.sync;

import com.archide.hsb.entity.KitchenMenuItemsEntity;
import com.archide.hsb.sync.json.GetKitchenOrders;
import com.archide.hsb.sync.json.PlaceOrdersJson;
import com.archide.hsb.sync.json.ResponseData;
import com.google.gson.JsonObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by Nithish on 12/11/16.
 */

public interface HsbAPI {

    /** Create User **/
    @GET("/hsb/mobile/getTableList.html")
    Call<ResponseData> getTableList();


    /** Create User **/
    @FormUrlEncoded
    @POST("/hsb/mobile/getMenuItems.html")
    Call<ResponseData> getMenuItems(@Field("lastServerSyncTime") long lastServerSyncTime,
                                    @Field("tableNumber") String tableNumber,
                                    @Field("userType") String userType,
                                    @Field("mobileNumber") String mobileNumber);

    @POST("/hsb/mobile/placeAnOrder.html")
    Call<ResponseData> placeAnOrder(@Body PlaceOrdersJson placeOrdersJson);


    /** Create User **/
    @POST ("/hsb/mobile/kitchen/getKitchenOrders.html")
    Call<ResponseData> getKitchenOrders(@Body List<GetKitchenOrders> getKitchenOrders);


    @POST ("/hsb/mobile/kitchen/sendUnSyncedKitchenOrders.html")
    Call<ResponseData> sendUnSyncedKitchenOrders(@Body List<PlaceOrdersJson> placeOrderList);

    @POST ("/hsb/mobile/kitchen/sendKitchenMenuUpdates.html")
    Call<ResponseData> sendKitchenMenuUpdates(@Body List<KitchenMenuItemsEntity> kitchenMenuItems);

    @POST("/hsb/mobile/kitchen/getMenuItems.html")
    Call<ResponseData> getMenuItems();

    @POST ("/hsb//mobile/getPreviousOrder.html")
    Call<ResponseData> getPreviousOrder(@Body JsonObject requestData);

    @FormUrlEncoded
    @POST ("/hsb//mobile/getUnAvailableMenuItems.html")
    Call<ResponseData> getUnAvailableMenuItems(@Field("lastServerSyncTime") long lastServerSyncTime);


    @FormUrlEncoded
    @POST ("/hsb//mobile/closeAnOrder.html")
    Call<ResponseData> closeAnOrder(@Field("tableNumber") String tableNumber,@Field("mobileNumber") String mobileNumber,@Field("placedOrderUUid") String placedOrderUUid);


    @FormUrlEncoded
    @POST ("/hsb//mobile/reSentBillDetails.html")
    Call<ResponseData> reSentBillDetails(@Field("placedOrderUUid") String placedOrderUUid);


    @FormUrlEncoded
    @POST ("/hsb//mobile/logout.html")
    Call<ResponseData> logout(@Field("tableNumber") String tableNumber,@Field("mobileNumber") String mobileNumber);

}
