package com.archide.hsb.sync;

import android.content.Context;
import android.util.Log;

import com.archide.hsb.enumeration.GsonAPI;
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

    private ResponseData getErrorResponse(){
        ResponseData responseData =new ResponseData(500,null);
        responseData.setStatusCode(500);
        return responseData;
    }

}
