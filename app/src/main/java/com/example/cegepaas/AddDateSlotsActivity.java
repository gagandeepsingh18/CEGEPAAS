package com.example.cegepaas;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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

public class AddDateSlotsActivity extends AppCompatActivity {
    Button btn_select_date,btn_submit;
    CheckBox chk10AM,chk1030AM,chk11AM,chk1130AM,chk12PM,chk130PM,chk2PM,chk230PM,chk3PM,chk330PM,chk4PM,chk430PM;
    int mYear,mMonth,mDay;
    String DAY,MONTH,YEAR;
    ProgressDialog loadingBar;
    String sel_timings="";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_date_slots);
        getSupportActionBar().setTitle("Add Time Slots");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        btn_submit=(Button)findViewById(R.id.btn_submit);
        btn_select_date=(Button)findViewById(R.id.btn_select_date);
        chk10AM=(CheckBox)findViewById(R.id.chk10_00AM);
        chk1030AM=(CheckBox)findViewById(R.id.chk10_30AM);
        chk11AM=(CheckBox)findViewById(R.id.chk11_00AM);
        chk1130AM=(CheckBox)findViewById(R.id.chk11_30AM);
        chk12PM=(CheckBox)findViewById(R.id.chk12_00PM);
        chk130PM=(CheckBox)findViewById(R.id.chk01_30PM);
        chk2PM=(CheckBox)findViewById(R.id.chk02_00PM);
        chk230PM=(CheckBox)findViewById(R.id.chk02_30PM);
        chk3PM=(CheckBox)findViewById(R.id.chk03_00PM);
        chk330PM=(CheckBox)findViewById(R.id.chk03_30PM);
        chk4PM=(CheckBox)findViewById(R.id.chk04_00PM);
        chk430PM=(CheckBox)findViewById(R.id.chk04_30PM);
        enableDisableCheckbox();
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btn_select_date.getText().toString().equals("Select Date")){
                    Toast.makeText(getApplicationContext(),"Please Select Date.",Toast.LENGTH_SHORT).show();
                }
                else {
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
    }

    void enableDisableCheckbox(){
        if(btn_select_date.getText().toString().equals("Select Date")){
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
        }
        else {
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
                        enableDisableCheckbox();
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.getDatePicker().setMinDate(new Date().getTime());
        c.add(Calendar.DAY_OF_MONTH,+14);
        datePickerDialog.getDatePicker().setMaxDate(c.getTimeInMillis());
        datePickerDialog.show();
    }

    private void advisorBooking() {
        sel_timings="";
        if(chk10AM.isChecked()){
            sel_timings+="10:00AM,";
        }

        if(chk1030AM.isChecked()){
            sel_timings+="10:30AM,";
        }

        if(chk11AM.isChecked()){
            sel_timings+="11:00AM,";
        }

        if(chk1130AM.isChecked()){
            sel_timings+="11:30AM,";
        }

        if(chk12PM.isChecked()){
            sel_timings+="12:00PM,";
        }

        if(chk130PM.isChecked()){
            sel_timings+="1:30PM,";
        }

        if(chk2PM.isChecked()){
            sel_timings+="2:00PM,";
        }

        if(chk230PM.isChecked()){
            sel_timings+="2:30PM,";
        }

        if(chk3PM.isChecked()){
            sel_timings+="3:00PM,";
        }

        if(chk330PM.isChecked()){
            sel_timings+="3:30PM,";
        }

        if(chk4PM.isChecked()){
            sel_timings+="4:00PM,";
        }

        if(chk430PM.isChecked()){
            sel_timings+="4:30PM,";
        }

        sel_timings=sel_timings.substring(0,sel_timings.length()-1);

        loadingBar = new ProgressDialog(AddDateSlotsActivity.this);
        loadingBar.setMessage("Please wait, Appointment is being created");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();
        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                SharedPreferences sp=getSharedPreferences("AA",0);
                Calendar cal = Calendar.getInstance(Locale.ENGLISH);
                //String str=sp.getString("suname","-")+"_"+cal.getTimeInMillis();
                String str = sp.getString("auname","-")+"_"+btn_select_date.getText().toString();
                if (!(dataSnapshot.child("Advisor_Availability").child(str).exists())) {
                    HashMap<String, Object> userdataMap = new HashMap<>();
                    userdataMap.put("adv_username",sp.getString("auname","-"));
                    userdataMap.put("booking_date",  btn_select_date.getText().toString());
                    userdataMap.put("booking_times", sel_timings);
                    RootRef.child("Advisor_Availability").child(str).updateChildren(userdataMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task)
                                {
                                    if (task.isSuccessful())
                                    {
                                        Toast.makeText(AddDateSlotsActivity.this, "Congratulations, your appointment has been created.", Toast.LENGTH_SHORT).show();
                                        loadingBar.dismiss();
                                        finish();
                                    }
                                    else
                                    {
                                        loadingBar.dismiss();
                                        Toast.makeText(AddDateSlotsActivity.this, "Network Error: Please try again after some time...", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
                else{
                    Toast.makeText(AddDateSlotsActivity.this, "Date is already taken, Choose another date.", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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
}