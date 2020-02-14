package com.example.metaweatherapp.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.metaweatherapp.R;
import com.example.metaweatherapp.pojo.ConsolidatedWeather;

import java.util.ArrayList;
import java.util.List;

//import com.example.metaretrofit.pojo.PostModel;

public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.ViewHolder> {

    private List<ConsolidatedWeather> postModelArrayList = new ArrayList<>();
    Context context ;

    public ForecastAdapter(List<ConsolidatedWeather> postModelArrayList, Context context) {
        this.postModelArrayList=postModelArrayList;
        this.context=context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.weather_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        ConsolidatedWeather  postModel=postModelArrayList.get(position);

        holder.windSpeed.setText(String.valueOf("Wind Speed : "+postModel.getWindSpeed()));
        holder.date.setText(String.valueOf("Date : "+postModel.getApplicableDate()));
        holder.maxTemperature.setText(String.valueOf("Max Temp. : "+ postModel.getMaxTemp()+"°"));
        holder.minTemperature.setText(String.valueOf("Min Temp. : "+postModel.getMinTemp()+"°"));
        String image=postModel.getWeatherStateAbbr();
        holder.weatherimage.setImageResource(context.getResources().getIdentifier(image,"drawable",context.getPackageName()));



        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });

    }

    @Override
    public int getItemCount() {
        return postModelArrayList == null ? 0 : postModelArrayList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView maxTemperature;
        TextView minTemperature;
        TextView windSpeed;
        TextView date;
        ImageView weatherimage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            weatherimage=itemView.findViewById(R.id.weather_imageview);
            minTemperature=itemView.findViewById(R.id.min_temperature);
            maxTemperature=itemView.findViewById(R.id.max_temperature);
            date=itemView.findViewById(R.id.date);
            windSpeed=itemView.findViewById(R.id.humidity);
        }
    }
}
