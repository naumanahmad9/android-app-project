package com.example.hpnotebook.letshome;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

public class ListingDetailViewHolder extends RecyclerView.ViewHolder {

    public ImageView listing_image;
    public TextView listing_title;
    public TextView listing_rate;
    public TextView listing_rating_count;
    public RatingBar listing_ratingbar;
    private de.hdodenhof.circleimageview.CircleImageView listing_host_image;

    public ListingDetailViewHolder(@NonNull View itemView) {
        super(itemView);

        listing_image = itemView.findViewById(R.id.listingImage);
        listing_title = itemView.findViewById(R.id.listingTitle);
        listing_rate = itemView.findViewById(R.id.listingRate);
        listing_ratingbar = (RatingBar) itemView.findViewById(R.id.listingRatingbar);
        listing_rating_count = itemView.findViewById(R.id.listingRatingCount);
        listing_host_image = itemView.findViewById(R.id.listing_host_image);
    }

}
