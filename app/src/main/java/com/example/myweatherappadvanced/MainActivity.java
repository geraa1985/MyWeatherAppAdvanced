package com.example.myweatherappadvanced;

import android.os.Bundle;
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

import com.example.myweatherappadvanced.interfaces.OnFragmentInteractionListener;
import com.example.myweatherappadvanced.ui.add.AddCity;
import com.example.myweatherappadvanced.ui.home.HomeFragment;
import com.example.myweatherappadvanced.ui.list.CitiesListFragment;
import com.example.myweatherappadvanced.ui.settings.SettingsFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements OnFragmentInteractionListener {

    private Toolbar toolbar;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private FloatingActionButton fab;

    private static String currentCityName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        checkTheme();
        setContentView(R.layout.activity_main);

        findViews();
        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        setCitiesListFragment();
        setOnClickForSideMenuItems();
        clickOnFAB();
    }


    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString("currentCityNameKey", currentCityName);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        currentCityName = savedInstanceState.getString("currentCityNameKey");
        onFragmentInteraction(currentCityName);
    }

    private void findViews() {
        toolbar = findViewById(R.id.toolbar);
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        fab = findViewById(R.id.fab);
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
            fab.setVisibility(View.GONE);
        }
    }


    private void setOnClickForSideMenuItems() {
        navigationView.setNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.nav_home: {
                    setHomeFragment();
                    fab.setVisibility(View.GONE);
                    navigationView.setCheckedItem(item);
                    drawer.close();
                    break;
                }
                case R.id.nav_list: {
                    setCitiesListFragment();
                    fab.setVisibility(View.VISIBLE);
                    navigationView.setCheckedItem(item);
                    drawer.close();
                    break;
                }
            }
            return true;
        });
    }

    private void setHomeFragment() {
        setFragment(new HomeFragment());
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
    public void onFragmentInteraction(String cityName) {
        currentCityName = cityName;
        HomeFragment fragment = new HomeFragment();
        setFragment(fragment);
        fab.setVisibility(View.GONE);
        navigationView.setCheckedItem(R.id.nav_home);
        fragment.getCity(cityName);
    }

    public static String getCurrentCityName() {
        return currentCityName;
    }


    public static void setCurrentCityName(String currentCityName) {
        MainActivity.currentCityName = currentCityName;
    }


    private void clickOnFAB() {
        fab.setOnClickListener(view -> {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragmentContainer, new AddCity());
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
            fab.setVisibility(View.GONE);
        });
    }

//    private void checkTheme() {
//        if (Settings.getInstance().isNight()) {
//            getTheme(R.style.AppThemeDark);
//        } else {
//            setTheme(R.style.AppTheme);
//        }
//    }
}