package com.example.cegepaas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cegepaas.Model.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
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

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.UUID;

public class StudentEditProfile extends AppCompatActivity {
    EditText up_studentEmail, up_studentName;
    ImageView up_studentImage;
    TextView up_studentId;
    Button up_studentSave, up_studentCancel;
    DatabaseReference dbStudent;
    StorageReference storageReference;
    private String parentDbName = "Student_Details";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_edit_profile);
        getSupportActionBar().setTitle("Edit Profile");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent data = getIntent();
        up_studentEmail = findViewById(R.id.upStudentEmail);
        up_studentId = findViewById(R.id.upStudentId);
        up_studentImage = findViewById(R.id.upStudent_image);
        up_studentName = findViewById(R.id.upStudent_name);
        up_studentSave = findViewById(R.id.upStudentSave);
        up_studentCancel = findViewById(R.id.up_studentCancel);

        up_studentName.setText(data.getStringExtra("name"));
        up_studentEmail.setText(data.getStringExtra("email"));
        up_studentId.setText( data.getStringExtra("id"));
        up_studentEmail.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                up_studentEmail.onTouchEvent(event);
                up_studentEmail.setSelection(up_studentEmail.getText().length());
                return true;
            }

        });
        up_studentName.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                up_studentName.onTouchEvent(event);
                up_studentName.setSelection(up_studentName.getText().length());
                return true;
            }

        });

        storageReference = FirebaseStorage.getInstance().getReference();
        dbStudent = FirebaseDatabase.getInstance().getReference();
        SharedPreferences sp = getSharedPreferences("AA", 0);
        String studentId = sp.getString("suname", "-");
        StorageReference profile_pic = storageReference.child("Student/" + studentId + " Profile.jpg");
        profile_pic.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {

                Picasso.get().load(uri).into(up_studentImage);
            }
        });


        up_studentImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, 1000);

            }
        });


        up_studentSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (up_studentEmail.getText().toString().isEmpty()  || up_studentName.getText().toString().isEmpty()) {
                    Toast.makeText(StudentEditProfile.this, "One or more fields are empty", Toast.LENGTH_SHORT).show();
                } else {
                    dbStudent.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            HashMap<String, Object> hashMap = new HashMap();
                            hashMap.put("email", up_studentEmail.getText().toString());
                            hashMap.put("name", up_studentName.getText().toString());
                            dbStudent.child(parentDbName).child(up_studentId.getText().toString()).updateChildren(hashMap)
                                    .addOnCompleteListener(new OnCompleteListener() {
                                        @Override
                                        public void onComplete(@NonNull Task task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(StudentEditProfile.this, "Successfully updated", Toast.LENGTH_SHORT).show();
                                                Intent i = new Intent(getApplicationContext(), StudentProfileActivity.class);
                                                startActivity(i);
                                            } else {
                                                Toast.makeText(StudentEditProfile.this, "Network Error: Please try again after some time...", Toast.LENGTH_SHORT).show();

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

        up_studentCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), StudentProfileActivity.class);
                startActivity(i);
            }
        });


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000) {
            if (resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
                Uri imageuri = data.getData();
                up_studentImage.setImageURI(imageuri);
                uploadImageToFireBase(imageuri);

            }
        }
    }

    public void uploadImageToFireBase(Uri imageUri) {
        Users users = new Users();
        storageReference = FirebaseStorage.getInstance().getReference();
        dbStudent = FirebaseDatabase.getInstance().getReference();
        SharedPreferences sp = getSharedPreferences("AA", 0);
        String studentId = sp.getString("suname", "-");
        final StorageReference imageUpload = storageReference.child("Student/" + studentId + " Profile.jpg");

        imageUpload.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imageUpload.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri).into(up_studentImage);

                        String image = uri.toString();
                        dbStudent.child(parentDbName).child(studentId).child("downloadImageUrl").setValue(image);
//                    users.setDownloadImageUrl(image);


                        dbStudent.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (!(dataSnapshot.child("Student_Details").child(studentId).exists())) {
                                    HashMap<String, Object> userdataMap = new HashMap<>();

                                    userdataMap.put("downloadImageUrl", users.getDownloadImageUrl());
                                    dbStudent.child(parentDbName).child(studentId).updateChildren(userdataMap);
                                }
//

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });


                    }
                });




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