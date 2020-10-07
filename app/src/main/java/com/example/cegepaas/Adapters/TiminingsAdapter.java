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
