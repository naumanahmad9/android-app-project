package com.example.hpnotebook.letshome.activities;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.example.hpnotebook.letshome.R;

public class TickMarkActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tick_mark);

        Handler handler=new Handler();

        Runnable runnable=new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(TickMarkActivity.this, MainActivity.class));
            }
        };
        handler.postDelayed(runnable,6000);
    }
}
