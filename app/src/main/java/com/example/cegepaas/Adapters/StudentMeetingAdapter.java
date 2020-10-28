package com.example.cegepaas.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.cegepaas.Model.AdvisorsPojo;
import com.example.cegepaas.Model.Users;
import com.example.cegepaas.R;
import com.example.cegepaas.Model.AdvisorBookingPojo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;

public class StudentMeetingAdapter extends BaseAdapter {
    List<AdvisorBookingPojo> ar;
    Context cnt;

    public StudentMeetingAdapter(List<AdvisorBookingPojo> ar, Context cnt) {
        this.ar = ar;
        this.cnt = cnt;
    }

    @Override
    public int getCount() {
        return ar.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int pos, View view, ViewGroup viewGroup) {
        LayoutInflater obj1 = (LayoutInflater) cnt.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View obj2 = obj1.inflate(R.layout.adapter_student_meeting, null);

        TextView tv_name = (TextView) obj2.findViewById(R.id.tv_name);
        getAdvisorName(tv_name,pos);
        //tv_name.setText("Advisor Uname  :" + ar.get(pos).getAdv_username());

        TextView tv_date_time = (TextView) obj2.findViewById(R.id.tv_date_time);
        tv_date_time.setText("Booking Slot: " + ar.get(pos).getBooked_date() + "  " + ar.get(pos).getBooked_time());

        TextView tv_desc = (TextView) obj2.findViewById(R.id.tv_desc);
        tv_desc.setText("Details  :" + ar.get(pos).getDescription());
        return obj2;
    }

    private void getAdvisorName( TextView tv_name, int pos) {
        String advisorId = ar.get(pos).getAdv_username();
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();
        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.child("Advisor_Details").child(advisorId).exists()) {
                    AdvisorsPojo advisor = snapshot.child("Advisor_Details").child(advisorId).getValue(AdvisorsPojo.class);
                    tv_name.setText("Advisor: "+advisor.getName());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(cnt, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }


    private void UpdateStatus(final String status, final String time_stamp) {
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if ((dataSnapshot.child("Advisor_Booking").child(time_stamp).exists())) {
                    HashMap<String, Object> userdataMap = new HashMap<>();
                    userdataMap.put("status", status);
                    RootRef.child("Advisor_Booking").child(time_stamp).updateChildren(userdataMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(cnt, "Status is updated successfully.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }

            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}