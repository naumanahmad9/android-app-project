package com.example.hpnotebook.letshome.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.hpnotebook.letshome.R;
import com.bumptech.glide.Glide;
import com.example.hpnotebook.letshome.ListingViewHolder;
import com.example.hpnotebook.letshome.activities.RestListingDetail;
import com.example.hpnotebook.letshome.modelClasses.RestaurantListing;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class smallRestListingAdapter extends RecyclerView.Adapter<ListingViewHolder> {

    private ArrayList<RestaurantListing> restListings;
    private Context mContext;
    private FirebaseDatabase database;
    private DatabaseReference reference, listingRef;

    public smallRestListingAdapter(ArrayList<RestaurantListing> restListings, Context context) {
        this.restListings = restListings;
        this.mContext = context;
    }

    public smallRestListingAdapter(ArrayList<RestaurantListing> restListings) {
        this.restListings = restListings;
    }

    @NonNull
    @Override
    public ListingViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.homes_listing_item_small, viewGroup, false);
        return new ListingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ListingViewHolder listingViewHolder, int i) {
        final RestaurantListing listing = restListings.get(i);
        database = FirebaseDatabase.getInstance();
        reference = database.getReference().child("restaurants").child(listing.getRest_listing_id());

        listingViewHolder.listing_title.setText(listing.getListing_title());
        listingViewHolder.listing_rate.setText(listing.getListing_pricing());

        Glide.with(mContext).load(listing.getListing_image()).into(listingViewHolder.listing_image);

        if (listing.getRest_listing_id() != null){
            listingRef= reference;

            listingRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if(dataSnapshot.hasChild("avgRating")) {
                        Float averageRating = dataSnapshot.child("avgRating").getValue(Float.class);

//                        Log.e("averageRating ","averageRating "+ averageRating);

                        listingViewHolder.listing_ratingbar.setRating(averageRating);

//                        Log.e("Detail ","listing object "+ dataSnapshot);
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
                bundle.putString("restListingId", listing.getRest_listing_id());
                Intent mIntent = new Intent(mContext, RestListingDetail.class);
                mIntent.putExtras(bundle);
                mContext.startActivity(mIntent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return restListings.size();
    }

    public void setFilter(ArrayList<RestaurantListing> filter) {
        restListings = new ArrayList<>();
        restListings.addAll(filter);
        notifyDataSetChanged();
    }
}
