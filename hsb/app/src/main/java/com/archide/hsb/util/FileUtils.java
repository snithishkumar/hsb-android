package com.archide.hsb.util;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by Admin on 3/18/2017.
 */

public class FileUtils {

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

    public static void createDir(String path) {
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

}
