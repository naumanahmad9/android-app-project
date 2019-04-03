package com.example.hpnotebook.letshome;

public class Listing {
    String listing_id, listing_name, listing_location, listing_rate, listing_host_name,
            listing_guest_space, listing_room, listing_bedrooms, listing_bathroom, listing_image;
    float listing_average_rating;

    public Listing(String listing_id, String listing_name, String listing_location, String listing_rate, String listing_host_name, String listing_guest_space, String listing_room, String listing_bedrooms, String listing_bathroom, String listing_image) {
        this.listing_id = listing_id;
        this.listing_name = listing_name;
        this.listing_location = listing_location;
        this.listing_rate = listing_rate;
        this.listing_host_name = listing_host_name;
        this.listing_guest_space = listing_guest_space;
        this.listing_room = listing_room;
        this.listing_bedrooms = listing_bedrooms;
        this.listing_bathroom = listing_bathroom;
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
