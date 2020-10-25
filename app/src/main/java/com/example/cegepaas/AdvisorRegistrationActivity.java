package com.example.cegepaas;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cegepaas.Model.AdvisorIdsPojo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AdvisorRegistrationActivity extends AppCompatActivity {
    Button btn_register;
    TextInputEditText et_name, et_uname, et_email, et_pwd;
    private ProgressDialog loadingBar;
    ProgressDialog progressDialog;
    String downloadImageUrl;
    private List<AdvisorIdsPojo> mAdvisorIds;
    DatabaseReference dbAdvisors;
    String name, email, password, username;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advisor_reg);

        getSupportActionBar().setTitle("Advisor Registration");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        et_name = findViewById(R.id.et_name);
        et_uname =  findViewById(R.id.et_uname);
        et_email =  findViewById(R.id.et_Email);
        et_pwd =  findViewById(R.id.et_pwd);
        loadingBar = new ProgressDialog(AdvisorRegistrationActivity.this);

        btn_register = (Button) findViewById(R.id.btn_register);
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateAccount();

            }
        });
        getAIDs();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }


    private void CreateAccount() {


        name= et_name.getEditableText().toString();
        email = et_email.getEditableText().toString();
        password = et_pwd.getEditableText().toString();
        username = et_uname.getEditableText().toString();
        downloadImageUrl = "https://firebasestorage.googleapis.com/v0/b/cegepaas.appspot.com/o/Default%2Fprofile.png?alt=media&token=b6e336d0-f12d-4c56-9c53-1c65cfbbb9bc";

        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, "Please write your name...", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Please write your Email...", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(username)) {
            Toast.makeText(this, "Please Choose your Username...", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please write your password...", Toast.LENGTH_SHORT).show();
        } else if (!checkAID()) {
            Toast.makeText(this, "Advisor Id is not authorized...", Toast.LENGTH_SHORT).show();
            return;
        } else {
            Log.d("abc", name);
            Toast.makeText(this, name, Toast.LENGTH_LONG).show();
            ValidateDetails();
        }
    }

    private void ValidateDetails() {
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();
        loadingBar.setTitle("Create Account");
        loadingBar.setMessage("Please wait, while we are checking the credentials.");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (!(dataSnapshot.child("Advisor_Details").child(username).exists())) {
                    HashMap<String, Object> userdataMap = new HashMap<>();
                    userdataMap.put("name", name);
                    userdataMap.put("image", downloadImageUrl);
                    userdataMap.put("email", email);
                    userdataMap.put("username", username);
                    userdataMap.put("password", password);
                    userdataMap.put("status", "inactive");

                    RootRef.child("Advisor_Details").child(username).updateChildren(userdataMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(AdvisorRegistrationActivity.this, "Congratulations, your account has been created.", Toast.LENGTH_SHORT).show();
                                        loadingBar.dismiss();

                                        finish();
                                    } else {
                                        loadingBar.dismiss();
                                        Toast.makeText(AdvisorRegistrationActivity.this, "Network Error: Please try again after some time...", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                } else {
                    Toast.makeText(AdvisorRegistrationActivity.this, "This " + username + " already exists.", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                    Toast.makeText(AdvisorRegistrationActivity.this, "Please try again using another Email.", Toast.LENGTH_SHORT).show();

                    finish();
                }
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    private boolean checkAID() {
        boolean fg = true;
        for (int i = 0; i < mAdvisorIds.size(); i++) {
            if (mAdvisorIds.get(i).getAid().equals(et_uname.getText().toString())) {
                fg = true;
                break;
            } else {
                fg = false;
            }
        }
        return fg;
    }

    private void getAIDs() {
        mAdvisorIds = new ArrayList<>();
        progressDialog = new ProgressDialog(AdvisorRegistrationActivity.this);
        progressDialog.setTitle("Please Wait data is being Loaded");
        progressDialog.show();
        dbAdvisors = FirebaseDatabase.getInstance().getReference("AdvisorIds");
        dbAdvisors.addListenerForSingleValueEvent(valueEventListener1);
    }

    ValueEventListener valueEventListener1 = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            progressDialog.dismiss();
            mAdvisorIds.clear();
            if (dataSnapshot.exists()) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    AdvisorIdsPojo advisor = snapshot.getValue(AdvisorIdsPojo.class);
                    mAdvisorIds.add(advisor);
                }
            } else {
                Toast.makeText(AdvisorRegistrationActivity.this, "No data Found", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            progressDialog.dismiss();
        }
    };

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
