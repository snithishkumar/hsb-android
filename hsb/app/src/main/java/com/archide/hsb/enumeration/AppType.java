package com.archide.hsb.enumeration;

/**
 * Created by Nithish on 03/12/16.
 */

public enum AppType {
    Kitchen(1),User(2);
    private int appType;
    AppType(int appType){
      this.appType = appType;
    }

    public int getAppType(){
        return appType;
    }
}
