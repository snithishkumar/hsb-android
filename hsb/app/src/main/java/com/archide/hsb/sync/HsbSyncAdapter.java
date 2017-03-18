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

        int currentScreen =  extras.getInt("currentScreen", SyncEvent.GET_KITCHEN_ORDERS_DATA);


        if(currentScreen != 0){
            init();
        }
        if(currentScreen == SyncEvent.GET_KITCHEN_ORDERS_DATA){
           while (true){
               try{
                   ResponseData  responseData =  kitchenSyncPerform.getKitchenOrders();
                   kitchenSyncPerform.sendUnSyncedOrderStatus();
                   kitchenSyncPerform.sendKitchenMenuUpdates();
                   if(ActivityUtil.isKitchenMenuList){
                       responseData = kitchenSyncPerform.getMenuItems();
                       ActivityUtil.isKitchenMenuList =false;
                   }
                   postData(responseData);
               }catch (Exception e){
                    e.printStackTrace();
               }
               try{
                   Thread.sleep(5000);
               }catch (Exception e){
                   e.printStackTrace();
               }

           }

        }else{
            switch (currentScreen){

                case SyncEvent.GET_MENU_LIST:
                    ResponseData responseData =   userMenusSyncPerform.getMenuItems();
                    postData(responseData);
                    break;

                case SyncEvent.PLACE_AN_ORDER:
                    responseData =   userMenusSyncPerform.sendOrderData();
                    postData(responseData);
                    break;

                case SyncEvent.GET_KITCHEN_ORDERS_DATA:

                    responseData =  kitchenSyncPerform.getKitchenOrders();
                    kitchenSyncPerform.sendUnSyncedOrderStatus();
                    postData(responseData);
                    break;

                /*case SyncEvent.RESENT_BILLING:
                    tableNumber =  extras.getString("tableNumber");
                    mobileNumber =  extras.getString("mobileNumber");
                    responseData = userMenusSyncPerform.resentBillingDetails(tableNumber,mobileNumber);
                    postData(responseData);
                    break;*/

                case SyncEvent.GET_PREVIOUS_ORDER:
                    responseData = userMenusSyncPerform.getPreviousOrderDetails();
                    postData(responseData);
                    break;

                case SyncEvent.CLOSE_AN_ORDER:
                    responseData = userMenusSyncPerform.closeAnOrder();
                    postData(responseData);
                    break;

                case SyncEvent.CHECK_AVAILABILITY:
                    responseData =  userMenusSyncPerform.getUnAvailableOrders();
                    postData(responseData);
                    break;


            }
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



}