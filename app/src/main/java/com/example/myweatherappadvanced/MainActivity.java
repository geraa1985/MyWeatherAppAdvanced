package com.example.myweatherappadvanced;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.wifi.WifiManager;
import android.os.Build;
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
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.myweatherappadvanced.databinding.ActivityMainBinding;
import com.example.myweatherappadvanced.db.AppDatabase;
import com.example.myweatherappadvanced.db.CityDAO;
import com.example.myweatherappadvanced.interfaces.OnLongItemClick;
import com.example.myweatherappadvanced.interfaces.OnNewCityClick;
import com.example.myweatherappadvanced.settings.Settings;
import com.example.myweatherappadvanced.ui.list.CitiesListFragment;
import com.example.myweatherappadvanced.ui.map.MapsFragment;
import com.example.myweatherappadvanced.ui.settings.SettingsFragment;
import com.example.myweatherappadvanced.ui.weather.WeatherFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements OnNewCityClick, OnLongItemClick {

    private ActivityMainBinding activityMainBinding;
    private static FloatingActionButton fab;
    private static final int PERMISSION_REQUEST_CODE = 777;
    private double latitude;
    private double longitude;
    private LocationListener listener;
    private LocationManager locationManager;

    private long itemPosition;
    MyWiFiListenerReceiver myWiFiListenerReceiver = new MyWiFiListenerReceiver();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkTheme();
        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(activityMainBinding.getRoot());

        fab = activityMainBinding.appBar.content.fab;
        registerReceiver(myWiFiListenerReceiver,
                new IntentFilter(WifiManager.SUPPLICANT_CONNECTION_CHANGE_ACTION));
        initNotificationChannel();

        setSupportActionBar(activityMainBinding.appBar.toolbar);
        setDrawer();

        setOnClickForSideMenuItems();
        getSupportFragmentManager().addOnBackStackChangedListener(this::setCheckedDrawerItems);
    }

    @Override
    protected void onStart() {
        super.onStart();
        firstStartBehaviour();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(myWiFiListenerReceiver);
    }

    public static FloatingActionButton getFab() {
        return fab;
    }

    private void initNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            int importance = NotificationManager.IMPORTANCE_LOW;
            NotificationChannel channel = new NotificationChannel("2", "name", importance);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void firstStartBehaviour() {
        if (Settings.getInstance().getCurrentFragment() != null) {
            setFragment(Settings.getInstance().getCurrentFragment());
            if (getSupportFragmentManager().getFragments().get(0).getClass() == SettingsFragment.class) {
                getSupportFragmentManager().popBackStack();
            }
        } else {
            SharedPreferences sharedPreferences = getSharedPreferences("LastCity", Context.MODE_PRIVATE);
            String lastCity = sharedPreferences.getString("LastCity", "");
            if (Objects.equals(lastCity, "")) {
                requestCurrentLocation();
            } else {
                setFragment(new WeatherFragment());
            }
        }
    }

    private void setDrawer() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, activityMainBinding.drawerLayout, activityMainBinding.appBar.toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        activityMainBinding.drawerLayout.addDrawerListener(toggle);
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
        if (item.getItemId() == R.id.current_geo) {
            requestCurrentLocation();
        }
    }

    private void setOnClickForSideMenuItems() {
        activityMainBinding.navView.setNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.nav_weather: {
                    WeatherFragment fragment = new WeatherFragment();
                    setFragment(fragment);
                    activityMainBinding.drawerLayout.close();
                    break;
                }
                case R.id.nav_list: {
                    setCitiesListFragment();
                    activityMainBinding.drawerLayout.close();
                    break;
                }
                case R.id.maps: {
                    MapsFragment fragment = new MapsFragment();
                    setFragment(fragment);
                    activityMainBinding.drawerLayout.close();
                    break;
                }
            }
            return true;
        });
    }

    private void setCheckedDrawerItems() {
        Class<? extends Fragment> aClass = getSupportFragmentManager().getFragments().get(0).getClass();
        if (CitiesListFragment.class.equals(aClass)) {
            activityMainBinding.navView.setCheckedItem(R.id.nav_list);
        } else if (WeatherFragment.class.equals(aClass)) {
            activityMainBinding.navView.setCheckedItem(R.id.nav_weather);
        } else {
            if (activityMainBinding.navView.getCheckedItem() != null) {
                activityMainBinding.navView.getCheckedItem().setChecked(false);
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
        SharedPreferences sharedPreferences = getSharedPreferences("LastCity",
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("LastCity", cityName);
        editor.apply();
        WeatherFragment fragment = new WeatherFragment();
        setFragment(fragment);
        locationManager.removeUpdates(listener);
    }

    private void getCurrentCity() {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            String city = addresses.get(0).getLocality();
            String area = addresses.get(0).getAdminArea();
            if (city == null) {
                onCityClick(area);
                return;
            }
            onCityClick(city);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
                new Thread(() -> {
                    AppDatabase db = App.getInstance().getDatabase();
                    CityDAO cityDAO = db.cityDAO();
                    cityDAO.deleteByID(itemPosition);
                    handler.post(this::setCitiesListFragment);
                }).start();
                break;
            }
            case R.id.action_clear_all: {
                Handler handler = new Handler(Looper.getMainLooper());
                new Thread(() -> {
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
        if (sharedPreferences.getBoolean("isNIGHT", false)) {
            setTheme(R.style.AppThemeDark);
        } else {
            setTheme(R.style.AppTheme);
        }
    }

    @Override
    public void onLongItemClick(long itemPosition) {
        this.itemPosition = itemPosition;
    }

    private void requestCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            requestLocation();
        } else {
            requestLocationPermissions();
        }
    }

    private void requestLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);
        String provider = locationManager.getBestProvider(criteria, true);
        if (provider != null) {
            listener = location -> {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                getCurrentCity();
            };
            locationManager.requestLocationUpdates(provider, 5000, 0, listener);
        }
    }

    private void requestLocationPermissions() {
        if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CALL_PHONE)) {
            ActivityCompat.requestPermissions(this,
                    new String[]{
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION
                    },
                    PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length == 2 &&
                    (grantResults[0] == PackageManager.PERMISSION_GRANTED || grantResults[1] == PackageManager.PERMISSION_GRANTED)) {
                if (isGeoDisabled()) {
                    startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                }
                requestLocation();
            } else {
                setFragment(new WeatherFragment());
            }
        }
    }

    public boolean isGeoDisabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (!isGPSEnabled) return !isNetworkEnabled;
        return false;
    }
}