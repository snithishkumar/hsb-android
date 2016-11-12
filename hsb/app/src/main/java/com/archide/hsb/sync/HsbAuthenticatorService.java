package com.archide.hsb.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by Nithish on 06/11/16.
 *
 * The service which allows the sync adapter framework to access the authenticator.
 */

public class HsbAuthenticatorService extends Service {

    // Instance field that stores the authenticator object
    private HsbAuthenticator mAuthenticator;

    @Override
    public void onCreate() {
        super.onCreate();
        mAuthenticator = new HsbAuthenticator(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mAuthenticator.getIBinder();
    }
}
