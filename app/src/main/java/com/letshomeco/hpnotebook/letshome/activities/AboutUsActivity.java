package com.letshomeco.hpnotebook.letshome.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.letshomeco.hpnotebook.letshome.R;

public class AboutUsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        getSupportActionBar().hide();


    }
}
