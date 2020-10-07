package com.example.cegepaas.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cegepaas.R;

import com.example.cegepaas.Model.TimingsPOJO;

import java.util.List;

public class TiminingsAdapter extends BaseAdapter {
    List<TimingsPOJO> ar;
    Context cnt;

    public TiminingsAdapter(List<TimingsPOJO> ar, Context cnt) {
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
        View obj2 = obj1.inflate(R.layout.adapter_timings, null);


        TextView tv_schedule_time = (TextView) obj2.findViewById(R.id.tv_schedule_time);
        tv_schedule_time.setText(ar.get(pos).getTime());

        tv_schedule_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(cnt, "Succussfully Allocated " + ar.get(pos).getTime() + "  Slot", Toast.LENGTH_SHORT).show();

            }
        });

        return obj2;
    }
}
