package com.designtech9studio.puntersapp.Model;

public class VendorProfileModel {

    String vendorId, lat, lot;
    int feebackpoints, coins;
    double distanceFromCustomer;

    String businessName, websiteLink, introduction, bankName ,bankAccountNumber, ifscCode, phone, gst, pan, aadhar;

    int catId, subId;

    public VendorProfileModel(String businessName, String websiteLink, String introduction, String bankName, String bankAccountNumber, String ifscCode, String phone, String gst, String pan, String aadhar) {
        this.businessName = businessName;
        this.websiteLink = websiteLink;
        this.introduction = introduction;
        this.bankName = bankName;
        this.bankAccountNumber = bankAccountNumber;
        this.ifscCode = ifscCode;
        this.phone = phone;
        this.gst = gst;
        this.pan = pan;
        this.aadhar = aadhar;
    }

    public int getCatId() {
        return catId;
    }

    public void setCatId(int catId) {
        this.catId = catId;
    }

    public int getSubId() {
        return subId;
    }

    public void setSubId(int subId) {
        this.subId = subId;
    }

    public double getDistanceFromCustomer() {
        return distanceFromCustomer;
    }

    public void setDistanceFromCustomer(double distanceFromCustomer) {
        this.distanceFromCustomer = distanceFromCustomer;
    }

    public String getVendorId() {
        return vendorId;
    }

    public int getCoins() {
        return coins;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }

    public void setVendorId(String vendorId) {
        this.vendorId = vendorId;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLot() {
        return lot;
    }

    public void setLot(String lot) {
        this.lot = lot;
    }

    public int getFeebackpoints() {
        return feebackpoints;
    }

    public void setFeebackpoints(int feebackpoints) {
        this.feebackpoints = feebackpoints;
    }

    public VendorProfileModel() {}

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getWebsiteLink() {
        return websiteLink;
    }

    public void setWebsiteLink(String websiteLink) {
        this.websiteLink = websiteLink;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankAccountNumber() {
        return bankAccountNumber;
    }

    public void setBankAccountNumber(String bankAccountNumber) {
        this.bankAccountNumber = bankAccountNumber;
    }

    public String getIfscCode() {
        return ifscCode;
    }

    public void setIfscCode(String ifscCode) {
        this.ifscCode = ifscCode;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getGst() {
        return gst;
    }

    public void setGst(String gst) {
        this.gst = gst;
    }

    public String getPan() {
        return pan;
    }

    public void setPan(String pan) {
        this.pan = pan;
    }

    public String getAadhar() {
        return aadhar;
    }

    public void setAadhar(String aadhar) {
        this.aadhar = aadhar;
    }

    @Override
    public String toString() {
        return "VendorProfileModel{" +
                "businessName='" + businessName + '\'' +
                ", websiteLink='" + websiteLink + '\'' +
                ", introduction='" + introduction + '\'' +
                ", bankName='" + bankName + '\'' +
                ", bankAccountNumber='" + bankAccountNumber + '\'' +
                ", ifscCode='" + ifscCode + '\'' +
                ", phone='" + phone + '\'' +
                ", gst='" + gst + '\'' +
                ", pan='" + pan + '\'' +
                ", aadhar='" + aadhar + '\'' +
                '}';
    }
}
