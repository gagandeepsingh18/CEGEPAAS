package com.example.cegepaas;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.example.cegepaas.Adapters.AdvisorMeetingAdapter;
import com.example.cegepaas.Model.AdvisorBookingPojo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AdvisorMeetingsActivity extends AppCompatActivity {
    ListView lv;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_meetings);
        getSupportActionBar().setTitle("My Meetings");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        lv=(ListView)findViewById(R.id.lv);
        getMeetings();
    }
    ProgressDialog progressDialog;
    private List<AdvisorBookingPojo> mAdvisorBooking;
    private void getMeetings(){
        mAdvisorBooking = new ArrayList<>();
        progressDialog=new ProgressDialog(AdvisorMeetingsActivity.this);
        progressDialog.setTitle("Please Wait data is being Loaded");
        progressDialog.show();
        //dbArtists = FirebaseDatabase.getInstance().getReference("Advisor_Booking").startAt("");
        // dbArtists.addListenerForSingleValueEvent(valueEventListener1);
        SharedPreferences sp=getSharedPreferences("AA",0);
        Query query= FirebaseDatabase.getInstance().getReference("Advisor_Booking").orderByChild("adv_username").equalTo(sp.getString("auname","-"));
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
                    if(ab.getStatus().equals(getIntent().getStringExtra("fg"))) {
                        mAdvisorBooking.add(ab);
                    }
                }

                lv.setAdapter(new AdvisorMeetingAdapter(mAdvisorBooking, AdvisorMeetingsActivity.this));
              /*  gridview.setAdapter(new AdvisorBookingAdapter(ab,AdvisorBookingActivity.this));
                gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        _time = ab.get(position).getTime();
                        btn_select_time.setText(_time);
                    }
                });*/
                Toast.makeText(AdvisorMeetingsActivity.this, ""+mAdvisorBooking.size(), Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(AdvisorMeetingsActivity.this, "No data Found", Toast.LENGTH_SHORT).show();
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