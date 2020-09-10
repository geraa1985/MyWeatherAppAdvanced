package com.example.myweatherappadvanced;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.myweatherappadvanced.db.App;
import com.example.myweatherappadvanced.db.AppDatabase;
import com.example.myweatherappadvanced.db.CityDAO;
import com.example.myweatherappadvanced.interfaces.OnLongItemClick;
import com.example.myweatherappadvanced.interfaces.OnNewCityClick;
import com.example.myweatherappadvanced.settings.Settings;
import com.example.myweatherappadvanced.ui.list.CitiesListFragment;
import com.example.myweatherappadvanced.ui.settings.SettingsFragment;
import com.example.myweatherappadvanced.ui.weather.WeatherFragment;
import com.google.android.material.navigation.NavigationView;

import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements OnNewCityClick, OnLongItemClick {

    private Toolbar toolbar;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private SharedPreferences sharedPreferences;
    private long itemPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkTheme();
        setContentView(R.layout.activity_main);

        findViews();
        setSupportActionBar(toolbar);
        setDrawer();
        firstStartBehaviour();
        setOnClickForSideMenuItems();
        getSupportFragmentManager().addOnBackStackChangedListener(this::setCheckedDrawerItems);
    }

    private void firstStartBehaviour() {
        if (Settings.getInstance().getCurrentFragment() == null) {
            sharedPreferences = getSharedPreferences("LastCity", Context.MODE_PRIVATE);
            String lastCity = sharedPreferences.getString("LastCity", "");
            if (Objects.equals(lastCity, "")) {
                setCitiesListFragment();
            } else {
                onCityClick(lastCity);
            }
        } else {
            setFragment(Settings.getInstance().getCurrentFragment());
            if (getSupportFragmentManager().getFragments().get(0).getClass() == SettingsFragment.class) {
                getSupportFragmentManager().popBackStack();
            }
        }
    }

    private void setDrawer() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }

    @Override
    public void onBackPressed() {
       if (getSupportFragmentManager().getFragments().get(0).getClass().equals(CitiesListFragment.class)) {
           Intent intent = new Intent(Intent.ACTION_MAIN);
           intent.addCategory(Intent.CATEGORY_HOME);
           intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
           startActivity(intent);
           finish();
       } else {
           getSupportFragmentManager().popBackStack();
       }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        List<Fragment> fragmentsList = getSupportFragmentManager().getFragments();
        for (Fragment f : fragmentsList) {
            if (f.isVisible()) {
                Settings.getInstance().setCurrentFragment(f);
            }
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        setCheckedDrawerItems();
    }

    private void findViews() {
        toolbar = findViewById(R.id.toolbar);
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        mainMenuItemClick(item);
        return super.onOptionsItemSelected(item);
    }

    private void mainMenuItemClick(MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {
            setSettingsFragment();
        }
    }


    private void setOnClickForSideMenuItems() {
        navigationView.setNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.nav_weather: {
                    WeatherFragment fragment = new WeatherFragment();
                    String lastCity = sharedPreferences.getString("LastCity", "");
                    fragment.getCity(lastCity, this);
                    setFragment(fragment);
                    drawer.close();
                    break;
                }
                case R.id.nav_list: {
                    setCitiesListFragment();
                    drawer.close();
                    break;
                }
            }
            return true;
        });
    }

    private void setCheckedDrawerItems() {
        Class<? extends Fragment> aClass = getSupportFragmentManager().getFragments().get(0).getClass();
        if (CitiesListFragment.class.equals(aClass)) {
            navigationView.setCheckedItem(R.id.nav_list);
        } else if (WeatherFragment.class.equals(aClass)) {
            navigationView.setCheckedItem(R.id.nav_weather);
        } else {
            if (navigationView.getCheckedItem() != null) {
                navigationView.getCheckedItem().setChecked(false);
            }
        }
    }

    private void setCitiesListFragment() {
        setFragment(new CitiesListFragment());
    }

    private void setSettingsFragment() {
        setFragment(new SettingsFragment());
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainer, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void onCityClick(String cityName) {
        WeatherFragment fragment = new WeatherFragment();
        setFragment(fragment);
        fragment.getCity(cityName, this);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        contextMenuItemClick(item);
        return super.onContextItemSelected(item);
    }

    private void contextMenuItemClick(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_delete: {
                Handler handler = new Handler(Looper.getMainLooper());
                new Thread(()->{
                    AppDatabase db = App.getInstance().getDatabase();
                    CityDAO cityDAO = db.cityDAO();
                    cityDAO.deleteByID(itemPosition);
                    handler.post(this::setCitiesListFragment);
                }).start();
                break;
            }
            case R.id.action_clear_all: {
                Handler handler = new Handler(Looper.getMainLooper());
                new Thread(()->{
                    AppDatabase db = App.getInstance().getDatabase();
                    CityDAO cityDAO = db.cityDAO();
                    cityDAO.clearAll();
                    handler.post(this::setCitiesListFragment);
                }).start();
                break;
            }
        }
    }

    private void checkTheme() {
        SharedPreferences sharedPreferences = getSharedPreferences("Settings", MODE_PRIVATE);
        if (sharedPreferences.getBoolean("isNight", false)) {
            setTheme(R.style.AppThemeDark);
        } else {
            setTheme(R.style.AppTheme);
        }
    }

    @Override
    public void onLongItemClick(long itemPosition) {
        this.itemPosition = itemPosition;
    }
}