package com.example.hpnotebook.letshome.modelClasses;

public class RestaurantListing {
    private String rest_listing_id, listing_userId, listing_title, listing_location, listing_pricing,
            listing_host_name,  listing_description, listing_image;
    private float listing_average_rating;

    public RestaurantListing(String rest_listing_id, String listing_userId, String listing_title, String listing_location,
                             String listing_pricing, String listing_host_name, String listing_description, String listing_image) {

        this.rest_listing_id = rest_listing_id;
        this.listing_userId = listing_userId;
        this.listing_title = listing_title;
        this.listing_location = listing_location;
        this.listing_pricing = listing_pricing;
        this.listing_host_name = listing_host_name;
        this.listing_description =  listing_description;
        this.listing_image = listing_image;
    }

    public RestaurantListing() {
    }

    public String getRest_listing_id() {
        return rest_listing_id;
    }

    public void setRest_listing_id(String rest_listing_id) {
        this.rest_listing_id = rest_listing_id;
    }

    public String getListing_userId() {
        return listing_userId;
    }

    public void setListing_userId(String listing_userId) {
        this.listing_userId = listing_userId;
    }

    public String getListing_title() {
        return listing_title;
    }

    public void setListing_title(String listing_title) {
        this.listing_title = listing_title;
    }

    public String getListing_location() {
        return listing_location;
    }

    public void setListing_location(String listing_location) {
        this.listing_location = listing_location;
    }

    public String getListing_pricing() {
        return listing_pricing;
    }

    public void setListing_pricing(String listing_pricing) {
        this.listing_pricing = listing_pricing;
    }

    public String getListing_host_name() {
        return listing_host_name;
    }

    public void setListing_host_name(String listing_host_name) {
        this.listing_host_name = listing_host_name;
    }

    public String getListing_description() {
        return listing_description;
    }

    public void setListing_description(String listing_description) {
        this.listing_description = listing_description;
    }

    public String getListing_image() {
        return listing_image;
    }

    public void setListing_image(String listing_image) {
        this.listing_image = listing_image;
    }

    public float getListing_average_rating() {
        return listing_average_rating;
    }

    public void setListing_average_rating(float listing_average_rating) {
        this.listing_average_rating = listing_average_rating;
    }
}
