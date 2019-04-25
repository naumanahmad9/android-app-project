package com.example.hpnotebook.letshome.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.hpnotebook.letshome.R;
import com.bumptech.glide.Glide;
import com.example.hpnotebook.letshome.ListingViewHolder;
import com.example.hpnotebook.letshome.activities.ExprListingDetail;
import com.example.hpnotebook.letshome.modelClasses.ExperienceListing;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class smallExprListingAdapter extends RecyclerView.Adapter<ListingViewHolder>{

    private ArrayList<ExperienceListing> exprListings;
    private Context mContext;
    private FirebaseDatabase database;
    private DatabaseReference reference, listingRef;

    public smallExprListingAdapter(ArrayList<ExperienceListing> exprListings, Context context) {
        this.exprListings = exprListings;
        this.mContext = context;
    }

    public smallExprListingAdapter(ArrayList<ExperienceListing> exprListings) {
        this.exprListings = exprListings;
    }

    @NonNull
    @Override
    public ListingViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.homes_listing_item_small, viewGroup, false);
        return new ListingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ListingViewHolder listingViewHolder, int i) {
        final ExperienceListing listing = exprListings.get(i);
        database = FirebaseDatabase.getInstance();
        reference = database.getReference().child("experiences").child(listing.getExpr_listing_id());

        listingViewHolder.listing_title.setText(listing.getListing_title());
        listingViewHolder.listing_rate.setText(listing.getListing_pricing());

        Glide.with(mContext).load(listing.getListing_image()).into(listingViewHolder.listing_image);

        if (listing.getExpr_listing_id() != null){
            listingRef= reference;

            listingRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if(dataSnapshot.hasChild("avgRating")) {
                        Float averageRating = dataSnapshot.child("avgRating").getValue(Float.class);

                        Log.e("averageRating ","averageRating "+ averageRating);

                        listingViewHolder.listing_ratingbar.setRating(averageRating);

                        Log.e("Detail ","listing object "+ dataSnapshot);
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
                bundle.putString("exprListingId", listing.getExpr_listing_id());
                Intent mIntent = new Intent(mContext, ExprListingDetail.class);
                mIntent.putExtras(bundle);
                mContext.startActivity(mIntent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return exprListings.size();
    }

    public void setFilter(ArrayList<ExperienceListing> filter) {
        exprListings = new ArrayList<>();
        exprListings.addAll(filter);
        notifyDataSetChanged();
    }

}
