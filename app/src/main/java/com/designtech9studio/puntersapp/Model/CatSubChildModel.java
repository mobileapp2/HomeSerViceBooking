package com.designtech9studio.puntersapp.Model;

import androidx.annotation.NonNull;

public class CatSubChildModel {

        /*ChildId
CatId
SubCatId
ChildSubCatId
ChildSubCatName
Amount
ChildDescription
Status
CreatedBy
CreatedDate
UpdatedBy
UpdatedDate
ImageUpload
*/

        int subChildId, catId, subCatId;
        String subChildName;
        double amount;
        String childDescription, childImage;
        int qty;
        int total;


    public CatSubChildModel(int subChildId, int catId, int subCatId, String subChildName, double amount, String childDescription, String childImage) {
        this.subChildId = subChildId;
        this.catId = catId;
        this.subCatId = subCatId;
        this.subChildName = subChildName;
        this.amount = amount;
        this.childDescription = childDescription;
        this.childImage = childImage;
        qty = 0;
        total = 0;
    }

    @NonNull
    @Override
    public String toString() {
        return "chidId: "+subChildId+ " catid: "+ catId +" subCatId: "+subCatId +" Name: "+subChildName + " amount: " + amount + " decription: "+childDescription + " Image: "+childImage + " QTY: " +qty+ " Amount: "+total;
    }

    public CatSubChildModel(){}

    public int getSubChildId() {
        return subChildId;
    }

    public void setSubChildId(int subChildId) {
        this.subChildId = subChildId;
    }

    public int getCatId() {
        return catId;
    }

    public void setCatId(int catId) {
        this.catId = catId;
    }

    public int getSubCatId() {
        return subCatId;
    }

    public void setSubCatId(int subCatId) {
        this.subCatId = subCatId;
    }

    public String getSubChildName() {
        return subChildName;
    }

    public void setSubChildName(String subChildName) {
        this.subChildName = subChildName;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getChildDescription() {
        return childDescription;
    }

    public void setChildDescription(String childDescription) {
        this.childDescription = childDescription;
    }

    public String getChildImage() {
        return childImage;
    }

    public void setChildImage(String childImage) {
        this.childImage = childImage;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
