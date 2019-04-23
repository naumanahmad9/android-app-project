package com.example.hpnotebook.letshome.activities;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.hpnotebook.letshome.adapters.bigHomeListingAdapter;
import com.example.hpnotebook.letshome.R;
import com.example.hpnotebook.letshome.modelClasses.HomeListing;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class HomesActivity extends AppCompatActivity {

    android.support.v7.widget.SearchView searchview_homes;
    RecyclerView recyclerView_homes;
    ArrayList<HomeListing> homeListings;
    bigHomeListingAdapter adapter;
    FirebaseAuth auth;
    FirebaseDatabase database;
    DatabaseReference homeListingRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homes);

        searchview_homes = findViewById(R.id.searchview_homes);
        recyclerView_homes = findViewById(R.id.recyclerView_homes);

        homeListings = new ArrayList<>();
        adapter=new bigHomeListingAdapter(homeListings, this);

        recyclerView_homes.setLayoutManager(new LinearLayoutManager(this));
        recyclerView_homes.setAdapter(adapter);

        auth=FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        homeListingRef=database.getReference("listings");

        homeListingRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                HomeListing homeListing = dataSnapshot.getValue(HomeListing.class);

                if(homeListing.getHome_listing_id() != null){
                homeListings.add(homeListing);
                adapter.notifyDataSetChanged();
                }
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