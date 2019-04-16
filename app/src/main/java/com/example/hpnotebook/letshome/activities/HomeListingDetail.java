package com.example.hpnotebook.letshome.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.hpnotebook.letshome.R;
import com.example.hpnotebook.letshome.modelClasses.HomeListing;
import com.example.hpnotebook.letshome.modelClasses.ListingRating;
import com.example.hpnotebook.letshome.modelClasses.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HomeListingDetail extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseDatabase database;
    DatabaseReference homeRef, userRef, viewRef;
    ImageView listing_detail_image, listing_host_image;
    TextView listing_detail_title, listing_detail_rate, listingHostName, listing_detail_location,
            listingGuests, listingRooms, listingBeds, listingBathrooms;
    Button listingRate_btn, goToBooking_button;
    RatingBar listing_detail_Ratingbar;
    float homeRating, average, totalRating;
    int count = 0;
    String homeListingId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_listing_detail);

        init();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            homeListingId = bundle.getString("homeListingId");
        }

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        homeRef = database.getReference().child("homes");
        database = FirebaseDatabase.getInstance();
        homeRef = database.getReference("homes").child(homeListingId);
        viewRef = database.getReference("homes").child(homeListingId).child("views");
        userRef = database.getReference("users").child(auth.getCurrentUser().getUid());


        homeRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                HomeListing homeListing = dataSnapshot.getValue(HomeListing.class);

                listing_detail_title.setText(homeListing.getListing_title());

                Glide.with(getApplicationContext()).load(homeListing.getListing_image()).into(listing_detail_image);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        viewRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                long viewCount = dataSnapshot.getChildrenCount();

                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    if (data.getValue().equals(auth.getCurrentUser().getUid())) {
                        homeRef.child("viewCount").setValue(viewCount);
                        return;
                    }
                }

                viewRef.push().setValue(auth.getCurrentUser().getUid());
                homeRef.child("viewCount").setValue(viewCount);
                //  viewRef.push().setValue(auth.getCurrentUser().getUid());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        listingRate_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                homeRating = listing_detail_Ratingbar.getRating();

                userRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                       // Log.e("Detail ","user "+userRef);

                        User user = dataSnapshot.getValue(User.class);

                        // Log.e("Detail ","user object "+dataSnapshot);
                        String uId = user.getUid();
                        String uName = user.getName();

                        ListingRating listingRating = new ListingRating(uId, uName, homeRating);
                        homeRef.child("listingRating").child(uId).setValue(listingRating);

                        homeRef.child("listingRating").addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                                ListingRating mListingRating = dataSnapshot.getValue(ListingRating.class);
                                totalRating += mListingRating.getRating();
                                count++;
                                average = totalRating / count;

                                homeRef.child("avgRating").setValue(average);
                            }

                            @Override
                            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                            }

                            @Override
                            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                            }

                            @Override
                            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
            }
        });

        goToBooking_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeListingDetail.this, BookingActivity.class));
            }
        });

    }

    private void init() {

        listing_detail_title = findViewById(R.id.listing_detail_title);
        listing_detail_image = findViewById(R.id.listing_detail_image);
        listing_detail_Ratingbar= findViewById(R.id.listing_detail_Ratingbar);
        listingRate_btn = findViewById(R.id.listingRate_btn);
        goToBooking_button = findViewById(R.id.goToBooking_button);

//        auth = FirebaseAuth.getInstance();
//        database = FirebaseDatabase.getInstance();
//        homeRef = database.getReference().child("homes");
//        database = FirebaseDatabase.getInstance();
//        homeRef = database.getReference("homes").child(homeListingId);
//        viewRef = database.getReference("homes").child(homeListingId).child("views");
//        userRef = database.getReference("homes").child(auth.getCurrentUser().getUid());

    }
}
