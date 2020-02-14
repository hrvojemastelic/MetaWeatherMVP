package com.example.metaweatherapp.mapforecast;

import com.example.metaweatherapp.networking.ApiClient;
import com.example.metaweatherapp.networking.ApiInterface;
import com.example.metaweatherapp.pojo.Location;
import com.example.metaweatherapp.pojo.ResponseModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapsPresenter implements MapsContract.MapsPresenter {
    MapsContract.MapsView mapsView;
    public MapsPresenter(MapsContract.MapsView mapsView) {
        this.mapsView=mapsView;
    }

    //GETS WOEID FROM LATITUDE AND LONGITUDE
    //WHICH IS NEEDED FOR FIVE DAY FORECAST
    @Override
    public void getNameAndWoeidByLocation(String lattlong) {

        ApiInterface apiInterface=ApiClient.getRetrofitInstance().create(ApiInterface.class);
        Call<List<Location>> call=apiInterface.getLocation(lattlong);
        call.enqueue(new Callback<List<Location>>() {
            @Override
            public void onResponse(Call<List<Location>> call, Response<List<Location>> response) {
                if (response.isSuccessful()) {

                    Location location=response.body().get(0);
                    String woied=String.valueOf(location.getWoeid());
                    String cityname=location.getTitle();
                    mapsView.setNameLocatio(cityname,woied);


                }


            }

            @Override
            public void onFailure(Call<List<Location>> call, Throwable t) {
                System.out.println("locationn" + t.getMessage().toString());
            }
        });

    }
    //SEARCH BY WOEID NUMBER
    //GETS FIVE DAY FORECAST
    @Override
    public void getFiveDayForecast(String woeid) {
        ApiInterface weatherInterface=ApiClient.getRetrofitInstance().create(ApiInterface.class);
        Call<ResponseModel> call =weatherInterface.getFiveDayForecast(woeid);
        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call <ResponseModel> call, Response<ResponseModel> response) {
                if (response.body()!=null) {
                    mapsView.setLocationValues(response.body().getConsolidatedWeather());

                }

            }

            @Override
            public void onFailure(Call <ResponseModel>call, Throwable t) {
                System.out.println("locationn"+ t.getMessage().toString());
            }
        });
    }
}
