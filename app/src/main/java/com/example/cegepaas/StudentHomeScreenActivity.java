package com.example.cegepaas;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.cegepaas.Adapters.StudentHomeAdapter;
import com.example.cegepaas.Model.AdvisorsPojo;
import com.example.cegepaas.Model.ResponseData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * StudentHomeScreenActivity is used to get the data on the student home screen.
 */
public class StudentHomeScreenActivity extends AppCompatActivity {
    private ActionBarDrawerToggle t;
    private NavigationView nv;
    private DrawerLayout dl;
    FloatingActionButton studentNotification;
    ListView lv;
    EditText et_search;

    /**
     * onCreate method is the main method that will trigger when the activity starts.
     * @param savedInstanceState Bundle object.
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_home_screen);
        registrationToken();
        navigationView();
        ActionBar mActionBar = getSupportActionBar();
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);
        mActionBar.setDisplayShowCustomEnabled(true);

        getSupportActionBar().setTitle("Home");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        lv = (ListView) findViewById(R.id.lv);
        et_search = (EditText) findViewById(R.id.et_search);
        studentNotification = findViewById(R.id.student_notification);
        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                //after the change calling the method and passing the search input
                filter(editable.toString());
            }
        });

        studentNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StudentHomeScreenActivity.this,StudentNotificationsActivity.class);
                startActivity(intent);
            }
        });

        getAdvisorsDetails();
    }

    /**
     * This methos is used to filter the advisors list.
     * @param text get the typed list.
     */
    private void filter(String text) {
        mAdvisorsTemp.clear();
        //Toast.makeText(getApplicationContext(),text+" "+mAdvisors.size(),Toast.LENGTH_SHORT).show();
        for (AdvisorsPojo adv : mAdvisors) {
            if (adv.getEmail() != null) {
                if (adv.getEmail().toLowerCase().contains(text.toLowerCase())) {
                    mAdvisorsTemp.add(adv);
                }
            }
        }

        if (mAdvisorsTemp.size() <= 0) {
            mAdvisorsTemp = mAdvisors;
        }
        // Toast.makeText(getApplicationContext()," "+mAdvisorsTemp.size(),Toast.LENGTH_SHORT).show();
        sha.filterList(mAdvisorsTemp);
    }

    ProgressDialog progressDialog;
    private List<AdvisorsPojo> mAdvisors;
    private List<AdvisorsPojo> mAdvisorsTemp;
    DatabaseReference dbAdvisors;
    StudentHomeAdapter sha;

    /**
     * This method is used to get the advisor details from the database.
     */
    private void getAdvisorsDetails() {
        mAdvisors = new ArrayList<>();
        mAdvisorsTemp = new ArrayList<>();
        progressDialog = new ProgressDialog(StudentHomeScreenActivity.this);
        progressDialog.setTitle("Please Wait data is being Loaded");
        progressDialog.show();
        dbAdvisors = FirebaseDatabase.getInstance().getReference("Advisor_Details");
        dbAdvisors.addListenerForSingleValueEvent(valueEventListener);
    }

    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            progressDialog.dismiss();
            mAdvisors.clear();
            if (dataSnapshot.exists()) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    AdvisorsPojo artist = snapshot.getValue(AdvisorsPojo.class);
                    mAdvisors.add(artist);
                }
                if (mAdvisors.size() > 0) {
                    sha = new StudentHomeAdapter(mAdvisors, StudentHomeScreenActivity.this);
                    lv.setAdapter(sha);
                }
            } else {
                Toast.makeText(StudentHomeScreenActivity.this, "No data Found", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            progressDialog.dismiss();

        }
    };


    /**
     * This method is used for the navigation drawer.
     */
    private void navigationView() {
        dl = (DrawerLayout) findViewById(R.id.activity_main);
        t = new ActionBarDrawerToggle(this, dl, R.string.Open, R.string.Close);
        dl.addDrawerListener(t);
        t.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        nv = (NavigationView) findViewById(R.id.nv);
        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id) {
                    case R.id.profile:
                        Intent profile = new Intent(getApplicationContext(), StudentProfileActivity.class);
                        startActivity(profile);
                        break;
                    case R.id.accept_meetings:
                        Intent intent_accept = new Intent(getApplicationContext(), StudentMeetingsActivity.class);
                        intent_accept.putExtra("fg", "accept");
                        startActivity(intent_accept);
                        break;
                    case R.id.reject_meetings:
                        Intent intent_reject = new Intent(getApplicationContext(), StudentMeetingsActivity.class);
                        intent_reject.putExtra("fg", "reject");
                        startActivity(intent_reject);
                        break;
                    case R.id.pending_meetings:
                        Intent intent_pending = new Intent(getApplicationContext(), StudentMeetingsActivity.class);
                        intent_pending.putExtra("fg", "pending");
                        startActivity(intent_pending);
                        break;
                    case R.id.logout:
                        Intent logout = new Intent(getApplicationContext(), StudentLoginActivity.class);
                        startActivity(logout);

                        finish();
                        break;

                    default:
                        return true;
                }
                dl.closeDrawer(GravityCompat.START);
                return true;

            }
        });
    }

    /**
     * This method is used to get back by onBackPressed.
     */
    @Override
    public void onBackPressed() {
        if (dl.isDrawerOpen(GravityCompat.START)) {
            dl.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /**
     * used to have the back button in that particular activity
     * @param item selected menu item.
     * @return returns to the home page.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (dl.isDrawerOpen(GravityCompat.START)) {
            dl.closeDrawer(GravityCompat.START);
        } else {
            dl.openDrawer(GravityCompat.START);
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * This method is used to get the registration token.
     */
    private void registrationToken(){
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            return;
                        }
                        String token = task.getResult().getToken();
                        submitdata(token);
                    }
                });
    }

    /**
     * This method is used to submit data on the token reference.
     * @param token string value.
     */
    public  void submitdata(String token) {
        SharedPreferences sp=getSharedPreferences("AA",0);
        EndPointUrl apiService = RetrofitInstance.getRetrofitInstance().create(EndPointUrl.class);
        Call<ResponseData> call = apiService.fcm_registor(token,sp.getString("auname","-"));
        call.enqueue(new Callback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
            }
            @Override
            public void onFailure(Call<ResponseData> call, Throwable t) {
            }
        });
    }


}