package com.example.myweatherappadvanced.ui.list;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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
import com.example.myweatherappadvanced.db.App;
import com.example.myweatherappadvanced.db.AppDatabase;
import com.example.myweatherappadvanced.db.CityDAO;
import com.example.myweatherappadvanced.db.CityDB;
import com.example.myweatherappadvanced.interfaces.OnLongItemClick;
import com.example.myweatherappadvanced.interfaces.OnNewCityClick;
import com.example.myweatherappadvanced.ui.add.AddCity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class CitiesListFragment extends Fragment {

    private RecyclerView cityRV;
    private FloatingActionButton fab;

    private MaterialButton sortByDate;
    private MaterialButton sortByCity;
    private MaterialButton sortByTemp;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        findViews();
        fab.setVisibility(View.VISIBLE);
        setupAdapter();
        clickOnFAB();
        sortByDate.setOnClickListener(onClickListener);
        sortByCity.setOnClickListener(onClickListener);
        sortByTemp.setOnClickListener(onClickListener);
    }

    private void findViews() {
        cityRV = requireActivity().findViewById(R.id.citiesRV);
        fab = requireActivity().findViewById(R.id.fab);

        sortByCity = requireActivity().findViewById(R.id.sortByCity);
        sortByDate = requireActivity().findViewById(R.id.sortByDate);
        sortByTemp = requireActivity().findViewById(R.id.sortByTemperature);
    }

    private void setupAdapter() {
        LinearLayoutManager lm = new LinearLayoutManager(getContext());
        lm.setOrientation(RecyclerView.VERTICAL);
        cityRV.setLayoutManager(lm);
        cityRV.addItemDecoration(new DividerItemDecoration(requireActivity(), DividerItemDecoration.VERTICAL));
        Handler handler = new Handler(Looper.getMainLooper());
        new Thread(() -> {
            AppDatabase db = App.getInstance().getDatabase();
            CityDAO cityDAO = db.cityDAO();
            List<CityDB> citiesList = cityDAO.getAll();
            handler.post(() -> setAdapterFromDB(citiesList));
        }).start();
    }

    private void clickOnFAB() {
        fab.setOnClickListener(view -> new AddCity().show(requireActivity().getSupportFragmentManager(), "AddCityDialog"));
    }

    View.OnClickListener onClickListener = view -> {
        Handler handler = new Handler(Looper.getMainLooper());
        new Thread(() -> {
            AppDatabase db = App.getInstance().getDatabase();
            CityDAO cityDAO = db.cityDAO();
            List<CityDB> citiesList;
            switch (view.getId()) {
                case R.id.sortByCity:
                    citiesList = cityDAO.sortByCity();
                    break;
                case R.id.sortByDate:
                    citiesList = cityDAO.sortByDate();
                    break;
                case R.id.sortByTemperature:
                    citiesList = cityDAO.sortByTemp();
                    break;
                default:
                    citiesList = null;
            }
            handler.post(() -> setAdapterFromDB(citiesList));
        }).start();
    };

    private void setAdapterFromDB(List<CityDB> citiesList) {
        CitiesListRVAdapter adapter = new CitiesListRVAdapter(citiesList, (OnNewCityClick) requireActivity(),
                (OnLongItemClick) requireActivity(), getActivity());
        cityRV.setAdapter(adapter);
        if (adapter.getItemCount() == 0) {
            new AddCity().show(requireActivity().getSupportFragmentManager(), "AddCityDialog");
        }
    }
}