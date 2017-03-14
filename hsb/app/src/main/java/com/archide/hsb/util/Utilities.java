package com.archide.hsb.util;

import android.content.Context;
import android.net.ConnectivityManager;

import java.text.DecimalFormat;

/**
 * Created by Nithish on 12/11/16.
 */

public class Utilities {

    static DecimalFormat decimalFormat = new DecimalFormat("#.##");
    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    public static double roundOff(double value){
        double totalCost =  Double.valueOf(decimalFormat.format(value));
        return totalCost;

    }
}
