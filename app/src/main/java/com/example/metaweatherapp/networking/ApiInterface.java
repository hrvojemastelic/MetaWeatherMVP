package com.example.metaweatherapp.networking;

import com.example.metaweatherapp.pojo.ConsolidatedWeather;
import com.example.metaweatherapp.pojo.Location;
import com.example.metaweatherapp.pojo.ResponseModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

//import com.example.metaretrofit.pojo.PostModel;

public interface ApiInterface {

    @GET("/api/location/search/")
    Call <List<Location>> getName(@Query("query") String query);

    @GET("/api/location/{woeid}")
    Call <ResponseModel> getFiveDayForecast(@Path("woeid") String woeid);

    @GET("/api/location/search/")
    Call<List<Location>> getLocation(@Query("lattlong") String lattlong);
}
