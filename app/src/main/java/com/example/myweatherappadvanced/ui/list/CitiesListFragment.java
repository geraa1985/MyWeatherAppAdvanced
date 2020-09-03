package com.example.myweatherappadvanced.ui.list;

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
import com.example.myweatherappadvanced.interfaces.OnNewCityClick;
import com.example.myweatherappadvanced.settings.Settings;
import com.example.myweatherappadvanced.ui.add.AddCity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class CitiesListFragment extends Fragment {

    private RecyclerView cityRV;
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

    private void findViews() {
        cityRV = requireActivity().findViewById(R.id.citiesRV);
        fab = requireActivity().findViewById(R.id.fab);
    }

    private void setupAdapter() {
        LinearLayoutManager lm = new LinearLayoutManager(getContext());
        lm.setOrientation(RecyclerView.VERTICAL);
        cityRV.setLayoutManager(lm);
        cityRV.addItemDecoration(new DividerItemDecoration(requireActivity(), DividerItemDecoration.VERTICAL));
        CitiesListRVAdapter adapter = new CitiesListRVAdapter(Settings.getInstance().getCitiesList(), (OnNewCityClick) requireActivity(), getActivity());
        cityRV.setAdapter(adapter);
    }

    private void clickOnFAB() {
        fab.setOnClickListener(view -> new AddCity().show(requireActivity().getSupportFragmentManager(), "AddCityDialog"));

    }
}