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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cegepaas.Model.Users;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * StudentLoginActivity is used to verify and login the user with his/her registered details.
 */
public class StudentLoginActivity extends AppCompatActivity {

    Button btn_login;
    EditText et_uname, et_pwd;
    ProgressDialog loadingBar;
    LinearLayout newStudent;
    private final String parentDbName = "Student_Details";
    int numberOfAttempts =0;

    /**
     * onCreate method is the main method that will trigger when the activity starts.
     * @param savedInstanceState Bundle object.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_login);

        getSupportActionBar().setTitle("Student Login");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        et_uname =  findViewById(R.id.et_uname);
        et_pwd =  findViewById(R.id.et_pwd);
        loadingBar = new ProgressDialog(StudentLoginActivity.this);

        newStudent = findViewById(R.id.layoutNewStudent);

        newStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StudentLoginActivity.this, StudentRegistrationActivity.class);
                startActivity(intent);

            }
        });

        btn_login = findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                  LoginUser();
            }
        });

    }

    /**
     * This method is used to login the user while checking the input fields were empty or not.
     */
    private void LoginUser() {
        String username = et_uname.getText().toString();
        String password = et_pwd.getText().toString();
        if(numberOfAttempts<=3){

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
        }}else{
            Toast.makeText(this,"4 failure attempts, please try again later", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * This method verifies the user detail.
     * @param username User Id.
     * @param password User Password.
     */
    private void AllowAccessToAccount(final String username, final String password) {

        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(parentDbName).child(username).exists()) {
                    Users usersData = snapshot.child(parentDbName).child(username).getValue(Users.class);
                    if (usersData.getUsername().equals(username)) {
                        if (usersData.getPassword().equals(password)) {
                            Toast.makeText(StudentLoginActivity.this, "logged in Successfully...", Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();
                            numberOfAttempts=0;
                            Intent intent = new Intent(StudentLoginActivity.this, StudentHomeScreenActivity.class);
                            SharedPreferences sp = getSharedPreferences("AA", 0);
                            SharedPreferences.Editor et = sp.edit();
                            et.putString("suname", username);
                            et.commit();
                            startActivity(intent);
                            finish();
                        } else {
                            numberOfAttempts++;
                            loadingBar.dismiss();
                            Toast.makeText(StudentLoginActivity.this, "Password is incorrect, you have "+(4-numberOfAttempts)+" attempts left", Toast.LENGTH_SHORT).show();
                    }}

                } else {
                    Toast.makeText(StudentLoginActivity.this, "Account with this " + username + " does not exist.", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    /**
     * used to have the back button in that particular activity
     * @param item selected menu item.
     * @return returns to the home page.
     */
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
