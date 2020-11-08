package com.example.cegepaas.Adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;

public class StudentMeetingAdapter extends BaseAdapter {
    List<AdvisorBookingPojo> ar;
    Context cnt;
    String status;
    DatabaseReference RootRef;
    String uname;

    public StudentMeetingAdapter(String status,List<AdvisorBookingPojo> ar, String uname,Context cnt) {
        this.status = status;
        this.ar = ar;
        this.cnt = cnt;
        this.uname=uname;
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
        LayoutInflater obj1;
        View obj2;
        if(status.equals("accept")){
            obj1 = (LayoutInflater) cnt.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            obj2 = obj1.inflate(R.layout.adapter_student_accept, null);
        }
        else {
            obj1 = (LayoutInflater) cnt.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            obj2 = obj1.inflate(R.layout.adapter_student_meeting, null);
        }
        TextView tv_name = (TextView) obj2.findViewById(R.id.tv_name);
        getAdvisorName(tv_name,pos);
        //tv_name.setText("Advisor Uname  :" + ar.get(pos).getAdv_username());

        TextView tv_date_time = (TextView) obj2.findViewById(R.id.tv_date_time);
        tv_date_time.setText("Booking Slot: " + ar.get(pos).getBooked_date() + "  " + ar.get(pos).getBooked_time());

        TextView tv_desc = (TextView) obj2.findViewById(R.id.tv_desc);
        tv_desc.setText("Details  :" + ar.get(pos).getDescription());

        if(status.equals("accept")){
            Button btn_cancle_meet=(Button)obj2.findViewById(R.id.btn_cancle_meet);
            btn_cancle_meet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cancelMeeting(ar.get(pos).getTimestamp(),pos);

                }
            });
        }
        return obj2;
    }
    private void cancelMeeting(String timestamp,int pos){
        RootRef = FirebaseDatabase.getInstance().getReference();
        AlertDialog.Builder builder = new AlertDialog.Builder(cnt);
        builder.setTitle("Delete");
        builder.setMessage("Are you Sure Want to Cancle this Meeting");
        builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                Query query = RootRef.child("Advisor_Booking").orderByChild("timestamp").equalTo(timestamp);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()){
                            ds.getRef().removeValue();
                        }
                        //cnt.startActivity(new Intent(cnt, StudentMeetingsActivity.class));
                        Toast.makeText(cnt, "This Meeting is deleted", Toast.LENGTH_SHORT).show();
                        ar.remove(pos);
                        notifyDataSetChanged();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dialogInterface.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
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