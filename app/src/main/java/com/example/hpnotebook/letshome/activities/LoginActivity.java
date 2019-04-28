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
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hpnotebook.letshome.R;
import com.example.hpnotebook.letshome.modelClasses.User;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    RelativeLayout login_container;
    Snackbar snackbar;
    EditText login_email, login_pass;
    TextView textview_signup;
    Button login_btn;
    ImageView google_signin_btn;
    FirebaseAuth auth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference userRef;
    FirebaseUser user, firebaseUser;
    GoogleSignInClient googleSignInClient;
    GoogleSignInOptions signInOptions;
    private int LOGIN = 1;
    private boolean fieldCheck;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().hide();

        init();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");

        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();
        NetworkCapabilities capabilities = null;
        // NetworkCapabilities capabilities  = manager.getNetworkCapabilities(manager.getActiveNetwork());
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
//
//        snackbar.setActionTextColor(Color.parseColor("#e6610049"));
//        View view = snackbar.getView();
//        view.setBackgroundColor(Color.WHITE);
//        TextView textView = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
//        textView.setTextColor(Color.parseColor("#e6610049"));

        if (user != null) {
            startActivity(new Intent(this, MainActivity.class));
        }

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = login_email.getText().toString();
                String pass = login_pass.getText().toString();

                if (email.isEmpty()) {
                    login_email.setError("This field is empty");
                    fieldCheck = true;
                }
                if (pass.isEmpty()) {
                    login_pass.setError("This field is empty");
                    fieldCheck = true;
                } else if (pass.length() <= 6) {
                    login_pass.setError("Password is too short");
                    fieldCheck = true;
                }
                if (!fieldCheck) {
                    authUser(email, pass);
                }

            }
        });

        /*
        google_signin_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = googleSignInClient.getSignInIntent();
                startActivityForResult(intent, LOGIN);

            }
        });
        */

        textview_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(LoginActivity.this, SignupActivity.class));
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        //check if user is null
        if (firebaseUser != null){

            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == LOGIN) {

            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            if (task.isSuccessful()) {

                final GoogleSignInAccount acc = task.getResult();

                user = auth.getCurrentUser();
                final String name = acc != null ? acc.getDisplayName() : null;
                final String email = acc != null ? acc.getEmail() : null;
                final String pass = "";
                final String uid = user != null ? user.getUid() : null;
                final String imageURL = String.valueOf(acc.getPhotoUrl());

                AuthCredential authCredential = GoogleAuthProvider.getCredential(acc != null ? acc.getIdToken() : null, null);
                auth.signInWithCredential(authCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            signupUser(name, email, pass, uid, imageURL);
                            Toast.makeText(LoginActivity.this, "sign in successful", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(LoginActivity.this, Objects.requireNonNull(task.getException()).toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }
    }

    private void signupUser(String name, String email, String pass, String uid, String imageURL ) {

        User user = new User(name, uid, email, pass, imageURL);
        userRef.child(uid).setValue(user);
        startActivity(new Intent(this, MainActivity.class));
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
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
            snackbar = Snackbar.make(login_container, "Connected to Wifi", Snackbar.LENGTH_LONG);
            snackbar.show();
        } else if (check == 2) {
            snackbar = Snackbar.make(login_container, "Connected to Mobile Data", Snackbar.LENGTH_LONG);
            snackbar.show();
        } else {
            snackbar = Snackbar.make(login_container, "No Internet Connection", Snackbar.LENGTH_INDEFINITE);
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

    private void authUser(String email, String pass) {

        progressDialog.show();

        auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                progressDialog.dismiss();

                if (task.isSuccessful()) {
                    user = auth.getCurrentUser();
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                }
            }
        });
    }

    private void init() {
        login_container = findViewById(R.id.login_container);
        login_email = findViewById(R.id.login_email);
        login_pass = findViewById(R.id.login_password);
        login_btn = findViewById(R.id.login_btn);
        textview_signup = findViewById(R.id.textview_signup);
        firebaseDatabase = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        userRef = firebaseDatabase.getReference("users");
        // google_signin_btn = findViewById(R.id.google_signin_btn);

        signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("1011663763173-9on2bfkrdf66cnhpq8kskfn8jt296p1r.apps.googleusercontent.com")
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, signInOptions);
    }

    @Override
    public void onBackPressed() {
    }

}