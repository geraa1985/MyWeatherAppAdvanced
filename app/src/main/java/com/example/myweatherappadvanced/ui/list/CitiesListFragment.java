package com.example.myweatherappadvanced.ui.list;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myweatherappadvanced.MainActivity;
import com.example.myweatherappadvanced.R;
import com.example.myweatherappadvanced.adapters.CitiesListRVAdapter;
import com.example.myweatherappadvanced.interfaces.IRVonCityClick;
import com.example.myweatherappadvanced.interfaces.OnFragmentInteractionListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Arrays;
import java.util.LinkedList;

public class CitiesListFragment extends Fragment implements IRVonCityClick {

    private LinkedList<String> citiesList;

    private RecyclerView cityRV;
    private OnFragmentInteractionListener mListener;
    private FloatingActionButton fab;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        findViews();
        fab.setVisibility(View.VISIBLE);
        if (MainActivity.getCurrentCityName() != null) {
            citiesList.remove(MainActivity.getCurrentCityName());
            citiesList.addFirst(MainActivity.getCurrentCityName());
        }
        setupAdapter();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            mListener = (OnFragmentInteractionListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " должен реализовывать интерфейс OnFragmentInteractionListener");
        }
    }

    private void findViews() {
        String[] citiesArray = getResources().getStringArray(R.array.cities);
        citiesList = new LinkedList<>(Arrays.asList(citiesArray));
        cityRV = requireActivity().findViewById(R.id.citiesRV);
        fab = requireActivity().findViewById(R.id.fab);
    }

    private void setupAdapter() {
        LinearLayoutManager lm = new LinearLayoutManager(getContext());
        lm.setOrientation(RecyclerView.VERTICAL);
        cityRV.setLayoutManager(lm);
        CitiesListRVAdapter adapter = new CitiesListRVAdapter(citiesList, this);
        cityRV.setAdapter(adapter);
    }

    @Override
    public void onCityClick(String cityName) {
        mListener.onFragmentInteraction(cityName);
    }
}