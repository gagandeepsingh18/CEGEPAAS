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
    ImageView profile;
    String password,uId;
    TextView name,email,id;
    Button update_profile, change_password;
    private String parentDbName = "Advisor_Details";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advisor_profile);

        getSupportActionBar().setTitle("Profile");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        profile = findViewById(R.id.advisor_image);
        name = findViewById(R.id.advisor_name);
        email = findViewById(R.id.advisorEmail);
        id = findViewById(R.id.advisorId);
        update_profile = findViewById(R.id.ad_edit_profile);
        change_password = findViewById(R.id.ad_edit_password);
        uId = getIntent().getStringExtra("un");

        getAdvisorDetails(uId);

        update_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),AdvisorEditProfile.class);
                i.putExtra("name",name.getText().toString());
                i.putExtra("email",email.getText().toString());
                i.putExtra("id",uId);
                startActivity(i);
            }
        });
        change_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),AdvisorEditPassword.class);
                i.putExtra("password",password);
                i.putExtra("name",name.getText().toString());
                i.putExtra("id",uId);
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
                    AdvisorsPojo users =snapshot.child(parentDbName).child(auname).getValue(AdvisorsPojo.class);
                    Glide.with(getApplicationContext()).load(users.getImage()).into(profile);
                    name.setText(users.getName());
                    email.setText(users.getEmail());
                    id.setText(users.getUsername());
                    password = users.getPassword();
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