package com.example.hpnotebook.letshome.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.hpnotebook.letshome.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ListingDetail extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseDatabase database;
    DatabaseReference homeRef, userRef, viewRef;
    ImageView listing_detail_image, listing_host_image;
    TextView listing_detail_title, listing_detail_rate, listingHostName, listing_detail_location,
            listingGuests, listingRooms, listingBeds, listingBathrooms;
    Button rate_btn;
    RatingBar ratingBar;
    float rating, average, totalRating;
    int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listing_detail);

        init();

    }

    private void init() {

    }
}
