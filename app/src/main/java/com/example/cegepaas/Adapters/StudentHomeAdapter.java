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
import com.example.cegepaas.StudentAdvisorDetails;

import java.util.List;

/**
 * StudentHomeAdapter class is used to load the list of Advisors details
 */
public class StudentHomeAdapter extends BaseAdapter
{
    List<AdvisorsPojo> ar;
    Context cnt;

    /**
     * Constructor is used to get the details
     * @param ar Advisor details
     * @param cnt context
     */
    public StudentHomeAdapter(List<AdvisorsPojo> ar, Context cnt)
    {
        this.ar = ar;
        this.cnt = cnt;
    }

    /**
     * To get the count
     * @return returns the count.
     */
    @Override
    public int getCount()
    {
        return ar.size();
    }

    /**
     * To get the item.
     * @param i passing the value item
     * @return returns the object value
     */
    @Override
    public Object getItem(int i)
    {
        return i;
    }

    /**
     * Return the Id
     * @param i passing the id
     * @return returns the item Id
     */
    @Override
    public long getItemId(int i)
    {
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
            public void onClick(View view)
            {
                Intent intent = new Intent(cnt, StudentAdvisorDetails.class);
                intent.putExtra("uname", ar.get(pos).getUsername());
                intent.putExtra("aname", ar.get(pos).getName());
                cnt.startActivity(intent);
            }
        });

        return obj2;
    }

    /**
     * To filter the list
     * @param advisors Advisors list
     */
    public void filterList(List<AdvisorsPojo> advisors)
    {
        this.ar = advisors;
        notifyDataSetChanged();
    }
}
