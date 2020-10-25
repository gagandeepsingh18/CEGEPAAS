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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.cegepaas.Model.AdvisorsPojo;
import com.example.cegepaas.Model.Users;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AdvisorProfileActivity extends AppCompatActivity {
    ImageView img_profile;
    TextView txt_name,txt_email,txt_advisorid,txt_phoneNumber,txt_department,txt_workingDays,txt_campus,txt_description;
    Button update_profile, change_password;
    String advisorid;
    private String parentDbName = "Advisor_Details";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advisor_profile);

        getSupportActionBar().setTitle("Profile");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        img_profile = findViewById(R.id.advisor_image);
        txt_name = findViewById(R.id.advisor_name);
        txt_email = findViewById(R.id.advisorEmail);
        txt_advisorid = findViewById(R.id.advisorId);
        txt_department = findViewById(R.id.advisorDepartment);
        txt_campus = findViewById(R.id.advisorCampus);
        txt_phoneNumber = findViewById(R.id.advisorPhone);
        txt_workingDays = findViewById(R.id.advisorWorkingdays);
        txt_description = findViewById(R.id.advisorDescription);

        update_profile = findViewById(R.id.ad_edit_profile);
        change_password = findViewById(R.id.ad_edit_password);

        advisorid = getSharedPreferences("AA",0).getString("auname","-");
        getAdvisorDetails(advisorid);

        update_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),AdvisorEditProfile.class);
                startActivity(i);
            }
        });
        change_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),AdvisorEditPassword.class);
                startActivity(i);
            }
        });

    }

    private void getAdvisorDetails(final String auname) {
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();
        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.child(parentDbName).child(auname).exists()){
                    AdvisorsPojo advisor =snapshot.child(parentDbName).child(auname).getValue(AdvisorsPojo.class);
                    Glide.with(getApplicationContext()).load(advisor.getImage()).into(img_profile);
                    txt_name.setText(advisor.getName());
                    txt_advisorid.setText(advisor.getUsername());
                    txt_campus.setText(advisor.getCampus());
                    txt_department.setText(advisor.getDepartment());
                    txt_email.setText(advisor.getEmail());

                    if(!advisor.getPhoneNumber().equals("")){
                        txt_phoneNumber.setText("+1 "+advisor.getPhoneNumber());
                    }else {
                        txt_phoneNumber.setText("+1 *** *** ****");
                    }

                    if(!advisor.getWorkingDays().equals("")){
                        txt_workingDays.setText(advisor.getWorkingDays());
                    }else {
                        txt_workingDays.setText("Data Not Found");
                    }

                    if(!advisor.getDescription().equals("")){
                        txt_description.setText(advisor.getDescription());
                    }else {
                        txt_description.setText("Data Not Found");
                    }


                }else{
                    Toast.makeText(getApplicationContext(),"Something went wrong",Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_SHORT).show();
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