package com.example.hpnotebook.letshome.activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

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

public class ExperiencesActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    RecyclerView recyclerView_experiences;
    ArrayList<ExperienceListing> exprListings;
    bigExprListingAdapter adapter;
    FirebaseAuth auth;
    FirebaseDatabase database;
    DatabaseReference exprListingRef;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_experiences);

        progressDialog = new ProgressDialog(this);
        recyclerView_experiences = findViewById(R.id.recyclerView_experiences);

        exprListings = new ArrayList<>();
        adapter = new bigExprListingAdapter(exprListings, this);

        recyclerView_experiences.setLayoutManager(new LinearLayoutManager(this));
        recyclerView_experiences.setAdapter(adapter);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        exprListingRef = database.getReference("experiences");

        exprListingRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                ExperienceListing experienceListing = dataSnapshot.getValue(ExperienceListing.class);

                if (experienceListing.getExpr_listing_id() != null) {
                    exprListings.add(experienceListing);
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

        searchView.setQueryHint("Search");
        searchView.setOnQueryTextListener(this);

        return super.onCreateOptionsMenu(menu);
    }

    public ArrayList<ExperienceListing> Search(ArrayList<ExperienceListing> exprListing, String query) {

        query = query.toLowerCase();
        final ArrayList<ExperienceListing> searchExprListings = new ArrayList<>();

        for (ExperienceListing s : exprListing) {
            final String name = s.getListing_title().toLowerCase();
            if (name.contains(query)) {
                searchExprListings.add(s);
            }
        }
        return searchExprListings;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        final ArrayList<ExperienceListing> searchExprListings = Search(exprListings, newText);

        if (searchExprListings.size() > 0) {

            adapter = new bigExprListingAdapter(searchExprListings, ExperiencesActivity.this);
            recyclerView_experiences.setAdapter(adapter);
            adapter.setFilter(searchExprListings);
            return true;
        }
        else {

            Toast.makeText(ExperiencesActivity.this, "No Result", Toast.LENGTH_SHORT).show();
            recyclerView_experiences.setAdapter(null);
            return false;
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

}