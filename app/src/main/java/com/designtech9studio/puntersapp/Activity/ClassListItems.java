package com.designtech9studio.puntersapp.Activity;

public class ClassListItems {

    public String img;
    public String name;
    public String catID;
    public String subCatId;
    public String ChildSubCatId;
    public ClassListItems(String name, String img,String catId,String _subCatId, String _ChildSUbCatID){

        this.img = img;
        this.name = name;
        this.catID = catId;
        this.subCatId = _subCatId;
        this.ChildSubCatId = _ChildSUbCatID;

    }
    public String getImg(){
        return img;
    }
    public String getName(){
        return name;
    }

    public String getCatID()
    {
        return catID;
    }

    public String getSubCatId()
    {
        return catID;
    }

    public String getChildSubCatId()
    {
        return catID;
    }

}
