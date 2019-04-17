package com.example.hpnotebook.letshome.fragments;

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
import android.widget.Toast;

import com.example.hpnotebook.letshome.R;
import com.example.hpnotebook.letshome.activities.HomesActivity;
import com.example.hpnotebook.letshome.adapters.smallExprListingAdapter;
import com.example.hpnotebook.letshome.adapters.smallHomeListingAdapter;
import com.example.hpnotebook.letshome.adapters.smallRestListingAdapter;
import com.example.hpnotebook.letshome.modelClasses.ExperienceListing;
import com.example.hpnotebook.letshome.modelClasses.HomeListing;
import com.example.hpnotebook.letshome.modelClasses.RestaurantListing;
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

//  implements android.support.v7.widget.SearchView.OnQueryTextListener

    RecyclerView homes_recyclerView_main, experiences_recyclerView_main, restaurants_recyclerView_main;
    CardView cvHome;
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

    public ExploreFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_explore, container, false);

        homes_recyclerView_main = view.findViewById(R.id.homes_recyclerView_main);
        experiences_recyclerView_main  = view.findViewById(R.id.experiences_recyclerView_main);
        restaurants_recyclerView_main = view.findViewById(R.id.restaurants_recyclerView_main);

        cvHome = view.findViewById(R.id.cvHome);

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

        auth=FirebaseAuth.getInstance();
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

        return view;
    }

    private void replaceFragment(Fragment fragmentObject) {

        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        transaction.replace(R.id.fragmentContainer_main, fragmentObject).addToBackStack( "tag" ).commit();
    }

//    public ArrayList<HomeListing> Search(ArrayList<HomeListing> studentList, String query) {
//        query = query.toLowerCase();
//        final ArrayList<HomeListing> searchList = new ArrayList<>();
//        for (HomeListing s : studentList) {
//            final String name = s.getListing_location().toLowerCase();
//            if (name.contains(query)) {
//                searchList.add(s);
//            }
//        }
//        return searchList;
//    }
//
//    @Override
//    public boolean onQueryTextSubmit(String s) {
//
//
//        return false;
//    }

//    @Override
//    public boolean onQueryTextChange(String s) {
//
//        final ArrayList<HomeListing> searchList = Search(homeListings, s);
//
//        if (searchList.size() > 0) {
//            adapter = new HomeListingAdapter(searchList);
//            homes_recyclerView_main.setAdapter(adapter);
//            adapter.setFilter(searchList);
//            return true;
//
//        } else {
//            Toast.makeText(getContext(), "No Record Found", Toast.LENGTH_SHORT).show();
//            homes_recyclerView_main.setAdapter(null);
//            return false;
//        }
//    }

}
