package com.archide.hsb.sync.json;

/**
 * Created by Nithish on 13/01/17.
 */

public class KitchenCookingComments {

    private String kitchenCookingCommentsUUID;
    private long dateTime;
    private String comments;

    public String getKitchenCookingCommentsUUID() {
        return kitchenCookingCommentsUUID;
    }

    public void setKitchenCookingCommentsUUID(String kitchenCookingCommentsUUID) {
        this.kitchenCookingCommentsUUID = kitchenCookingCommentsUUID;
    }

    public long getDateTime() {
        return dateTime;
    }

    public void setDateTime(long dateTime) {
        this.dateTime = dateTime;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    @Override
    public String toString() {
        return "KitchenCookingComments{" +
                "kitchenCookingCommentsUUID='" + kitchenCookingCommentsUUID + '\'' +
                ", dateTime='" + dateTime + '\'' +
                ", comments='" + comments + '\'' +
                '}';
    }
}
