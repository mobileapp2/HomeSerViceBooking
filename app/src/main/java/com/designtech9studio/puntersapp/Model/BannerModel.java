package com.designtech9studio.puntersapp.Model;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class BannerModel {

    /*BannerID
            BannerName
    ImageUpload*/

    int bannerId;
    String bannerName, imageUpload;

    public BannerModel(int bannerId, String bannerName, String imageUpload) {
        this.bannerId = bannerId;
        this.bannerName = bannerName;
        this.imageUpload = imageUpload;
    }

    public int getBannerId() {
        return bannerId;
    }

    public void setBannerId(int bannerId) {
        this.bannerId = bannerId;
    }

    public String getBannerName() {
        return bannerName;
    }

    public void setBannerName(String bannerName) {
        this.bannerName = bannerName;
    }

    public String getImageUpload() {
        return imageUpload;
    }

    public void setImageUpload(String imageUpload) {
        this.imageUpload = imageUpload;
    }

    public static List<String> getBannerImageList(List<BannerModel> models) {

        List<String> bannerImages = new ArrayList<>();


        for (BannerModel bannerModel: models) {
            Log.i("BannerURL", bannerModel.getImageUpload());
            bannerImages.add(bannerModel.getImageUpload());
        }
        Log.i("BannerImage", bannerImages.size()+"");
        if (bannerImages.size()==0) {
            bannerImages.add("http://www.perfectpunters.com/UploadImage/CategoryImage/26/Untitled_design_(25).png");
            bannerImages.add("http://www.perfectpunters.com/UploadImage/CategoryImage/27/Untitled_design_(30).png");
            bannerImages.add("http://www.perfectpunters.com/UploadImage/CategoryImage/2/Untitled%20design%20(4).png");
            bannerImages.add("http://www.perfectpunters.com/UploadImage/CategoryImage/15/Untitled%20design%20(9).png");
        }
        Collections.shuffle(bannerImages);
        return bannerImages;
    }
}
