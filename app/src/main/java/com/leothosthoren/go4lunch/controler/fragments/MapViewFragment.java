package com.leothosthoren.go4lunch.controler.fragments;


import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.leothosthoren.go4lunch.R;
import com.leothosthoren.go4lunch.api.PlaceStreams;
import com.leothosthoren.go4lunch.api.RestaurantHelper;
import com.leothosthoren.go4lunch.base.BaseFragment;
import com.leothosthoren.go4lunch.controler.activities.Go4LunchActivity;
import com.leothosthoren.go4lunch.controler.activities.RestaurantInfoActivity;
import com.leothosthoren.go4lunch.data.DataSingleton;
import com.leothosthoren.go4lunch.model.detail.PlaceDetail;
import com.leothosthoren.go4lunch.model.firebase.Restaurants;
import com.leothosthoren.go4lunch.utils.DataConverterHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;

/**
 * A simple {@link Fragment} subclass.
 */
@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public class MapViewFragment extends BaseFragment implements OnMapReadyCallback,
        GoogleMap.OnMarkerClickListener, DataConverterHelper {

    // CONSTANT
    public static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    public static final float DEFAULT_ZOOM = 16f;
    public static final String TAG = MapViewFragment.class.getSimpleName();
    private static final String KEY_LOCATION = "location";
    // WIDGET
    @BindView(R.id.position_icon)
    ImageButton mGpsLocation;
    // VAR
    private MapView mMapView;
    private GoogleMap mMap;
    private boolean mLocationPermissionGranted;
    private Location mLastKnownLocation;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private LatLng mDefaultLocation = new LatLng(48.813326, 2.348383);
    private GeoDataClient mGeoDataClient;
    private PlaceDetectionClient mPlaceDetectionClient;
    private Disposable mDisposable;
    private Marker marker;
    // DATA
    private List<PlaceDetail> mPlaceDetailList = new ArrayList<>();
    private HashMap<String, PlaceDetail> mMarkerMap = new HashMap<>();
    private List<Restaurants> restaurantListFromFirestore = new ArrayList<>();

    @Override
    protected BaseFragment newInstance() {
        return new MapViewFragment();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        if (mMap != null) {
            outState.putParcelable(KEY_LOCATION, mLastKnownLocation);
        }
        super.onSaveInstanceState(outState);

    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_map_view;
    }

    @Override
    protected void configureDesign() {
        this.instantiatePlacesApiClients();
        this.getAllRestaurantSelected();
    }

    @Override
    protected void updateDesign() {

    }


    //---------------------------------------------------------------------------------------------//
    //                                         MAP                                                 //
    //---------------------------------------------------------------------------------------------//


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        getLocationPermission();
        updateUI();
        setMapStyle(mMap);
        setDevicePositionOnMap();
        mMap.setOnMarkerClickListener(this);

    }

    private void setMapStyle(GoogleMap googleMap) {
        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            if (getContext() != null) {
                boolean success = googleMap.setMapStyle(
                        MapStyleOptions.loadRawResourceStyle(
                                getContext(), R.raw.style_json));
                if (!success) {
                    Log.e(TAG, "Style parsing failed.");
                }
            }

        } catch (Resources.NotFoundException e) {
            Log.e(TAG, "Can't find style. Error: ", e);
        }

    }

    //---------------------------------------------------------------------------------------------//
    //                                     CONFIGURATION                                           //
    //---------------------------------------------------------------------------------------------//


    //Useful to initiate a map inside a fragment
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //To retrieve data when device rotate
        if (savedInstanceState != null) {
            mLastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION);
        }

        mMapView = (MapView) view.findViewById(R.id.map);
        if (mMapView != null) {
            mMapView.onCreate(null);
            mMapView.onResume();
            mMapView.getMapAsync(this);
        }
    }

    private void instantiatePlacesApiClients() {
        // Construct a GeoDataClient.
        mGeoDataClient = Places.getGeoDataClient(Objects.requireNonNull(getContext()));

        // Construct a PlaceDetectionClient.
        mPlaceDetectionClient = Places.getPlaceDetectionClient(getContext());

        // Construct a FusedLocationProviderClient
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());

    }



    //---------------------------------------------------------------------------------------------//
    //                                      PERMISSION                                             //
    //---------------------------------------------------------------------------------------------//


    private void getLocationPermission() {
        if (getContext() != null) {
            if (ContextCompat.checkSelfPermission(getContext(),
                    android.Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                mLocationPermissionGranted = true;
            } else {
                ActivityCompat.requestPermissions(Objects.requireNonNull(getActivity()),
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions
            , @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                    //Update Ui
                    updateUI();
                }
            }
        }

    }


    //---------------------------------------------------------------------------------------------//
    //                                         LOCATION                                            //
    //---------------------------------------------------------------------------------------------//


    private void getDeviceLocation() {
        try {
            if (mLocationPermissionGranted && getActivity() != null) {
                Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(getActivity(), task -> {
                    if (task.isSuccessful()) {
                        // Set the map's camera position to the current location of the device.
                        mLastKnownLocation = task.getResult();

                        if (mLastKnownLocation != null) {

                            // Store device coordinates
                            Double latitude = mLastKnownLocation.getLatitude();
                            Double longitude = mLastKnownLocation.getLongitude();
                            DataSingleton.getInstance().setDeviceLatitude(latitude);
                            DataSingleton.getInstance().setDeviceLongitude(longitude);

                            //Move camera toward device position
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                    new LatLng(latitude, longitude), DEFAULT_ZOOM));

                            //Execute http request with retrofit and RxJava2
                            this.executeHttpRequestWithNearBySearchAndPlaceDetail(setLocationIntoString(latitude, longitude));

                        } else {
                            Toast.makeText(getContext(),
                                    R.string.toast_message_geolocation,
                                    Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Log.d(TAG, "Current location is null. Using defaults.");
                        Log.e(TAG, "getDeviceLocation => Exception: %s" + task.getException());
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
                        mMap.getUiSettings().setMyLocationButtonEnabled(false);
                    }
                });
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());

        }
    }


    //---------------------------------------------------------------------------------------------//
    //                                      HTTP RxJava                                            //
    //---------------------------------------------------------------------------------------------//


    public void executeHttpRequestWithNearBySearchAndPlaceDetail(String location) {
        mDisposable = PlaceStreams.streamFetchListPlaceDetail(location)
                .subscribeWith(new DisposableObserver<List<PlaceDetail>>() {
                    @Override
                    public void onNext(List<PlaceDetail> placeDetail) {
                        Log.d(TAG, "onNext: " + placeDetail.size());
                        addMarkerOnMap(placeDetail);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError: " + e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete: " + mPlaceDetailList.size());
                        if (mPlaceDetailList.isEmpty())
                            Toast.makeText(getContext(), R.string.no_restaurant_found, Toast.LENGTH_LONG).show();
                    }
                });
    }

    // Dispose subscription
    private void disposeWhenDestroy() {
        if (this.mDisposable != null && !this.mDisposable.isDisposed())
            this.mDisposable.dispose();
    }

    // Called for better performances
    @Override
    public void onDestroy() {
        super.onDestroy();
        this.disposeWhenDestroy();
    }

    //---------------------------------------------------------------------------------------------//
    //                                       HTTP FIREBASE                                         //
    //---------------------------------------------------------------------------------------------//


    private void getAllRestaurantSelected() {
        RestaurantHelper.getAllRestaurants().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (DocumentSnapshot documentSnapshot : task.getResult()) {
                    Restaurants restaurants = documentSnapshot.toObject(Restaurants.class);
                    if (restaurants != null && getCurrentUser() != null) {
                        if (!restaurants.getWorkmate().getUid().equals(getCurrentUser().getUid())
                                && formatDate(restaurants.getDateChoice()).equals(formatDate(Calendar.getInstance().getTime()))) {
                            restaurantListFromFirestore.add(restaurants);
                        }
                    }
                }
            } else {
                Log.d(TAG, "Error getting documents: ", task.getException());
            }
        }).addOnFailureListener(this.onFailureListener(getContext()));
    }


    //---------------------------------------------------------------------------------------------//
    //                                          UI                                                 //
    //---------------------------------------------------------------------------------------------//


    private void updateUI() {
        if (mMap == null) {
            return;
        }
        try {
            if (mLocationPermissionGranted) {
                mGpsLocation.setVisibility(View.VISIBLE);
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                getDeviceLocation();
            } else {
                mGpsLocation.setVisibility(View.GONE);
                mMap.setMyLocationEnabled(false);
                mLastKnownLocation = null;
                //Try to obtain location permission
                getLocationPermission();
            }
        } catch (SecurityException e) {
            Log.e(TAG, "updateUI: SecurityException " + e.getMessage());
        }
    }

    private void addMarkerOnMap(List<PlaceDetail> placeDetailList) {
        //Initialize and store data in both array and singleton
        this.mPlaceDetailList.addAll(placeDetailList);
        DataSingleton.getInstance().setPlaceDetailList(mPlaceDetailList);

        if (mPlaceDetailList.size() != 0 || mPlaceDetailList != null) {
            for (int i = 0; i < mPlaceDetailList.size(); i++) {
                if (mPlaceDetailList.get(i).getResult() != null) {
                    marker = mMap.addMarker(new MarkerOptions()
                            .position(new LatLng(mPlaceDetailList.get(i).getResult().getGeometry().getLocation().getLat(),
                                    mPlaceDetailList.get(i).getResult().getGeometry().getLocation().getLng()))
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.restaurant_locator_for_map_orange))
                            .title(mPlaceDetailList.get(i).getResult().getName()));

                    //Change color of marker if a workmate selected a restaurant
                    this.changeMarkerColor(i);

                    // Store in HashMap for Marker id for clickHandler
                    mMarkerMap.put(marker.getId(), mPlaceDetailList.get(i));
                }
            }

        } else {
            Log.d(TAG, "addMarkerOnMap is empty :" + mPlaceDetailList.size());
        }
    }

    private void changeMarkerColor(int index) {
        for (int j = 0; j < restaurantListFromFirestore.size(); j++) {
            if (restaurantListFromFirestore.get(j).getPlaceDetail().getResult().getPlaceId()
                    .equals(mPlaceDetailList.get(index).getResult().getPlaceId())) {
                marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.restaurant_locator_for_map_green));
                break;
            }
        }
    }


    //---------------------------------------------------------------------------------------------//
    //                                          ACTION                                             //
    //---------------------------------------------------------------------------------------------//


    @Override
    public boolean onMarkerClick(final Marker marker) {
        // Store PlaceDetail object in Singleton
        DataSingleton.getInstance().setPlaceDetail(mMarkerMap.get(marker.getId()));
        Log.d(TAG, "onMarkerClick: " + mMarkerMap.get(marker.getId()) + " Vs mMarker size:" + mMarkerMap.size());
        //Launch Activity
        startActivity(RestaurantInfoActivity.class);
        return false;
    }

    private void setDevicePositionOnMap() {
        this.mGpsLocation.setOnClickListener(v -> {
            Log.d(TAG, "onClick: clicked gps icon");
            if (DataSingleton.getInstance().getDeviceLatitude() != null || DataSingleton.getInstance().getDeviceLongitude() != null) {
                Double latitude = DataSingleton.getInstance().getDeviceLatitude();
                Double longitude = DataSingleton.getInstance().getDeviceLongitude();
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                        new LatLng(latitude, longitude), DEFAULT_ZOOM));
            } else {
                Toast.makeText(getContext(), R.string.location_button_error, Toast.LENGTH_SHORT).show();
            }
        });
    }
}

