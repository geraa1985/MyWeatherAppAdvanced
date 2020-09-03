package com.example.myweatherappadvanced.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myweatherappadvanced.R;
import com.example.myweatherappadvanced.interfaces.OnNewCityClick;

import java.util.LinkedList;

public class CitiesListRVAdapter extends RecyclerView.Adapter<CitiesListRVAdapter.ViewHolder> {
    private LinkedList<String> citiesList;
    private OnNewCityClick onNewCityClick;
    private Activity activity;

    public CitiesListRVAdapter(LinkedList<String> citiesList, OnNewCityClick onNewCityClick, Activity activity) {
        if (this.citiesList == null) {
            this.citiesList = citiesList;
        }
        this.onNewCityClick = onNewCityClick;
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
        String city = citiesList.get(position);
        holder.setTextToTextView(city);
        holder.setOnItemClick(city);
        activity.registerForContextMenu(holder.textView);
    }

    @Override
    public int getItemCount() {
        return citiesList == null ? 0 : citiesList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.item_city);
        }

        void setTextToTextView(String text) {
            textView.setText(text);
        }

        void setOnItemClick(String cityName) {
            textView.setOnClickListener((v) -> {
                if (onNewCityClick != null) {
                    onNewCityClick.onCityClick(cityName);
                }
            });
        }
    }
}
