package com.archide.hsb.view.model;

import com.archide.hsb.entity.KitchenCookingCmntsEntity;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Nithish on 18/02/17.
 */

public class KitchenCommentsViewModel {

    private String comments;
    private String time;

    public KitchenCommentsViewModel(KitchenCookingCmntsEntity kitchenCookingCmntsEntity){
        this.comments = kitchenCookingCmntsEntity.getCookingComments() != null && !kitchenCookingCmntsEntity.getCookingComments().trim().isEmpty() ? kitchenCookingCmntsEntity.getCookingComments() : "No Comments";
        this.time = formatTime(kitchenCookingCmntsEntity.getDateTime());
    }

    private String formatTime(long dateTime){
        Date date = new Date(dateTime);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm a");
        return simpleDateFormat.format(date);

    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "KitchenCommentsViewModel{" +
                "comments='" + comments + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}
