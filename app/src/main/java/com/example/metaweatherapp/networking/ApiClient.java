package com.example.metaweatherapp.networking;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

//import com.example.metaretrofit.pojo.PostModel;

public class ApiClient {

    private static final String BASE_URL="https://www.metaweather.com/api/";
    private static Retrofit retrofit;

  public static   Retrofit getRetrofitInstance(){
      if (retrofit==null) {
          retrofit=new Retrofit.Builder()
                  .baseUrl(BASE_URL)
                  .addConverterFactory(GsonConverterFactory.create())
                  .build();
      }
        return retrofit;

    }



}
