package com.archide.hsb.util;

import android.content.Context;
import android.net.ConnectivityManager;

import java.text.DecimalFormat;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;

import hsb.archide.com.hsb.R;

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
    public static int convertToPixels(Context context,int nDP) {
        final float conversionScale = context.getResources().getDisplayMetrics().density;
        return (int) ((nDP * conversionScale) + 0.5f);
    }
    public static void setBadgeCount(Context context, LayerDrawable icon, int count) {

        BadgeDrawable badge;

        // Reuse drawable if possible
        Drawable reuse = icon.findDrawableByLayerId(R.id.ic_badge);
        if (reuse != null && reuse instanceof BadgeDrawable) {
            badge = (BadgeDrawable) reuse;
        } else {
            badge = new BadgeDrawable(context);
        }

        badge.setCount(count);
        icon.mutate();
        icon.setDrawableByLayerId(R.id.ic_badge, badge);
    }
}
