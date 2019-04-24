package com.example.hpnotebook.letshome.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.hpnotebook.letshome.OnboardingViewPagerAdapter;
import com.example.hpnotebook.letshome.R;

public class OnboardingActivity extends AppCompatActivity {

    ViewPager viewPager;
    Button continueBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);

        // check if the app is open first time, then show this activity, if it's opened before, then
        // it will go to LoginActivity.
        SharedPreferences pref = getSharedPreferences("ActivityPREF", Context.MODE_PRIVATE);
        if(pref.getBoolean("activity_executed", false)){
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
        else {
            SharedPreferences.Editor ed = pref.edit();
            ed.putBoolean("activity_executed", true);
            // ed.commit();
            ed.apply();
        }

        continueBtn = (Button) findViewById(R.id.continueBtn);
        viewPager = (ViewPager) findViewById(R.id.viewPager);

        OnboardingViewPagerAdapter viewPagerAdapter = new OnboardingViewPagerAdapter(OnboardingActivity.class,this);
        viewPager.setAdapter(viewPagerAdapter);

    }

}
