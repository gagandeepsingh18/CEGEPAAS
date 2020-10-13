package com.example.cegepaas;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
    Button btn_select_date, btn_submit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_date_slots);
        btn_submit = (Button) findViewById(R.id.btn_submit);
        btn_select_date = (Button) findViewById(R.id.btn_select_date);
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                advisorBooking();
            }
        });
        btn_select_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datepicker();
            }
        });
    }

    int mYear, mMonth, mDay;
    String DAY, MONTH, YEAR;

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
                        btn_select_date.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.getDatePicker().setMinDate(new Date().getTime());
        datePickerDialog.show();
    }

    ProgressDialog loadingBar;

    private void advisorBooking() {
        if (btn_select_date.getText().toString().length() < 3) {
            Toast.makeText(getApplicationContext(), "Please Select Date.", Toast.LENGTH_SHORT).show();
            return;
        }

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
                if (!(dataSnapshot.child("Advisor_Availability").child(str).exists())) {
                    HashMap<String, Object> userdataMap = new HashMap<>();
                    userdataMap.put("adv_username", sp.getString("auname", "-"));
                    userdataMap.put("booking_date", btn_select_date.getText().toString());
                    RootRef.child("Advisor_Availability").child(str).updateChildren(userdataMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(AddDateSlotsActivity.this, "Congratulations, your appointment has been created.", Toast.LENGTH_SHORT).show();
                                        loadingBar.dismiss();
                                        finish();
                                    } else {
                                        loadingBar.dismiss();
                                        Toast.makeText(AddDateSlotsActivity.this, "Network Error: Please try again after some time...", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                } else {
                    Toast.makeText(AddDateSlotsActivity.this, "Date is already taken, Choose another date.", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}