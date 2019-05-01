package com.letshomeco.hpnotebook.letshome.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.letshomeco.hpnotebook.letshome.R;
import com.letshomeco.hpnotebook.letshome.modelClasses.HomeListing;
import com.letshomeco.hpnotebook.letshome.modelClasses.ListingRating;
import com.letshomeco.hpnotebook.letshome.modelClasses.User;
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
    DatabaseReference homeRef, userRef, viewRef, favoritesRef, listingUserIdRef;
    private ImageView listing_detail_image, listing_host_image, listing_detail_fav;
    private TextView listing_detail_title, listing_detail_rate, listingHostName, listing_detail_location,
            listingGuests, listingRooms, listingBeds, listingBathrooms;
    Button listingRate_btn, goToBooking_button;
    RatingBar listing_detail_Ratingbar;
    float homeRating, average, totalRating;
    int count = 0;
    private String homeListingId, listingId, listing_userId;
    private boolean mProcessFavorite = false;
    HomeListing homeListing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_listing_detail);

        getSupportActionBar().hide();
        init();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            homeListingId = bundle.getString("homeListingId");
        }

        listingId = homeListingId;

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        database = FirebaseDatabase.getInstance();
        homeRef = database.getReference("homes").child(homeListingId);
        viewRef = database.getReference("homes").child(homeListingId).child("views");
        userRef = database.getReference("users").child(auth.getCurrentUser().getUid());
        favoritesRef = database.getReference("favorites");

        homeRef.keepSynced(true);
        viewRef.keepSynced(true);
        userRef.keepSynced(true);
        favoritesRef.keepSynced(true);

        homeRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                //HomeListing homeListing = dataSnapshot.getValue(HomeListing.class);
                homeListing = dataSnapshot.getValue(HomeListing.class);

                listing_detail_title.setText(homeListing.getListing_title());
                listing_detail_rate.setText(homeListing.getListing_pricing());
                listingHostName.setText(homeListing.getListing_host_name());
                listing_detail_location.setText(homeListing.getListing_location());
                listingGuests.setText(homeListing.getListing_guest_space());
                listingRooms.setText(homeListing.getListing_room());
                listingBeds.setText(homeListing.getListing_bedrooms());
                listingBathrooms.setText(homeListing.getListing_bathroom());

                 Glide.with(getApplicationContext()).load(homeListing.getListing_image()).into(listing_detail_image);


                listing_userId = homeListing.getListing_userId();

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

                Intent mIntent = new Intent(HomeListingDetail.this, BookingActivity.class);

                Bundle mBundle = new Bundle();
                // mBundle.putString("homeListingId", homeListingId);
                mBundle.putString("listingId", listingId);
                mBundle.putString("listing_userId", listing_userId);
                mBundle.putString("listing_detail_title", listing_detail_title.getText().toString());

                mIntent.putExtras(mBundle);

                startActivity(mIntent);
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
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
                            if (dataSnapshot.hasChild(homeListingId)) {

                                favoritesRef.child(homeListingId).removeValue();
                                listing_detail_fav.setImageResource(R.drawable.heart);
                                mProcessFavorite = false;

                            } else {

                                //HomeListing data=dataSnapshot.getValue(HomeListing.class);
                                Log.e("Home Deatail", "dataa " + homeListing);
                                Log.e("Home Deatail", "ref " + favoritesRef);
                                favoritesRef.child(homeListingId).setValue(homeListing);
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
        listingGuests= findViewById(R.id.listingGuests);
        listingRooms= findViewById(R.id.listingRooms);
        listingBeds= findViewById(R.id.listingBeds);
        listingBathrooms= findViewById(R.id.listingBathrooms);
        listing_detail_image = findViewById(R.id.listing_detail_image);
        listing_host_image = findViewById(R.id.listing_host_image);
        listing_detail_Ratingbar = findViewById(R.id.listing_detail_Ratingbar);
        listingRate_btn = findViewById(R.id.listingRate_btn);
        listing_detail_fav = findViewById(R.id.listing_detail_fav);
        goToBooking_button = findViewById(R.id.goToBooking_button);
    }

}
