package com.example.cegepaas;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.example.cegepaas.Adapters.StudentHomeAdapter;
import com.example.cegepaas.Model.AdvisorsPojo;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class StudentHomeScreenActivity extends AppCompatActivity {

    private ActionBarDrawerToggle t;
    private NavigationView nv;
    private DrawerLayout dl;
    ListView lv;
    ProgressDialog progressDialog;
    private List<AdvisorsPojo> mAdvisors;
    DatabaseReference dbAdvisors;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_home_screen);

        ActionBar mActionBar=getSupportActionBar();
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);
        mActionBar.setDisplayShowCustomEnabled(true);

        getSupportActionBar().setTitle("Home");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        lv=(ListView)findViewById(R.id.lv);
        navigationView();
        getAdvisorsDetails();
    }

    private void getAdvisorsDetails(){
        mAdvisors = new ArrayList<>();
        progressDialog=new ProgressDialog(StudentHomeScreenActivity.this);
        progressDialog.setTitle("Please Wait data is being Loaded");
        progressDialog.show();
        dbAdvisors = FirebaseDatabase.getInstance().getReference("Advisor_Details");
        dbAdvisors.addListenerForSingleValueEvent(valueEventListener);
    }

    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            progressDialog.dismiss();
            mAdvisors.clear();
            if (dataSnapshot.exists()) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    AdvisorsPojo artist = snapshot.getValue(AdvisorsPojo.class);
                    mAdvisors.add(artist);
                }
                if(mAdvisors.size()>0){
                    lv.setAdapter(new StudentHomeAdapter(mAdvisors, StudentHomeScreenActivity.this));
                }
            }
            else {
                Toast.makeText(StudentHomeScreenActivity.this, "No data Found", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            progressDialog.dismiss();

        }
    };

    private void navigationView(){
        dl = (DrawerLayout)findViewById(R.id.activity_main);
        t = new ActionBarDrawerToggle(this, dl,R.string.Open, R.string.Close);
        dl.addDrawerListener(t);
        t.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        nv = (NavigationView)findViewById(R.id.nv);
        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch(id)
                {
                    case R.id.home:

                        break;
                    case R.id.about:

                        break;

                    case R.id.logout:
                        Intent logout=new Intent(getApplicationContext(), StudentLoginActivity.class);
                        startActivity(logout);

                        finish();
                        break;

                    default:
                        return true;
                }
                dl.closeDrawer(GravityCompat.START);
                return true;

            }
        });
    }
    @Override
    public void onBackPressed() {
        if (dl.isDrawerOpen(GravityCompat.START)) {
            dl.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (dl.isDrawerOpen(GravityCompat.START)) {
            dl.closeDrawer(GravityCompat.START);
        } else {
            dl.openDrawer(GravityCompat.START);
        }
        return super.onOptionsItemSelected(item);
    }


}