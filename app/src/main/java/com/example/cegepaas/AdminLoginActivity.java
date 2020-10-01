package com.example.cegepaas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cegepaas.Model.AdminPojo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class AdminLoginActivity extends AppCompatActivity {
    Button btn_login;
    EditText et_uname,et_pwd;
    ProgressDialog loadingBar;
    private String parentDbName = "Admin_Details";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);
        getSupportActionBar().setTitle("Admin Login");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        et_uname=(EditText)findViewById(R.id.et_uname);
        et_pwd=(EditText)findViewById(R.id.et_pwd);
        loadingBar=new ProgressDialog(AdminLoginActivity.this);

        btn_login=(Button)findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Toast.makeText(getApplicationContext(),"comming Soon...",Toast.LENGTH_LONG).show();
                LoginUser();
            }
        });
    }
    private void LoginUser() {
        String username = et_uname.getText().toString();
        String password = et_pwd.getText().toString();

        if (TextUtils.isEmpty(username))
        {
            Toast.makeText(this, "Please write your Username...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(password))
        {
            Toast.makeText(this, "Please write your password...", Toast.LENGTH_SHORT).show();
        }
        else {
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
                if (snapshot.child(parentDbName).child(username).exists()){
                    AdminPojo adminPojo = snapshot.child(parentDbName).child(username).getValue(AdminPojo.class);
                    if(adminPojo.getUname().equals(username)){
                        if(adminPojo.getPwd().equals(password)){
                            Toast.makeText(AdminLoginActivity.this, "logged in Successfully...", Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();

                            Intent intent = new Intent(AdminLoginActivity.this, AdminDashboardActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                    else {
                        loadingBar.dismiss();
                        Toast.makeText(AdminLoginActivity.this, "Password is incorrect", Toast.LENGTH_SHORT).show();
                    }

                }
                else
                {
                    Toast.makeText(AdminLoginActivity.this, "Account with this " + username + " do not exists.", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                loadingBar.dismiss();
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