package com.example.hpnotebook.letshome.activities;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.SearchView;

import com.example.hpnotebook.letshome.R;
import com.example.hpnotebook.letshome.fragments.ExploreFragment;
import com.example.hpnotebook.letshome.fragments.FavoritesFragment;
import com.example.hpnotebook.letshome.fragments.MessagesFragment;
import com.example.hpnotebook.letshome.fragments.ProfileFragment;

public class MainActivity extends AppCompatActivity {

    android.support.v7.widget.SearchView searchview_main;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchview_main = (android.support.v7.widget.SearchView) findViewById(R.id.searchview_main);
        searchview_main.setFocusable(false);
        BottomNavigationView bottomNav = findViewById(R.id.bottomNavView_main);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        // added this if statement to keep the selected fragment when rotating the device
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer_main,
                    new ExploreFragment()).commit();
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;

                    switch (item.getItemId()) {
                        case R.id.explore:
                            selectedFragment = new ExploreFragment();
                            searchview_main.setVisibility(View.VISIBLE);
                            break;
                        case R.id.favorites:
                            selectedFragment = new FavoritesFragment();
                            searchview_main.setVisibility(View.GONE);
                            break;
                        case R.id.messages:
                            selectedFragment = new MessagesFragment();
                            searchview_main.setVisibility(View.GONE);
                            break;
                        case R.id.profile:
                            selectedFragment = new ProfileFragment();
                            searchview_main.setVisibility(View.GONE);
                            break;
                    }

                    getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer_main,
                            selectedFragment).commit();

                    return true;
                }
            };

}