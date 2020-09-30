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
        return view;
    }
}
