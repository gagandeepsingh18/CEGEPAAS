package com.example.cegepaas;

import com.example.cegepaas.Model.FCMPojo;
import com.example.cegepaas.Model.NotificationPojo;
import com.example.cegepaas.Model.ResponseData;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface EndPointUrl {
    @GET("cegepaas/aa_fcm_register.php")
    Call<ResponseData> fcm_registor(@Query("fcm_id") String fcm_id, @Query("uname") String uname);
    @GET("cegepaas/aa_send_notification.php")
    Call<FCMPojo> send_advisor_notification(@Query("uname") String uname, @Query("user_type") String user_type, @Query("title") String title, @Query("msg") String msg);


    @GET("/cegepaas/get_notiication.php?")
    Call<List<NotificationPojo>> get_notiication(
            @Query("created_date") String created_date,
            @Query("uname") String uname
    );
}
