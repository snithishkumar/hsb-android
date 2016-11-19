package com.archide.hsb.service;

import android.content.Context;

/**
 * Created by Nithish on 12/11/16.
 */

public interface TableListService {

    void getTableList(Context context);
    void getMenuItems(Context context,String tableNumber);
}
