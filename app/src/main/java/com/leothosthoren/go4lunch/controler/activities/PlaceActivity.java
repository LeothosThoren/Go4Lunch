package com.leothosthoren.go4lunch.controler.activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.PlaceLikelihoodBufferResponse;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.leothosthoren.go4lunch.R;

public class PlaceActivity extends FragmentActivity
        implements GoogleApiClient.OnConnectionFailedListener, OnMapReadyCallback {

    //CONSTANT
    public static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    public static final float DEFAULT_ZOOM = 18f;
    private static final String TAG = "SecondMapActivity";
    private static final int MAX_PLACES = 30;
    private static final int REQUEST_PICK_PLACE = 2;

    //VAR
    private GoogleMap mMap;
    private GeoDataClient mGeoDataClient;
    private PlaceDetectionClient mPlaceDetectionClient;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private Location mLastKnownLocation;
    private boolean mLocationPermissionGranted;
    private LatLng mDefaultLocation = new LatLng(48.7927684, 2.3591994999999315);
    private GoogleApiClient mGoogleApiClient;
    private LatLng[] mLikelyPlaceLatlng;
    private String[] mLikelyPlaceName;
    private Integer[] mPlaceType;
    private LatLngBounds mLatLngBounds;
    private Place place;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.place_activity);

        instantiatePlacesApiClients();
        initMap();

    }

    //---------------------------------------------------------------------------------------------//
    //                                         MAP                                                 //
    //---------------------------------------------------------------------------------------------//


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        getLocationPermission();
        getDeviceLocation();
        updateUI();

    }

    //---------------------------------------------------------------------------------------------//
    //                                     CONFIGURATION                                           //
    //---------------------------------------------------------------------------------------------//

    private void initMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    private void instantiatePlacesApiClients() {
        // Construct a GeoDataClient.
        mGeoDataClient = Places.getGeoDataClient(this);

        // Construct a PlaceDetectionClient.
        mPlaceDetectionClient = Places.getPlaceDetectionClient(this);

        // Construct a FusedLocationProviderClient
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

    }

    //---------------------------------------------------------------------------------------------//
    //                                      PERMISSION                                             //
    //---------------------------------------------------------------------------------------------//

    private void getLocationPermission() {
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
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
                }
            }
        }
        //Update Ui here
        updateUI();
    }

    //---------------------------------------------------------------------------------------------//
    //                                         UI                                                  //
    //---------------------------------------------------------------------------------------------//

    private void updateUI() {
        if (mMap == null) {
            return;
        }
        try {
            if (mLocationPermissionGranted) {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
//                showProximityPlace();
            } else {
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                mLastKnownLocation = null;
                //Try to obtain location permission
                getLocationPermission();
            }
        } catch (SecurityException e) {
            Log.e(TAG, "updateUI: SecurityException " + e.getMessage());
        }
    }

    private void addDefaultMarkerOnMap() {
        // Add a default marker, because the user hasn't selected a place.
        mMap.addMarker(new MarkerOptions()
                .title(getString(R.string.default_info_title))
                .position(mDefaultLocation)
                .snippet(getString(R.string.default_info_snippet)));

    }

    private void addMarkerOnMap(int i) {
        if (mLikelyPlaceLatlng.length != 0) {
            mMap.addMarker(new MarkerOptions()
                    .position(mLikelyPlaceLatlng[i])
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
        }
    }

    //---------------------------------------------------------------------------------------------//
    //                                      LOCATION                                               //
    //---------------------------------------------------------------------------------------------//


    private void getDeviceLocation() {
        try {
            if (mLocationPermissionGranted) {
                Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            mLastKnownLocation = task.getResult();
                            //Move camera toward device position
                            if (mLastKnownLocation != null) {

                                //Define the bounds
                                mLatLngBounds = new LatLngBounds(
                                        new LatLng(48.856614, 2.3522219),
                                        new LatLng(48.856614, 2.3522219));

                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                        new LatLng(mLastKnownLocation.getLatitude(),
                                                mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                                showProximityPlace();
                            } else {
                                Toast.makeText(PlaceActivity.this,
                                        "Make sure your emulator device got map position on true",
                                        Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            Log.d(TAG, "Current location is null. Using defaults.");
                            Log.e(TAG, "getPhoneLocation => Exception: %s" + task.getException());
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
                            mMap.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                    }
                });
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());

        }
    }

    private void showProximityPlace() {
        if (mMap == null) {
            return;
        }

        if (mLocationPermissionGranted) {
            @SuppressWarnings("MissingPermission") final Task<PlaceLikelihoodBufferResponse> placeResult =
                    mPlaceDetectionClient.getCurrentPlace(null);

            placeResult.addOnCompleteListener(new OnCompleteListener<PlaceLikelihoodBufferResponse>() {
                @Override
                public void onComplete(@NonNull Task<PlaceLikelihoodBufferResponse> task) {
                    if (task.isSuccessful() && task.getResult() != null) {
                        PlaceLikelihoodBufferResponse likelyPlace = task.getResult();
                        //Find all point of interest of restaurant type
                        findRestaurant(likelyPlace);
                        //To avoid memory leaks
                        likelyPlace.release();

                    } else {
                        Log.e(TAG, "showProximityPlace: exception: %s", task.getException());
                    }
                }
            });
        } else {
            // The user has not granted permission.
            Log.d(TAG, "ShowProximityPlace => The user did not grant location permission.");
            addDefaultMarkerOnMap();
            // Prompt the user for permission.
            getLocationPermission();
        }
    }

    private void findRestaurant(PlaceLikelihoodBufferResponse likelyPlace) {
        //Store maximum entries
        int count;
        if (likelyPlace.getCount() < MAX_PLACES) {
            count = likelyPlace.getCount();
        } else {
            count = MAX_PLACES;
        }

        //Add likelyPlace elements into arrays
        int i = 0;
        mLikelyPlaceName = new String[count];
        mLikelyPlaceLatlng = new LatLng[count];
        //Check if the nearest poi are restaurants
        for (PlaceLikelihood placeLikelihood : likelyPlace) {
            place = placeLikelihood.getPlace();
            for (int j = 0; j < place.getPlaceTypes().size(); j++) {
                if (place.getPlaceTypes().get(j) == Place.TYPE_RESTAURANT) {
                    mLikelyPlaceName[i] = place.getName().toString();
                    mLikelyPlaceLatlng[i] = place.getLatLng();

                    //Add marker in every place found
                    addMarkerOnMap(i);

                    Log.d(TAG, "onComplete: show me Latlng marker: " + mLikelyPlaceName[i]
                            + " " + mLikelyPlaceLatlng[i]
                            + "\n" + place.getPlaceTypes());
                }
            }

            i++;
            if (i > (count - 1)) {
                break;
            }

        }
    }

    //---------------------------------------------------------------------------------------------//
    //                                    PLACE PICKER                                             //
    //---------------------------------------------------------------------------------------------//

    private void selectPlace() {
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        try {
            startActivityForResult(builder.build(this), REQUEST_PICK_PLACE);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_PICK_PLACE) {
            if (resultCode == RESULT_OK) {
                place = PlacePicker.getPlace(this, data);
                String toastMsg = String.format("Place: %s", place.getName());

                Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, R.string.map_error_connexion, Toast.LENGTH_SHORT).show();
    }
}
