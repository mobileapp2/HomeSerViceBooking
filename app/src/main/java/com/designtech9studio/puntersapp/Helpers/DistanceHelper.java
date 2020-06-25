package com.designtech9studio.puntersapp.Helpers;

import com.designtech9studio.puntersapp.Model.VendorProfileModel;

import java.util.ArrayList;

public class DistanceHelper {

    public double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (dist);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

    public ArrayList<VendorProfileModel> getUserVendorDistance(ArrayList<VendorProfileModel> vendorProfileModelArrayList, double userLat, double userLot) {

        for (VendorProfileModel vendorProfileModel: vendorProfileModelArrayList) {

            String vendorLat = vendorProfileModel.getLat();
            String vendorLot = vendorProfileModel.getLot();

            double distance = distance(userLat, userLot, Double.parseDouble(vendorLat), Double.parseDouble(vendorLot));
            System.out.println("Vendor id: " +vendorProfileModel.getVendorId() + " Dis: " + distance);

            vendorProfileModel.setDistanceFromCustomer(distance);

        }
        return vendorProfileModelArrayList;

    }

}
