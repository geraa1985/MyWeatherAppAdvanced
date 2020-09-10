package com.example.myweatherappadvanced.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myweatherappadvanced.R;
import com.example.myweatherappadvanced.db.CityDB;
import com.example.myweatherappadvanced.interfaces.OnLongItemClick;
import com.example.myweatherappadvanced.interfaces.OnNewCityClick;

import java.util.List;

public class CitiesListRVAdapter extends RecyclerView.Adapter<CitiesListRVAdapter.ViewHolder> {
    private List<CityDB> citiesList;
    private OnNewCityClick onNewCityClick;
    private OnLongItemClick onLongItemClick;
    private Activity activity;

    public CitiesListRVAdapter(List<CityDB> citiesList, OnNewCityClick onNewCityClick, OnLongItemClick onLongItemClick, Activity activity) {
        if (this.citiesList == null) {
            this.citiesList = citiesList;
        }
        this.onNewCityClick = onNewCityClick;
        this.onLongItemClick = onLongItemClick;
        this.activity = activity;
    }

    @NonNull
    @Override
    public CitiesListRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_citieslist_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CitiesListRVAdapter.ViewHolder holder, int position) {
        CityDB city = citiesList.get(citiesList.size() - 1 - position);
        holder.setTextToTextView(city);
        holder.setOnItemClick(city.name);

        activity.registerForContextMenu(holder.cityName);

        holder.cityName.setOnLongClickListener(view -> {
            onLongItemClick.onLongItemClick(city.id);
            return false;
        });

    }

    @Override
    public int getItemCount() {
        return citiesList == null ? 0 : citiesList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        View itemView;
        TextView cityName;
        TextView cityTemperature;
        TextView cityTime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
            cityName = itemView.findViewById(R.id.item_city);
            cityTemperature = itemView.findViewById(R.id.item_temperature);
            cityTime = itemView.findViewById(R.id.item_date);
        }

        void setTextToTextView(CityDB city) {
            cityName.setText(city.name);
            cityTime.setText(city.date);
            cityTemperature.setText(city.temperature);
        }

        void setOnItemClick(String cityName) {
            this.cityName.setOnClickListener((v) -> {
                if (onNewCityClick != null) {
                    onNewCityClick.onCityClick(cityName);
                }
            });
        }
    }
}
