package com.example.hpnotebook.letshome.fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.hpnotebook.letshome.R;
import com.example.hpnotebook.letshome.adapters.FavoritesAdapter;
import com.example.hpnotebook.letshome.modelClasses.HomeListing;
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
public class FavoritesFragment extends Fragment {

    RecyclerView rv_fav;
    FavoritesAdapter favoritesAdapter;
    ArrayList<HomeListing> homeListings;
    FirebaseAuth auth;
    FirebaseDatabase database;
    DatabaseReference homeListingRef;

    public FavoritesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_favorites, container, false);

        rv_fav = view.findViewById(R.id.rv_fav);
        homeListings = new ArrayList<>();
        favoritesAdapter = new FavoritesAdapter(homeListings, getContext());

        rv_fav.setLayoutManager(new LinearLayoutManager(getContext()));
        rv_fav.setAdapter(favoritesAdapter);

        auth=FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        homeListingRef=database.getReference("Likes");

        homeListingRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                HomeListing homeListing = dataSnapshot.getValue(HomeListing.class);
                homeListings.add(homeListing);
                favoritesAdapter.notifyDataSetChanged();
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

        return view;
    }

}
