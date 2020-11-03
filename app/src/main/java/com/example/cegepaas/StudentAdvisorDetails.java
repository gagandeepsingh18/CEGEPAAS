package com.example.cegepaas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.cegepaas.Model.AdvisorsPojo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class StudentAdvisorDetails extends AppCompatActivity {
    ImageView image;
    TextView name, email, phoneNumber, campus, department, workingDays, description;
    Button scheduleMeeting, chat;
    String advisorId, advisorName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_advisor_details);
        advisorId = getIntent().getStringExtra("uname");
        advisorName = getIntent().getStringExtra("aname");
        getSupportActionBar().setTitle("Advisor " + advisorName + " Details");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        image = findViewById(R.id.advisorImage);
        name = findViewById(R.id.advisorName);
        email = findViewById(R.id.advisorEmail);
        phoneNumber = findViewById(R.id.advisorPhone);
        campus = findViewById(R.id.advisorCampus);
        department = findViewById(R.id.advisorDepartment);
        workingDays = findViewById(R.id.advisorWD);
        description = findViewById(R.id.advisorDescription);

        scheduleMeeting = findViewById(R.id.ScheduleMeetingButton);
        chat = findViewById(R.id.chatbutton);


        getAdvisorDetails(advisorId);

        scheduleMeeting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StudentAdvisorDetails.this, AdvisorBookingActivity.class);
                intent.putExtra("uname", advisorId);
                startActivity(intent);
            }
        });

        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StudentAdvisorDetails.this, StudentChatActivity.class);
                intent.putExtra("uname", advisorId);
                startActivity(intent);
            }
        });
    }

    private void getAdvisorDetails(final String advisorId) {
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child("Advisor_Details").child(advisorId).exists()) {
                    AdvisorsPojo users = snapshot.child("Advisor_Details").child(advisorId).getValue(AdvisorsPojo.class);
                    Glide.with(getApplicationContext()).load(users.getImage()).into(image);
                    name.setText(users.getName());
                    email.setText(users.getEmail());
                    if (users.getPhoneNumber() == null || users.getPhoneNumber().isEmpty()) {
                        phoneNumber.setText("000 000 0000");
                    } else {
                        phoneNumber.setText(users.getPhoneNumber());
                    }
                    if (users.getCampus() == null || users.getCampus().isEmpty()) {
                        campus.setText("Data Not Found");
                    } else {
                        campus.setText(users.getCampus());
                    }
                    if (users.getDepartment() == null || users.getDepartment().isEmpty()) {
                        department.setText("Data Not Found");
                    } else {
                        department.setText(users.getDepartment());
                    }
                    if (users.getDescription() == null || users.getDescription().isEmpty()) {
                        description.setText("Data Not Found");
                    } else {
                        description.setText(users.getDescription());
                    }
                    if (users.getWorkingDays() == null || users.getWorkingDays().isEmpty()) {
                        workingDays.setText("Data Not Found");
                    } else {
                        workingDays.setText(users.getWorkingDays());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}