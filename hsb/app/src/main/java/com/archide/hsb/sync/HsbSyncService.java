package com.archide.hsb.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

/**
 * Created by Nithish on 06/11/16.
 */

public class HsbSyncService extends Service {

    private static final Object sSyncAdapterLock = new Object();
    private static HsbSyncAdapter sMobilePaySyncAdapter = null;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("HsbSyncService", "onCreate - SunshineSyncService");
        synchronized (sSyncAdapterLock) {
            if (sMobilePaySyncAdapter == null) {
                sMobilePaySyncAdapter = new HsbSyncAdapter(getApplicationContext(), true);
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return sMobilePaySyncAdapter.getSyncAdapterBinder();
    }
}
