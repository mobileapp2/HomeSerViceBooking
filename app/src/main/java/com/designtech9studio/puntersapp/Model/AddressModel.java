package com.designtech9studio.puntersapp.Model;

public class AddressModel {

    String StreetAddress1, StreetAddress2;
    String cityName, stateName, countryName;
    String lat, lot;
    String landmark;

    public AddressModel() {
        StreetAddress1 = "";
        StreetAddress2 ="";
        cityName = "";
        stateName = "";
        countryName = "";
        lat = "";
        lot = "";
        landmark="";
    }

    public AddressModel(String streetAddress1, String streetAddress2, String cityName, String stateName, String countryName, String lat, String lot) {
        StreetAddress1 = streetAddress1;
        StreetAddress2 = streetAddress2;
        this.cityName = cityName;
        this.stateName = stateName;
        this.countryName = countryName;
        this.lat = lat;
        this.lot = lot;
    }

    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }

    public String getStreetAddress1() {
        return StreetAddress1;
    }

    public void setStreetAddress1(String streetAddress1) {
        StreetAddress1 = streetAddress1;
    }

    public String getStreetAddress2() {
        return StreetAddress2;
    }

    public void setStreetAddress2(String streetAddress2) {
        StreetAddress2 = streetAddress2;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
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
    public boolean isNull() {
        if (lat.equals("") && lot.equals(""))return false;
        return true;
    }

    @Override
    public String toString() {
        /*return "AddressModel{" +
                "StreetAddress1='" + StreetAddress1 + '\'' +
                ", StreetAddress2='" + StreetAddress2 + '\'' +
                ", cityName='" + cityName + '\'' +
                ", stateName='" + stateName + '\'' +
                ", countryName='" + countryName + '\'' +
                ", lat='" + lat + '\'' +
                ", lot='" + lot + '\'' +
                '}';*/
        return "" +
                "" + StreetAddress1 +
                ", " + StreetAddress2 +
                ", " + cityName +
                ", " + stateName +
                ", " + countryName;
    }
}
