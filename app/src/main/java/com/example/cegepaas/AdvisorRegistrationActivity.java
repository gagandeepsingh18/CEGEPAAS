package com.example.cegepaas;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cegepaas.Model.AdvisorIdsPojo;
import com.example.cegepaas.Model.FCMPojo;
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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * AdvisorRegistration Activity Class
 */
public class AdvisorRegistrationActivity extends AppCompatActivity {
    Button btn_register;
    EditText et_name, et_uname, et_Email, et_pwd;
    Spinner campusName,departmentName;
    private ProgressDialog loadingBar;
    ProgressDialog progressDialog;
    String downloadImageUrl;
    private List<AdvisorIdsPojo> mAdvisorIds;
    DatabaseReference dbAdvisors;
    private List<String> campusList,departmentList;
    String name, email, password, username, campus, department;

    /**
     * onCreate functionality
     * @param savedInstanceState : Bundle type
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advisor_reg);

        getSupportActionBar().setTitle("Advisor Registration");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        et_name = (EditText) findViewById(R.id.et_name);
        et_uname = (EditText) findViewById(R.id.et_uname);
        et_Email = (EditText) findViewById(R.id.et_Email);
        et_pwd = (EditText) findViewById(R.id.et_pwd);
        departmentName = (Spinner)findViewById(R.id.departmentName);
        loadDepartment();
        campusName = (Spinner)findViewById(R.id.campusName);
        loadCampuses();


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

    /**
     * load departments
     */
    private void loadDepartment() {
        departmentList = new ArrayList<>();
        DatabaseReference campusRef = FirebaseDatabase.getInstance().getReference("Department");

        campusRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                departmentList.clear();
                if(dataSnapshot.exists()){
                    for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                        String department = snapshot.child("name").getValue(String.class);
                        departmentList.add(department);
                    }

                    ArrayAdapter<String> areasAdapter = new ArrayAdapter<String>(AdvisorRegistrationActivity.this, android.R.layout.simple_spinner_item, departmentList);
                    areasAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    departmentName.setAdapter(areasAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    /**
     * load campuses
     */
    private void loadCampuses() {
        campusList = new ArrayList<>();
        DatabaseReference campusRef = FirebaseDatabase.getInstance().getReference("Campus");

        campusRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                campusList.clear();
                if(dataSnapshot.exists()){
                    for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                        String campus = snapshot.child("name").getValue(String.class);
                        campusList.add(campus);
                    }

                    ArrayAdapter<String> areasAdapter = new ArrayAdapter<String>(AdvisorRegistrationActivity.this, android.R.layout.simple_spinner_item, campusList);
                    areasAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    campusName.setAdapter(areasAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * create account
     */
    private void CreateAccount() {

        name = et_name.getText().toString();
        email = et_Email.getText().toString();
        password = et_pwd.getText().toString();
        username = et_uname.getText().toString();
        campus = campusName.getSelectedItem().toString();
        department = departmentName.getSelectedItem().toString();
        downloadImageUrl = "https://firebasestorage.googleapis.com/v0/b/cegepaas.appspot.com/o/Default%2Fprofile.png?alt=media&token=b6e336d0-f12d-4c56-9c53-1c65cfbbb9bc";

        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, "Please write your name...", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Please write your Email...", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(username)) {
            Toast.makeText(this, "Please Choose your Username...", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please write your password...", Toast.LENGTH_SHORT).show();
        }else if(departmentName.getSelectedItemId() == 0){
            Toast.makeText(this, "Select your department...", Toast.LENGTH_SHORT).show();
        }else if(campusName.getSelectedItemId() == 0){
            Toast.makeText(this, "Select your campus...", Toast.LENGTH_SHORT).show();
        }else if (!checkAID()) {
            Toast.makeText(this, "Advisor Id is not authorized...", Toast.LENGTH_SHORT).show();
            return;
        } else {
            ValidateDetails();
        }
    }

    /**
     * validate data before creating account
     */
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
                    userdataMap.put("username", username);
                    userdataMap.put("email", email);
                    userdataMap.put("name", name);
                    userdataMap.put("image", downloadImageUrl);
                    userdataMap.put("password", password);
                    userdataMap.put("status", "inactive");
                    userdataMap.put("phoneNumber","");
                    userdataMap.put("campus",campus);
                    userdataMap.put("department",department);
                    userdataMap.put("workingDays","");
                    userdataMap.put("description","");


                    RootRef.child("Advisor_Details").child(username).updateChildren(userdataMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        submitdata("admin","Advisor Registration",name+" requested for Registration.");
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
                    Toast.makeText(AdvisorRegistrationActivity.this, "This username" + username + " already exists.", Toast.LENGTH_SHORT).show();
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

    /**
     * cross reference ID's before creating Account
     * @return true or false
     */
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

    /**
     * get Advisor Id's for reference
     */
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

    /**
     * functionality invoked on back button press
     * @param item : menu element
     * @return : true or false
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

    /**
     * submit data to submitData
     * @param uname : userName
     * @param title : title
     * @param msg : message
     */
    public void submitdata(String uname,String title,String msg) {
        EndPointUrl apiService = RetrofitInstance.getRetrofitInstance().create(EndPointUrl.class);
        Call<FCMPojo> call = apiService.send_advisor_notification(uname,"advisor",title,msg);
        call.enqueue(new Callback<FCMPojo>() {
            @Override
            public void onResponse(Call<FCMPojo> call, Response<FCMPojo> response) {
                Toast.makeText(AdvisorRegistrationActivity.this, "Notification sent successfully.", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onFailure(Call<FCMPojo> call, Throwable t) {
            }
        });
    }
}