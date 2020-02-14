package com.example.metaweatherapp.mapforecast;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.metaweatherapp.R;
import com.example.metaweatherapp.adapter.ForecastAdapter;
import com.example.metaweatherapp.pojo.ConsolidatedWeather;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, MapsContract.MapsView {

    MapsPresenter mapsPresenter;
    private GoogleMap mMap;
    double mlatitude;
    double mlongitude;
    String lattlong;
    Button getforecast;
    TextView textViewCityName;
    List<ConsolidatedWeather> consolidatedWeatherList =new ArrayList<>();
    ForecastAdapter forecastAdapter;
    RecyclerView recyclerView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        mapsPresenter=new MapsPresenter(this);

        mlatitude=getIntent().getDoubleExtra("latitude",0);
        mlongitude=getIntent().getDoubleExtra("longitude",0);

        getforecast=findViewById(R.id.button);
        textViewCityName=findViewById(R.id.maps_cityname);
        recyclerView=findViewById(R.id.rec_maps);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,RecyclerView.HORIZONTAL,false));
        forecastAdapter=new ForecastAdapter(consolidatedWeatherList,this);
        recyclerView.setAdapter(forecastAdapter);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment=(SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        getforecast.setOnClickListener (new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mapsPresenter.getNameAndWoeidByLocation(lattlong);

            }
        });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap=googleMap;

        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                MarkerOptions markerOptions=new MarkerOptions();
                markerOptions.position(latLng);
                markerOptions.title(latLng.latitude+":"+latLng.longitude);
                googleMap.clear();
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,5));
                googleMap.addMarker(markerOptions);
                lattlong=String.valueOf(latLng.latitude+","+latLng.longitude);
            }
        });
        LatLng currentLocation=new LatLng(mlatitude, mlongitude);
        mMap.addMarker(new MarkerOptions().position(currentLocation).title("Current Location"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocation));
    }

    //Sets city name and starts request with woeid for five day forecast
    @Override
    public void setNameLocatio(String cityname,String woeid) {
        textViewCityName.setText(cityname);
        mapsPresenter.getFiveDayForecast(woeid);

    }
    //set five day forecast for selected location
    @Override
    public void setLocationValues(List<ConsolidatedWeather> consolidatedWeather) {
        consolidatedWeatherList.clear();
        consolidatedWeatherList.addAll(consolidatedWeather);
        forecastAdapter.notifyDataSetChanged();
    }
}
