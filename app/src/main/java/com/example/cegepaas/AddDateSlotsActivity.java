package com.example.cegepaas;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cegepaas.Model.AdvisorAvailableDates;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

/**
 * AddDateSlots Activity Class
 */
public class AddDateSlotsActivity extends AppCompatActivity {
    Button btn_select_date, btn_submit;
    CheckBox chk10AM, chk1030AM, chk11AM, chk1130AM, chk12PM, chk130PM, chk2PM, chk230PM, chk3PM, chk330PM, chk4PM, chk430PM;
    int mYear, mMonth, mDay;
    String DAY, MONTH, YEAR;
    ProgressDialog loadingBar;
    String sel_timings = "";
    TextView showDate;

    /**
     * onCreate functionality
     * @param savedInstanceState : Bundle type
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_date_slots);
        getSupportActionBar().setTitle("Add Time Slots");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        showDate = (TextView) findViewById(R.id.showDate);
        btn_submit = (Button) findViewById(R.id.btn_submit);
        btn_select_date = (Button) findViewById(R.id.btn_select_date);
        chk10AM = (CheckBox) findViewById(R.id.chk10_00AM);
        chk1030AM = (CheckBox) findViewById(R.id.chk10_30AM);
        chk11AM = (CheckBox) findViewById(R.id.chk11_00AM);
        chk1130AM = (CheckBox) findViewById(R.id.chk11_30AM);
        chk12PM = (CheckBox) findViewById(R.id.chk12_00PM);
        chk130PM = (CheckBox) findViewById(R.id.chk01_30PM);
        chk2PM = (CheckBox) findViewById(R.id.chk02_00PM);
        chk230PM = (CheckBox) findViewById(R.id.chk02_30PM);
        chk3PM = (CheckBox) findViewById(R.id.chk03_00PM);
        chk330PM = (CheckBox) findViewById(R.id.chk03_30PM);
        chk4PM = (CheckBox) findViewById(R.id.chk04_00PM);
        chk430PM = (CheckBox) findViewById(R.id.chk04_30PM);
        enableDisableCheckbox();
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btn_select_date.getText().toString().equals("Select Date")) {
                    Toast.makeText(getApplicationContext(), "Please Select Date.", Toast.LENGTH_SHORT).show();
                } else {
                    advisorBooking();
                }
            }
        });
        btn_select_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datepicker();
            }
        });

        btn_select_date.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                final DatabaseReference RootRef;
                RootRef = FirebaseDatabase.getInstance().getReference();
                RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        SharedPreferences sp = getSharedPreferences("AA", 0);
                        String str = sp.getString("auname", "-") + "_" + btn_select_date.getText().toString();
                        unCheck();
                        if ((dataSnapshot.child("Advisor_Availability").child(str).exists())) {
                            AdvisorAvailableDates availableDates = dataSnapshot.child("Advisor_Availability").child(str).getValue(AdvisorAvailableDates.class);
                            String timings[] = availableDates.getBooking_times().split(",");
                            for (int i = 0; i < timings.length; i++) {
                                checkBox(timings[i]);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });

    }

    /**
     * method for uncheck all checkboxes
     */
    private void unCheck() {
        chk10AM.setChecked(false);
        chk1030AM.setChecked(false);
        chk11AM.setChecked(false);
        chk1130AM.setChecked(false);
        chk12PM.setChecked(false);
        chk130PM.setChecked(false);
        chk2PM.setChecked(false);
        chk230PM.setChecked(false);
        chk3PM.setChecked(false);
        chk330PM.setChecked(false);
        chk4PM.setChecked(false);
        chk430PM.setChecked(false);
    }

    /**
     * method for checking the corresponding checkbox
     * @param time : time
     */
    private void checkBox(String time) {
        if (time.equals(chk10AM.getText())) {
            chk10AM.setChecked(true);
        } else if (time.equals(chk1030AM.getText())) {
            chk1030AM.setChecked(true);
        } else if (time.equals(chk11AM.getText())) {
            chk11AM.setChecked(true);
        } else if (time.equals(chk1130AM.getText())) {
            chk1130AM.setChecked(true);
        } else if (time.equals(chk12PM.getText())) {
            chk12PM.setChecked(true);
        } else if (time.equals(chk130PM.getText())) {
            chk130PM.setChecked(true);
        } else if (time.equals(chk2PM.getText())) {
            chk2PM.setChecked(true);
        } else if (time.equals(chk230PM.getText())) {
            chk230PM.setChecked(true);
        } else if (time.equals(chk3PM.getText())) {
            chk3PM.setChecked(true);
        } else if (time.equals(chk330PM.getText())) {
            chk330PM.setChecked(true);
        } else if (time.equals(chk4PM.getText())) {
            chk4PM.setChecked(true);
        } else if (time.equals(chk430PM.getText())) {
            chk430PM.setChecked(true);
        }
    }

