package com.archide.hsb.enumeration;

/**
 * Created by Nithish on 26/11/16.
 */

public enum ViewStatus {
    VIEWED(1),UN_VIEWED(2),UPDATES(3);

    private int viewStatus;
    ViewStatus(int viewStatus){
        this.viewStatus = viewStatus;
    }

    public int getViewStatus(){
        return viewStatus;
    }


}
