package com.archide.hsb.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;
import android.util.Log;

import com.archide.hsb.enumeration.AppType;
import com.archide.hsb.enumeration.GsonAPI;
import com.archide.hsb.sync.json.ResponseData;
import com.archide.hsb.sync.json.TableListJson;
import com.archide.hsb.view.activities.ActivityUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import hsb.archide.com.hsb.R;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Nithish on 06/11/16.
 */

public class HsbSyncAdapter extends AbstractThreadedSyncAdapter {

  private UserMenusSyncPerform userMenusSyncPerform = null;
    private KitchenSyncPerform kitchenSyncPerform = null;

    private final String LOG_TAG = HsbSyncAdapter.class.getSimpleName();
    private Context context;

    public HsbSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
       this.context = context;

    }

    /**
     * Initialize DAO's and Gson
     *
     */
    private void init() {
        try {
            if (ActivityUtil.APP_TYPE == AppType.Kitchen.getAppType()) {
                if (kitchenSyncPerform == null) {
                    kitchenSyncPerform = new KitchenSyncPerform(context);
                }
            } else {
                if(userMenusSyncPerform == null){
                    userMenusSyncPerform = new UserMenusSyncPerform(context);
                    kitchenSyncPerform = new KitchenSyncPerform(context);
                }

            }


        } catch (Exception e) {
            //  MobilePayAnalytics.getInstance().trackException(e,"Error in init - MobilePaySyncAdapter");
            //
            Log.e(LOG_TAG, "Error in HsbSyncAdapter", e);
        }
    }

    /**
     * Common Sync Location. Call Corresponding Sync method based on flag
     *
     * @param account
     * @param extras
     * @param authority
     * @param provider
     * @param syncResult
     */
    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        int currentScreen =  extras.getInt("currentScreen",0);
        if(currentScreen != 0){
            init();
        }
        switch (currentScreen){
            case SyncEvent.GET_TABLE_LIST:
                ResponseData responseData =  getTableDetails();
                //responseData =  kitchenSyncPerform.getKitchenOrders();
                postData(responseData);
                break;

            case SyncEvent.GET_MENU_LIST:

                String tableNumber =  extras.getString("tableNumber");
                String mobileNumber =  extras.getString("mobileNumber");
                responseData =   userMenusSyncPerform.getMenuItems(tableNumber,mobileNumber);
               // responseData =  userMenusSyncPerform.getKitchenOrders();
                postData(responseData);
                break;

            case SyncEvent.PLACE_AN_ORDER:
                responseData =   userMenusSyncPerform.sendOrderData();
                postData(responseData);
                break;

            case SyncEvent.GET_KITCHEN_ORDERS_DATA:
                responseData =  kitchenSyncPerform.getKitchenOrders();
                postData(responseData);
                break;

            case 3:
                 tableNumber =  extras.getString("tableNumber");
                 mobileNumber =  extras.getString("mobileNumber");
                responseData = userMenusSyncPerform.getPreviousOrderDetails(tableNumber,mobileNumber);
                postData(responseData);
                break;

            case 4:
                tableNumber =  extras.getString("tableNumber");
                mobileNumber =  extras.getString("mobileNumber");
                responseData = userMenusSyncPerform.closeAnOrder(tableNumber,mobileNumber);
                postData(responseData);
                break;

            case 5:
                responseData =  userMenusSyncPerform.getUnAvailableOrders();
                postData(responseData);
                break;
        }

    }


    private void postData(ResponseData responseData){
        EventBus.getDefault().post(responseData);
    }


    /**
     * Helper method to get the fake account to be used with SyncAdapter, or make a new one
     * if the fake account doesn't exist yet.  If we make a new account, we call the
     * onAccountCreated method so we can initialize things.
     *
     * @param context The context used to access the account service
     * @return a fake account.
     */
    public static Account getSyncAccount(Context context) {
        // Get an instance of the Android account manager
        AccountManager accountManager =
                (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);

        // Create the account type and default account
        Account newAccount = new Account(
                context.getString(R.string.app_name), context.getString(R.string.auth_type));

        // If the password doesn't exist, the account doesn't exist
        if ( null == accountManager.getPassword(newAccount) ) {

        /*
         * Add the account and account type, no password or user data
         * If successful, return the Account object, otherwise report an error.
         */
            if (!accountManager.addAccountExplicitly(newAccount, "", null)) {
                return null;
            }
            /*
             * If you don't set android:syncable="true" in
             * in your <provider> element in the manifest,
             * then call ContentResolver.setIsSyncable(account, AUTHORITY, 1)
             * here.
             */

        }
        return newAccount;
    }


    /**
     * Get Table list from server and send back to UI
     * @return
     */
    public ResponseData getTableDetails() {
        try {
            HsbAPI hsbAPI = ServiceAPI.INSTANCE.getHsbAPI();
            Call<ResponseData> tableListResponse = hsbAPI.getTableList();
            Response<ResponseData> response = tableListResponse.execute();
            if (response != null && response.isSuccessful()) {
                ResponseData responseData = response.body();
                // ResponseData responseData =  gson.fromJson(stringResponse,ResponseData.class);
                String tableListJsonString =  responseData.getData();
                Gson gson = GsonAPI.INSTANCE.getGson();
                List<TableListJson> tableListJson =  gson.fromJson(tableListJsonString, new TypeToken<List<TableListJson>>() {
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