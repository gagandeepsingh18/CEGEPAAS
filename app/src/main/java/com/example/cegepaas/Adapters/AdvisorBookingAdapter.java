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

public class AdvisorBookingAdapter extends BaseAdapter {
    List<BookingTimesPojo> ar;
    Context cnt;

    public AdvisorBookingAdapter(List<BookingTimesPojo> ar, Context cnt) {
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
        View obj2 = obj1.inflate(R.layout.row_time, null);

        TextView tv_time = (TextView) obj2.findViewById(R.id.tv_time);
        tv_time.setText(ar.get(pos).getTime());
        if (ar.get(pos).getAvailable().equals("Booked")) {
            tv_time.setTextColor(Color.RED);
        }

        return obj2;
    }
}
