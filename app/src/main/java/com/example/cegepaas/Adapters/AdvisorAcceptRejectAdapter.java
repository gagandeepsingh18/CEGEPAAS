package com.example.cegepaas.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.cegepaas.EndPointUrl;
import com.example.cegepaas.Model.FCMPojo;
import com.example.cegepaas.R;
import com.example.cegepaas.Model.AdvisorBookingPojo;
import com.example.cegepaas.RetrofitInstance;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * AdvisorAcceptRejectAdapter class is used to load the list of AdvisorAcceptReject details
 */
public class AdvisorAcceptRejectAdapter extends BaseAdapter {
    List<AdvisorBookingPojo> ar;
    Context cnt;

    /**
     * Constructor is used to get the details
     * @param ar Advisors Booking details
     * @param cnt context
     */
    public AdvisorAcceptRejectAdapter(List<AdvisorBookingPojo> ar, Context cnt) {
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
        View obj2 = obj1.inflate(R.layout.adapter_advisor_home, null);

        TextView tv_name = (TextView) obj2.findViewById(R.id.tv_name);
        tv_name.setText("Booked By  :" + ar.get(pos).getBooked_by());

        TextView tv_date_time = (TextView) obj2.findViewById(R.id.tv_date_time);
        tv_date_time.setText("Date & Time :" + ar.get(pos).getBooked_date() + "  " + ar.get(pos).getBooked_time());

        TextView tv_desc = (TextView) obj2.findViewById(R.id.tv_desc);
        tv_desc.setText("Desc  :" + ar.get(pos).getDescription());

        Button btn_accept = (Button) obj2.findViewById(R.id.btn_accept);
        btn_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdateStatus("accept", ar.get(pos).getTimestamp());
                submitdata(ar.get(pos).getBooked_by(),"Accepted Status","Your requested is accepted.");
            }
        });


        Button btn_submit = (Button) obj2.findViewById(R.id.btn_submit);
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdateStatus("reject", ar.get(pos).getTimestamp());
                submitdata(ar.get(pos).getBooked_by(),"Rejected Status","Your requested is rejected.");
            }
        });

        return obj2;
    }

    /**
     * Updating the details in the database
     * @param status current status
     * @param time_stamp time stamp value
     */
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
    public void submitdata(String uname,String title,String msg) {
        EndPointUrl apiService = RetrofitInstance.getRetrofitInstance().create(EndPointUrl.class);
        Call<FCMPojo> call = apiService.send_advisor_notification(uname,"student",title,msg);
        call.enqueue(new Callback<FCMPojo>() {
            @Override
            public void onResponse(Call<FCMPojo> call, Response<FCMPojo> response) {
                Toast.makeText(cnt, "Notification sent successfully.", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onFailure(Call<FCMPojo> call, Throwable t) {
            }
        });
    }
}