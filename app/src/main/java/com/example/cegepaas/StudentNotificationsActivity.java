package com.example.cegepaas;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

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

/**
 * StudentNotificationsActivity is used to get the notifications.
 */
public class StudentNotificationsActivity extends AppCompatActivity {
    ListView lv;
    List<NotificationPojo> notificationPojos;
    SharedPreferences sharedPreferences;
    String session,date;

    /**
     * onCreate method is the main method that will trigger when the activity starts.
     * @param savedInstanceState Bundle object.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_notifications);

        sharedPreferences =getSharedPreferences("AA", Context.MODE_PRIVATE);
        session = sharedPreferences.getString("suname", "def-val");

        date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        //Toast.makeText(this, ""+date, Toast.LENGTH_SHORT).show();


        getSupportActionBar().setTitle("Notifications");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        lv=(ListView)findViewById(R.id.lv);
        notificationPojos= new ArrayList<>();
        serverData();
    }
    ProgressDialog progressDialog;
    public void serverData(){
        progressDialog = new ProgressDialog(StudentNotificationsActivity.this);
        progressDialog.setMessage("Loading....");
        progressDialog.show();

        EndPointUrl service = RetrofitInstance.getRetrofitInstance().create(EndPointUrl.class);
        Call<List<NotificationPojo>> call = service.get_notiication(date,session);
        call.enqueue(new Callback<List<NotificationPojo>>() {
            @Override
            public void onResponse(Call<List<NotificationPojo>> call, Response<List<NotificationPojo>> response) {
                // Toast.makeText(StudentNotificationsActivity.this,response.body().size(),Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
                if(response.body()==null){
                    Toast.makeText(StudentNotificationsActivity.this,"No data found",Toast.LENGTH_SHORT).show();
                }else {
                    notificationPojos = response.body();
                    lv.setAdapter(new StudentNotificationAdapter(notificationPojos, StudentNotificationsActivity.this));

                }
            }

            @Override
            public void onFailure(Call<List<NotificationPojo>> call, Throwable t) {
                progressDialog.dismiss();

                Toast.makeText(StudentNotificationsActivity.this, "Something went wrong...Please try later!"+t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * used to have the back button in that particular activity
     * @param item selected menu item.
     * @return returns to the home page.
     */
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