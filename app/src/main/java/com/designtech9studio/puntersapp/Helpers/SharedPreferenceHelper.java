package com.designtech9studio.puntersapp.Helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.designtech9studio.puntersapp.Model.AddressModel;

import static android.content.Context.MODE_PRIVATE;

public class SharedPreferenceHelper {
    public final String PREFERENCE_NAME = "MySharedPref";
    public static final String USER_ID_KEY = "USER_ID_KEY";
    public static final String MOBILE_LEY = "MOBILE";
    public static final String ROLE_LEY = "ROLE";
    public static final String STREET_1_KEY = "STREET_1";
    public static final String STREET_2_KEY = "STREET_2";
    public static final String CITY_NAME_KEY = "CITY_NAME";
    public static final String STATE_NAME_KEY = "STATE_NAME";
    public static final String COUNTRY_NAME_KEY = "COUNTRY_NAME";
    public static final String LAT_KEY = "LAT";
    public static final String LOT_KEY = "LOT";
    public static final String IS_AVAILABLE = "Available";

    public static final String VENDOR_ID_KEY = "VendorId";
    public static final String VENDOR_Mobile = "VendorMobile";


    Context context;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor myEdit;
    public SharedPreferenceHelper(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREFERENCE_NAME, MODE_PRIVATE);
        myEdit = sharedPreferences.edit();
    }

    public void setUserId(String userId) {
        myEdit.putString(USER_ID_KEY, userId);
        myEdit.commit();
    }

    public void setVendorId(String userId) {
        myEdit.putString(VENDOR_ID_KEY, userId);
        myEdit.commit();
    }
    public void setVendorStatus(String status) {
        myEdit.putString(IS_AVAILABLE, status);
        myEdit.commit();
    }

    public void setMobile(String mobile) {
        myEdit.putString(MOBILE_LEY, mobile);
        myEdit.commit();
        System.out.println("Mobile: " +mobile + " enterd in shared preference");
    }

    public void setRole(String role) {
        myEdit.putString(ROLE_LEY, role);
        myEdit.commit();
        System.out.println("Role: " +role + " enterd in shared preference");
    }

    public boolean isUserLogedIn() {
        String userId = getUserid();
        Log.i("USERLogedIn", userId);
        return !userId.equals("");
    }
    public boolean isAvailable() {
        String avaible = sharedPreferences.getString(IS_AVAILABLE, "");
        Log.i("VendorAvail", avaible);
        if (avaible.equals("0")) return false;
        return !avaible.equals("");
    }
    public boolean isVendorLogedIn() {
        String userId = getVendorId();
        Log.i("VendorLogedIn", userId);
        return !userId.equals("");
    }

    public void getUserSignOut() {
        myEdit.clear();
        myEdit.commit();
        System.out.println("All User Info from shared preference revoed removed");
    }

    public void getUserLogIn(String userId, String mobile, String role) {
        setMobile(mobile);
        setUserId(userId);
        setRole(role);
    }

    public void getUserLogIn(String userId, String role) {
        setUserId(userId);
        setRole(role);
    }
    public String getUserid() {
        return sharedPreferences.getString(USER_ID_KEY, "");
    }

    public String getVendorId() {
        return sharedPreferences.getString(VENDOR_ID_KEY, "");
    }

    public String getVENDOR_Mobile() {
        return sharedPreferences.getString(VENDOR_Mobile, "");
    }
    public void setVendorMobile(String mobile) {

        Log.i("SharedPref", "Vendor Mobile set : " + mobile);
        myEdit.putString(VENDOR_Mobile, mobile);
        myEdit.commit();
    }
    public void setAddress(AddressModel address) {
        /*StreetAddress1 = "";
        StreetAddress2 ="";
        cityName = "";
        stateName = "";
        countryName = "";
        lat = "";
        lot = "";*/
        myEdit.putString(STREET_1_KEY, address.getStreetAddress1());
        myEdit.putString(STREET_2_KEY, address.getStreetAddress2());
        myEdit.putString(CITY_NAME_KEY, address.getCityName());
        myEdit.putString(STATE_NAME_KEY, address.getStateName());
        myEdit.putString(COUNTRY_NAME_KEY, address.getCountryName());
        myEdit.putString(LAT_KEY, address.getLat());
        myEdit.putString(LOT_KEY, address.getLot());
        myEdit.commit();
        Log.i("SharedPref", "AdreesSet State: " + address.getStateName());
    }

    public AddressModel getAddress() {
        AddressModel result = new AddressModel();
        result.setStreetAddress1(sharedPreferences.getString(STREET_1_KEY, ""));
        result.setStreetAddress2(sharedPreferences.getString(STREET_2_KEY, ""));
        result.setCityName(sharedPreferences.getString(CITY_NAME_KEY, ""));
        result.setStateName(sharedPreferences.getString(STATE_NAME_KEY, ""));

        result.setCountryName(sharedPreferences.getString(COUNTRY_NAME_KEY, ""));
        result.setLat(sharedPreferences.getString(LAT_KEY, ""));
        result.setLot(sharedPreferences.getString(LOT_KEY, ""));

        Log.i("SharedPref", "AdreesSet GET State: " + sharedPreferences.getString(STATE_NAME_KEY, ""));
        return result;
    }


}
