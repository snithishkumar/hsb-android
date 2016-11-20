package com.archide.hsb.util;

import android.content.Context;
import android.net.ConnectivityManager;

/**
 * Created by Nithish on 12/11/16.
 */

public class Utilities {


    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }
}
