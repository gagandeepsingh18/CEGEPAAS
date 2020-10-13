package com.example.cegepaas;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.cegepaas.Adapters.AdminStudentDeleteAdapter;
import com.example.cegepaas.Model.Users;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AdminStudentDeleteActivity extends AppCompatActivity {
    private ActionBarDrawerToggle t;
    private NavigationView nv;
    private DrawerLayout dl;
    ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_student_delete);
        ActionBar mActionBar = getSupportActionBar();
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);
        mActionBar.setDisplayShowCustomEnabled(true);

        getSupportActionBar().setTitle("Student List");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        lv = (ListView) findViewById(R.id.lv);

        getAdvisorsDetails();
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

    ProgressDialog progressDialog;
    private List<Users> mStudents;
    DatabaseReference dbAdvisors;

    private void getAdvisorsDetails() {
        mStudents = new ArrayList<>();
        progressDialog = new ProgressDialog(AdminStudentDeleteActivity.this);
        progressDialog.setTitle("Please Wait data is being Loaded");
        progressDialog.show();
        dbAdvisors = FirebaseDatabase.getInstance().getReference("Student_Details");
        dbAdvisors.addListenerForSingleValueEvent(valueEventListener);
    }

    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            progressDialog.dismiss();
            mStudents.clear();
            if (dataSnapshot.exists()) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Users student = snapshot.getValue(Users.class);
                    mStudents.add(student);
                }
                if (mStudents.size() > 0) {
                    lv.setAdapter(new AdminStudentDeleteAdapter(mStudents, AdminStudentDeleteActivity.this));
                }
            } else {
                Toast.makeText(AdminStudentDeleteActivity.this, "No data Found", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            progressDialog.dismiss();

        }
    };
}