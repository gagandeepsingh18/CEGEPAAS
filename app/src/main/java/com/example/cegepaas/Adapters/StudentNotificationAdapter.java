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

import com.bumptech.glide.Glide;
import com.example.cegepaas.Model.NotificationPojo;
import com.example.cegepaas.R;

import java.util.List;

public class StudentNotificationAdapter extends BaseAdapter {
    List<NotificationPojo> ar;
    Context cnt;

    public StudentNotificationAdapter(List<NotificationPojo> ar, Context cnt) {
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
        View obj2 = obj1.inflate(R.layout.adapter_notification, null);

        TextView tv_aname = (TextView) obj2.findViewById(R.id.tv_aname);
        tv_aname.setText("Title :"+ar.get(pos).getTitle());

        TextView tv_aemail = (TextView) obj2.findViewById(R.id.tv_aemail);
        tv_aemail.setText("Message :"+ar.get(pos).getMsg());


        return obj2;
    }
}
