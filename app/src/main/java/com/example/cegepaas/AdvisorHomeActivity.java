package com.example.cegepaas;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.example.cegepaas.Adapters.AdvisorHomeAdapter;
import com.example.cegepaas.Model.AdvisorBookingPojo;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AdvisorHomeActivity extends AppCompatActivity {
    ListView list_view;
    List<AdvisorBookingPojo> a1;
    private ActionBarDrawerToggle t;
    private NavigationView nv;
    private DrawerLayout dl;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advisor_home);
        navigationView();

        getSupportActionBar().setTitle("Home");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        list_view = (ListView) findViewById(R.id.list_view);
        getMeetings();
    }

    ProgressDialog progressDialog;
    private List<AdvisorBookingPojo> mAdvisors;
    DatabaseReference dbArtists;

    private void getMeetings() {
        mAdvisors = new ArrayList<>();
        progressDialog = new ProgressDialog(AdvisorHomeActivity.this);
        progressDialog.setTitle("Please Wait data is being Loaded");
        progressDialog.show();
        SharedPreferences sp = getSharedPreferences("AA", 0);
        Query query = FirebaseDatabase.getInstance().getReference("Advisor_Booking").orderByChild("adv_username").equalTo(sp.getString("auname", "-"));
        query.addListenerForSingleValueEvent(valueEventListener);
    }

    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            progressDialog.dismiss();
            mAdvisors.clear();
            if (dataSnapshot.exists()) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    AdvisorBookingPojo advs = snapshot.getValue(AdvisorBookingPojo.class);
                    mAdvisors.add(advs);
                }
                if (mAdvisors.size() > 0) {
                    list_view.setAdapter(new AdvisorHomeAdapter(mAdvisors, AdvisorHomeActivity.this));
                }
                //Toast.makeText(AdvisorHomeActivity.this, ""+mAdvisors.size(), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(AdvisorHomeActivity.this, "No data Found", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            progressDialog.dismiss();
        }
    };

    private void navigationView() {
        dl = (DrawerLayout) findViewById(R.id.activity_main);
        t = new ActionBarDrawerToggle(this, dl, R.string.Open, R.string.Close);
        dl.addDrawerListener(t);
        t.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        nv = (NavigationView) findViewById(R.id.nv);
        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id) {
                    case R.id.rejected_meetings:
                        Intent intent_reject = new Intent(getApplicationContext(), AdvisorMeetingsActivity.class);
                        intent_reject.putExtra("fg", "reject");
                        startActivity(intent_reject);
                        break;
                    case R.id.accepted_meetings:
                        Intent intent_accept = new Intent(getApplicationContext(), AdvisorMeetingsActivity.class);
                        intent_accept.putExtra("fg", "accept");
                        startActivity(intent_accept);
                        break;

                    case R.id.pending_meetings:
                        Intent intent_pending = new Intent(getApplicationContext(), AdvisorMeetingsActivity.class);
                        intent_pending.putExtra("fg", "pending");
                        startActivity(intent_pending);
                        break;

                    case R.id.accept_reject_meetings:
                        Intent intent_accept_reject = new Intent(getApplicationContext(), AdvisorAcceptRejectActivity.class);
                        intent_accept_reject.putExtra("fg", "pending");
                        startActivity(intent_accept_reject);
                        break;


                    case R.id.logout:
                        Intent logout = new Intent(getApplicationContext(), AdvisorLoginActivity.class);
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
