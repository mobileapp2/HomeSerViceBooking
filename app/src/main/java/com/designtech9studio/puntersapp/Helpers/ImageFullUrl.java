package com.designtech9studio.puntersapp.Helpers;

public class ImageFullUrl {


    public static String getFullImageURL(String url) {
        return "http://www.perfectpunters.com"+url.replace("~", "").replace(" ", "%20");
    }

    public  String getFullImageURLNonStatic(String url) {
        return "http://www.perfectpunters.com"+url.replace("~", "").replace(" ", "%20");
    }

}
