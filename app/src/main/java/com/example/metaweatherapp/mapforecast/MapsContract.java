package com.example.metaweatherapp.mapforecast;

import com.example.metaweatherapp.pojo.ConsolidatedWeather;

import java.util.List;

public interface MapsContract {
   interface MapsView {
        void setNameLocatio(String cityname,String woeid);
        void setLocationValues(List<ConsolidatedWeather> consolidatedWeather);

   }
    interface MapsPresenter{
       void getNameAndWoeidByLocation(String lattlong);
       void getFiveDayForecast(String woeid);
    }
}
