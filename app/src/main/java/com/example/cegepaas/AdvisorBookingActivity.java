package com.example.cegepaas;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.example.cegepaas.Adapters.AdvisorBookingAdapter;
import com.example.cegepaas.Model.AdvisorAvailableDates;
import com.example.cegepaas.Model.AvailableTimings;
import com.example.cegepaas.Model.BookingTimesPojo;
import com.example.cegepaas.Model.FCMPojo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * AdvisorBooking Activity Class
 */
public class AdvisorBookingActivity extends AppCompatActivity {
    List<BookingTimesPojo> ab = new ArrayList<>();
    Button btn_select_date, btn_submit, btn_select_time;
    GridView gridview;
    String _time = null;
    private com.wdullaer.materialdatetimepicker.date.DatePickerDialog dpd;
    EditText et_des;
    String time_slots = "";
    ProgressDialog progressDialog;
    private List<AdvisorAvailableDates> mAvailableDates;
    Calendar calendar;
    List<Calendar> dates = new ArrayList<>();
    private List<AvailableTimings> mAvailableTimings;
    ProgressDialog loadingBar;

    /**
     * onCreate functionality
     * @param savedInstanceState : Bundle type
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advisor_booking);
        getSupportActionBar().setTitle("Advisor Booking");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        gridview = (GridView) findViewById(R.id.gridview);
        et_des = (EditText) findViewById(R.id.et_des);
        btn_select_date = (Button) findViewById(R.id.btn_select_date);
        btn_select_time = (Button) findViewById(R.id.btn_select_time);
        btn_submit = (Button) findViewById(R.id.btn_submit);
        btn_select_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAvaialbilities();
            }
        });
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                advisorBooking();
            }
        });

        getAvaialbilities();
    }
    //Get Available Dates - Start

    com.wdullaer.materialdatetimepicker.date.DatePickerDialog.OnDateSetListener selliste = new com.wdullaer.materialdatetimepicker.date.DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(com.wdullaer.materialdatetimepicker.date.DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
            btn_select_date.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
            for (AdvisorAvailableDates aa : mAvailableDates) {
                if (aa.getBooking_date().equals(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year)) {
                    //Toast.makeText(getApplicationContext(),aa.getBooking_times(),Toast.LENGTH_SHORT).show();
                    time_slots = aa.getBooking_times();
                    break;
                }
            }
            getAvailableTimes();
        }
    };

    /**
     * Show calender Custom
     */
    private void showCustomDialog() {
        Calendar now = Calendar.getInstance();
        dpd = com.wdullaer.materialdatetimepicker.date.DatePickerDialog.newInstance(
                selliste,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        for (AdvisorAvailableDates aa : mAvailableDates) {
            getEnbDates(aa.getBooking_date());
        }

        Calendar[] disabledDays1 = dates.toArray(new Calendar[dates.size()]);
        dpd.setSelectableDays(disabledDays1);
        dpd.show(getSupportFragmentManager(), "ddd");
    }

    /**
     * get Advisor availability for selected date
     */
    private void getAvaialbilities() {
        mAvailableDates = new ArrayList<>();
        progressDialog = new ProgressDialog(AdvisorBookingActivity.this);
        progressDialog.setTitle("Please Wait data is being Loaded");
        progressDialog.show();
        Query query = FirebaseDatabase.getInstance().getReference("Advisor_Availability").orderByChild("adv_username").equalTo(getIntent().getStringExtra("uname"));
        query.addListenerForSingleValueEvent(valueEventListener);
    }

    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            progressDialog.dismiss();
            mAvailableDates.clear();
            if (dataSnapshot.exists()) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    AdvisorAvailableDates advs = snapshot.getValue(AdvisorAvailableDates.class);
                    mAvailableDates.add(advs);
                }
                if (mAvailableDates.size() > 0) {
                    showCustomDialog();
                }
                //Toast.makeText(SelectAppointmentDateActivity.this, ""+mAvailableDates.size(), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(AdvisorBookingActivity.this, "No data Found", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            progressDialog.dismiss();
        }
    };

    /**
     * Get advisor availability Dates which are available to be enabled
     * @param a : date as string
     */
    private void getEnbDates(String a) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        java.util.Date date1 = null, today = null;
        try {
            Date date = new Date();
            today = sdf.parse(sdf.format(date));
            date1 = sdf.parse(a);
            AdvisorBookingActivity obj = new AdvisorBookingActivity();
            calendar = obj.dateToCalendar(date1);
        } catch (ParseException e) {
        }
        if (date1.compareTo(today) >= 0) {
            dates.add(calendar);
        }
    }

    /**
     * Load calander
     * @param date : date
     * @return : date
     */
    private Calendar dateToCalendar(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }

    /**
     * create time slots method
     */
    private void createTimeSlot() {
        ab.clear();
        if (time_slots.length() > 3) {
            String timings[] = time_slots.split(",");
            if (timings != null && timings.length > 0) {
                for (int i = 0; i < timings.length; i++) {
                    //ab.add(new BookingTimesPojo(timings[i], "Available",displayName(timings[i])));
                    ab.add(new BookingTimesPojo(timings[i], "Available"));
                }
            }
        }
    }

    /**
     * get available dates method
     */
    private void getAvailableTimes() {
        mAvailableTimings = new ArrayList<>();
        progressDialog = new ProgressDialog(AdvisorBookingActivity.this);
        progressDialog.setTitle("Please Wait data is being Loaded");
        progressDialog.show();
        //dbArtists = FirebaseDatabase.getInstance().getReference("Advisor_Booking").startAt("");
        // dbArtists.addListenerForSingleValueEvent(valueEventListener1);
        Query query = FirebaseDatabase.getInstance().getReference("Advisor_Booking").orderByChild("created_at").equalTo(getIntent().getStringExtra("uname") + btn_select_date.getText().toString());
        query.addListenerForSingleValueEvent(valueEventListener1);
    }

    ValueEventListener valueEventListener1 = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            progressDialog.dismiss();
            mAvailableTimings.clear();
            if (dataSnapshot.exists()) {
                createTimeSlot();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    AvailableTimings atime = snapshot.getValue(AvailableTimings.class);
                    //mAvailableTimings.add(artist);
                    for (BookingTimesPojo a : ab) {
                        if (a.getTime().equals(atime.getBooked_time())) {
                            a.setAvailable(atime.getStatus());
                            //int pos=ab.indexOf(atime.getBooked_time());
                            // ab.set(pos,new AdvisorBookingPojo(atime.getBooked_time(),"Booked"));
                        }
                    }
                }
                gridview.setAdapter(new AdvisorBookingAdapter(ab, AdvisorBookingActivity.this));
                gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if (ab.get(position).getAvailable().equals("accept")) {
                            _time = null;
                            btn_select_time.setText("-");
                            Toast.makeText(getApplicationContext(), "Another appointment is booked for this time", Toast.LENGTH_SHORT).show();
                        } else {
                            _time = ab.get(position).getTime();
                            btn_select_time.setText(_time);
                        }
                    }
                });
                // Toast.makeText(AdvisorBookingActivity.this, ""+mAvailableTimings.size(), Toast.LENGTH_SHORT).show();
            } else {
                //Toast.makeText(AdvisorBookingActivity.this, "No data Found", Toast.LENGTH_SHORT).show();
                createTimeSlot();
                gridview.setAdapter(new AdvisorBookingAdapter(ab, AdvisorBookingActivity.this));
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

    /**
     * advisor booking method
     */
    private void advisorBooking() {
        if (btn_select_date.getText().toString().length() < 3) {
            Toast.makeText(getApplicationContext(), "Please Select Date.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (_time == null) {
            Toast.makeText(getApplicationContext(), "Please Select Time.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (et_des.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please Enter Description.", Toast.LENGTH_SHORT).show();
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
                SharedPreferences sp = getSharedPreferences("AA", 0);
                Calendar cal = Calendar.getInstance(Locale.ENGLISH);
                String str = sp.getString("suname", "-") + "_" + cal.getTimeInMillis();
                //getIntent().getStringExtra("uname")+btn_select_date.getText().toString()+_time
                if (!(dataSnapshot.child("Advisor_Booking").child(str).exists())) {
                    HashMap<String, Object> userdataMap = new HashMap<>();
                    userdataMap.put("adv_username", getIntent().getStringExtra("uname"));
                    userdataMap.put("booked_time", _time);
                    userdataMap.put("booked_date", btn_select_date.getText().toString());
                    userdataMap.put("booked_by", sp.getString("suname", "-"));
                    userdataMap.put("timestamp", str);
                    userdataMap.put("status", "pending");
                    userdataMap.put("description", et_des.getText().toString());
                    userdataMap.put("created_at", getIntent().getStringExtra("uname") + btn_select_date.getText().toString());
                    RootRef.child("Advisor_Booking").child(str).updateChildren(userdataMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        submitdata(getIntent().getStringExtra("uname"), sp.getString("suname","-")+ " has booked appointment for "+btn_select_date.getText().toString()+ " at "+_time,et_des.getText().toString());
                                        Toast.makeText(AdvisorBookingActivity.this, "Congratulations, your appointment has been created.", Toast.LENGTH_SHORT).show();
                                        loadingBar.dismiss();
                                        finish();
                                    } else {
                                        loadingBar.dismiss();
                                        Toast.makeText(AdvisorBookingActivity.this, "Network Error: Please try again after some time...", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                } else {
                    loadingBar.dismiss();
                    Toast.makeText(AdvisorBookingActivity.this, "Another Appointment is booked for this date & time.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

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
     * submit data for notification
     * @param uname : uname
     * @param title : title
     * @param msg : message
     */
    public void submitdata(String uname,String title,String msg) {
        EndPointUrl apiService = RetrofitInstance.getRetrofitInstance().create(EndPointUrl.class);
        Call<FCMPojo> call = apiService.send_advisor_notification(uname,"advisor",title,msg);
        call.enqueue(new Callback<FCMPojo>() {
            @Override
            public void onResponse(Call<FCMPojo> call, Response<FCMPojo> response) {
            }
            @Override
            public void onFailure(Call<FCMPojo> call, Throwable t) {
            }
        });
    }

}