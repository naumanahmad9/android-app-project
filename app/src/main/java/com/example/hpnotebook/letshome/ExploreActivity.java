package com.example.hpnotebook.letshome;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class ExploreActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore);

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
                        Intent intent1 = new Intent(ExploreActivity.this, ExploreActivity.class);
                        startActivity(intent1);
                        break;

                    case R.id.favorites:
                        Intent intent2 = new Intent(ExploreActivity.this, FavoriteActivity.class);
                        startActivity(intent2);
                        break;

                    case R.id.messages:
                        Intent intent3 = new Intent(ExploreActivity.this, MessageActivity.class);
                        startActivity(intent3);
                        break;

                    case R.id.profile:
                        Intent intent4 = new Intent(ExploreActivity.this, ProfileActivity.class);
                        startActivity(intent4);
                        break;
                }

                return false;
            }
        });
    }
}
