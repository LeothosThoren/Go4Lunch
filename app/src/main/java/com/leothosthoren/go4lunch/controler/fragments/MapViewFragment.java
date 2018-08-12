package com.leothosthoren.go4lunch.controler.fragments;


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
import com.leothosthoren.go4lunch.R;
import com.leothosthoren.go4lunch.api.PlaceStreams;
import com.leothosthoren.go4lunch.base.BaseFragment;
import com.leothosthoren.go4lunch.controler.activities.RestaurantInfoActivity;
import com.leothosthoren.go4lunch.data.DataSingleton;
import com.leothosthoren.go4lunch.model.detail.PlaceDetail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import butterknife.BindView;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;


/**
 * A simple {@link Fragment} subclass.
 */
@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public class MapViewFragment extends BaseFragment implements OnMapReadyCallback,
        GoogleMap.OnMarkerClickListener {

    // CONSTANT
    public static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    public static final float DEFAULT_ZOOM = 15f;
    public static final String TAG = MapViewFragment.class.getSimpleName();
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
    private Marker mMarker;
    // DATA
    private List<PlaceDetail> mPlaceDetailList = new ArrayList<>();
    private Map<String, PlaceDetail> mMarkerMap = new HashMap<>();

    @Override
    protected BaseFragment newInstance() {
        return new MapViewFragment();
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_map_view;
    }

    @Override
    protected void configureDesign() {
        instantiatePlacesApiClients();
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
        setMyPositionOnMap();
        mMap.setOnMarkerClickListener(this);

    }

    //---------------------------------------------------------------------------------------------//
    //                                     CONFIGURATION                                           //
    //---------------------------------------------------------------------------------------------//


    //Useful to initiate a map inside a fragment != Activity
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

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

    private void setMapStyle(GoogleMap googleMap) {
        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            Objects.requireNonNull(getContext()), R.raw.style_json));

            if (!success) {
                Log.e(TAG, "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e(TAG, "Can't find style. Error: ", e);
        }

    }


    //---------------------------------------------------------------------------------------------//
    //                                      PERMISSION                                             //
    //---------------------------------------------------------------------------------------------//


    private void getLocationPermission() {
        if (ContextCompat.checkSelfPermission(Objects.requireNonNull(getContext()),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(Objects.requireNonNull(getActivity()),
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
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

                            //HTTP RXJAVA
                            executeHttpRequestWithNearBySearchAndPlaceDetail(setLocationIntoString(latitude, longitude));

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
                        if (!placeDetail.isEmpty())
                            addMarkerOnMap(placeDetail);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError: " + e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete: " + mPlaceDetailList.size());
                        if (mPlaceDetailList.isEmpty() || mPlaceDetailList == null) {
                            Toast.makeText(getContext(), R.string.no_restaurant_found, Toast.LENGTH_LONG).show();
                        }
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
                mGpsLocation.setVisibility(View.INVISIBLE);
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
        this.mPlaceDetailList.addAll(placeDetailList);
        DataSingleton.getInstance().setPlaceDetailList(mPlaceDetailList);

        if (!mPlaceDetailList.isEmpty() || mPlaceDetailList != null) {
            for (int i = 0; i < mPlaceDetailList.size(); i++) {
                mMarker = mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(mPlaceDetailList.get(i).getResult().getGeometry().getLocation().getLat(),
                                mPlaceDetailList.get(i).getResult().getGeometry().getLocation().getLng()))
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_pizza_icon_map))
                        .title(mPlaceDetailList.get(i).getResult().getName()));

                // Store in HASHMAP for Marker clickHandler
                mMarkerMap.put(mMarker.getId(), mPlaceDetailList.get(i));
            }
        } else
            Log.d(TAG, "addMarkerOnMap is empty :" + mPlaceDetailList.size());
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

    private void setMyPositionOnMap() {
        this.mGpsLocation.setOnClickListener(v -> {
            Log.d(TAG, "onClick: clicked gps icon");
            if (DataSingleton.getInstance().getDeviceLatitude() != null
                    || DataSingleton.getInstance().getDeviceLongitude() != null) {
                Double latitude = DataSingleton.getInstance().getDeviceLatitude();
                Double longitude = DataSingleton.getInstance().getDeviceLongitude();
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                        new LatLng(latitude, longitude), DEFAULT_ZOOM));
            } else {
                Toast.makeText(getContext(), "An error occurred, please clickHandler on map view button", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

