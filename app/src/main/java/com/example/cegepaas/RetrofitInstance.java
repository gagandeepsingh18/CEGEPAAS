package com.example.cegepaas;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * RetrofitInstance Class
 */
public class RetrofitInstance {
    private static Retrofit retrofit;
    private static final String BASE_URL = "http://cegepaas.site/";

    /**
     * get retrofitInstance Method
     * @return : retrofit
     */
    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}