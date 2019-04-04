package com.example.hpnotebook.letshome;

import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SignupActivity extends AppCompatActivity {

    RelativeLayout signup_container;
    Snackbar snackbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        init();

        testConnection();

        snackbar.setActionTextColor(Color.parseColor("#e6610049"));
        View view = snackbar.getView();
        view.setBackgroundColor(Color.WHITE);
        TextView textView = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.parseColor("#e6610049"));
    }

    private void testConnection() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();
        NetworkCapabilities capabilities = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                capabilities = manager.getNetworkCapabilities(manager.getActiveNetwork());
            }
        }

        if (info != null && info.isConnected()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    showSnackBar(1);
                } else {
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR);
                    showSnackBar(2);
                }
            }
        } else {
            showSnackBar(0);
        }
    }

    private void showSnackBar(int check) {
        if (check == 1) {
            snackbar = Snackbar.make(signup_container, "Connected to Wifi", Snackbar.LENGTH_LONG);
            snackbar.show();
        } else if (check == 2) {
            snackbar = Snackbar.make(signup_container, "Connected to Mobile Data", Snackbar.LENGTH_LONG);
            snackbar.show();
        } else {
            snackbar = Snackbar.make(signup_container, "No Internet Connection", Snackbar.LENGTH_INDEFINITE);
            snackbar.show();
            snackbar.setAction("Retry", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    testConnection();
                }
            });
            snackbar.setActionTextColor(Color.parseColor("#e6610049"));
        }
    }

    private void init() {
        signup_container= findViewById(R.id.signup_container);
    }
}
