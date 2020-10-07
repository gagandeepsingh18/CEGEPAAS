package com.example.cegepaas.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.example.cegepaas.AdvisorBookingActivity;
import com.example.cegepaas.R;
import com.bumptech.glide.Glide;
import com.example.cegepaas.Model.AdvisorsPojo;

import java.util.List;

public class StudentHomeAdapter extends BaseAdapter {
    List<AdvisorsPojo> ar;
    Context cnt;

    public StudentHomeAdapter(List<AdvisorsPojo> ar, Context cnt) {
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
        View obj2 = obj1.inflate(R.layout.adapter_student_home, null);


        TextView tv_aname = (TextView) obj2.findViewById(R.id.tv_aname);
        tv_aname.setText(ar.get(pos).getName());

        TextView tv_aemail = (TextView) obj2.findViewById(R.id.tv_aemail);
        tv_aemail.setText(ar.get(pos).getEmail());

        ImageView img_profile = (ImageView) obj2.findViewById(R.id.img_profile);
        Glide.with(cnt).load(ar.get(pos).getImage()).into(img_profile);

        CardView cvParent = (CardView) obj2.findViewById(R.id.cvParent);
        cvParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(cnt, AdvisorBookingActivity.class);
                intent.putExtra("uname", ar.get(pos).getUsername());
                cnt.startActivity(intent);
            }
        });

        return obj2;
    }
}
