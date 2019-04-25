package com.example.hpnotebook.letshome.activities;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.hpnotebook.letshome.R;
import com.example.hpnotebook.letshome.adapters.bigHomeListingAdapter;
import com.example.hpnotebook.letshome.adapters.bigRestListingAdapter;
import com.example.hpnotebook.letshome.modelClasses.HomeListing;
import com.example.hpnotebook.letshome.modelClasses.RestaurantListing;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class RestaurantsActivity extends AppCompatActivity {
    android.support.v7.widget.SearchView searchview_restaurants;
    RecyclerView recyclerView_restaurants;
    ArrayList<RestaurantListing> restListings;
    bigRestListingAdapter adapter;
    FirebaseAuth auth;
    FirebaseDatabase database;
    DatabaseReference restListingRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurants);

        recyclerView_restaurants = findViewById(R.id.recyclerView_restaurants);

        restListings = new ArrayList<>();
        adapter = new bigRestListingAdapter(restListings, this);

        recyclerView_restaurants.setLayoutManager(new LinearLayoutManager(this));
        recyclerView_restaurants.setAdapter(adapter);

        auth=FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        restListingRef = database.getReference("restaurants");

        restListingRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                RestaurantListing restaurantListing = dataSnapshot.getValue(RestaurantListing.class);
                restListings.add(restaurantListing);
                adapter.notifyDataSetChanged();
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

}