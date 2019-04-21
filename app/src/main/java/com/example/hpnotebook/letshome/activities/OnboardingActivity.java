package com.example.hpnotebook.letshome.activities;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.hpnotebook.letshome.OnboardingViewPagerAdapter;
import com.example.hpnotebook.letshome.R;

public class OnboardingActivity extends AppCompatActivity {

    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        OnboardingViewPagerAdapter viewPagerAdapter = new OnboardingViewPagerAdapter(this);
        viewPager.setAdapter(viewPagerAdapter);

    }
}
