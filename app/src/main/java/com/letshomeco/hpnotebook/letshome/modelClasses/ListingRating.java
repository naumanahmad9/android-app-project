package com.letshomeco.hpnotebook.letshome.modelClasses;

public class ListingRating {
    String uId, uName;
    float rating;

    public ListingRating(String uId, String uName, float rating) {
        this.uId = uId;
        this.uName = uName;
        this.rating = rating;
    }

    public ListingRating() {
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public String getuName() {
        return uName;
    }

    public void setuName(String uName) {
        this.uName = uName;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }
}
