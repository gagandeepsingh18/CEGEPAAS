package com.example.cegepaas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.cegepaas.Model.Users;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class StudentProfileActivity extends AppCompatActivity {
    String imageUrl, studentName, studentEmail, studentId;
    ImageView profile;
    TextView name, email, id;
    DatabaseReference dbStudent;
    private String parentDbName = "Student_Details";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_profile);

        getSupportActionBar().setTitle("Profile");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        profile = findViewById(R.id.student_image);
        name = findViewById(R.id.student_name);
        email = findViewById(R.id.studentEmail);
        id = findViewById(R.id.studentId);
        SharedPreferences sp = getSharedPreferences("AA", 0);
        getUserDetails(sp.getString("suname", "-"));
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