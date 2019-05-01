package com.letshomeco.hpnotebook.letshome.activities;

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
import com.letshomeco.hpnotebook.letshome.R;
import com.letshomeco.hpnotebook.letshome.modelClasses.ExperienceListing;
import com.letshomeco.hpnotebook.letshome.modelClasses.ListingRating;
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

public class AddExperienceActivity extends AppCompatActivity {

    EditText addExpr_title, addExpr_location, addExpr_pricing, addExpr_host_name,
            addExpr_guest_space, addExpr_description;
    ImageView addExpr_images;
    Button addExpr_button;
    private int Pick_image = 1;
    private Uri imageUri;

    FirebaseAuth auth;
    FirebaseDatabase firebaseDatabase;
    FirebaseUser user;
    float expr_avgRating;
    DatabaseReference ref, exprRatingRef;
    FirebaseStorage storage;
    StorageReference imageRef;
    String exprId;
    String key;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_experience);

        init();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            exprId = bundle.getString("exprId");

            ref.child(exprId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    ExperienceListing experienceListing = dataSnapshot.getValue(ExperienceListing.class);
                    if (experienceListing != null) {
                        expr_avgRating = experienceListing.getListing_average_rating();
                    }
                    addExpr_title.setText(experienceListing.getListing_title());
                    addExpr_location.setText(experienceListing.getListing_location());

                    addExpr_pricing.setText(experienceListing.getListing_pricing());

                    Glide.with(AddExperienceActivity.this)
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
                String userId = user.getUid();
                String title = addExpr_title.getText().toString();
                String location = addExpr_location.getText().toString();
                String pricing = addExpr_pricing.getText().toString();
                String host_name = addExpr_host_name.getText().toString();
                String description = addExpr_description.getText().toString();

                key = exprId;
                if (exprId == null) {
                    key = ref.push().getKey();
                }
                imageRef = storage.getReference("experience listing images/" + key);

                addExpr(key, userId, title, location, pricing, host_name, description);
            }
        });
    }

    private void addExpr(final String exprId, final String userId, final String title, final String location, final String pricing,
                         final String host_name, final String description) {

        final ProgressDialog pd = new ProgressDialog(this);
        pd.setTitle("Uploading");
        pd.setMessage("Please Wait...");
        pd.show();

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

                            exprRatingRef = ref.child(exprId).child("exprRating");

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

                            ExperienceListing experienceListing = new ExperienceListing(exprId, userId, title, location, pricing,
                                    host_name, description, imageUrl);
                            ref.child(exprId).setValue(experienceListing);
                            ref.child(exprId).child("avgRating").setValue(expr_avgRating);

                            pd.dismiss();

                            Toast.makeText(AddExperienceActivity.this, "Listing added", Toast.LENGTH_LONG).show();

                            startActivity(new Intent(AddExperienceActivity.this, MainActivity.class));

                            overridePendingTransition(R.anim.slide_in, R.anim.slide_out);

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
        addExpr_description = findViewById(R.id.addExpr_description);
        addExpr_images = findViewById(R.id.addExpr_images);

        addExpr_button = findViewById(R.id.addExpr_button);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading");
        progressDialog.setMessage("Please Wait...");

        storage = FirebaseStorage.getInstance();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        // exprRef = firebaseDatabase.getReference("experiences");
        ref = firebaseDatabase.getReference("experiences");

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
