package com.example.cegepaas;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.example.cegepaas.Adapters.AdvisorBookingAdapter;
import com.example.cegepaas.Model.AvailableTimings;
import com.example.cegepaas.Model.BookingTimesPojo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class AdvisorBookingActivity  extends AppCompatActivity  {
    List<BookingTimesPojo> ab=new ArrayList<>();
    Button btn_select_date,btn_submit,btn_select_time;
    GridView gridview;
    String _time=null;
    EditText et_des;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advisor_booking);
        gridview= (GridView)findViewById(R.id.gridview);
        et_des= (EditText)findViewById(R.id.et_des);
        btn_select_date= (Button)findViewById(R.id.btn_select_date);
        btn_select_time= (Button)findViewById(R.id.btn_select_time);
        btn_submit= (Button)findViewById(R.id.btn_submit);
        btn_select_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datepicker();
            }
        });
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                advisorBooking();
            }
        });

        datepicker();
    }
    private void createTimeSlot(){
        ab.clear();
        ab.add(new BookingTimesPojo("10:00AM","Available"));
        ab.add(new BookingTimesPojo("11:00AM","Available"));
        ab.add(new BookingTimesPojo("12:00PM","Available"));
        ab.add(new BookingTimesPojo("1:00MM","Available"));
        ab.add(new BookingTimesPojo("2:00PM","Available"));
        ab.add(new BookingTimesPojo("3:00PM","Available"));
        ab.add(new BookingTimesPojo("4:00PM","Available"));
        ab.add(new BookingTimesPojo("5:00PM","Available"));
        ab.add(new BookingTimesPojo("6:00PM","Available"));
    }
    int mYear,mMonth,mDay;
    String DAY,MONTH,YEAR;
    public void datepicker() {
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        DAY = dayOfMonth + "";
                        MONTH = (monthOfYear + 1 )+ "";
                        YEAR = year + "";
                        btn_select_date.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                        getAvailableTimes();
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }
    ProgressDialog progressDialog;
    private List<AvailableTimings> mAvailableTimings;
    DatabaseReference dbArtists;
    private void getAvailableTimes(){
        mAvailableTimings = new ArrayList<>();
        progressDialog=new ProgressDialog(AdvisorBookingActivity.this);
        progressDialog.setTitle("Please Wait data is being Loaded");
        progressDialog.show();
        //dbArtists = FirebaseDatabase.getInstance().getReference("Advisor_Booking").startAt("");
        // dbArtists.addListenerForSingleValueEvent(valueEventListener1);
        Query query= FirebaseDatabase.getInstance().getReference("Advisor_Booking").orderByChild("created_at").equalTo(getIntent().getStringExtra("uname")+btn_select_date.getText().toString());
        query.addListenerForSingleValueEvent(valueEventListener);
    }
    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            progressDialog.dismiss();
            mAvailableTimings.clear();
            if (dataSnapshot.exists()) {
                createTimeSlot();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    AvailableTimings atime = snapshot.getValue(AvailableTimings.class);
                    //mAvailableTimings.add(artist);
                    for(BookingTimesPojo a:ab){
                        if(a.getTime().equals(atime.getBooked_time())){
                            a.setAvailable("accept");
                            //int pos=ab.indexOf(atime.getBooked_time());
                            // ab.set(pos,new AdvisorBookingPojo(atime.getBooked_time(),"Booked"));
                        }
                    }
                }
                gridview.setAdapter(new AdvisorBookingAdapter(ab,AdvisorBookingActivity.this));
                gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        _time = ab.get(position).getTime();
                        btn_select_time.setText(_time);
                    }
                });
                // Toast.makeText(AdvisorBookingActivity.this, ""+mAvailableTimings.size(), Toast.LENGTH_SHORT).show();
            }
            else {
                //Toast.makeText(AdvisorBookingActivity.this, "No data Found", Toast.LENGTH_SHORT).show();
                createTimeSlot();
                gridview.setAdapter(new AdvisorBookingAdapter(ab,AdvisorBookingActivity.this));
                gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        _time = ab.get(position).getTime();
                        btn_select_time.setText(_time);
                    }
                });
            }
        }
        @Override
        public void onCancelled(DatabaseError databaseError) {
            progressDialog.dismiss();

        }
    };

    ProgressDialog loadingBar;
    private void advisorBooking() {
        if(btn_select_date.getText().toString().length()<3){
            Toast.makeText(getApplicationContext(),"Please Select Date.",Toast.LENGTH_SHORT).show();
            return;
        }

        if(_time==null){
            Toast.makeText(getApplicationContext(),"Please Select Time.",Toast.LENGTH_SHORT).show();
            return;
        }
        if(et_des.getText().toString().isEmpty()){
            Toast.makeText(getApplicationContext(),"Please Enter Description.",Toast.LENGTH_SHORT).show();
            return;
        }



        loadingBar = new ProgressDialog(AdvisorBookingActivity.this);
        loadingBar.setTitle("Create Account");
        loadingBar.setMessage("Please wait, while we are checking the credentials.");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();
        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                SharedPreferences sp=getSharedPreferences("AA",0);
                Calendar cal = Calendar.getInstance(Locale.ENGLISH);
                String str=sp.getString("suname","-")+"_"+cal.getTimeInMillis();
                //getIntent().getStringExtra("uname")+btn_select_date.getText().toString()+_time
                if (!(dataSnapshot.child("Advisor_Booking").child(str).exists()))
                {
                    HashMap<String, Object> userdataMap = new HashMap<>();
                    userdataMap.put("adv_username", getIntent().getStringExtra("uname"));
                    userdataMap.put("booked_time", _time);
                    userdataMap.put("booked_date", btn_select_date.getText().toString());
                    userdataMap.put("booked_by",  sp.getString("suname","-"));
                    userdataMap.put("timestamp", str );
                    userdataMap.put("status", "pending");
                    userdataMap.put("description",et_des.getText().toString());
                    userdataMap.put("created_at",  getIntent().getStringExtra("uname")+btn_select_date.getText().toString());
                    RootRef.child("Advisor_Booking").child(str).updateChildren(userdataMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task)
                                {
                                    if (task.isSuccessful())
                                    {
                                        Toast.makeText(AdvisorBookingActivity.this, "Congratulations, your appointment has been created.", Toast.LENGTH_SHORT).show();
                                        loadingBar.dismiss();
                                        finish();
                                    }
                                    else
                                    {
                                        loadingBar.dismiss();
                                        Toast.makeText(AdvisorBookingActivity.this, "Network Error: Please try again after some time...", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
                else{
                    Toast.makeText(AdvisorBookingActivity.this, "This a101 already exists.", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                    Toast.makeText(AdvisorBookingActivity.this, "Another Appointment is booked for this date & time.", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}