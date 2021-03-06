package com.example.cegepaas.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

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

/**
 * AdvisorMeetingAdapter class is used to load the list of AdvisorMeeting details
 */
public class AdvisorMeetingAdapter extends BaseAdapter {
    List<AdvisorBookingPojo> ar;
    Context cnt;

    /**
     * Constructor is used to get the details
     * @param ar Advisor Booking details
     * @param cnt context
     */
    public AdvisorMeetingAdapter(List<AdvisorBookingPojo> ar, Context cnt) {
        this.ar = ar;
        this.cnt = cnt;
    }

    /**
     * To get the count
     * @return returns the count.
     */
    @Override
    public int getCount() {
        return ar.size();
    }

    /**
     * To get the item.
     * @param i passing the value item
     * @return returns the object value
     */
    @Override
    public Object getItem(int i) {
        return i;
    }

    /**
     * Return the Id
     * @param i passing the id
     * @return returns the item Id
     */
    @Override
    public long getItemId(int i) {
        return i;
    }

    /**
     * Gets the view type
     * @param pos shows the position
     * @param view inflate the view
     * @param viewGroup rootValue
     * @return reflects the page
     */
    @Override
    public View getView(final int pos, View view, ViewGroup viewGroup) {
        LayoutInflater obj1 = (LayoutInflater) cnt.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View obj2 = obj1.inflate(R.layout.adapter_student_meeting, null);

        TextView tv_name = (TextView) obj2.findViewById(R.id.tv_name);
        getStudentName(tv_name,pos);

        TextView tv_date_time = (TextView) obj2.findViewById(R.id.tv_date_time);
        tv_date_time.setText("Booking Slot: " + ar.get(pos).getBooked_date() + "  " + ar.get(pos).getBooked_time());

        TextView tv_desc = (TextView) obj2.findViewById(R.id.tv_desc);
        tv_desc.setText("Details: " + ar.get(pos).getDescription());
        return obj2;
    }

    /**
     * Gets the student details
     * @param tv_name student name
     * @param pos respective position
     */
    private void getStudentName( TextView tv_name, int pos) {
        String studentId = ar.get(pos).getBooked_by();
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();
        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.child("Student_Details").child(studentId).exists()) {
                    Users users = snapshot.child("Student_Details").child(studentId).getValue(Users.class);
                    tv_name.setText("Student: "+users.getName()+" (" + studentId+")");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(cnt, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    /**
     * Updating the status in the database
     * @param status status
     * @param time_stamp time stamp
     */
    private void UpdateStatus(final String status, final String time_stamp) {
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if ((dataSnapshot.child("Advisor_Booking").child(time_stamp).exists())) {
                    HashMap<String, Object> userdataMap = new HashMap<>();
                    /*userdataMap.put("image", adv.getImage());
                    userdataMap.put("name", adv.getName());
                    userdataMap.put("email", adv.getEmail());
                    userdataMap.put("username", adv.getUsername());
                    userdataMap.put("password", adv.getPassword());*/
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