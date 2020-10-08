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

import com.example.cegepaas.Model.AdvisorIdsPojo;
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

public class AdvisorRegistrationActivity extends AppCompatActivity {
    Button btn_register,btn_img_upload;;
    EditText et_name,et_uname,et_Email,et_pwd;
    private ProgressDialog loadingBar;
    ProgressDialog progressDialog;
    private List<AdvisorIdsPojo> mAdvisorIds;
    DatabaseReference dbAdvisors;
    String name,email,password,username;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advisor_reg);

        getSupportActionBar().setTitle("Advisor Registration");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        et_name=(EditText) findViewById(R.id.et_name);
        et_uname=(EditText) findViewById(R.id.et_uname);
        et_Email=(EditText) findViewById(R.id.et_Email);
        et_pwd=(EditText) findViewById(R.id.et_pwd);
        loadingBar = new ProgressDialog(AdvisorRegistrationActivity.this);

        btn_register=(Button) findViewById(R.id.btn_register);
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateAccount();

            }
        });
        getAIDs();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

    }

    
    private void CreateAccount() {

        String name = et_name.getText().toString();
        String Email = et_Email.getText().toString();
        String password = et_pwd.getText().toString();
        String username = et_uname.getText().toString();

        if (TextUtils.isEmpty(name))
        {
            Toast.makeText(this, "Please write your name...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(Email))
        {
            Toast.makeText(this, "Please write your Email...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(username))
        {
            Toast.makeText(this, "Please Choose your Username...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(password))
        {
            Toast.makeText(this, "Please write your password...", Toast.LENGTH_SHORT).show();
        }
        else if(!checkAID()){
            Toast.makeText(this, "Advisor Id is not authorized...", Toast.LENGTH_SHORT).show();
            return;
        }
        else
        {
            ValidateDetails();
        }
    }

    private void ValidateDetails() {
    }

    private boolean checkAID(){
        boolean fg=true;
        for(int i=0;i<mAdvisorIds.size();i++){
            if(mAdvisorIds.get(i).getAid().equals(et_uname.getText().toString())){
                fg=true;
                break;
            }else{
                fg=false;
            }
        }
        return  fg;
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
