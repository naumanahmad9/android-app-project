package com.letshomeco.hpnotebook.letshome.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.letshomeco.hpnotebook.letshome.R;
import com.letshomeco.hpnotebook.letshome.adapters.bigRestListingAdapter;
import com.letshomeco.hpnotebook.letshome.modelClasses.RestaurantListing;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class RestaurantsActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

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

        auth = FirebaseAuth.getInstance();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.search_listings, menu);

        MenuItem menuItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);

        searchView.setQueryHint("Search");
        searchView.setOnQueryTextListener(this);

        return super.onCreateOptionsMenu(menu);
    }

    public ArrayList<RestaurantListing> Search(ArrayList<RestaurantListing> restListing, String query) {

        query = query.toLowerCase();
        final ArrayList<RestaurantListing> searchRestListings = new ArrayList<>();

        for (RestaurantListing s : restListing) {
            final String name = s.getListing_title().toLowerCase();
            if (name.contains(query)) {
                searchRestListings.add(s);
            }
        }
        return searchRestListings;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        final ArrayList<RestaurantListing> searchRestListings = Search(restListings, newText);

        if (searchRestListings.size() > 0) {

            adapter = new bigRestListingAdapter(searchRestListings, RestaurantsActivity.this);
            recyclerView_restaurants.setAdapter(adapter);
            adapter.setFilter(searchRestListings);
            return true;
        }
        else {

            Toast.makeText(RestaurantsActivity.this, "No Result", Toast.LENGTH_SHORT).show();
            recyclerView_restaurants.setAdapter(null);
            return false;
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

}