package com.designtech9studio.puntersapp.Model;

public class SubCategoryModel {


/*SubID
CatID
SubCategoryName
Status
CreatedBy
CreatedDate
UpdatedBy
UpdatedDate
ImageUpload
MainPage
*/
    int subCatId, catID;
    String subcategoryName;
    int status;
    String image;
    int mainPage;

    public SubCategoryModel(int subCatId, int catID, String subcategoryName, int status, String image, int mainPage) {
        this.subCatId = subCatId;
        this.catID = catID;
        this.subcategoryName = subcategoryName;
        this.status = status;
        this.image = image;
        this.mainPage = mainPage;
    }

    public int getSubCatId() {
        return subCatId;
    }

    public void setSubCatId(int subCatId) {
        this.subCatId = subCatId;
    }

    public int getCatID() {
        return catID;
    }

    public void setCatID(int catID) {
        this.catID = catID;
    }

    public String getSubcategoryName() {
        return subcategoryName;
    }

    public void setSubcategoryName(String subcategoryName) {
        this.subcategoryName = subcategoryName;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getMainPage() {
        return mainPage;
    }

    public void setMainPage(int mainPage) {
        this.mainPage = mainPage;
    }
}
