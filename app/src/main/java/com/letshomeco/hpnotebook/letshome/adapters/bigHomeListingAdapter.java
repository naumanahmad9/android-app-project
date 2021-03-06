package com.letshomeco.hpnotebook.letshome.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.letshomeco.hpnotebook.letshome.ListingDetailViewHolder;
import com.letshomeco.hpnotebook.letshome.R;
import com.bumptech.glide.Glide;
import com.letshomeco.hpnotebook.letshome.activities.HomeListingDetail;
import com.letshomeco.hpnotebook.letshome.modelClasses.HomeListing;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class bigHomeListingAdapter  extends RecyclerView.Adapter<ListingDetailViewHolder>{

    private ArrayList<HomeListing> homeListings;
    private Context mContext;
    private FirebaseDatabase database;
    private DatabaseReference reference;

    public bigHomeListingAdapter(ArrayList<HomeListing> homeListings, Context context) {
        this.homeListings = homeListings;
        this.mContext = context;
    }

    @NonNull
    @Override
    public ListingDetailViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.homes_listing_item_big, viewGroup, false);
        return new ListingDetailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ListingDetailViewHolder listingViewHolder, int i) {

        final HomeListing listing = homeListings.get(i);

        database = FirebaseDatabase.getInstance();

        reference = database.getReference().child("homes").child(listing.getHome_listing_id());

        listingViewHolder.listing_title.setText(listing.getListing_title());
        listingViewHolder.listing_rate.setText(listing.getListing_pricing());

        Glide.with(mContext).load(listing.getListing_image()).into(listingViewHolder.listing_image);


            reference.addValueEventListener(new ValueEventListener() {
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

    public void setFilter(ArrayList<HomeListing> filter) {
        homeListings = new ArrayList<>();
        homeListings.addAll(filter);
        notifyDataSetChanged();
    }
}
