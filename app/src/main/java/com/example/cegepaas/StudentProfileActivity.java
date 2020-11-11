package com.example.cegepaas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.cegepaas.Model.AdvisorsPojo;
import com.example.cegepaas.Model.Users;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * StudentProfileActivity is used to show the current user details.
 */
public class StudentProfileActivity extends AppCompatActivity {
    ImageView profile;
    TextView name, email, id;
    Button edit_profile, edit_password;
    String studentid;
    private String parentDbName = "Student_Details";

    /**
     * onCreate method is the main method that will trigger when the activity starts.
     * @param savedInstanceState Bundle object.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_profile);

        getSupportActionBar().setTitle("Profile");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        profile = findViewById(R.id.student_image);
        name = findViewById(R.id.student_name);
        email = findViewById(R.id.studentEmail);
        id = findViewById(R.id.studentId);
        edit_profile = findViewById(R.id.student_profile_edit);
        edit_password = findViewById(R.id.up_studentPass);

        studentid = getSharedPreferences("AA", 0).getString("suname","-");
        getStudentDetails(studentid);
        edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), StudentEditProfile.class);
                startActivity(i);
            }
        });
        edit_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), StudentEditPassword.class);
                startActivity(i);
            }
        });
    }

    /**
     * This method is used to get the student details.
     * @param studentid current user Id.
     */
    private void getStudentDetails(final String studentid) {
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();
        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.child(parentDbName).child(studentid).exists()){
                    Users student =snapshot.child(parentDbName).child(studentid).getValue(Users.class);
                    Glide.with(getApplicationContext()).load(student.getDownloadImageUrl()).into(profile);
                    name.setText(student.getName());
                    id.setText(student.getUsername());
                    email.setText(student.getEmail());

                }else{
                    Toast.makeText(getApplicationContext(),"Something went wrong",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_SHORT).show();
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