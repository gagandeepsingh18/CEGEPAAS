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

import com.bumptech.glide.Glide;
import com.example.cegepaas.Model.AdvisorsPojo;
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
    String studentId;
    StorageReference storageReference;
    Uri imageUri;
    private String parentDbName = "Student_Details";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_edit_profile);

        getSupportActionBar().setTitle("Edit Profile");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        up_studentEmail = findViewById(R.id.upStudentEmail);
        up_studentId = findViewById(R.id.upStudentId);
        up_studentImage = findViewById(R.id.upStudent_image);
        up_studentName = findViewById(R.id.upStudent_name);
        up_studentSave = findViewById(R.id.upStudentSave);
        up_studentCancel = findViewById(R.id.up_studentCancel);

        storageReference = FirebaseStorage.getInstance().getReference();
        dbStudent = FirebaseDatabase.getInstance().getReference();

        studentId = getSharedPreferences("AA", 0).getString("suname", "-");
        getStudentDetails(studentId);
        up_studentImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadProfilePicture();
            }
        });


        up_studentSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateStudentDetails(studentId);
                startActivity(new Intent(getApplicationContext(),StudentProfileActivity.class));
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

    private void getStudentDetails(String studentId) {
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();
        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(parentDbName).child(studentId).exists()){
                    Users student =snapshot.child(parentDbName).child(studentId).getValue(Users.class);
                    Glide.with(getApplicationContext()).load(student.getDownloadImageUrl()).into(up_studentImage);
                    up_studentEmail.setText(student.getEmail());
                    up_studentId.setText(studentId);
                    up_studentName.setText(student.getName());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void updateStudentDetails(String studentId) {
        dbStudent.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                HashMap<String, Object> userdataMap = new HashMap<>();
                userdataMap.put("email", up_studentEmail.getText().toString());
                userdataMap.put("name", up_studentName.getText().toString());

                dbStudent.child(parentDbName).child(studentId).updateChildren(userdataMap);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void uploadProfilePicture() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == RESULT_OK && data!=null && data.getData()!=null){
            imageUri = data.getData();
            Picasso.get().load(imageUri).into(up_studentImage);
            uploadPicture();
        }
    }

    private void uploadPicture() {
        final String randomKey = UUID.randomUUID().toString();
        final StorageReference reference = storageReference.child("Student/"+randomKey);

        reference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        final String imageUrl= uri.toString();
                        dbStudent.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                HashMap<String, Object> userdataMap = new HashMap<>();
                                userdataMap.put("downloadImageUrl", imageUrl);

                                dbStudent.child(parentDbName).child(studentId).updateChildren(userdataMap);

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                });
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