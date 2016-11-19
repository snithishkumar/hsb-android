package com.archide.hsb.sync;

import com.archide.hsb.sync.json.PlaceOrdersJson;
import com.archide.hsb.sync.json.ResponseData;

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
    Call<ResponseData> getMenuItems(@Field("lastServerSyncTime") long lastServerSyncTime,@Field("tableNumber") String tableNumber);

    @POST("/hsb/mobile/placeAnOrder.html")
    Call<ResponseData> placeAnOrder(@Body PlaceOrdersJson placeOrdersJson);

}
