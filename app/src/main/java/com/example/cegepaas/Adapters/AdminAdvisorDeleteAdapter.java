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

import com.bumptech.glide.Glide;
import com.example.cegepaas.Model.AdvisorsPojo;
import com.example.cegepaas.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;

/**
 * AdminAdvisorDeleteAdapter class to load the AdminAdvisorDelete details.
 */
public class AdminAdvisorDeleteAdapter extends BaseAdapter
{

    List<AdvisorsPojo> ar;
    Context cnt;

    /**
     * AdminAdvisorDeleteAdapter constructor to get the details
     * @param ar list of the Advisors
     * @param cnt context
     */
    public AdminAdvisorDeleteAdapter(List<AdvisorsPojo> ar, Context cnt)
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
    public View getView(final int pos, View view, ViewGroup viewGroup)
    {
        LayoutInflater obj1 = (LayoutInflater) cnt.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View obj2 = obj1.inflate(R.layout.adapter_admin_advisor_delete, null);

        TextView tv_aname = (TextView) obj2.findViewById(R.id.tv_aname);
        tv_aname.setText(ar.get(pos).getName());

        TextView tv_aemail = (TextView) obj2.findViewById(R.id.tv_aemail);
        tv_aemail.setText(ar.get(pos).getEmail());


        ImageView img_profile = (ImageView) obj2.findViewById(R.id.img_profile);
        Glide.with(cnt).load(ar.get(pos).getImage()).into(img_profile);

        // Toast.makeText(cnt,ar.get(pos).getImage(),Toast.LENGTH_SHORT).show();

        CardView cvParent = (CardView) obj2.findViewById(R.id.cvParent);

        final Button btn_delete = (Button) obj2.findViewById(R.id.btn_delete);
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteAdvisor(ar.get(pos), btn_delete);
            }
        });

        return obj2;
    }

    /**
     * Deletes the details in the database
     * @param adv Advisor detsails
     * @param btn button
     */
    private void deleteAdvisor(final AdvisorsPojo adv, final Button btn) {
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if ((dataSnapshot.child("Advisor_Details").child(adv.getUsername()).exists())) {
                    HashMap<String, Object> userdataMap = new HashMap<>();
                    RootRef.child("Advisor_Details").child(adv.getUsername()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(cnt, "Advisor Deleted..", Toast.LENGTH_SHORT).show();
                            } else {

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