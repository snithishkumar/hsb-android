package com.archide.hsb.entity;

public class PlacedOrdersEntity {
	
	public static final String PLACED_ORDERS_ID = "placeOrdersId";
	public static final String PLACED_ORDERS_UUID = "placeOrdersUUID";
	public static final String ORDER_ID = "orderId";
	public static final String TABLE_NUMBER = "tableNumber";
	public static final String DATE_TIME = "dateTime";
	public static final String PRICE = "price";
	public static final String TAX_AMOUNT = "taxAmount";
	public static final String DISCOUNT = "discount";
	public static final String TOTAL_PRICE = "totalPrice";
	
	

	private int placeOrdersId;
	private String placeOrdersUUID;
	private String orderId;
	private String tableNumber;
	private long dateTime;
	private double price;
	private double taxAmount;
	private double discount;
	private double totalPrice;
	
	public PlacedOrdersEntity(){
		
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
	@Override
	public String toString() {
		return "PlacedOrdersEntity [placeOrdersId=" + placeOrdersId + ", placeOrdersUUID=" + placeOrdersUUID + ", orderId="
				+ orderId + ", tableNumber=" + tableNumber + ", dateTime=" + dateTime + ", price=" + price
				+ ", taxAmount=" + taxAmount + ", discount=" + discount + ", totalPrice=" + totalPrice + "]";
	}
	
	

}
