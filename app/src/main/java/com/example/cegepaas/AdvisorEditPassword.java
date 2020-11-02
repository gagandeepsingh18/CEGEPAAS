package com.example.cegepaas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.cegepaas.Model.AdvisorsPojo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class AdvisorEditPassword extends AppCompatActivity {
    EditText password1, password2;
    TextView student_name, current_pass;
    Button cancel, update_password;
    DatabaseReference adAdvisor;
    String advisorId;
    private String parentDbName = "Advisor_Details";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advisor_edit_password);
        getSupportActionBar().setTitle("Change Password");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        advisorId = getSharedPreferences("AA", 0).getString("auname", "-");
        password1 = findViewById(R.id.ad_new_password);
        password2 = findViewById(R.id.ad_new_password2);
        student_name = findViewById(R.id.advisor_name1);
        current_pass = findViewById(R.id.currentPass1);
        cancel = findViewById(R.id.ad_cancel);
        update_password = findViewById(R.id.advisor_password_edit);


        getCurrentPassword(advisorId);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), AdvisorProfileActivity.class);
                startActivity(i);
            }
        });
        update_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pass1 = password1.getText().toString();
                String pass2 = password2.getText().toString();
                if (!pass1.equals("")) {
                    if (!pass2.equals("")) {
                        if (pass1.equals(pass2)) {
                            updatePassword(advisorId, pass1);
                            Intent i = new Intent(getApplicationContext(), AdvisorProfileActivity.class);
                            startActivity(i);
                        } else {
                            Toast.makeText(getApplicationContext(), "Passwords should match!!", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Enter both passwords", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void updatePassword(String advisorId,String pass1) {
        adAdvisor = FirebaseDatabase.getInstance().getReference();
        adAdvisor.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                HashMap<String, Object> passwordDetails = new HashMap<>();
                passwordDetails.put("password", pass1);

                adAdvisor.child(parentDbName).child(advisorId).updateChildren(passwordDetails);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();


            }

        });
    }

    private void getCurrentPassword(String advisorId) {
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();
        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.child(parentDbName).child(advisorId).exists()){
                    AdvisorsPojo advisor =snapshot.child(parentDbName).child(advisorId).getValue(AdvisorsPojo.class);
                    current_pass.setText(advisor.getPassword());


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