package com.example.cegepaas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class StudentEditPassword extends AppCompatActivity {
    EditText password1, password2;
    TextView student_name, current_pass;
    Button cancel, update_password;
    ImageView up_studentImage;
    DatabaseReference dbStudent;
    private String parentDbName = "Student_Details";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_edit_password);
        getSupportActionBar().setTitle("Change Password");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent data = getIntent();
        current_pass = findViewById(R.id.currentPass);
        student_name = findViewById(R.id.student_name1);
        password1 = findViewById(R.id.new_password);
        password2 = findViewById(R.id.new_password2);
        cancel = findViewById(R.id.p_cancel);
        update_password = findViewById(R.id.student_password_edit);
        up_studentImage = findViewById(R.id.student_image1);

        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        dbStudent = FirebaseDatabase.getInstance().getReference();
        SharedPreferences sp = getSharedPreferences("AA", 0);
        String studentId = sp.getString("suname", "-");
        StorageReference profile_pic = storageReference.child("Student/" + studentId + " Profile.jpg");
        //To get the image from the FireStore and set it into the imageView.
        profile_pic.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {

                Picasso.get().load(uri).into(up_studentImage);
            }
        });

        current_pass.setText(data.getStringExtra("password"));
        student_name.setText(data.getStringExtra("name"));
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), StudentProfileActivity.class);
                startActivity(i);
            }
        });

        update_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sp = getSharedPreferences("AA", 0);
                String studentId = sp.getString("suname", "-");
                dbStudent = FirebaseDatabase.getInstance().getReference();
                String pass1 = password1.getText().toString();
                String pass2 = password2.getText().toString();
                if (pass1.equals(pass2)) {
                    if (pass1.isEmpty()) {
                        Intent i = new Intent(getApplicationContext(), AdvisorProfileActivity.class);
                        startActivity(i);
                    } else {
                        dbStudent.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                HashMap<String, Object> passwordDetails = new HashMap<>();
                                passwordDetails.put("password", pass1);


                                dbStudent.child(parentDbName).child(studentId).updateChildren(passwordDetails)
                                        .addOnCompleteListener(new OnCompleteListener() {
                                            @Override
                                            public void onComplete(@NonNull Task task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(StudentEditPassword.this, "Successfully updated", Toast.LENGTH_SHORT).show();
                                                    Intent i = new Intent(getApplicationContext(), StudentProfileActivity.class);
                                                    startActivity(i);
                                                } else {
                                                    Toast.makeText(StudentEditPassword.this, "Network Error: Please try again after some time...", Toast.LENGTH_SHORT).show();

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

                } else {
                    Toast.makeText(getApplicationContext(), "Password was not matched!", Toast.LENGTH_SHORT).show();
                }


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