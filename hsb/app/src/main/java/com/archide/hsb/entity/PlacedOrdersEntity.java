package com.archide.hsb.entity;

import com.archide.hsb.sync.json.PlaceOrdersJson;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "PlacedOrders")
public class PlacedOrdersEntity {
	
	public static final String PLACED_ORDERS_ID = "PlaceOrdersId";
	public static final String PLACED_ORDERS_UUID = "PlaceOrdersUUID";
	public static final String ORDER_ID = "OrderId";
	public static final String TABLE_NUMBER = "TableNumber";
	public static final String DATE_TIME = "DateTime";
	public static final String PRICE = "Price";
	public static final String TAX_AMOUNT = "TaxAmount";
	public static final String DISCOUNT = "Discount";
	public static final String TOTAL_PRICE = "TotalPrice";
	public static final String USER_MOBILE_NUMBER = "UserMobileNumber";

    public static final String SERVER_DATE_TIME = "ServerDateTime";
    public static final String ORDER_DATE_TIME = "OrderDateTime";
    public static final String LAST_UPDATED_TIME = "LastUpdatedDateTime";
	public static final String IS_CLOSED = "IsClosed";
	public static final String COMMENTS = "comments";



    @DatabaseField(columnName = PLACED_ORDERS_ID,generatedId = true)
	private int placeOrdersId;
    @DatabaseField(columnName = PLACED_ORDERS_UUID)
	private String placeOrdersUUID;
    @DatabaseField(columnName = ORDER_ID)
	private String orderId;
    @DatabaseField(columnName = TABLE_NUMBER)
	private String tableNumber;
	@DatabaseField(columnName =  USER_MOBILE_NUMBER )
	private String userMobileNumber;
    @DatabaseField(columnName = DATE_TIME)
	private long dateTime;
    @DatabaseField(columnName = PRICE)
	private double price;
    @DatabaseField(columnName = TAX_AMOUNT)
	private double taxAmount;
    @DatabaseField(columnName = DISCOUNT)
	private double discount;
    @DatabaseField(columnName = TOTAL_PRICE)
	private double totalPrice;
    @DatabaseField(columnName = SERVER_DATE_TIME)
    private long serverDateTime;
    @DatabaseField(columnName = ORDER_DATE_TIME)
    private long orderDateTime;
    @DatabaseField(columnName = LAST_UPDATED_TIME)
    private long lastUpdatedDateTime;
	@DatabaseField(columnName = IS_CLOSED)
	private boolean isClosed;

    @DatabaseField(columnName = COMMENTS)
	private String comments;
	
	public PlacedOrdersEntity(){
		
	}

	public PlacedOrdersEntity(PlaceOrdersJson placeOrdersJson){
        this.placeOrdersUUID = placeOrdersJson.getPlaceOrderUuid();
		this.orderId = placeOrdersJson.getOrderId();
		this.tableNumber = placeOrdersJson.getTableNumber();
		this.price = placeOrdersJson.getPrice();
		this.totalPrice = placeOrdersJson.getTotalPrice();
		this.taxAmount = placeOrdersJson.getTaxAmount();
		this.discount = placeOrdersJson.getDiscount();
		this.totalPrice = placeOrdersJson.getTotalPrice();
        this.serverDateTime = placeOrdersJson.getServerDateTime();
        this.orderDateTime = placeOrdersJson.getOrderDateTime();
        this.userMobileNumber = placeOrdersJson.getUserMobileNumber();
        this.lastUpdatedDateTime = placeOrdersJson.getLastUpdatedDateTime();

	}

	public void populateBilling(PlaceOrdersJson placeOrdersJson){
        this.price = placeOrdersJson.getPrice();
        this.totalPrice = placeOrdersJson.getTotalPrice();
        this.taxAmount = placeOrdersJson.getTaxAmount();
        this.discount = placeOrdersJson.getDiscount();
        this.totalPrice = placeOrdersJson.getTotalPrice();
        this.serverDateTime = placeOrdersJson.getServerDateTime();
        this.lastUpdatedDateTime = placeOrdersJson.getLastUpdatedDateTime();
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getUserMobileNumber() {
        return userMobileNumber;
    }

    public void setUserMobileNumber(String userMobileNumber) {
        this.userMobileNumber = userMobileNumber;
    }

	public boolean isClosed() {
		return isClosed;
	}

	public void setClosed(boolean closed) {
		isClosed = closed;
	}

	public int getPlaceOrdersId() {
		return placeOrdersId;
	}
	public void setPlaceOrdersId(int placeOrdersId) {
		this.placeOrdersId = placeOrdersId;
	}
	public String getPlaceOrdersUUID() {
		return placeOrdersUUID;
	}
	public void setPlaceOrdersUUID(String placeOrdersUUID) {
		this.placeOrdersUUID = placeOrdersUUID;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getTableNumber() {
		return tableNumber;
	}
	public void setTableNumber(String tableNumber) {
		this.tableNumber = tableNumber;
	}
	public long getDateTime() {
		return dateTime;
	}
	public void setDateTime(long dateTime) {
		this.dateTime = dateTime;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public double getTaxAmount() {
		return taxAmount;
	}
	public void setTaxAmount(double taxAmount) {
		this.taxAmount = taxAmount;
	}
	public double getDiscount() {
		return discount;
	}
	public void setDiscount(double discount) {
		this.discount = discount;
	}
	public double getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}

	public long getServerDateTime() {
		return serverDateTime;
	}

	public void setServerDateTime(long serverDateTime) {
		this.serverDateTime = serverDateTime;
	}

	public long getOrderDateTime() {
		return orderDateTime;
	}

	public void setOrderDateTime(long orderDateTime) {
		this.orderDateTime = orderDateTime;
	}

	public long getLastUpdatedDateTime() {
		return lastUpdatedDateTime;
	}

	public void setLastUpdatedDateTime(long lastUpdatedDateTime) {
		this.lastUpdatedDateTime = lastUpdatedDateTime;
	}

	@Override
	public String toString() {
		return "PlacedOrdersEntity [placeOrdersId=" + placeOrdersId + ", placeOrdersUUID=" + placeOrdersUUID + ", orderId="
				+ orderId + ", tableNumber=" + tableNumber + ", dateTime=" + dateTime + ", price=" + price
				+ ", taxAmount=" + taxAmount + ", discount=" + discount + ", totalPrice=" + totalPrice + "]";
	}
	
	

}
