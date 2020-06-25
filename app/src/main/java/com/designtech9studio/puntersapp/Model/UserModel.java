package com.designtech9studio.puntersapp.Model;

import com.google.android.gms.maps.model.LatLng;

import java.sql.Date;

public class UserModel {
    /*
* UserMaster	UserId	1	NULL	NO	bigint
UserMaster	UserCode	2	NULL	YES	varchar
UserMaster	UserName	3	NULL	YES	varchar
UserMaster	Password	4	NULL	YES	varchar
UserMaster	RoleID	5	NULL	YES	bigint
UserMaster	MobileNo	6	NULL	YES	varchar
UserMaster	StreetAddress1	7	NULL	YES	varchar
UserMaster	StreetAddress2	8	NULL	YES	varchar
UserMaster	LandMark	9	NULL	YES	varchar
UserMaster	CityId	10	NULL	YES	int
UserMaster	StateID	11	NULL	YES	int
UserMaster	CountryID	12	NULL	YES	int
UserMaster	Lat	13	NULL	YES	varchar
UserMaster	Lot	14	NULL	YES	varchar
UserMaster	Status	15	NULL	YES	int
UserMaster	CreatedBy	16	NULL	YES	int
UserMaster	CreatedDate	17	NULL	YES	datetime
UserMaster	UpdatedBy	18	NULL	YES	int
UserMaster	UpdatedDate	19	NULL	YES	datetime
UserMaster	FirstName	20	NULL	YES	varchar
UserMaster	LastName	21	NULL	YES	varchar
UserMaster	EmailID	22	NULL	YES	varchar
*/
    String userName, password;
    int roleId;
    String mobile, StreetAddress1, StreetAddress2, LandMark;
    int cityId, stateId,countryId;
    String lat, lot;
    Date createdDate;
    String firstName, lastName, emailId;

    public UserModel(String userName, String password, int roleId, String mobile, String streetAddress1, String streetAddress2, String landMark, int cityId, int stateId, int countryId, String lat, String lot, Date createdDate, String firstName, String lastName, String emailId) {
        this.userName = userName;
        this.password = password;
        this.roleId = roleId;
        this.mobile = mobile;
        StreetAddress1 = streetAddress1;
        StreetAddress2 = streetAddress2;
        LandMark = landMark;
        this.cityId = cityId;
        this.stateId = stateId;
        this.countryId = countryId;
        this.lat = lat;
        this.lot = lot;
        this.createdDate = createdDate;
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailId = emailId;
    }

    public UserModel(String userName, String password, int roleId, String mobile, String firstName, String lastName) {
        this.userName = userName;
        this.password = password;
        this.roleId = roleId;
        this.mobile = mobile;
        this.firstName = firstName;
        this.lastName = lastName;
        cityId =0;
        stateId=0;
        countryId=0;
        emailId = null;

    }

    public UserModel() {

    }
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
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

    public String getLandMark() {
        return LandMark;
    }

    public void setLandMark(String landMark) {
        LandMark = landMark;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public int getStateId() {
        return stateId;
    }

    public void setStateId(int stateId) {
        this.stateId = stateId;
    }

    public int getCountryId() {
        return countryId;
    }

    public void setCountryId(int countryId) {
        this.countryId = countryId;
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

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }
}
