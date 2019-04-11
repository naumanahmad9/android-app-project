package com.example.hpnotebook.letshome;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

public class ListingViewHolder extends RecyclerView.ViewHolder {
    ImageView listing_image;
    TextView listing_title, listing_rate, listing_rating_count;
    RatingBar listing_ratingbar;

    public ListingViewHolder(@NonNull View itemView) {
        super(itemView);

        listing_image = itemView.findViewById(R.id.listingImage);
        listing_title = itemView.findViewById(R.id.listingTitle);
        listing_rate = itemView.findViewById(R.id.listingRate);
        listing_ratingbar = (RatingBar) itemView.findViewById(R.id.listingRatingbar);
        listing_rating_count = itemView.findViewById(R.id.listingRatingCount);
    }
}
