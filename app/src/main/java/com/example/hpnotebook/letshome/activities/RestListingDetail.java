package com.example.hpnotebook.letshome.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.hpnotebook.letshome.R;
import com.example.hpnotebook.letshome.modelClasses.ListingRating;
import com.example.hpnotebook.letshome.modelClasses.RestaurantListing;
import com.example.hpnotebook.letshome.modelClasses.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class RestListingDetail extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseDatabase database;
    DatabaseReference restRef, userRef, viewRef, favoritesRef, listingUserIdRef;
    private ImageView listing_detail_image, listing_host_image, listing_detail_fav;
    private TextView listing_detail_title, listing_detail_rate, listingHostName, listing_detail_location,
            listingDescription;
    Button listingRate_btn, goToBooking_button;
    RatingBar listing_detail_Ratingbar;
    float homeRating, average, totalRating;
    int count = 0;
    private String restListingId, listingId, listing_userId;
    private boolean mProcessFavorite = false;
    RestaurantListing restListing;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rest_listing_detail);

        Objects.requireNonNull(getSupportActionBar()).hide();
        init();
        progressDialog = new ProgressDialog(this);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            restListingId = bundle.getString("restListingId");
        }

        listingId = restListingId;

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        database = FirebaseDatabase.getInstance();
        restRef = database.getReference("restaurants").child(restListingId);
        viewRef = database.getReference("restaurants").child(restListingId).child("views");
        userRef = database.getReference("users").child(auth.getCurrentUser().getUid());
        favoritesRef = database.getReference("favorites");

        restRef.keepSynced(true);
        viewRef.keepSynced(true);
        userRef.keepSynced(true);
        favoritesRef.keepSynced(true);

        restRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                restListing = dataSnapshot.getValue(RestaurantListing.class);

                listing_detail_title.setText(restListing.getListing_title());
                listing_detail_rate.setText(restListing.getListing_pricing());
                listingHostName.setText(restListing.getListing_host_name());
                listing_detail_location.setText(restListing.getListing_location());
                listingDescription.setText(restListing.getListing_description());

                Glide.with(getApplicationContext()).load(restListing.getListing_image()).into(listing_detail_image);

                listing_userId = restListing.getListing_userId();

                listingUserIdRef = database.getReference("users").child(listing_userId);

                listingUserIdRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        User listing_user = dataSnapshot.getValue(User.class);

                        Glide.with(getApplicationContext())
                                .load(listing_user.getImageURL())
                                .into(listing_host_image);
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

        viewRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                long viewCount = dataSnapshot.getChildrenCount();

                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    if (data.getValue().equals(auth.getCurrentUser().getUid())) {
                        restRef.child("viewCount").setValue(viewCount);
                        return;
                    }
                }

                viewRef.push().setValue(auth.getCurrentUser().getUid());
                restRef.child("viewCount").setValue(viewCount);
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

                        User user = dataSnapshot.getValue(User.class);

                        String uId = user.getUid();
                        String uName = user.getName();

                        ListingRating listingRating = new ListingRating(uId, uName, homeRating);
                        restRef.child("listingRating").child(uId).setValue(listingRating);

                        restRef.child("listingRating").addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                                ListingRating mListingRating = dataSnapshot.getValue(ListingRating.class);
                                totalRating += mListingRating.getRating();
                                count++;
                                average = totalRating / count;

                                restRef.child("avgRating").setValue(average);
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

                Intent mIntent = new Intent(RestListingDetail.this, BookingActivity.class);

                Bundle mBundle = new Bundle();
                mBundle.putString("listingId", listingId);
                mBundle.putString("listing_userId", listing_userId);
                mBundle.putString("listing_detail_title", listing_detail_title.getText().toString());

                mIntent.putExtras(mBundle);

                startActivity(mIntent);
            }
        });

        listing_detail_fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mProcessFavorite = true;

                favoritesRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if (mProcessFavorite) {
                            if (dataSnapshot.hasChild(restListingId)) {

                                favoritesRef.child(restListingId).removeValue();
                                listing_detail_fav.setImageResource(R.drawable.heart);
                                mProcessFavorite = false;

                            } else {

                                favoritesRef.child(restListingId).setValue(restListing);
                                listing_detail_fav.setImageResource(R.drawable.icon_liked);
                                mProcessFavorite = false;
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });

    }

    private void init() {

        listing_detail_title = findViewById(R.id.listing_detail_title);
        listing_detail_rate= findViewById(R.id.listing_detail_rate);
        listingHostName= findViewById(R.id.listingHostName);
        listing_detail_location= findViewById(R.id.listing_detail_location);
        listingDescription= findViewById(R.id.listingDescription);
        listing_detail_image = findViewById(R.id.listing_detail_image);
        listing_host_image = findViewById(R.id.listing_host_image);
        listing_detail_Ratingbar = findViewById(R.id.listing_detail_Ratingbar);
        listingRate_btn = findViewById(R.id.listingRate_btn);
        listing_detail_fav = findViewById(R.id.listing_detail_fav);
        goToBooking_button = findViewById(R.id.goToBooking_button);
    }

}