    /**
     * enable or disable corresponding checkbox
     */
    void enableDisableCheckbox() {
        if (btn_select_date.getText().toString().equals("Select Date")) {
            chk10AM.setEnabled(false);
            chk1030AM.setEnabled(false);
            chk11AM.setEnabled(false);
            chk1130AM.setEnabled(false);
            chk12PM.setEnabled(false);
            chk130PM.setEnabled(false);
            chk2PM.setEnabled(false);
            chk230PM.setEnabled(false);
            chk3PM.setEnabled(false);
            chk330PM.setEnabled(false);
            chk4PM.setEnabled(false);
            chk430PM.setEnabled(false);
        } else {
            chk10AM.setEnabled(true);
            chk1030AM.setEnabled(true);
            chk11AM.setEnabled(true);
            chk1130AM.setEnabled(true);
            chk12PM.setEnabled(true);
            chk130PM.setEnabled(true);
            chk2PM.setEnabled(true);
            chk230PM.setEnabled(true);
            chk3PM.setEnabled(true);
            chk330PM.setEnabled(true);
            chk4PM.setEnabled(true);
            chk430PM.setEnabled(true);
        }
    }

    /**
     * Date picker method
     */
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
                        MONTH = (monthOfYear + 1) + "";
                        YEAR = year + "";
                        showDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                        btn_select_date.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                        enableDisableCheckbox();
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.getDatePicker().setMinDate(new Date().getTime());
        c.add(Calendar.DAY_OF_MONTH, +14);
        datePickerDialog.getDatePicker().setMaxDate(c.getTimeInMillis());
        datePickerDialog.show();
    }

    /**
     * advisor booking method
     */
    private void advisorBooking() {
        sel_timings = "";
        if (chk10AM.isChecked()) {
            sel_timings += "10:00 AM,";
        }

        if (chk1030AM.isChecked()) {
            sel_timings += "10:30 AM,";
        }

        if (chk11AM.isChecked()) {
            sel_timings += "11:00 AM,";
        }

        if (chk1130AM.isChecked()) {
            sel_timings += "11:30 AM,";
        }

        if (chk12PM.isChecked()) {
            sel_timings += "12:00 PM,";
        }

        if (chk130PM.isChecked()) {
            sel_timings += "01:30 PM,";
        }

        if (chk2PM.isChecked()) {
            sel_timings += "02:00 PM,";
        }

        if (chk230PM.isChecked()) {
            sel_timings += "02:30 PM,";
        }

        if (chk3PM.isChecked()) {
            sel_timings += "03:00 PM,";
        }

        if (chk330PM.isChecked()) {
            sel_timings += "03:30 PM,";
        }

        if (chk4PM.isChecked()) {
            sel_timings += "04:00 PM,";
        }

        if (chk430PM.isChecked()) {
            sel_timings += "04:30 PM,";
        }

        sel_timings = sel_timings.substring(0, sel_timings.length() - 1);

        loadingBar = new ProgressDialog(AddDateSlotsActivity.this);
        loadingBar.setMessage("Please wait, Appointment is being created");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();
        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                SharedPreferences sp = getSharedPreferences("AA", 0);
                Calendar cal = Calendar.getInstance(Locale.ENGLISH);
                //String str=sp.getString("suname","-")+"_"+cal.getTimeInMillis();
                String str = sp.getString("auname", "-") + "_" + btn_select_date.getText().toString();
                HashMap<String, Object> userdataMap = new HashMap<>();
                userdataMap.put("adv_username", sp.getString("auname", "-"));
                userdataMap.put("booking_date", btn_select_date.getText().toString());
                userdataMap.put("booking_times", sel_timings);
                RootRef.child("Advisor_Availability").child(str).updateChildren(userdataMap)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(AddDateSlotsActivity.this, "Congratulations, your appointment has been created.", Toast.LENGTH_SHORT).show();
                                    loadingBar.dismiss();
                                } else {
                                    loadingBar.dismiss();
                                    Toast.makeText(AddDateSlotsActivity.this, "Network Error: Please try again after some time...", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
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
}