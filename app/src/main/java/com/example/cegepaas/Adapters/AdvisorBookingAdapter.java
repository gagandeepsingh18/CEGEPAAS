package com.example.cegepaas.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.cegepaas.R;
import com.example.cegepaas.Model.BookingTimesPojo;

import java.util.List;

/**
 * AdvisorBookingAdapter class is used to load the list of AdvisorBooking details
 */
public class AdvisorBookingAdapter extends BaseAdapter {
    List<BookingTimesPojo> ar;
    Context cnt;

    /**
     * Constructor is used to get the details
     * @param ar Booking times  details
     * @param cnt context
     */
    public AdvisorBookingAdapter(List<BookingTimesPojo> ar, Context cnt) {
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
        View obj2 = obj1.inflate(R.layout.row_time, null);

        TextView tv_time = (TextView) obj2.findViewById(R.id.tv_time);
        tv_time.setText(ar.get(pos).getTime());
        if (ar.get(pos).getAvailable().equals("accept")) {
            tv_time.setTextColor(Color.RED);
        } else if (ar.get(pos).getAvailable().equals("pending")) {
            tv_time.setTextColor(Color.YELLOW);
        }

        return obj2;
    }
}
