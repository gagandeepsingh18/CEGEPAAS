package com.example.cegepaas;


import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cegepaas.Model.AdvisorsPojo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AdvisorLoginActivity extends AppCompatActivity {
    Button btn_login;
    EditText et_uname, et_pwd;
    ProgressDialog loadingBar;
    LinearLayout newAdvisor;
    private final String parentDbName = "Advisor_Details";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advisor_login);

        getSupportActionBar().setTitle("Advisor Login");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        et_uname = findViewById(R.id.et_uname);
        et_pwd = findViewById(R.id.et_pwd);
        loadingBar = new ProgressDialog(AdvisorLoginActivity.this);
        newAdvisor = findViewById(R.id.layoutNewAdvisor);

        newAdvisor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdvisorLoginActivity.this, AdvisorRegistrationActivity.class);
                startActivity(intent);
            }
        });

        btn_login =  findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginUser();

            }
        });
    }

    private void LoginUser() {
        String username = et_uname.getText().toString();
        String password = et_pwd.getText().toString();

        if (TextUtils.isEmpty(username)) {
            Toast.makeText(this, "Please write your Username...", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please write your password...", Toast.LENGTH_SHORT).show();
        } else {
            loadingBar.setTitle("Login Account");
            loadingBar.setMessage("Please wait, while we are checking the credentials.");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            AllowAccessToAccount(username, password);
        }
    }

    private void AllowAccessToAccount(final String username, final String password) {

        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(parentDbName).child(username).exists()) {
                    AdvisorsPojo usersData = snapshot.child(parentDbName).child(username).getValue(AdvisorsPojo.class);
                    if (usersData.getUsername().equals(username)) {
                        if (usersData.getPassword().equals(password)) {
                            if (usersData.getStatus().equals("active")) {
                                Toast.makeText(AdvisorLoginActivity.this, "logged in Successfully...", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();

                                SharedPreferences sp = getSharedPreferences("AA", 0);
                                SharedPreferences.Editor et = sp.edit();
                                et.putString("auname", username);
                                et.commit();

                                Intent intent = new Intent(AdvisorLoginActivity.this, AdvisorHomeActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                loadingBar.dismiss();
                                Toast.makeText(AdvisorLoginActivity.this, "Your account is not actived,please contact admin...", Toast.LENGTH_SHORT).show();
                            }
                        }
                    } else {
                        loadingBar.dismiss();
                        Toast.makeText(AdvisorLoginActivity.this, "Password is incorrect", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(AdvisorLoginActivity.this, "Account with this " + username + " number do not exists.", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
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
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}