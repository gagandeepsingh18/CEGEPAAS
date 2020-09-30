package com.example.cegepaas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class UserSelectActivity extends AppCompatActivity implements View.OnClickListener {
    TextView tv_student,tv_advisor, tv_admin;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_select);

        tv_student = (TextView)findViewById(R.id.student);
        tv_advisor = (TextView)findViewById(R.id.advisor);
        tv_admin = (TextView)findViewById(R.id.admin);

        tv_student.setOnClickListener(this);
        tv_advisor.setOnClickListener(this);
        tv_admin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        if(v.getId() == R.id.student){
            intent = new Intent(UserSelectActivity.this,StudentLoginActivity.class);
            startActivity(intent);
            finish();
        }
        else if(v.getId() == R.id.advisor){
            intent = new Intent(UserSelectActivity.this,AdvisorLoginActivity.class);
            startActivity(intent);
            finish();
        }
        else if(v.getId() == R.id.admin){
            intent = new Intent(UserSelectActivity.this,AdminLoginActivity.class);
            startActivity(intent);
            finish();
        }

    }
}