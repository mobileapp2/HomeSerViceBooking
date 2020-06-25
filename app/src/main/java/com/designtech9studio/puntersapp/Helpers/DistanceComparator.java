package com.designtech9studio.puntersapp.Helpers;

import com.designtech9studio.puntersapp.Model.VendorProfileModel;

import java.util.Comparator;

public class DistanceComparator implements Comparator<VendorProfileModel> {

    @Override
    public int compare(VendorProfileModel o1, VendorProfileModel o2) {
        return String.valueOf(o1.getDistanceFromCustomer()).compareTo(String.valueOf(o2.getDistanceFromCustomer()));
    }
}
