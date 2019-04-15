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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filterable;
import android.widget.Toast;

import com.example.hpnotebook.letshome.ListingAdapter;
import com.example.hpnotebook.letshome.R;
import com.example.hpnotebook.letshome.activities.HomesActivity;
import com.example.hpnotebook.letshome.modelClasses.HomeListing;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class ExploreFragment extends Fragment implements android.support.v7.widget.SearchView.OnQueryTextListener {

    //

    android.support.v7.widget.SearchView searchview_main;
    RecyclerView homes_recyclerView_main;
    CardView cvHome;
    FragmentManager manager;
    FragmentTransaction transaction;
    ArrayList<HomeListing> homeListings;
    ListingAdapter adapter;
    FirebaseAuth auth;
    FirebaseDatabase database;
    DatabaseReference homeListingRef;

    public ExploreFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_explore, container, false);

         searchview_main = (android.support.v7.widget.SearchView) view.findViewById(R.id.searchview_main);
         searchview_main.setFocusable(false);

        homes_recyclerView_main = view.findViewById(R.id.homes_recyclerView_main);
        cvHome = view.findViewById(R.id.cvHome);

        homeListings = new ArrayList<>();
        adapter=new ListingAdapter(homeListings,getContext());

        homes_recyclerView_main.setLayoutManager(new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false));
        homes_recyclerView_main.setAdapter(adapter);

        auth=FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        homeListingRef=database.getReference("homes");

        homeListingRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                HomeListing homeListing = dataSnapshot.getValue(HomeListing.class);
                homeListings.add(homeListing);
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


        cvHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), HomesActivity.class));
            }
        });

//        searchview_main.setOnQueryTextListener(new android.support.v7.widget.SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//
//                if(homeListings.contains(query)){
//                    adapter.getFilter().filter(query);
//                }else{
//                    Toast.makeText(getContext(), "No Match found",Toast.LENGTH_LONG).show();
//                }
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                //    adapter.getFilter().filter(newText);
//                return false;
//            }
//        });

        return view;
    }



    private void replaceFragment(Fragment fragmentObject) {

        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        transaction.replace(R.id.fragmentContainer_main, fragmentObject).addToBackStack( "tag" ).commit();
    }

    public ArrayList<HomeListing> Search(ArrayList<HomeListing> studentList, String query) {
        query = query.toLowerCase();
        final ArrayList<HomeListing> searchList = new ArrayList<>();
        for (HomeListing s : studentList) {
            final String name = s.getListing_location().toLowerCase();
            if (name.contains(query)) {
                searchList.add(s);
            }
        }
        return searchList;
    }

    @Override
    public boolean onQueryTextSubmit(String s) {


        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {

        final ArrayList<HomeListing> searchList = Search(homeListings, s);

        if (searchList.size() > 0) {
            adapter = new ListingAdapter(searchList);
            homes_recyclerView_main.setAdapter(adapter);
            adapter.setFilter(searchList);
            return true;

        } else {
            Toast.makeText(getContext(), "No Record Found", Toast.LENGTH_SHORT).show();
            homes_recyclerView_main.setAdapter(null);
            return false;
        }
    }


}
