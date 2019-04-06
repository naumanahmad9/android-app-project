package com.example.hpnotebook.letshome;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class FavoriteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavView_Bar);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(0);
        menuItem.setChecked(true);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.explore:
                        Intent intent1 = new Intent(FavoriteActivity.this, ExploreActivity.class);
                        startActivity(intent1);
                        break;

                    case R.id.favorites:
                        Intent intent2 = new Intent(FavoriteActivity.this, FavoriteActivity.class);
                        startActivity(intent2);
                        break;

                    case R.id.messages:
                        Intent intent3 = new Intent(FavoriteActivity.this, MessageActivity.class);
                        startActivity(intent3);
                        break;

                    case R.id.profile:
                        Intent intent4 = new Intent(FavoriteActivity.this, ProfileActivity.class);
                        startActivity(intent4);
                        break;
                }

                return false;
            }
        });
    }
}
