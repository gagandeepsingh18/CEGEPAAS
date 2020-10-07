package com.example.cegepaas.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;

import com.example.cegepaas.R;
import com.bumptech.glide.Glide;
import com.example.cegepaas.Model.AdvisorsPojo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;

public class AdminHomeAdapter extends BaseAdapter {
    List<AdvisorsPojo> ar;
    Context cnt;

    public AdminHomeAdapter(List<AdvisorsPojo> ar, Context cnt) {
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
        View obj2 = obj1.inflate(R.layout.adapter_admin_home, null);

        TextView tv_aname = (TextView) obj2.findViewById(R.id.tv_aname);
        tv_aname.setText(ar.get(pos).getName());

        TextView tv_aemail = (TextView) obj2.findViewById(R.id.tv_aemail);
        tv_aemail.setText(ar.get(pos).getEmail());


        ImageView img_profile = (ImageView) obj2.findViewById(R.id.img_profile);
        Glide.with(cnt).load(ar.get(pos).getImage()).into(img_profile);

        // Toast.makeText(cnt,ar.get(pos).getImage(),Toast.LENGTH_SHORT).show();

        CardView cvParent=(CardView)obj2.findViewById(R.id.cvParent);

        final Button btn_active = (Button) obj2.findViewById(R.id.btn_active);
        btn_active.setText(""+ar.get(pos).getStatus());
        btn_active.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ValidateDetails(ar.get(pos),btn_active);
            }
        });

        return obj2;
    }

    private void ValidateDetails(final AdvisorsPojo adv,final Button btn) {
//Toast.makeText(getApplicationContext(),"vv",Toast.LENGTH_SHORT).show();
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if ((dataSnapshot.child("Advisor_Details").child(adv.getUsername()).exists()))
                {
                    HashMap<String, Object> userdataMap = new HashMap<>();
                    /*userdataMap.put("image", adv.getImage());
                    userdataMap.put("name", adv.getName());
                    userdataMap.put("email", adv.getEmail());
                    userdataMap.put("username", adv.getUsername());
                    userdataMap.put("password", adv.getPassword());*/
                    userdataMap.put("status", "active");
                    RootRef.child("Advisor_Details").child(adv.getUsername()).updateChildren(userdataMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task)
                                {
                                    if (task.isSuccessful())
                                    {
                                        Toast.makeText(cnt, "Status is updated successfully.", Toast.LENGTH_SHORT).show();
                                        btn.setText("Active");

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

}
