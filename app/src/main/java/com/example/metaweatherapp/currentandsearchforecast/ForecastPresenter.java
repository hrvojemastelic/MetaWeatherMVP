package com.example.metaweatherapp.currentandsearchforecast;


import com.example.metaweatherapp.networking.ApiClient;
import com.example.metaweatherapp.networking.ApiInterface;
import com.example.metaweatherapp.pojo.Location;
import com.example.metaweatherapp.pojo.ResponseModel;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForecastPresenter implements ForecastContract.ForecastPresenter {
    ForecastContract.MainView mainView;

    public ForecastPresenter(ForecastContract.MainView mainView) {
        this.mainView=mainView;
    }

    @Override
    public void getCityName(String query) {



        //Search BY CITYNAME IN SEARCHVIEW
        //GETS SEARCHED CITY LATITUDE AND LONGITUDE
        //AND WOIED
       ApiInterface apiInterface=ApiClient.getRetrofitInstance().create(ApiInterface.class);
        Call<List<Location>> call =apiInterface.getName(query);
        call.enqueue(new Callback<List<Location>>() {
            @Override
            public void onResponse(Call<List<Location>> call, Response<List<Location>> response) {
                if (response.body().size()!=0) {
                    Location location= response.body().get(0);
                    mainView.setValuesByName(location);
                }

            }

            @Override
            public void onFailure(Call<List<Location>>call, Throwable t) {
            }
        });
    }

    //SEARCH BY WOEID NUMBER
    //GETS FIVE DAY FORECAST
    @Override
    public void getWoeid(String woeid) {
        ApiInterface weatherInterface=ApiClient.getRetrofitInstance().create(ApiInterface.class);
        Call<ResponseModel> call =weatherInterface.getFiveDayForecast(woeid);
        call.enqueue(new Callback <ResponseModel>() {
            @Override
            public void onResponse(Call <ResponseModel> call, Response<ResponseModel>response) {
                if (response.body()!=null) {
                    mainView.setLocationValues(response.body().getConsolidatedWeather());
                }
            }

            @Override
            public void onFailure(Call <ResponseModel>call, Throwable t) {
                System.out.println("locationn"+ t.getMessage().toString());
            }
        });
    }
    //GETS WOEID FROM LATITUDE AND LONGITUDE
    //WHICH IS NEEDED FOR FIVE DAY FORECAST
    @Override
    public void getLattLong(String lattlong) {
        ApiInterface apiInterface=ApiClient.getRetrofitInstance().create(ApiInterface.class);
        Call<List<Location>> call =apiInterface.getLocation(lattlong);
        call.enqueue(new Callback<List<Location>>() {
            @Override
            public void onResponse(Call<List<Location>> call, Response<List<Location>> response) {
                if (response.isSuccessful()) {

                    Location location= response.body().get(0);
                    mainView.setWoeidValues(String.valueOf(location.getWoeid()),String.valueOf(location.getTitle()),String.valueOf(location.getLocationType()));


                }
                System.out.println("notgood"+ String.valueOf(response.body()));

            }

            @Override
            public void onFailure(Call<List<Location>>call, Throwable t) {
                System.out.println("locationn"+ t.getMessage().toString());
            }
        });
    }



}
