package com.letshomeco.hpnotebook.letshome.modelClasses;

public class HomeListing {
    private String home_listing_id,listing_userId, listing_title, listing_location, listing_pricing, listing_host_name,
            listing_guest_space, listing_room, listing_bedrooms, listing_bathroom, listing_image;
    private float listing_average_rating;

    public HomeListing(String home_listing_id, String listing_userId, String listing_title, String listing_location,
                       String listing_pricing, String listing_host_name, String listing_guest_space,
                       String listing_room, String listing_bedrooms, String listing_bathroom,
                       String listing_image) {

        this.home_listing_id = home_listing_id;
        this.listing_userId = listing_userId;
        this.listing_title = listing_title;
        this.listing_location = listing_location;
        this.listing_pricing = listing_pricing;
        this.listing_host_name = listing_host_name;
        this.listing_guest_space = listing_guest_space;
        this.listing_room = listing_room;
        this.listing_bedrooms = listing_bedrooms;
        this.listing_bathroom = listing_bathroom;
        this.listing_image = listing_image;
    }

    public HomeListing(String home_listing_id, String listing_title, String listing_location,
                       String listing_pricing, String listing_image) {

        this.home_listing_id = home_listing_id;
        this.listing_title = listing_title;
        this.listing_location = listing_location;
        this.listing_pricing = listing_pricing;
        this.listing_image = listing_image;
    }

    public HomeListing() {
    }

    public String getHome_listing_id() {
        return home_listing_id;
    }

    public void setHome_listing_id(String home_listing_id) {
        this.home_listing_id = home_listing_id;
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

    public String getListing_guest_space() {
        return listing_guest_space;
    }

    public void setListing_guest_space(String listing_guest_space) {
        this.listing_guest_space = listing_guest_space;
    }

    public String getListing_room() {
        return listing_room;
    }

    public void setListing_room(String listing_room) {
        this.listing_room = listing_room;
    }

    public String getListing_bedrooms() {
        return listing_bedrooms;
    }

    public void setListing_bedrooms(String listing_bedrooms) {
        this.listing_bedrooms = listing_bedrooms;
    }

    public String getListing_bathroom() {
        return listing_bathroom;
    }

    public void setListing_bathroom(String listing_bathroom) {
        this.listing_bathroom = listing_bathroom;
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
