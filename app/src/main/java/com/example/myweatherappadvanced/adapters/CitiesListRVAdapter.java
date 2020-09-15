package com.example.myweatherappadvanced.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myweatherappadvanced.R;
import com.example.myweatherappadvanced.calculate.Calculator;
import com.example.myweatherappadvanced.databinding.ItemCitieslistLayoutBinding;
import com.example.myweatherappadvanced.db.CityDB;
import com.example.myweatherappadvanced.interfaces.OnLongItemClick;
import com.example.myweatherappadvanced.interfaces.OnNewCityClick;

import java.util.List;

public class CitiesListRVAdapter extends RecyclerView.Adapter<CitiesListRVAdapter.ViewHolder> {

    private List<CityDB> citiesList;
    private OnNewCityClick onNewCityClick;
    private OnLongItemClick onLongItemClick;
    private Activity activity;

    public CitiesListRVAdapter(List<CityDB> citiesList, OnNewCityClick onNewCityClick,
                               OnLongItemClick onLongItemClick, Activity activity) {
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
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemCitieslistLayoutBinding binding =
                ItemCitieslistLayoutBinding.inflate(inflater, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CitiesListRVAdapter.ViewHolder holder, int position) {
        CityDB city = citiesList.get(citiesList.size() - 1 - position);
        holder.setTextToTextView(city);
        holder.setOnItemClick(city.name);

        activity.registerForContextMenu(holder.binding.itemCity);

        holder.binding.itemCity.setOnLongClickListener(view -> {
            onLongItemClick.onLongItemClick(city.id);
            return false;
        });

    }

    @Override
    public int getItemCount() {
        return citiesList == null ? 0 : citiesList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ItemCitieslistLayoutBinding binding;

        public ViewHolder(ItemCitieslistLayoutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void setTextToTextView(CityDB city) {
            binding.itemCity.setText(city.name);
            binding.itemDate.setText(city.date);
            binding.itemTemperature.setText(getTemperatureString(city.temperature));
        }

        void setOnItemClick(String cityName) {
            binding.itemCity.setOnClickListener((v) -> {
                if (onNewCityClick != null) {
                    onNewCityClick.onCityClick(cityName);
                }
            });
        }

        private String getTemperatureString(long temp) {
            String temperature;
            SharedPreferences sharedPreferences = activity.getSharedPreferences("Settings",
                    Context.MODE_PRIVATE);

            if (sharedPreferences.getBoolean("isF", false)) {
                long mainTemp = Calculator.cToF((double) temp);
                if (mainTemp > 0) {
                    temperature = "+" + mainTemp + activity.getResources().getString(R.string.deg_f);
                } else {
                    temperature = mainTemp + activity.getResources().getString(R.string.deg_f);
                }
            } else {
                if (Math.round(temp) > 0) {
                    temperature = "+" + Math.round(temp) + activity.getResources().getString(R.string.deg_c);
                } else {
                    temperature = Math.round(temp) + activity.getResources().getString(R.string.deg_c);
                }
            }

            return temperature;
        }
    }
}
