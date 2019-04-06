package com.example.hpnotebook.letshome;

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

    private void authUser(final String name, final String email, final String pass) {

        auth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    user=auth.getCurrentUser();
                    saveData(name,email,pass,user.getUid());
                    finish();
                }
                else {
                    Toast.makeText(SignupActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void saveData(String name, String email, String pass, String uid) {

        User user = new User(name, uid, email, pass);
        userRef.child(uid).setValue(user);
        startActivity(new Intent(this, ExploreActivity.class));
    }

    private void init() {
        signup_container = findViewById(R.id.signup_container);
        signup_email = findViewById(R.id.signup_email);
        signup_password = findViewById(R.id.signup_pass);
        signup_name = findViewById(R.id.signup_name);
        signup_btn = findViewById(R.id.signup_btn);
        auth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        userRef = firebaseDatabase.getReference("users");
    }
}
