package com.javaservlet;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class Corder {
	private String orderId;
    private String vendorName;
    private Timestamp orderDate;
    private Timestamp edd;
    private String shippingAddress;
    private boolean isSale;
    private boolean isActive;
    private String createdby;
    private String updatedby;
    private static final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    // Getters and setters
    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public Timestamp getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Timestamp orderDate) {
        this.orderDate = orderDate;
    }

    public Timestamp getEdd() {
        return edd;
    }

    public void setEdd(Timestamp edd) {
        this.edd = edd;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public boolean isSale() {
        return isSale;
    }

    public void setSale(boolean isSale) {
        this.isSale = isSale;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

    public String getCreatedby() {
        return createdby;
    }

    public void setCreatedby(String createdby) {
        this.createdby = createdby;
    }

    public String getUpdatedby() {
        return updatedby;
    }

    public void setUpdatedby(String updatedby) {
        this.updatedby = updatedby;
    }

	public String getTotalAmount() {
		
		return null;
	}

}
