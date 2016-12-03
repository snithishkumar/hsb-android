package com.archide.hsb.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;
import android.util.Log;

import com.archide.hsb.sync.json.ResponseData;

import org.greenrobot.eventbus.EventBus;

import hsb.archide.com.hsb.R;

/**
 * Created by Nithish on 06/11/16.
 */

public class HsbSyncAdapter extends AbstractThreadedSyncAdapter {

  private SyncPerform syncPerform = null;

    private final String LOG_TAG = HsbSyncAdapter.class.getSimpleName();

    public HsbSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        init(context);

    }

    /**
     * Initialize DAO's and Gson
     *
     * @param context
     */
    private void init(Context context) {
        try {


            syncPerform = new SyncPerform(context);

        } catch (Exception e) {
            //  MobilePayAnalytics.getInstance().trackException(e,"Error in init - MobilePaySyncAdapter");
            //
             Log.e(LOG_TAG,"Error in HsbSyncAdapter",e);
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
        switch (currentScreen){
            case 0:
               ResponseData responseData =  syncPerform.getTableDetails();
                postData(responseData);
                break;

            case 1:
                String tableNumber =  extras.getString("tableNumber");
                String mobileNumber =  extras.getString("mobileNumber");
                responseData =   syncPerform.getMenuItems(tableNumber,mobileNumber);
               // responseData =  syncPerform.getKitchenOrders();
                postData(responseData);
                break;

            case 2:
                responseData =   syncPerform.sendOrderData();
                postData(responseData);
                break;

            case 3:
                responseData =  syncPerform.getKitchenOrders();
                postData(responseData);
                break;

            case 4:
                responseData = syncPerform.getPreviousOrderDetails();
                postData(responseData);
                break;

            case 5:
                responseData =  syncPerform.getUnAvailableOrders();
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
}