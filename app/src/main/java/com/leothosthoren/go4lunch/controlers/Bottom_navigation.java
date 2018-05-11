package com.leothosthoren.go4lunch.controlers;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
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


import java.util.Objects;

import butterknife.BindView;

public class Bottom_navigation extends AppCompatActivity implements OnMapReadyCallback {


    //    @BindView(R.id.navigation)
//    BottomNavigationView mBottomNavigationView;
    GoogleMap mMap;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;

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
        configureToolbar();
        //Bottom view configuration
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        //Google Map configuration
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
}
