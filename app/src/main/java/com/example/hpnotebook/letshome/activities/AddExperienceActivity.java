package com.example.hpnotebook.letshome.activities;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.hpnotebook.letshome.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class AddExperienceActivity extends AppCompatActivity {

    EditText addHome_title, addHome_location, addHome_pricing, addHome_host_name,
            addHome_guest_space;
    ImageView addHome_images;
    Button addHome_button;
    private int Pick_image = 1;
    private Uri imageUri;

    FirebaseAuth auth;
    FirebaseDatabase firebaseDatabase;
    FirebaseUser user;
    float home_avgRating;
    DatabaseReference homeRef, homeRatingRef;
    FirebaseStorage storage;
    StorageReference imageRef;
    String homeId;
    String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_experience);
    }

    private void init() {

        addHome_title = findViewById(R.id.addHome_title);
        addHome_location = findViewById(R.id.addHome_location);
        addHome_pricing = findViewById(R.id.addHome_pricing);
        addHome_host_name = findViewById(R.id.addHome_host_name);
        addHome_guest_space = findViewById(R.id.addHome_guest_space);
        addHome_images = findViewById(R.id.addHome_images);

        addHome_button = findViewById(R.id.addHome_button);

        storage = FirebaseStorage.getInstance();

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        homeRef = firebaseDatabase.getReference("homes");

    }

    public void opengallery(View view) {
        Intent gallery = new Intent();
        gallery.setAction(Intent.ACTION_PICK);
        gallery.setType("image/*");
        startActivityForResult(Intent.createChooser(gallery, "Select"), Pick_image);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == 1 && resultCode == RESULT_OK) {
            if (data != null) {
                imageUri = data.getData();
                addHome_images.setImageURI(imageUri);
            }
        }
    }
}
