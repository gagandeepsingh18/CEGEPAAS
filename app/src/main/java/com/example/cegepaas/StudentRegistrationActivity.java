package com.example.cegepaas;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cegepaas.Model.StudentIdsPojo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class StudentRegistrationActivity extends AppCompatActivity {
    Button btn_register;
    EditText et_name,et_uname,et_eMail,et_pwd;
    private ProgressDialog loadingBar;
    private List<StudentIdsPojo> mStudentIds;
    DatabaseReference dbStudents;
    ProgressDialog progressDialog;
    String imageUrl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_reg);

        getSupportActionBar().setTitle("Student Registration");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        et_name=(EditText) findViewById(R.id.et_name);
        et_uname=(EditText) findViewById(R.id.et_uname);
        et_eMail=(EditText) findViewById(R.id.et_eMail);
        et_pwd=(EditText) findViewById(R.id.et_pwd);
        loadingBar = new ProgressDialog(StudentRegistrationActivity.this);

        btn_register=(Button) findViewById(R.id.btn_register);
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateAccount();

            }
        });
        getSIDs();
    }

    private void getSIDs(){
        mStudentIds =new ArrayList<>();

        progressDialog=new ProgressDialog(StudentRegistrationActivity.this);
        progressDialog.setTitle("Please Wait data is being Loaded");
        progressDialog.show();

        dbStudents = FirebaseDatabase.getInstance().getReference("StudentIds");
        dbStudents.addListenerForSingleValueEvent(valueEventListener);
    }

    ValueEventListener valueEventListener =new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            progressDialog.dismiss();
            mStudentIds.clear();
            if(dataSnapshot.exists()){
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    StudentIdsPojo student = snapshot.getValue(StudentIdsPojo.class);
                    mStudentIds.add(student);
                }
            }
            else {
                Toast.makeText(StudentRegistrationActivity.this, "No data Found", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
            progressDialog.dismiss();
        }
    };

    private void CreateAccount() {

        String name = et_name.getText().toString();
        String eMail = et_eMail.getText().toString();
        String password = et_pwd.getText().toString();
        String username = et_uname.getText().toString();
        imageUrl = "https://firebasestorage.googleapis.com/v0/b/cegepaas.appspot.com/o/Default%2Fprofile.png?alt=media&token=b6e336d0-f12d-4c56-9c53-1c65cfbbb9bc";
        if (TextUtils.isEmpty(name))
        {
            Toast.makeText(this, "Please write your name...", Toast.LENGTH_SHORT).show();
            return;
        }
        else if (TextUtils.isEmpty(eMail))
        {
            Toast.makeText(this, "Please write your eMail...", Toast.LENGTH_SHORT).show();
            return;
        }
        else if (TextUtils.isEmpty(username))
        {
            Toast.makeText(this, "Please Choose your Username...", Toast.LENGTH_SHORT).show();
            return;
        }
        else if (TextUtils.isEmpty(password))
        {
            Toast.makeText(this, "Please write your password...", Toast.LENGTH_SHORT).show();
            return;
        }else if(!checkSID()){
            Toast.makeText(this, "Student Id is not authorized...", Toast.LENGTH_SHORT).show();
            return;
        }
        else
        {
            loadingBar.setTitle("Create Account");
            loadingBar.setMessage("Please wait, while we are checking the credentials.");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            ValidatepEmail(name, eMail, username,password);
        }
    }

    private boolean checkSID(){
        boolean fg=true;
        for(int i=0;i<mStudentIds.size();i++){
            if(mStudentIds.get(i).getSid().equals(et_uname.getText().toString())){
                fg=true;
                break;
            }else{
                fg=false;
            }
        }
        return  fg;
    }

    private void ValidatepEmail(final String name, final String eMail, final String username, final String password) {
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (!(dataSnapshot.child("Student_Details").child(username).exists()))
                {
                    HashMap<String, Object> userdataMap = new HashMap<>();
                    userdataMap.put("username", username);
                    userdataMap.put("email", eMail);
                    userdataMap.put("password", password);
                    userdataMap.put("name", name);
                    userdataMap.put("downloadImageUrl", imageUrl);

                    RootRef.child("Student_Details").child(username).updateChildren(userdataMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task)
                                {
                                    if (task.isSuccessful())
                                    {
                                        Toast.makeText(StudentRegistrationActivity.this, "Congratulations, your account has been created.", Toast.LENGTH_SHORT).show();
                                        loadingBar.dismiss();

                                        Intent intent = new Intent(StudentRegistrationActivity.this, StudentLoginActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                    else
                                    {
                                        loadingBar.dismiss();
                                        Toast.makeText(StudentRegistrationActivity.this, "Network Error: Please try again after some time...", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                }
                else
                {
                    Toast.makeText(StudentRegistrationActivity.this, "This " + username + " already exists.", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                    Toast.makeText(StudentRegistrationActivity.this, "Please try again using another Email.", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

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
