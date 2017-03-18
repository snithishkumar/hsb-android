package com.archide.hsb.view.activities;


import java.io.File;

        import android.content.Context;
        import android.os.Build;
        import android.os.Environment;

public class DiskCacheUtils {



    private DiskCacheUtils() {};

    public static boolean isExternalStorageRemovable() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            return Environment.isExternalStorageRemovable();
        }
        return true;
    }

    public static File getExternalCacheDir(Context context) {
        if (hasExternalCacheDir()) {
            return context.getExternalCacheDir();
        }

        // Before Froyo we need to construct the external cache dir ourselves
        final String cacheDir = "/Android/data/" + context.getPackageName() + "/cache/";
        return new File(Environment.getExternalStorageDirectory().getPath() + cacheDir);
    }

    public static boolean hasExternalCacheDir() {

        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO;
    }


    public static String getDiskCacheDir(Context context, String uniqueName,String appName) {

        // Check if media is mounted or storage is built-in, if so, try and use external cache dir
        // otherwise use internal cache dir
        final String cachePath =
                Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) ||
                        !DiskCacheUtils.isExternalStorageRemovable() ?
                        DiskCacheUtils.getExternalCacheDir(context).getPath() :
                        context.getCacheDir().getPath();
        return cachePath+ File.separator + appName +File.separator +uniqueName;
        // return new File(cachePath + File.separator + uniqueName);
    }

    public static String getDiskCacheDirNotes(Context context, String appName) {

        // Check if media is mounted or storage is built-in, if so, try and use external cache dir
        // otherwise use internal cache dir
        final String cachePath =
                Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) ||
                        !DiskCacheUtils.isExternalStorageRemovable() ?
                        DiskCacheUtils.getExternalCacheDir(context).getPath() :
                        context.getCacheDir().getPath();
        return cachePath+ File.separator + appName;
        // return new File(cachePath + File.separator + uniqueName);
    }

    /**
     * Check if External Storage is available or not. If its available it creates App folder in External Storage
     * otherwise it will create App folder in Internal storage
     * @param context
     * @return
     */
    public static String getAppRootPath(Context context){
        String state = Environment.getExternalStorageState();

        if (Environment.MEDIA_MOUNTED.equals(state)) {
            String  path = Environment.getExternalStoragePublicDirectory("/").getAbsolutePath();
            File file =  new File(path+File.separator+"hsb");
            if(!file.exists()){
                file.mkdirs();
            }
            return  file.getPath();
        }else {
            return  context.getDir("hsb",Context.MODE_PRIVATE).getAbsolutePath();
        }
    }
}