package com.example.hpnotebook.letshome.activities;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

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

public class HomesActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

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


        recyclerView_homes = findViewById(R.id.recyclerView_homes);

        homeListings = new ArrayList<>();
        adapter=new bigHomeListingAdapter(homeListings, this);

        recyclerView_homes.setLayoutManager(new LinearLayoutManager(this));
        recyclerView_homes.setAdapter(adapter);

        auth=FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        homeListingRef=database.getReference("homes");

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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.search_listings, menu);

        MenuItem menuItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);

        Log.e("Home","search "+searchView);
        searchView.setQueryHint("Search");
        searchView.setOnQueryTextListener(this);

        return super.onCreateOptionsMenu(menu);
    }

    public ArrayList<HomeListing> Search(ArrayList<HomeListing> homeListing, String query) {

        query = query.toLowerCase();
        final ArrayList<HomeListing> searchHomeListings = new ArrayList<>();

        for (HomeListing s : homeListing) {
            final String name = s.getListing_title().toLowerCase();
            if (name.contains(query)) {
                searchHomeListings.add(s);
            }
        }
        return searchHomeListings;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        Log.e("Home","search "+newText);
        final ArrayList<HomeListing> searchHomeListings = Search(homeListings, newText);

        if (searchHomeListings.size() > 0) {

            Log.e("Home","search "+searchHomeListings.size());
            adapter = new bigHomeListingAdapter(searchHomeListings, HomesActivity.this);
            recyclerView_homes.setAdapter(adapter);
            adapter.setFilter(searchHomeListings);
            return true;
        }
        else {

            Toast.makeText(HomesActivity.this, "No Result", Toast.LENGTH_SHORT).show();
            recyclerView_homes.setAdapter(null);
            return false;
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}