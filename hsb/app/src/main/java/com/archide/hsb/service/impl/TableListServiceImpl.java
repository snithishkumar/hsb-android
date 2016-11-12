package com.archide.hsb.service.impl;

import android.accounts.Account;
import android.content.ContentResolver;
import android.content.Context;
import android.os.Bundle;

import com.archide.hsb.service.TableListService;
import com.archide.hsb.sync.HsbSyncAdapter;
import com.archide.hsb.sync.SyncEvent;

import hsb.archide.com.hsb.R;

/**
 * Created by Nithish on 12/11/16.
 */

public class TableListServiceImpl implements TableListService {

    private Bundle settingsBundle;
    private Account account ;

    public TableListServiceImpl(){
        init();
    }

    @Override
    public void getTableList(Context context) {
        account = HsbSyncAdapter.getSyncAccount(context);
        settingsBundle.putInt("currentScreen", SyncEvent.GET_TABLE_LIST);
        ContentResolver.requestSync(account, context.getString(R.string.auth_type), settingsBundle);
    }




    private void init(){
        if(settingsBundle == null){
            settingsBundle = new Bundle();
            settingsBundle.putBoolean(
                    ContentResolver.SYNC_EXTRAS_MANUAL, true);
            settingsBundle.putBoolean(
                    ContentResolver.SYNC_EXTRAS_EXPEDITED, true);

        }

    }

}
