package com.example.cegepaas;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.annotation.GlideModule;
import com.example.cegepaas.Model.Users;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class StudentProfileActivity extends AppCompatActivity {
    String imageUrl, studentName, studentEmail, studentId,password;
    ImageView profile;
    TextView name, email, id;
    Button edit_profile, edit_password;
    DatabaseReference dbStudent;
    private String parentDbName = "Student_Details";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_profile);

        getSupportActionBar().setTitle("Profile");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent data = getIntent();
        profile = findViewById(R.id.student_image);
        name = findViewById(R.id.student_name);
        email = findViewById(R.id.studentEmail);
        id = findViewById(R.id.studentId);
        edit_profile = findViewById(R.id.student_profile_edit);
        edit_password = findViewById(R.id.up_studentPass);

        SharedPreferences sp = getSharedPreferences("AA", 0);
        getUserDetails(sp.getString("suname", "-"));
        edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(),StudentEditProfile.class);
                i.putExtra("name",name.getText().toString());
                i.putExtra("email",email.getText().toString());
                i.putExtra("id",id.getText().toString());
                startActivity(i);

            }
        });
        edit_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(),StudentEditPassword.class);
                i.putExtra("name",name.getText().toString());
                i.putExtra("password",password);
                startActivity(i);


            }
        });


    }



    private void getUserDetails(String suname) {
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();
        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.child(parentDbName).child(suname).exists()) {
                    Users users = snapshot.child(parentDbName).child(suname).getValue(Users.class);
                    Glide.with(getApplicationContext()).load(users.getDownloadImageUrl()).into(profile);
                    name.setText(users.getName());
                    email.setText(users.getEmail());
                    id.setText(users.getUsername());
                    password = users.getPassword();


                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
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