package com.example.metaweatherapp.currentandsearchforecast;

import com.example.metaweatherapp.pojo.ConsolidatedWeather;
import com.example.metaweatherapp.pojo.Location;

import java.util.List;

public interface ForecastContract {

    interface MainView{
    void setValuesByName(Location locationValues);
    void setLocationValues(List<ConsolidatedWeather> consolidatedWeather);
    void setWoeidValues(String woeid,String title,String location_type);

    }
    interface ForecastPresenter{
        void getCityName(String query);
        void getWoeid( String woeid);
        void getLattLong(String lattlong);
    }
}
