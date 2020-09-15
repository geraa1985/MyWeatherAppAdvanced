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

import com.example.myweatherappadvanced.App;
import com.example.myweatherappadvanced.MainActivity;
import com.example.myweatherappadvanced.R;
import com.example.myweatherappadvanced.adapters.CitiesListRVAdapter;
import com.example.myweatherappadvanced.databinding.FragmentListBinding;
import com.example.myweatherappadvanced.db.AppDatabase;
import com.example.myweatherappadvanced.db.CityDAO;
import com.example.myweatherappadvanced.db.CityDB;
import com.example.myweatherappadvanced.interfaces.OnLongItemClick;
import com.example.myweatherappadvanced.interfaces.OnNewCityClick;
import com.example.myweatherappadvanced.ui.add.AddCity;

import java.util.List;

public class CitiesListFragment extends Fragment {

    private FragmentListBinding fragmentListBinding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        fragmentListBinding = FragmentListBinding.inflate(inflater, container, false);
        return fragmentListBinding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        fragmentListBinding = null;
    }

    @Override
    public void onStart() {
        super.onStart();

        MainActivity.getFab().setVisibility(View.VISIBLE);
        setupAdapter();
        clickOnFAB();
        fragmentListBinding.sortByDate.setOnClickListener(onClickListener);
        fragmentListBinding.sortByCity.setOnClickListener(onClickListener);
        fragmentListBinding.sortByTemperature.setOnClickListener(onClickListener);
    }

    private void setupAdapter() {
        LinearLayoutManager lm = new LinearLayoutManager(getContext());
        lm.setOrientation(RecyclerView.VERTICAL);
        fragmentListBinding.citiesRV.setLayoutManager(lm);
        fragmentListBinding.citiesRV.addItemDecoration(new DividerItemDecoration(requireActivity(),
                DividerItemDecoration.VERTICAL));
        Handler handler = new Handler(Looper.getMainLooper());
        new Thread(() -> {
            AppDatabase db = App.getInstance().getDatabase();
            CityDAO cityDAO = db.cityDAO();
            List<CityDB> citiesList = cityDAO.getAll();
            handler.post(() -> setAdapterFromDB(citiesList));
        }).start();
    }

    private void clickOnFAB() {
        MainActivity.getFab().setOnClickListener(view -> new AddCity().show(requireActivity().getSupportFragmentManager(), "AddCityDialog"));
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
        fragmentListBinding.citiesRV.setAdapter(adapter);
        if (adapter.getItemCount() == 0) {
            new AddCity().show(requireActivity().getSupportFragmentManager(), "AddCityDialog");
        }
    }
}