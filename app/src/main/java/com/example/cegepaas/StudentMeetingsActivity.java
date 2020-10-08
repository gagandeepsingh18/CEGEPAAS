package com.example.cegepaas;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.example.cegepaas.Adapters.StudentMeetingAdapter;
import com.example.cegepaas.Model.AdvisorBookingPojo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class StudentMeetingsActivity extends AppCompatActivity {
    ListView lv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_meetings);
        getSupportActionBar().setTitle("My Meetings");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        lv = (ListView) findViewById(R.id.lv);
        getMeetings();
    }

    ProgressDialog progressDialog;
    private List<AdvisorBookingPojo> mAdvisorBooking;

    private void getMeetings() {
        mAdvisorBooking = new ArrayList<>();
        progressDialog = new ProgressDialog(StudentMeetingsActivity.this);
        progressDialog.setTitle("Please Wait data is being Loaded");
        progressDialog.show();

        SharedPreferences sp = getSharedPreferences("AA", 0);
        Query query = FirebaseDatabase.getInstance().getReference("Advisor_Booking").orderByChild("booked_by").equalTo(sp.getString("suname", "-"));
        query.addListenerForSingleValueEvent(valueEventListener);
    }

    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            progressDialog.dismiss();
            mAdvisorBooking.clear();
            if (dataSnapshot.exists()) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    AdvisorBookingPojo ab = snapshot.getValue(AdvisorBookingPojo.class);
                    if (ab.getStatus().equals(getIntent().getStringExtra("fg"))) {
                        mAdvisorBooking.add(ab);
                    }
                }

                lv.setAdapter(new StudentMeetingAdapter(mAdvisorBooking, StudentMeetingsActivity.this));

                if (mAdvisorBooking.size() == 0)
                    Toast.makeText(StudentMeetingsActivity.this, "No data found", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(StudentMeetingsActivity.this, "No data Found", Toast.LENGTH_SHORT).show();
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