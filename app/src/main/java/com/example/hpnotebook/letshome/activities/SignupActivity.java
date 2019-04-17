package com.example.hpnotebook.letshome.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hpnotebook.letshome.R;
import com.example.hpnotebook.letshome.modelClasses.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignupActivity extends AppCompatActivity {

    RelativeLayout signup_container;
    Snackbar snackbar;
    EditText signup_email, signup_name, signup_password;
    Button signup_btn;
    FirebaseAuth auth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference userRef;
    FirebaseUser user;
    private boolean connectionCheck, fieldCheck;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        init();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");

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

                } else {
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR);

                }
            }
        } else if (info == null) {
            testConnection();

            snackbar.setActionTextColor(Color.parseColor("#e6610049"));
            View view = snackbar.getView();
            view.setBackgroundColor(Color.WHITE);
            TextView textView = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(Color.parseColor("#e6610049"));
        }

//        testConnection();

//        snackbar.setActionTextColor(Color.parseColor("#e6610049"));
//        View view = snackbar.getView();
//        view.setBackgroundColor(Color.WHITE);
//        TextView textView = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
//        textView.setTextColor(Color.parseColor("#e6610049"));

        signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = signup_name.getText().toString();
                String email = signup_email.getText().toString();
                String password = signup_password.getText().toString();

                if(name.isEmpty()){
                    signup_name.setError("This field is empty");
                    fieldCheck = true;
                }
                if(email.isEmpty()){
                    signup_email.setError("This field is empty");
                    fieldCheck = true;
                }
                if(password.isEmpty()){
                    signup_password.setError("This field is empty");
                    fieldCheck = true;
                }
                else if(password.length() <= 6){
                    signup_password.setError("Password is too short");
                    fieldCheck = true;
                }
                if(!fieldCheck){
                    authUser(name, email, password);
                }

            }
        });
    }

    private void testConnection() {

        connectionCheck = false;
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();
        NetworkCapabilities capabilities = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                capabilities = manager.getNetworkCapabilities(manager.getActiveNetwork());
            }
        }

        if (connectionCheck && info != null && info.isConnected()) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    snackBar(1);
                } else {
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR);
                    snackBar(2);
                }
            }

        } else {
            connectionCheck = true;
            snackBar(0);
        }
    }

    private void snackBar(int check) {
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

    private void authUser(final String name, final String email, final String pass) {

        progressDialog.show();

        auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                progressDialog.dismiss();

                if (task.isSuccessful()) {
                    user = auth.getCurrentUser();
                    signupUser(name, email, pass, user.getUid());

                } else {
                    Toast.makeText(SignupActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void signupUser(String name, String email, String pass, String uid) {

        User user = new User(name, uid, email, pass);
        userRef.child(uid).setValue(user);
        startActivity(new Intent(this, MainActivity.class));
    }

    private void init() {
        signup_container = findViewById(R.id.signup_container);
        signup_email = findViewById(R.id.signup_email);
        signup_password = findViewById(R.id.signup_password);
        signup_name = findViewById(R.id.signup_name);
        signup_btn = findViewById(R.id.signup_btn);
        auth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        userRef = firebaseDatabase.getReference("users");
    }
}
