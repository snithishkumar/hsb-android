package com.archide.hsb.sync.json;

/**
 * Created by Nithish on 12/11/16.
 */

public class TableListJson {

    private int tableListId;
    private String tableNumber;

    public int getTableListId() {
        return tableListId;
    }

    public void setTableListId(int tableListId) {
        this.tableListId = tableListId;
    }

    public String getTableNumber() {
        return tableNumber;
    }

    public void setTableNumber(String tableNumber) {
        this.tableNumber = tableNumber;
    }

    @Override
    public String toString() {
        return "TableListJson{" +
                "tableListId=" + tableListId +
                ", tableNumber='" + tableNumber + '\'' +
                '}';
    }
}
