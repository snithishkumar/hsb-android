package com.archide.hsb.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Environment;

import java.io.File;
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

    public static String getAppRootPath(Context context) {
        String state = Environment.getExternalStorageState();
        String path = null;
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            path = Environment.getExternalStoragePublicDirectory("/").getAbsolutePath();
        } else {
            path = context.getDir("hsb", Context.MODE_PRIVATE).getAbsolutePath();
        }
        File file = new File(path + File.separator + "hsb");
        if (!file.exists()) {
            file.mkdirs();
        }
        return file.getPath();

    }

    public static void createDir(String path) {
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
    }
}
