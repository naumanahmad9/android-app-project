package com.example.hpnotebook.letshome;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.hpnotebook.letshome.modelClasses.HomeListing;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class ListingAdapter extends RecyclerView.Adapter<ListingViewHolder> {

    private ArrayList<HomeListing> listings;
    private Context mContext;
    private FirebaseDatabase database;
    private DatabaseReference reference, listingRef;

    public ListingAdapter(ArrayList<HomeListing> listings, Context context) {
        this.listings = listings;
        this.mContext = context;
    }

    @NonNull
    @Override
    public ListingViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.listing_item, viewGroup, false);
        return new ListingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ListingViewHolder listingViewHolder, int i) {
        HomeListing listing = listings.get(i);
        database = FirebaseDatabase.getInstance();
        reference = database.getReference().child("listings").child(listing.getListing_id());

        listingViewHolder.listing_title.setText(listing.getListing_title());
        listingViewHolder.listing_rate.setText(listing.getListing_pricing());

        Glide.with(mContext).load(listing.getListing_image()).into(listingViewHolder.listing_image);

        if (listing.getListing_id() != null){
             listingRef= reference.child(listing.getListing_id());

            listingRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if(dataSnapshot.hasChild("avgRating")) {
                        float averageRating = dataSnapshot.child("avgRating").getValue(Float.class);
                        listingViewHolder.listing_ratingbar.setRating(averageRating);
                    }
                    listingViewHolder.listing_rating_count.setText(String.valueOf(dataSnapshot.child("viewCount").getValue(Long.class)));

                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return listings.size();
    }
}
