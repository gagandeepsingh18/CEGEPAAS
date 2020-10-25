package com.example.cegepaas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cegepaas.Model.AdvisorsPojo;
import com.example.cegepaas.Model.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.Nullable;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class AdvisorEditProfile extends AppCompatActivity {
    EditText ap_email, ap_name;
    TextView ap_id;
    ImageView ap_profile;
    Button cancel, save;
    DatabaseReference dbAdvisor;
    StorageReference storageReference;
    private String parentDbName = "Advisor_Details";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advisor_edit_profile);
        Intent data = getIntent();

        getSupportActionBar().setTitle("Edit Profile");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ap_email = findViewById(R.id.advisorEmail);
        ap_name = findViewById(R.id.upAdvisor_name);
        ap_id = findViewById(R.id.upAdvisorId);
        ap_profile = findViewById(R.id.upAdvisor_image);
        cancel = findViewById(R.id.up_AdvisorCancel);
        save = findViewById(R.id.up_AdvisorSave);

        SharedPreferences sp = getSharedPreferences("AA", 0);
        String advisorId = data.getStringExtra("id");
        ap_id.setText(advisorId);
        ap_email.setText(data.getStringExtra("email"));
        ap_name.setText(data.getStringExtra("name"));

        ap_email.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ap_email.onTouchEvent(event);
                ap_email.setSelection(ap_email.getText().length());
                return true;
            }

        });
        storageReference = FirebaseStorage.getInstance().getReference();
        dbAdvisor = FirebaseDatabase.getInstance().getReference();

        StorageReference profile_pic = storageReference.child("Advisor/" + advisorId + " Profile.jpg");
        profile_pic.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {

                Picasso.get().load(uri).into(ap_profile);
            }
        });

        ap_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, 1000);
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), AdvisorProfileActivity.class);
                i.putExtra("un", data.getStringExtra("id"));
                startActivity(i);
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ap_email.getText().toString().isEmpty() || ap_name.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "One or more fields are empty", Toast.LENGTH_SHORT).show();
                } else {
                    dbAdvisor.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            HashMap<String, Object> hashMap = new HashMap();
                            hashMap.put("email", ap_email.getText().toString());
                            hashMap.put("name", ap_name.getText().toString());
                            dbAdvisor.child(parentDbName).child(advisorId).updateChildren(hashMap)
                                    .addOnCompleteListener(new OnCompleteListener() {
                                        @Override
                                        public void onComplete(@NonNull Task task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(getApplicationContext(), "Successfully updated", Toast.LENGTH_SHORT).show();
                                                Intent i = new Intent(getApplicationContext(), AdvisorProfileActivity.class);
                                                startActivity(i);
                                            } else {
                                                Toast.makeText(getApplicationContext(), "Network Error: Please try again after some time...", Toast.LENGTH_SHORT).show();

                                            }
                                        }
                                    });

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();


                        }

                    });


                }
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000) {
            if (resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
                Uri imageuri = data.getData();
                ap_profile.setImageURI(imageuri);
                uploadImageToFireBase(imageuri);

            }
        }
    }

    public void uploadImageToFireBase(Uri imageUri) {
        AdvisorsPojo users = new AdvisorsPojo();
        storageReference = FirebaseStorage.getInstance().getReference();
        dbAdvisor = FirebaseDatabase.getInstance().getReference();
        String advisorId = getIntent().getStringExtra("id");
        final StorageReference imageUpload = storageReference.child("Advisor/" + advisorId + " Profile.jpg");
        imageUpload.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imageUpload.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri).into(ap_profile);
                        String image = uri.toString();
                        dbAdvisor.child(parentDbName).child(advisorId).child("image").setValue(image);
                        dbAdvisor.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (!(dataSnapshot.child(parentDbName).child(advisorId).exists())) {
                                    HashMap<String, Object> userdataMap = new HashMap<>();

                                    userdataMap.put("image", users.getImage());
                                    dbAdvisor.child(parentDbName).child(advisorId).updateChildren(userdataMap);
                                }
//

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });


                    }
                });

//                DatabaseReference db = dbStudent.child(parentDbName).child(studentId).child("downloadImageUrl").push();
//                users.setDownloadImageUrl("jksf ");
//                db.setValue(users);
//                String key = db.getKey();
//                users.setUsername(key);


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}