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

/**
 * StudentNotificationAdapter class is used to load the list of StudentNotification details
 */
public class StudentNotificationAdapter extends BaseAdapter {
    List<NotificationPojo> ar;
    Context cnt;


    /**
     * Constructor is used to get the details
     * @param ar Booking times  details
     * @param cnt context
     */
    public StudentNotificationAdapter(List<NotificationPojo> ar, Context cnt) {
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
        View obj2 = obj1.inflate(R.layout.adapter_notification, null);

        TextView tv_aname = (TextView) obj2.findViewById(R.id.tv_aname);
        tv_aname.setText("Title :"+ar.get(pos).getTitle());

        TextView tv_aemail = (TextView) obj2.findViewById(R.id.tv_aemail);
        tv_aemail.setText("Message :"+ar.get(pos).getMsg());


        return obj2;
    }
}
