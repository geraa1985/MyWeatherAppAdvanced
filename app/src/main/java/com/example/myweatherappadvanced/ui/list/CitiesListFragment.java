package com.example.myweatherappadvanced.ui.list;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myweatherappadvanced.R;
import com.example.myweatherappadvanced.adapters.CitiesListRVAdapter;
import com.example.myweatherappadvanced.interfaces.IRVonCityClick;
import com.example.myweatherappadvanced.interfaces.OnFragmentInteractionListener;
import com.example.myweatherappadvanced.settings.Settings;
import com.example.myweatherappadvanced.ui.add.AddCity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class CitiesListFragment extends Fragment implements IRVonCityClick {

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
        setupAdapter();
        clickOnFAB();
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
        cityRV = requireActivity().findViewById(R.id.citiesRV);
        fab = requireActivity().findViewById(R.id.fab);
    }

    private void setupAdapter() {
        LinearLayoutManager lm = new LinearLayoutManager(getContext());
        lm.setOrientation(RecyclerView.VERTICAL);
        cityRV.setLayoutManager(lm);
        cityRV.addItemDecoration(new DividerItemDecoration(requireActivity(), DividerItemDecoration.VERTICAL));
        CitiesListRVAdapter adapter = new CitiesListRVAdapter(Settings.getInstance().getCitiesList(), this, getActivity());
        cityRV.setAdapter(adapter);
    }

    @Override
    public void onCityClick(String cityName) {
        fab.setVisibility(View.GONE);
        mListener.onFragmentInteraction(cityName);
    }

    private void clickOnFAB() {
        fab.setOnClickListener(view -> new AddCity().show(requireActivity().getSupportFragmentManager(), "AddCityDialog"));

    }
}