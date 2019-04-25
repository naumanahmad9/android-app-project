package com.example.hpnotebook.letshome.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.hpnotebook.letshome.R;

public class SelectListingTypeActivity extends AppCompatActivity {

    RelativeLayout listingType_home, listingType_experience, listingType_restaurant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_listing_type);

        listingType_home=findViewById(R.id.listingType_home);
        listingType_experience=findViewById(R.id.listingType_experience);
        listingType_restaurant=findViewById(R.id.listingType_restaurant);

        listingType_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SelectListingTypeActivity.this, AddHomeActivity.class));
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            }
        });

        listingType_experience.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SelectListingTypeActivity.this, AddExperienceActivity.class));
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            }
        });
        listingType_restaurant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SelectListingTypeActivity.this, AddRestaurantActivity.class));
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            }
        });
    }
}
