package com.simoncariou.pepperhome;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

//Class that returns a retrofit object built
public class RetrofitInstance {
    public static final String BASE_URL = "https://192.168.1.10/";
    public static Retrofit retrofit = null;

    public static Retrofit getClient(){
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(CertificateAuth.getUnsafeOkHttpClient().build())
                .build();
        return retrofit;
    }
    //LightService lService = retrofit.create(LightService.class);
}
