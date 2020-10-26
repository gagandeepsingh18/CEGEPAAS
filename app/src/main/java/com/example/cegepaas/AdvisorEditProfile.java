package com.example.cegepaas;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.cegepaas.Model.AdvisorsPojo;
import com.example.cegepaas.Model.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.Nullable;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class AdvisorEditProfile extends AppCompatActivity {
    EditText advisorEmail, advisorName,advisorPhone,advisorDescription;
    Spinner advisorDepartment,advisorCampus;
    TextView advisorUsername;
    ImageView advisorProfile;
    CheckBox chkMonday,chkTuesday,chkWednesday,chkThursday,chkFriday,chkSaturday,chkSunday;
    Button cancel, save;
    String advisorId;
    DatabaseReference dbAdvisor;
    StorageReference storageReference;
    Uri imageUri;
    private String parentDbName = "Advisor_Details";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advisor_edit_profile);
        Intent data = getIntent();

        getSupportActionBar().setTitle("Edit Profile");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        storageReference = FirebaseStorage.getInstance().getReference();

        advisorEmail = findViewById(R.id.advisorEmail);
        advisorName = findViewById(R.id.upAdvisor_name);
        advisorUsername = findViewById(R.id.upAdvisorId);
        advisorProfile = findViewById(R.id.upAdvisor_image);
        advisorPhone = findViewById(R.id.advisorPhone);
        advisorDepartment = findViewById(R.id.departmentName);
        advisorCampus = findViewById(R.id.campusName);
        advisorDescription = findViewById(R.id.advisorDescription);

        chkMonday = findViewById(R.id.monday);
        chkTuesday = findViewById(R.id.tuesday);
        chkWednesday = findViewById(R.id.wednesday);
        chkThursday = findViewById(R.id.thursday);
        chkFriday = findViewById(R.id.friday);
        chkSaturday = findViewById(R.id.saturday);
        chkSunday = findViewById(R.id.sunday);

        cancel = findViewById(R.id.up_AdvisorCancel);
        save = findViewById(R.id.up_AdvisorSave);

        advisorId = getSharedPreferences("AA", 0).getString("auname", "-");

        storageReference = FirebaseStorage.getInstance().getReference();
        dbAdvisor = FirebaseDatabase.getInstance().getReference();

        getAdvisorDetails(advisorId);


        advisorProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadProfilePicture();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), AdvisorProfileActivity.class);
                startActivity(i);
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateAdvisorDetails(advisorId);
                startActivity(new Intent(AdvisorEditProfile.this,AdvisorProfileActivity.class));
            }
        });
    }

    private void updateAdvisorDetails(String advisorId) {
        dbAdvisor.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                HashMap<String, Object> userdataMap = new HashMap<>();
                userdataMap.put("email", advisorEmail.getText().toString());
                userdataMap.put("name", advisorName.getText().toString());
                userdataMap.put("phoneNumber",advisorPhone.getText().toString());
                userdataMap.put("campus",advisorCampus.getSelectedItem().toString());
                userdataMap.put("department",advisorDepartment.getSelectedItem().toString());
                userdataMap.put("workingDays",getWD());
                userdataMap.put("description",advisorDescription.getText().toString());

                dbAdvisor.child(parentDbName).child(advisorId).updateChildren(userdataMap);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private String getWD() {
        String workingDays = "";

        if (chkMonday.isChecked()) {
            workingDays += chkMonday.getText() + ",";
        }

        if (chkTuesday.isChecked()) {
            workingDays += chkTuesday.getText() + ",";
        }

        if (chkWednesday.isChecked()) {
            workingDays += chkWednesday.getText() + ",";
        }

        if (chkThursday.isChecked()) {
            workingDays += chkThursday.getText() + ",";
        }

        if (chkFriday.isChecked()) {
            workingDays += chkFriday.getText() + ",";
        }

        if (chkSaturday.isChecked()) {
            workingDays += chkSaturday.getText() + ",";
        }

        if (chkSunday.isChecked()) {
            workingDays += chkSunday.getText() + ",";
        }
        workingDays = workingDays.substring(0, workingDays.length() - 1);
        return workingDays;
    }

    private void uploadProfilePicture() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,1);
    }

    private void getAdvisorDetails(String advisorId) {
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();
        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(parentDbName).child(advisorId).exists()){
                    AdvisorsPojo advisor =snapshot.child(parentDbName).child(advisorId).getValue(AdvisorsPojo.class);
                    Glide.with(getApplicationContext()).load(advisor.getImage()).into(advisorProfile);
                    advisorEmail.setText(advisor.getEmail());
                    advisorUsername.setText(advisorId);
                    advisorName.setText(advisor.getName());
                    advisorPhone.setText(advisor.getPhoneNumber());
                    advisorDescription.setText(advisor.getDescription());
                    loadCampuses(advisor.getCampus());
                    loadDepartment(advisor.getDepartment());
                    getWorkingDays(advisor.getWorkingDays());


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getWorkingDays(String workingDays) {
        if(!workingDays.equals("")){
            String[] days = workingDays.split(",");

            for (String day : days) {
                checkUncheck(day, true);
            }
        }
    }

    public void checkUncheck(String day, boolean flag){
        if(day.equals(chkMonday.getText())){
            chkMonday.setChecked(flag);
        }else if(day.equals(chkTuesday.getText())){
            chkTuesday.setChecked(flag);
        }else if(day.equals(chkWednesday.getText())){
            chkWednesday.setChecked(flag);
        }else if(day.equals(chkThursday.getText())){
            chkThursday.setChecked(flag);
        }else if(day.equals(chkFriday.getText())){
            chkFriday.setChecked(flag);
        }else if(day.equals(chkSaturday.getText())){
            chkSaturday.setChecked(flag);
        }else if(day.equals(chkSunday.getText())){
            chkSunday.setChecked(flag);
        }
    }

    private void loadCampuses(String selectedCampus) {
        List<String> campusList = new ArrayList<>();
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

                    ArrayAdapter<String> areasAdapter = new ArrayAdapter<String>(AdvisorEditProfile.this, android.R.layout.simple_spinner_item, campusList);
                    areasAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    advisorCampus.setAdapter(areasAdapter);
                    advisorCampus.setSelection(areasAdapter.getPosition(selectedCampus));
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void loadDepartment(String selectedDepartment) {
        List<String> departmentList = new ArrayList<>();
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

                    ArrayAdapter<String> areasAdapter = new ArrayAdapter<String>(AdvisorEditProfile.this, android.R.layout.simple_spinner_item, departmentList);
                    areasAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    advisorDepartment.setAdapter(areasAdapter);
                    advisorDepartment.setSelection(areasAdapter.getPosition(selectedDepartment));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == RESULT_OK && data!=null && data.getData()!=null){
            imageUri = data.getData();
            Picasso.get().load(imageUri).into(advisorProfile);
            uploadPicture();
        }
    }

    private void uploadPicture() {
        final String randomKey = UUID.randomUUID().toString();
        final StorageReference reference = storageReference.child("Advisor/"+randomKey);

        reference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        final String imageUrl= uri.toString();
                        dbAdvisor.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                HashMap<String, Object> userdataMap = new HashMap<>();
                                userdataMap.put("image", imageUrl);

                                dbAdvisor.child(parentDbName).child(advisorId).updateChildren(userdataMap);

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                });
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