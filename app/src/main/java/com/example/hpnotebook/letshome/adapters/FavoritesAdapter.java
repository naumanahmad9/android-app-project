package com.example.hpnotebook.letshome.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.hpnotebook.letshome.ListingViewHolder;
import com.example.hpnotebook.letshome.R;
import com.example.hpnotebook.letshome.activities.HomeListingDetail;
import com.example.hpnotebook.letshome.modelClasses.HomeListing;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FavoritesAdapter extends RecyclerView.Adapter<ListingViewHolder> {

    private ArrayList<HomeListing> homeListings;
    private Context mContext;
    FirebaseAuth auth;
    private FirebaseDatabase database;
    private DatabaseReference favoritesReference;

    public FavoritesAdapter(ArrayList<HomeListing> homeListings, Context context) {
        this.homeListings = homeListings;
        this.mContext = context;
    }

    public FavoritesAdapter(ArrayList<HomeListing> homeListings) {
        this.homeListings = homeListings;
    }

    @NonNull
    @Override
    public ListingViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.homes_listing_item_small, viewGroup, false);
        return new ListingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ListingViewHolder listingViewHolder, int i) {

        final HomeListing listing = homeListings.get(i);
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        favoritesReference = database.getReference().child("favorites");

        listingViewHolder.listing_title.setText(listing.getListing_title());
        listingViewHolder.listing_rate.setText(listing.getListing_pricing());

        Glide.with(mContext).load(listing.getListing_image()).into(listingViewHolder.listing_image);

        if (listing.getHome_listing_id() != null){

            favoritesReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if(dataSnapshot.hasChild("avgRating")) {
                        Float averageRating = dataSnapshot.child("avgRating").getValue(Float.class);

                        // Log.e("averageRating ","averageRating "+ averageRating);

                        listingViewHolder.listing_ratingbar.setRating(averageRating);

                        // Log.e("Detail ","listing object "+ dataSnapshot);
                    }
                    if(dataSnapshot.hasChild("viewCount")) {
                        listingViewHolder.listing_rating_count.setText(String.valueOf(dataSnapshot.child("viewCount").getValue(Long.class)));
                    }

                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
        }

        listingViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("homeListingId", listing.getHome_listing_id());
                Intent mIntent = new Intent(mContext, HomeListingDetail.class);
                mIntent.putExtras(bundle);
                mContext.startActivity(mIntent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return homeListings.size();
    }

}
