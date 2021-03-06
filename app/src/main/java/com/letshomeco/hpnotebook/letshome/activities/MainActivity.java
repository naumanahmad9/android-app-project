package com.letshomeco.hpnotebook.letshome.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.letshomeco.hpnotebook.letshome.R;
import com.letshomeco.hpnotebook.letshome.fragments.ExploreFragment;
import com.letshomeco.hpnotebook.letshome.fragments.FavoritesFragment;
import com.letshomeco.hpnotebook.letshome.fragments.MessagesFragment;
import com.letshomeco.hpnotebook.letshome.fragments.ProfileFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().hide();

        setContentView(R.layout.activity_main);

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
                            break;
                        case R.id.favorites:
                            selectedFragment = new FavoritesFragment();
                            break;
                        case R.id.messages:
                            selectedFragment = new MessagesFragment();
                            break;
                        case R.id.profile:
                            selectedFragment = new ProfileFragment();
                            break;
                    }

                    getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer_main,
                            selectedFragment).commit();

                    return true;
                }
            };

    @Override
    public void onBackPressed() { }
}