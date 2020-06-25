package com.designtech9studio.puntersapp.Model;

public class CartDataModel {

    CatSubChildModel basicDetails;
    int qty;
    int tottal;

    public CartDataModel(CatSubChildModel basicDetails, int qty, int grandTotal) {
        this.basicDetails = basicDetails;
        this.qty = qty;
        this.tottal = grandTotal;
    }
    public CartDataModel(){}

    public CatSubChildModel getBasicDetails() {
        return basicDetails;
    }

    public void setBasicDetails(CatSubChildModel basicDetails) {
        this.basicDetails = basicDetails;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public int getTottal() {
        return tottal;
    }

    public void setTottal(int tottal) {
        this.tottal = tottal;
    }
}
