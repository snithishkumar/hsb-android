package com.archide.hsb.service.impl;

import android.content.Context;

import com.archide.hsb.dao.KitchenDao;
import com.archide.hsb.dao.impl.KitchenDaoImpl;
import com.archide.hsb.entity.KitchenOrderDetailsEntity;
import com.archide.hsb.entity.KitchenOrdersListEntity;

import java.util.List;

/**
 * Created by Nithish on 18/03/17.
 */

public class PrinterServiceImpl {

    private KitchenDao kitchenDao;
    private Context context;

    public PrinterServiceImpl(Context context){
        try{
            kitchenDao = new KitchenDaoImpl(context);
            this.context = context;
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void printOrders(String orderId){
try{
    KitchenOrdersListEntity kitchenOrdersListEntity = kitchenDao.getKitchenOrdersListEntity(orderId);
    List<KitchenOrderDetailsEntity> kitchenOrderDetailsEntities =  kitchenDao.getKitchenOrderDetails(kitchenOrdersListEntity);
}catch (Exception e){
    e.printStackTrace();
}
    }
}
