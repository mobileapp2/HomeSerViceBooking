package com.designtech9studio.puntersapp.Helpers;

import com.designtech9studio.puntersapp.Model.VendorProfileModel;

import java.util.Comparator;

public class CoinsComparator implements Comparator<VendorProfileModel> {
    @Override
    public int compare(VendorProfileModel o1, VendorProfileModel o2) {
        return o1.getCoins() - o2.getCoins();
    }
}
