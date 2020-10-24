package com.example.cegepaas;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.example.cegepaas.Adapters.AdvisorAcceptRejectAdapter;
import com.example.cegepaas.Model.AdvisorBookingPojo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AdvisorAcceptRejectActivity extends AppCompatActivity {
    ListView lv;
    ProgressDialog progressDialog;
    private List<AdvisorBookingPojo> mAdvisors;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_meetings);
        getSupportActionBar().setTitle("Accept or Reject Meetings");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        lv = (ListView) findViewById(R.id.lv);
        getMeetings();
    }

    private void getMeetings() {
        mAdvisors = new ArrayList<>();
        progressDialog = new ProgressDialog(AdvisorAcceptRejectActivity.this);
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
                    if (advs.getStatus().equals("pending")) {
                        mAdvisors.add(advs);
                    }
                }
                if (mAdvisors.size() > 0) {
                    lv.setAdapter(new AdvisorAcceptRejectAdapter(mAdvisors, AdvisorAcceptRejectActivity.this));
                }
                //Toast.makeText(AdvisorHomeActivity.this, ""+mAdvisors.size(), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(AdvisorAcceptRejectActivity.this, "No data Found", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            progressDialog.dismiss();
        }
    };

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
