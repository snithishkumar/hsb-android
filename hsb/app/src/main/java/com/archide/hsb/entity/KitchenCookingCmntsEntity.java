package com.archide.hsb.entity;

import com.archide.hsb.sync.json.KitchenCookingComments;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Nithish on 13/01/17.
 */
@DatabaseTable(tableName = "KitchenCookingCmnts")
public class KitchenCookingCmntsEntity {

    public static final String KITCHEN_ORDER_LIST = "KitchenOrdersList";
    public static final String COOKING_COMMENTS_ID = "CookingCommentsId";
    public static final String COOKING_COMMENTS = "CookingComments";
    public static final String DATE_TIME = "DateTime";
    public static final String COOKING_COMMENTS_UUID = "CookingCommentsUUID";

    @DatabaseField(columnName = COOKING_COMMENTS_ID,generatedId = true)
    private int cookingCommentsId;
    @DatabaseField(columnName = COOKING_COMMENTS)
    private String cookingComments;

    @DatabaseField(columnName = COOKING_COMMENTS_UUID)
    private String cookingCommentsUUID;

    @DatabaseField(columnName = DATE_TIME)
    private long dateTime;

    @DatabaseField(columnName = KITCHEN_ORDER_LIST,foreign = true)
    private KitchenOrdersListEntity kitchenOrdersList;


    public KitchenCookingCmntsEntity(){

    }

    public KitchenCookingCmntsEntity(KitchenCookingComments kitchenCookingComments){
        this.cookingComments = kitchenCookingComments.getComments();
        this.dateTime = kitchenCookingComments.getDateTime();
        this.cookingCommentsUUID = kitchenCookingComments.getKitchenCookingCommentsUUID();
    }

    public long getDateTime() {
        return dateTime;
    }

    public void setDateTime(long dateTime) {
        this.dateTime = dateTime;
    }

    public int getCookingCommentsId() {
        return cookingCommentsId;
    }

    public void setCookingCommentsId(int cookingCommentsId) {
        this.cookingCommentsId = cookingCommentsId;
    }

    public String getCookingComments() {
        return cookingComments;
    }

    public void setCookingComments(String cookingComments) {
        this.cookingComments = cookingComments;
    }

    public KitchenOrdersListEntity getKitchenOrdersList() {
        return kitchenOrdersList;
    }

    public void setKitchenOrdersList(KitchenOrdersListEntity kitchenOrdersList) {
        this.kitchenOrdersList = kitchenOrdersList;
    }

    @Override
    public String toString() {
        return "KitchenCookingCmntsEntity{" +
                "cookingCommentsId=" + cookingCommentsId +
                ", cookingComments='" + cookingComments + '\'' +
                ", kitchenOrdersList=" + kitchenOrdersList +
                '}';
    }
}
