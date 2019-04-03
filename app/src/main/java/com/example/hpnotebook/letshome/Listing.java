package com.example.hpnotebook.letshome;

public class Listing {
    String listing_id, listing_name, listing_location, listing_rate, listing_host_name, listing_image;
    float listing_average_rating;

    public Listing(String listing_id, String listing_name, String listing_location, String listing_rate, String listing_host_name, String listing_image) {

        this.listing_id = listing_id;
        this.listing_name = listing_name;
        this.listing_location = listing_location;
        this.listing_rate = listing_rate;
        this.listing_host_name = listing_host_name;
        this.listing_image = listing_image;
    }

    public Listing() {
    }

    public String getListing_id() {
        return listing_id;
    }

    public void setListing_id(String listing_id) {
        this.listing_id = listing_id;
    }

    public String getListing_name() {
        return listing_name;
    }

    public void setListing_name(String listing_name) {
        this.listing_name = listing_name;
    }

    public String getListing_location() {
        return listing_location;
    }

    public void setListing_location(String listing_location) {
        this.listing_location = listing_location;
    }

    public String getListing_rate() {
        return listing_rate;
    }

    public void setListing_rate(String listing_rate) {
        this.listing_rate = listing_rate;
    }

    public String getListing_host_name() {
        return listing_host_name;
    }

    public void setListing_host_name(String listing_host_name) {
        this.listing_host_name = listing_host_name;
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
