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

public class AddHomeActivity extends AppCompatActivity {

    EditText addHome_title, addHome_location, addHome_pricing, addHome_host_name,
            addHome_guest_space, addHome_room, addHome_bedrooms, addHome_bathroom;
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
        setContentView(R.layout.activity_add_home);

        init();

        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            homeId = bundle.getString("homeId");

            homeRef.child(homeId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    HomeListing homeListing = dataSnapshot.getValue(HomeListing.class);
                    if (homeListing != null) {
                        home_avgRating = homeListing.getListing_average_rating();
                    }
                    addHome_title.setText(homeListing.getListing_title());
                    addHome_location.setText(homeListing.getListing_location());

                    addHome_pricing.setText(homeListing.getListing_pricing());

                    Glide
                            .with(AddHomeActivity.this)
                            .load(homeListing.getListing_image())
                            .into(addHome_images);
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
        }

        addHome_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userId = user.getUid();
                String title = addHome_title.getText().toString();
                String location = addHome_location.getText().toString();
                String pricing = addHome_pricing.getText().toString();
                String host_name = addHome_host_name.getText().toString();
                String guest_space = addHome_guest_space.getText().toString();
                String rooms = addHome_room.getText().toString();
                String bedrooms = addHome_bedrooms.getText().toString();
                String bathroom = addHome_bathroom.getText().toString();

                key=homeId;

                if (homeId == null) {
                    key = homeRef.push().getKey();
                }

                imageRef = storage.getReference("home listing images/" + key);

                addHome(key, userId, title, location, pricing, host_name, guest_space, rooms, bedrooms, bathroom);
            }
        });
    }

    private void addHome(final String homeId, final String userId, final String title, final String location, final String pricing,
                         final String host_name, final String guest_space,
                         final String rooms, final String bedrooms, final String bathroom) {

        final ProgressDialog pd = new ProgressDialog(this);
        pd.setTitle("Uploading");
        pd.setMessage("Please Wait...");
        pd.show();

        BitmapDrawable drawable = (BitmapDrawable) addHome_images.getDrawable();
        Bitmap bitmap = drawable.getBitmap();

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] bytes = stream.toByteArray();

        imageRef.putBytes(bytes).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                imageRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if ((task.isSuccessful())) {
                            String imageUrl = task.getResult().toString();

                            homeRatingRef=homeRef.child(homeId).child("homeRating");

                            if (homeRatingRef!=null) {
                                homeRatingRef.addChildEventListener(new ChildEventListener() {
                                    @Override
                                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                                        ListingRating listingRating = dataSnapshot.getValue(ListingRating.class);
                                        String ratingKey = dataSnapshot.getKey();
                                        homeRatingRef.child(ratingKey).setValue(listingRating);

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

                            HomeListing homeListing = new HomeListing(homeId, userId, title, location, pricing,
                                    host_name, guest_space, rooms, bedrooms, bathroom, imageUrl);
                            homeRef.child(homeId).setValue(homeListing);
                            homeRef.child(homeId).child("avgRating").setValue(home_avgRating);

                            pd.dismiss();

                            Toast.makeText(AddHomeActivity.this, "Listing added", Toast.LENGTH_LONG).show();

                            startActivity(new Intent(AddHomeActivity.this, MainActivity.class));

                            finish();
                        }
                    }
                });
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                pd.setMessage((int) progress + "% Uploaded");


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AddHomeActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void init() {

        addHome_title = findViewById(R.id.addHome_title);
        addHome_location = findViewById(R.id.addHome_location);
        addHome_pricing = findViewById(R.id.addHome_pricing);
        addHome_host_name = findViewById(R.id.addHome_host_name);
        addHome_guest_space = findViewById(R.id.addHome_guest_space);
        addHome_room = findViewById(R.id.addHome_room);
        addHome_bedrooms = findViewById(R.id.addHome_bedrooms);
        addHome_bathroom = findViewById(R.id.addHome_bathroom);
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
