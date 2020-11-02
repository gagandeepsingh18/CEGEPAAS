package com.example.cegepaas;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cegepaas.Adapters.StudentNotificationAdapter;
import com.example.cegepaas.Model.NotificationPojo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdvisorNotificationsActivity extends AppCompatActivity {
    ListView lv;
    List<NotificationPojo> notificationPojos;
    SharedPreferences sharedPreferences;
    String session,date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advisor_notifications);

        sharedPreferences =getSharedPreferences("AA", Context.MODE_PRIVATE);
        session = sharedPreferences.getString("auname", "def-val");

        date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        getSupportActionBar().setTitle("Notifications");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        lv=(ListView)findViewById(R.id.lv);
        notificationPojos= new ArrayList<>();
        serverData();
    }
    ProgressDialog progressDialog;
    public void serverData(){
        progressDialog = new ProgressDialog(AdvisorNotificationsActivity.this);
        progressDialog.setMessage("Loading....");
        progressDialog.show();

        EndPointUrl service = RetrofitInstance.getRetrofitInstance().create(EndPointUrl.class);
        Call<List<NotificationPojo>> call = service.get_notiication(date,session);
        call.enqueue(new Callback<List<NotificationPojo>>() {
            @Override
            public void onResponse(Call<List<NotificationPojo>> call, Response<List<NotificationPojo>> response) {
                progressDialog.dismiss();
                if(response.body()==null){
                    Toast.makeText(AdvisorNotificationsActivity.this,"No data found", Toast.LENGTH_SHORT).show();
                }else {
                    notificationPojos = response.body();
                    lv.setAdapter(new StudentNotificationAdapter(notificationPojos, AdvisorNotificationsActivity.this));

                }
            }

            @Override
            public void onFailure(Call<List<NotificationPojo>> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(AdvisorNotificationsActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}