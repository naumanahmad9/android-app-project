package com.example.hpnotebook.letshome.activities;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.hpnotebook.letshome.R;
import com.example.hpnotebook.letshome.adapters.bigExprListingAdapter;
import com.example.hpnotebook.letshome.adapters.bigHomeListingAdapter;
import com.example.hpnotebook.letshome.modelClasses.ExperienceListing;
import com.example.hpnotebook.letshome.modelClasses.HomeListing;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ExperiencesActivity extends AppCompatActivity {

    android.support.v7.widget.SearchView searchview_experiences;
    RecyclerView recyclerView_experiences;
    ArrayList<ExperienceListing> exprListings;
    bigExprListingAdapter adapter;
    FirebaseAuth auth;
    FirebaseDatabase database;
    DatabaseReference exprListingRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_experiences);

        searchview_experiences = findViewById(R.id.searchview_experiences);
        recyclerView_experiences = findViewById(R.id.recyclerView_experiences);

        exprListings = new ArrayList<>();
        adapter = new bigExprListingAdapter(exprListings, this);

        recyclerView_experiences.setLayoutManager(new LinearLayoutManager(this));
        recyclerView_experiences.setAdapter(adapter);

        auth=FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        exprListingRef=database.getReference("listings");

        exprListingRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                ExperienceListing experienceListing = dataSnapshot.getValue(ExperienceListing.class);

                if(experienceListing.getExpr_listing_id() != null){
                exprListings.add(experienceListing);
                adapter.notifyDataSetChanged(); }
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