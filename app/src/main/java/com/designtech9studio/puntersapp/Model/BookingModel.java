package com.designtech9studio.puntersapp.Model;

import java.util.Date;

public class BookingModel {

    int tran_id;
    String  date, time;
    String address;
    int amount, qty, rate;
    String modeOfPayment;

    String invoiceNo;
    int vendorId, userId;
    String vendorName, vendorMobile, serviceName, serviceId;

    boolean onGoing;
    String vendorUserName;

    int isAccepted, isCancelled;

    String cancelReason, cancelDate;


    String customerName, customerMobile;
    double customerLat, customerLng;
    public BookingModel(){}

    public BookingModel(int tran_id, String date, String time, String address, int amount) {
        this.tran_id = tran_id;
        this.date = date;
        this.time = time;
        this.address = address;
        this.amount = amount;
    }

    public double getCustomerLat() {
        return customerLat;
    }

    public void setCustomerLat(double customerLat) {
        this.customerLat = customerLat;
    }

    public double getCustomerLng() {
        return customerLng;
    }

    public void setCustomerLng(double customerLng) {
        this.customerLng = customerLng;
    }

    public int getIsAccepted() {
        return isAccepted;
    }

    public void setIsAccepted(int isAccepted) {
        this.isAccepted = isAccepted;
    }

    public int getIsCancelled() {
        return isCancelled;
    }

    public void setIsCancelled(int isCancelled) {
        this.isCancelled = isCancelled;
    }

    public String getCancelReason() {
        return cancelReason;
    }

    public void setCancelReason(String cancelReason) {
        this.cancelReason = cancelReason;
    }

    public String getCancelDate() {
        return cancelDate;
    }

    public void setCancelDate(String cancelDate) {
        this.cancelDate = cancelDate;
    }

    public String getVendorUserName() {
        return vendorUserName;
    }

    public void setVendorUserName(String vendorUserName) {
        this.vendorUserName = vendorUserName;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public boolean isOnGoing() {
        return onGoing;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public void setOnGoing(boolean onGoing) {
        this.onGoing = onGoing;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerMobile() {
        return customerMobile;
    }

    public void setCustomerMobile(String customerMobile) {
        this.customerMobile = customerMobile;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public int getVendorId() {
        return vendorId;
    }

    public void setVendorId(int vendorId) {
        this.vendorId = vendorId;
    }

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public String getVendorMobile() {
        return vendorMobile;
    }

    public void setVendorMobile(String vendorMobile) {
        this.vendorMobile = vendorMobile;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getModeOfPayment() {
        return modeOfPayment;
    }

    public void setModeOfPayment(String modeOfPayment) {
        this.modeOfPayment = modeOfPayment;
    }

    public int getTran_id() {
        return tran_id;
    }

    public void setTran_id(int tran_id) {
        this.tran_id = tran_id;
    }


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "BookingModel{" +
                "tran_id=" + tran_id +
                ", datetime=" + date +
                ", address='" + address + '\'' +
                ", amount=" + amount +
                ", timing = " +time +
                '}';
    }
}
