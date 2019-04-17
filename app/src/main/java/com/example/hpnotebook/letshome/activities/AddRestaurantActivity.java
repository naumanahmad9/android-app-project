package com.example.hpnotebook.letshome.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.hpnotebook.letshome.R;
import com.example.hpnotebook.letshome.modelClasses.HomeListing;
import com.example.hpnotebook.letshome.modelClasses.ListingRating;
import com.example.hpnotebook.letshome.modelClasses.RestaurantListing;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

public class AddRestaurantActivity extends AppCompatActivity {

    EditText addRest_title, addRest_location, addRest_pricing, addRest_host_name,
            addRest_guest_space;
    ImageView addRest_images;
    Button addRest_button;
    private int Pick_image = 1;
    private Uri imageUri;

    FirebaseAuth auth;
    FirebaseDatabase firebaseDatabase;
    FirebaseUser user;
    float rest_avgRating;
    DatabaseReference restRef, restRatingRef;
    FirebaseStorage storage;
    StorageReference restImageRef;
    String restId;
    String key;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_restaurant);

        init();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            restId = bundle.getString("restId");

            restRef.child(restId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    RestaurantListing restaurantListing = dataSnapshot.getValue(RestaurantListing.class);
                    if (restaurantListing != null) {
                        rest_avgRating = restaurantListing.getListing_average_rating();
                    }
                    addRest_title.setText(restaurantListing.getListing_title());
                    addRest_location.setText(restaurantListing.getListing_location());

                    addRest_pricing.setText(restaurantListing.getListing_pricing());

                    Glide
                            .with(AddRestaurantActivity.this)
                            .load(restaurantListing.getListing_image())
                            .into(addRest_images);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
        }

        addRest_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = addRest_title.getText().toString();
                String location = addRest_location.getText().toString();
                String pricing = addRest_pricing.getText().toString();
                String host_name = addRest_host_name.getText().toString();

                key = restId;
                if (restId == null) {
                    key = restRef.push().getKey();
                }
                restImageRef = storage.getReference("restaurant listing images/" + key);

                addRest(key, title, location, pricing, host_name);
            }
        });
    }

    private void addRest(final String restId, final String title, final String location, final String pricing,
                         final String host_name) {

        BitmapDrawable drawable = (BitmapDrawable) addRest_images.getDrawable();
        Bitmap bitmap = drawable.getBitmap();

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] bytes = stream.toByteArray();

        restImageRef.putBytes(bytes).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                restImageRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if ((task.isSuccessful())) {
                            String imageUrl = task.getResult().toString();

                            restRatingRef = restRef.child(restId).child("restRating");

                            if (restRatingRef != null) {
                                restRatingRef.addChildEventListener(new ChildEventListener() {
                                    @Override
                                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                                        ListingRating listingRating = dataSnapshot.getValue(ListingRating.class);
                                        String ratingKey = dataSnapshot.getKey();
                                        restRatingRef.child(ratingKey).setValue(listingRating);

                                    }

                                    @Override
                                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                                    }

                                    @Override
                                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                                    }

                                    @Override
                                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                    }
                                });
                            }

                            RestaurantListing restaurantListing = new RestaurantListing(restId, title, location, pricing,
                                    host_name, imageUrl);
                            restRef.child(restId).setValue(restaurantListing);
                            restRef.child(restId).child("avgRating").setValue(rest_avgRating);
                            Toast.makeText(AddRestaurantActivity.this, "Listing added", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(AddRestaurantActivity.this, MainActivity.class));
                            finish();

                        }
                    }
                });
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                progressDialog.setMessage((int) progress + "% Uploaded");

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(AddRestaurantActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void init() {

        addRest_title = findViewById(R.id.addRest_title);
        addRest_location = findViewById(R.id.addRest_location);
        addRest_pricing = findViewById(R.id.addRest_pricing);
        addRest_host_name = findViewById(R.id.addRest_host_name);
        addRest_guest_space = findViewById(R.id.addRest_guest_space);
        addRest_images = findViewById(R.id.addRest_images);

        addRest_button = findViewById(R.id.addRest_button);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading");
        progressDialog.setMessage("Please Wait...");

        storage = FirebaseStorage.getInstance();

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        restRef = firebaseDatabase.getReference("restaurants");

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
                addRest_images.setImageURI(imageUri);
            }
        }
    }
}
