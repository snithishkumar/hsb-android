package com.archide.hsb.sync.json;

public class ResponseData {

	private int statusCode;
	private String data;
	private Object message;
	private Boolean success;
	
	
	public ResponseData(boolean success, String data,int statusCode) {
		this.statusCode = statusCode;
		this.success = success;
		this.data = data;
	}

	public ResponseData(Object data) {
		this.message = data;
	}
	
	public ResponseData(boolean success, String data) {
		this.success = success;
		this.data = data;
	}

	public ResponseData(int statusCode, String data) {
		this.statusCode = statusCode;
		this.data = data;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public Object getMessage() {
		return message;
	}

    public void setMessage(Object message) {
        this.message = message;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    @Override
	public String toString() {
		return "ResponseData [statusCode=" + statusCode + ", data=" + data + "]";
	}

}
