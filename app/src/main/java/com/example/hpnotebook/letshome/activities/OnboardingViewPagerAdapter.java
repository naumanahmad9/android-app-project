package com.example.hpnotebook.letshome.activities;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.example.hpnotebook.letshome.R;

public class OnboardingViewPagerAdapter extends PagerAdapter {

    private Class<OnboardingActivity> context;
    private  Context context1;
    private LayoutInflater layoutInflater;
    private Button continueBtn;
    private Integer [] images = {R.drawable.onboarding_image_1, R.drawable.onboarding_image_2, R.drawable.onboarding_image_3, R.drawable.onboarding_image_4};


    public OnboardingViewPagerAdapter(Class<OnboardingActivity> context, Context context1) {
        this.context = context;
        this.context1 = context1;
    }

    @Override
    public int getCount() {
        return images.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        layoutInflater = (LayoutInflater) context1.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.onboarding_screens_layout, null);

        continueBtn = view.findViewById(R.id.continueBtn);
        ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
        imageView.setImageResource(images[position]);

        if(position == 3){
            continueBtn.setVisibility(View.VISIBLE);
        }

        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                context1.startActivity(new Intent(context1, LoginActivity.class));
            }
        });

        ViewPager vp = (ViewPager) container;
        vp.addView(view, 0);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        ViewPager vp = (ViewPager) container;
        View view = (View) object;
        vp.removeView(view);
    }
}
