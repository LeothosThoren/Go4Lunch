package com.leothosthoren.go4lunch.controlers;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.leothosthoren.go4lunch.R;

import butterknife.BindView;

public class Bottom_navigation extends AppCompatActivity implements OnMapReadyCallback, NavigationView.OnNavigationItemSelectedListener {


    //    @BindView(R.id.navigation)
//    BottomNavigationView mBottomNavigationView;
    GoogleMap mMap;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;


    //Use bottom navigation view
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_map:
                    //TODO

                    return true;
                case R.id.navigation_list:
                    //TODO
                    return true;
                case R.id.navigation_workmates:
                    //TODO
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_navigation);
        //Menu configuration
        this.configureToolbar();
        this.configureDrawerLayout();
        this.configureNavigationView();
        //Bottom view configuration
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        //Google Map configuration
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }
    
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //2 - Inflate the menu and add it to the Toolbar
        getMenuInflater().inflate(R.menu.search, menu);
        return true;
    }

    //    @Override
//    public int getFragmentLayout() {
////        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
////        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
//
//        return R.layout.activity_bottom_navigation;
//    }

    /*
     * @method configureToolbar
     *
     * This method call the toolbar layout and display it in the actionbar
     * */
    private void configureToolbar() {
        this.mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
    }

    //Handle menu drawer on back press button
    @Override
    public void onBackPressed() {
        // 5 - Handle back click to close menu
        if (this.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            this.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    //Handle the click on drawer menu
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        // 4 - Handle Navigation Item Click
        int id = item.getItemId();
        switch (id) {
            case R.id.nav_lunch:
                break;
            case R.id.nav_settings:
                break;
            case R.id.nav_logout:
                break;
            default:
                break;
        }

        this.drawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }

    // ---------------------
    // CONFIGURATION
    // ---------------------


    // 2 - Configure Drawer Layout
    private void configureDrawerLayout() {
        this.drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    // 3 - Configure NavigationView
    private void configureNavigationView() {
        this.navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }


}
