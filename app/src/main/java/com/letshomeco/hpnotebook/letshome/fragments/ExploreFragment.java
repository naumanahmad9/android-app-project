package com.letshomeco.hpnotebook.letshome.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.letshomeco.hpnotebook.letshome.R;
import com.letshomeco.hpnotebook.letshome.activities.ExperiencesActivity;
import com.letshomeco.hpnotebook.letshome.activities.HomesActivity;
import com.letshomeco.hpnotebook.letshome.activities.RestaurantsActivity;
import com.letshomeco.hpnotebook.letshome.adapters.smallExprListingAdapter;
import com.letshomeco.hpnotebook.letshome.adapters.smallHomeListingAdapter;
import com.letshomeco.hpnotebook.letshome.adapters.smallRestListingAdapter;
import com.letshomeco.hpnotebook.letshome.modelClasses.ExperienceListing;
import com.letshomeco.hpnotebook.letshome.modelClasses.HomeListing;
import com.letshomeco.hpnotebook.letshome.modelClasses.RestaurantListing;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ExploreFragment extends Fragment  {

    RecyclerView homes_recyclerView_main, experiences_recyclerView_main, restaurants_recyclerView_main;
    CardView cvHome, cvExpr, cvRest;
    FragmentManager manager;
    FragmentTransaction transaction;
    ArrayList<HomeListing> homeListings;
    ArrayList<ExperienceListing> exprListings;
    ArrayList<RestaurantListing> restListings;
    smallHomeListingAdapter homeAdapter;
    smallExprListingAdapter exprAdapter;
    smallRestListingAdapter restAdapter;
    FirebaseAuth auth;
    FirebaseDatabase database;
    DatabaseReference homeListingRef, exprListingRef, restListingRef;
    Button button_homes, button_experiences, button_restaurants;

    public ExploreFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_explore, container, false);

        cvHome = view.findViewById(R.id.cvHome);
        cvExpr = view.findViewById(R.id.cvExpr);
        cvRest = view.findViewById(R.id.cvRest);

        button_homes = view.findViewById(R.id.button_homes);
        button_experiences = view.findViewById(R.id.button_experiences);
        button_restaurants = view.findViewById(R.id.button_restaurants);

        homes_recyclerView_main = view.findViewById(R.id.homes_recyclerView_main);
        experiences_recyclerView_main  = view.findViewById(R.id.experiences_recyclerView_main);
        restaurants_recyclerView_main = view.findViewById(R.id.restaurants_recyclerView_main);

        homeListings = new ArrayList<>();
        restListings = new ArrayList<>();
        exprListings = new ArrayList<>();

        homeAdapter = new smallHomeListingAdapter(homeListings,getContext());
        exprAdapter = new smallExprListingAdapter(exprListings,getContext());
        restAdapter = new smallRestListingAdapter(restListings,getContext());

        homes_recyclerView_main.setLayoutManager(new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false));
        homes_recyclerView_main.setAdapter(homeAdapter);

        experiences_recyclerView_main.setLayoutManager(new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false));
        experiences_recyclerView_main.setAdapter(exprAdapter);

        restaurants_recyclerView_main.setLayoutManager(new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false));
        restaurants_recyclerView_main.setAdapter(restAdapter);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        homeListingRef = database.getReference("homes");
        exprListingRef = database.getReference("experiences");
        restListingRef = database.getReference("restaurants");

        homeListingRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                HomeListing homeListing = dataSnapshot.getValue(HomeListing.class);
                homeListings.add(homeListing);
                homeAdapter.notifyDataSetChanged();
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

        exprListingRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                ExperienceListing exprListing = dataSnapshot.getValue(ExperienceListing.class);
                exprListings.add(exprListing);
                exprAdapter.notifyDataSetChanged();
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

        restListingRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                RestaurantListing restListing = dataSnapshot.getValue(RestaurantListing.class);
                restListings.add(restListing);
                restAdapter.notifyDataSetChanged();
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

        cvHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), HomesActivity.class));
            }
        });

        cvExpr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), ExperiencesActivity.class));
            }
        });

        cvRest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), RestaurantsActivity.class));
            }
        });
        button_homes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), HomesActivity.class));
            }
        });

        button_experiences.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), ExperiencesActivity.class));
            }
        });

        button_restaurants.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), RestaurantsActivity.class));
            }
        });

        return view;

    }

    private void replaceFragment(Fragment fragmentObject) {

        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.fragmentContainer_main, fragmentObject).addToBackStack( "tag" ).commit();
    }

}
