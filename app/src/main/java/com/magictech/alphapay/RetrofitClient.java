package com.magictech.alphapay;

import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    // Replace this with your exact live localtunnel URL (Make sure it ends with '/')
    private static final String BASE_URL = "https://heavy-towns-scream.loca.lt/";
    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(ScalarsConverterFactory.create()) // <--- 1st priority for plain strings
                    .addConverterFactory(GsonConverterFactory.create())    // <--- 2nd priority for objects
                    .build();
        }
        return retrofit;
    }
}
