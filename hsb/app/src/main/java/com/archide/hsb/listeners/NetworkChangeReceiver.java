package com.archide.hsb.listeners;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.archide.hsb.sync.json.ResponseData;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Nithish on 11/02/17.
 */

public class NetworkChangeReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null
                && activeNetwork.isConnectedOrConnecting();

        ResponseData responseData = new ResponseData(600);
        responseData.setStatusCode(600);
        responseData.setSuccess(isConnected);
        EventBus.getDefault().post(responseData);
    }
}
