package com.designtech9studio.puntersapp.Model;

public class CategoryMasterModel {

    /*This class is created to keep data to be populated in main page recycler viewer*/
    int catId;
    String catName, image, description;
    int mainPage;

    public CategoryMasterModel(int catId, String catName, String image, String description, int mainPage) {
        this.catId = catId;
        this.catName = catName;
        this.image = "http://www.perfectpunters.com"+image.replace("~", "").replace(" ", "%20");

        this.description = description;
        this.mainPage = mainPage;
    }

    public int getCatId() {
        return catId;
    }

    public void setCatId(int catId) {
        this.catId = catId;
    }

    public String getCatName() {
        return catName;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getMainPage() {
        return mainPage;
    }

    public void setMainPage(int mainPage) {
        this.mainPage = mainPage;
    }
    public String toString() {
        return "catId: "+catId +" catName: " + catName+ " image: " + image +" description: "+description;
    }
}
