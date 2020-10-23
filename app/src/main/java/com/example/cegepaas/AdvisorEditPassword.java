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

public class AdvisorEditPassword extends AppCompatActivity {
    EditText password1, password2;
    TextView student_name, current_pass;
    Button cancel, update_password;
    ImageView ap_profile;
    DatabaseReference adAdvisor;
    private String parentDbName = "Advisor_Details";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advisor_edit_password);
        getSupportActionBar().setTitle("Change Password");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent data = getIntent();

        password1 = findViewById(R.id.ad_new_password);
        password2 = findViewById(R.id.ad_new_password2);
        student_name = findViewById(R.id.advisor_name1);
        current_pass = findViewById(R.id.currentPass1);
        cancel = findViewById(R.id.ad_cancel);
        update_password = findViewById(R.id.advisor_password_edit);
        ap_profile = findViewById(R.id.ps_advisor_image1);

        current_pass.setText(data.getStringExtra("password"));
        student_name.setText(data.getStringExtra("name"));
        SharedPreferences sp = getSharedPreferences("AA", 0);
        String advisorId = sp.getString("auname", "-");
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        adAdvisor = FirebaseDatabase.getInstance().getReference();

        StorageReference profile_pic = storageReference.child("Advisor/" + advisorId + " Profile.jpg");
        profile_pic.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {

                Picasso.get().load(uri).into(ap_profile);
            }
        });

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
                {
                    SharedPreferences sp = getSharedPreferences("AA", 0);
                    String advisorId = sp.getString("auname", "-");
                    adAdvisor = FirebaseDatabase.getInstance().getReference();
                    String pass1 = password1.getText().toString();
                    String pass2 = password2.getText().toString();

                    if (pass1.equals(pass2)) {
                        if (pass1.isEmpty()) {
                            Intent i = new Intent(getApplicationContext(), AdvisorProfileActivity.class);
                            startActivity(i);
                        } else {
                            adAdvisor.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    HashMap<String, Object> passwordDetails = new HashMap<>();
                                    passwordDetails.put("password", pass1);


                                    adAdvisor.child(parentDbName).child(advisorId).updateChildren(passwordDetails)
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
                    } else {
                        Toast.makeText(getApplicationContext(), "Password was not matched!", Toast.LENGTH_SHORT).show();
                    }


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