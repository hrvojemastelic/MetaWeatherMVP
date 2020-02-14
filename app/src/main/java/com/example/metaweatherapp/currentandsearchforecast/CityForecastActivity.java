package com.example.metaweatherapp.currentandsearchforecast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.metaweatherapp.R;
import com.example.metaweatherapp.adapter.ForecastAdapter;

import com.example.metaweatherapp.mapforecast.MapsActivity;
import com.example.metaweatherapp.pojo.ConsolidatedWeather;
import com.example.metaweatherapp.pojo.Location;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class CityForecastActivity extends AppCompatActivity  implements ForecastContract.MainView{
    public static final int REQUEST_CODE_LOCATION_PERMISSION=1;

    private ForecastPresenter forecastPresenter;
    private RecyclerView recyclerView;
    private List<ConsolidatedWeather> consolidatedWeathers=new ArrayList<>();
    ForecastAdapter forecastAdapter;
    private androidx.appcompat.widget.SearchView searchView;
    private TextView textCityName, textWoeid,textLocationType;
    FloatingActionButton floatingActionButton;
    double latitude;
    double longitude;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.city_search_layout);
        forecastPresenter=new ForecastPresenter(this);

        floatingActionButton=findViewById(R.id.floatingActionButton);
        textCityName=findViewById(R.id.cityname);
        textLocationType=findViewById(R.id.location_type);
        textWoeid=findViewById(R.id.woeid);
        searchView=findViewById(R.id.search_view);
        recyclerView=findViewById(R.id.recycler_vieww);
        forecastAdapter=new ForecastAdapter(consolidatedWeathers, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(forecastAdapter);
        //ASK FOR LOCATION PERMISSION
        //PERMISSION GRANTED CHECK FOR CURRENT LOCATION
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(CityForecastActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}
                    , REQUEST_CODE_LOCATION_PERMISSION);
        } else {
            getCurrentLocation();


        }

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(CityForecastActivity.this, MapsActivity.class);
                intent.putExtra("latitude", latitude);
                intent.putExtra("longitude", longitude);
                startActivity(intent);

            }
        });

        //Get user input from searchview and pass it to MainPresenter to getCityName method
        //for retrofit1 getName request query
        searchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                forecastPresenter.getCityName(query);

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }



    //CHECK FOR PERMISSION RESULTS IF PERMISSION IS GRANTED GET CURRENT LOCATION
    // IF NOT PRINT TOAST MESSAGE
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode==REQUEST_CODE_LOCATION_PERMISSION && grantResults.length>0){
            if (grantResults[0] ==PackageManager.PERMISSION_GRANTED){
                getCurrentLocation();
            }
            else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
    //IF PERMISSION IS GRANTED GET USER CURRENT LOCATION AND START REQUEST SEARCH FOR LOCATION WOEID
    // WOEID IS USED TO GET  5 DAYS FORECAST
    private void getCurrentLocation() {

        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(1000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationServices.getFusedLocationProviderClient(CityForecastActivity.this).
                requestLocationUpdates(locationRequest,new LocationCallback(){

                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        super.onLocationResult(locationResult);
                        LocationServices.getFusedLocationProviderClient(CityForecastActivity.this)
                                .removeLocationUpdates(this);
                        if (locationResult!=null && locationResult.getLocations().size()>0){
                            int latestlocationindex=locationResult.getLocations().size()-1;
                             latitude=locationResult.getLocations().get(latestlocationindex).getLatitude();
                             longitude=locationResult.getLocations().get(latestlocationindex).getLongitude();

                            String lattlong =String.valueOf(latitude+","+longitude);
                            //PASSS LATITUDE AND LONGITUDE THEN REQUEST GETWOEID FROM PASSED LOCATION
                            forecastPresenter.getLattLong(lattlong);
                            //PASS LATITUDE AND LONGITUDE TO INTENT AN

                        }

                    }
                }, Looper.getMainLooper());
    }


    //SET VALUES FROM SEARCHVIEW QUERY TO TEXTVIEWS
    // AND PASS WOIED TO PRESENTER METHOD GETWOEID FOR FIVE DAY FORECAST
    @Override
    public void setValuesByName(Location locationValues) {
        textCityName.setText(locationValues.getTitle());
        textWoeid.setText(String.valueOf(locationValues.getWoeid()));
        textLocationType.setText(String.valueOf("Location type :\n"+locationValues.getLocationType()));
        //PASS WOIED , ITS NEED FOR FIVE DAYS FORECAST
        String  woeid=locationValues.getWoeid().toString();
        forecastPresenter.getWoeid(woeid);

    }



    //SET VALUES FOR FIVE DAY FORECAST FROM CURRENT LOCATION PROVIDED BY LOCATION SERVICES
    // AND FROM SEARCHVIEW CITY NAME

    @Override
    public void setLocationValues(List<ConsolidatedWeather> consolidatedWeather) {
        consolidatedWeathers.clear();
        consolidatedWeathers.addAll(consolidatedWeather);
        forecastAdapter.notifyDataSetChanged();

    }

    //SET VALUES FROM WOEID
    //AND PASSES IT FOR FIVE DAY FORECAST
    @Override
    public void setWoeidValues(String woeid,String title,String locationType) {
        textCityName.setText(title);
        textWoeid.setText(woeid);
        textLocationType.setText(String.valueOf("Location type:\n"+locationType));

        forecastPresenter.getWoeid(woeid);
    }

}
