package com.example.hpnotebook.letshome.activities;

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
import com.example.hpnotebook.letshome.modelClasses.ExperienceListing;
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
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

public class AddExperienceActivity extends AppCompatActivity {

    EditText addExpr_title, addExpr_location, addExpr_pricing, addExpr_host_name,
            addExpr_guest_space;
    ImageView addExpr_images;
    Button addExpr_button;
    private int Pick_image = 1;
    private Uri imageUri;

    FirebaseAuth auth;
    FirebaseDatabase firebaseDatabase;
    FirebaseUser user;
    float expr_avgRating;
    DatabaseReference exprRef, exprRatingRef;
    FirebaseStorage storage;
    StorageReference imageRef;
    String exprId;
    String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_experience);

        init();


        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            exprId = bundle.getString("exprId");

            exprRef.child(exprId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    ExperienceListing experienceListing = dataSnapshot.getValue(ExperienceListing.class);
                    if (experienceListing != null) {
                        expr_avgRating = experienceListing.getListing_average_rating();
                    }
                    addExpr_title.setText(experienceListing.getListing_title());
                    addExpr_location.setText(experienceListing.getListing_location());

                    addExpr_pricing.setText(experienceListing.getListing_pricing());

                    Glide
                            .with(AddExperienceActivity.this)
                            .load(experienceListing.getListing_image())
                            .into(addExpr_images);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
        }

        addExpr_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = addExpr_title.getText().toString();
                String location = addExpr_location.getText().toString();
                String pricing = addExpr_pricing.getText().toString();

                key = exprId;
                if (exprId == null) {
                    key = exprRef.push().getKey();
                }
                imageRef = storage.getReference("experience listing images/" + key);

                addExpr(title, location, pricing, key);
            }
        });
    }

    private void addExpr(final String title, final String location, final String pricing, final String exprId) {

        BitmapDrawable drawable = (BitmapDrawable) addExpr_images.getDrawable();
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

                            exprRatingRef = exprRef.child(exprId).child("exprRating");

                            if (exprRatingRef != null) {
                                exprRatingRef.addChildEventListener(new ChildEventListener() {
                                    @Override
                                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                                        ListingRating listingRating = dataSnapshot.getValue(ListingRating.class);
                                        String ratingKey = dataSnapshot.getKey();
                                        exprRatingRef.child(ratingKey).setValue(listingRating);

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

                            ExperienceListing experienceListing = new ExperienceListing(exprId, title, location, pricing, imageUrl);
                            exprRef.child(exprId).setValue(experienceListing);
                            exprRef.child(exprId).child("avgRating").setValue(expr_avgRating);
                            Toast.makeText(AddExperienceActivity.this, "Listing added", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(AddExperienceActivity.this, MainActivity.class));
                            finish();

                        }
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AddExperienceActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void init() {

        addExpr_title = findViewById(R.id.addExpr_title);
        addExpr_location = findViewById(R.id.addExpr_location);
        addExpr_pricing = findViewById(R.id.addExpr_pricing);
        addExpr_host_name = findViewById(R.id.addExpr_host_name);
        addExpr_guest_space = findViewById(R.id.addExpr_guest_space);
        addExpr_images = findViewById(R.id.addExpr_images);

        addExpr_button = findViewById(R.id.addExpr_button);

        storage = FirebaseStorage.getInstance();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        exprRef = firebaseDatabase.getReference("experiences");

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
                addExpr_images.setImageURI(imageUri);
            }
        }
    }
}
