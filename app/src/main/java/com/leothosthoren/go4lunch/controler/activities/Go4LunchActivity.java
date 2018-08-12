package com.leothosthoren.go4lunch.controler.activities;

import android.Manifest;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.tasks.OnSuccessListener;
import com.leothosthoren.go4lunch.R;
import com.leothosthoren.go4lunch.adapter.PlaceAutocompleteAdapter;
import com.leothosthoren.go4lunch.api.UserHelper;
import com.leothosthoren.go4lunch.base.BaseActivity;
import com.leothosthoren.go4lunch.controler.fragments.MapViewFragment;
import com.leothosthoren.go4lunch.controler.fragments.RestaurantViewFragment;
import com.leothosthoren.go4lunch.controler.fragments.WorkMatesViewFragment;
import com.leothosthoren.go4lunch.model.firebase.Users;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class Go4LunchActivity extends BaseActivity implements
        NavigationView.OnNavigationItemSelectedListener, GoogleApiClient.OnConnectionFailedListener {

    // CONSTANT
    private static final int SIGN_OUT_TASK = 83; //ASCII 'S'
    // STATIC DATA FOR PICTURE
    private static final String PERMS = Manifest.permission.READ_EXTERNAL_STORAGE;
    private static final int RC_IMAGE_PERMS = 100;
    // WIDGET
    private Toolbar mToolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private TextView mTextViewUser;
    private TextView mTextViewEmail;
    private ImageView mImageViewProfile;
    private AutoCompleteTextView mSearchText;
    private PlaceAutocompleteAdapter mPlaceAutocompleteAdapter;
    private RelativeLayout relativeLayout;
    // VAR
    private GoogleApiClient mGoogleApiClient;
    private LatLng mDefaultLocation = new LatLng(48.813326, 2.348383);


    //BOTTOM NAVIGATION VIEW
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = item -> {

        switch (item.getItemId()) {
            case R.id.navigation_map:
                //TODO
                Objects.requireNonNull(getSupportActionBar()).setTitle(getString(R.string.title_action_bar_hungry));
                configureContentFrameFragment(new MapViewFragment());
                return true;
            case R.id.navigation_list:
                Objects.requireNonNull(getSupportActionBar()).setTitle(getString(R.string.title_action_bar_hungry));
                configureContentFrameFragment(new RestaurantViewFragment());
                return true;
            case R.id.navigation_workmates:
                //TODO
                Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.title_action_bar_workmate);
                configureContentFrameFragment(new WorkMatesViewFragment());
                return true;
        }
        return false;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //For autocomplete researching
        this.relativeLayout = (RelativeLayout) findViewById(R.id.autocomplete_relative_layout);
        this.mSearchText = (AutoCompleteTextView) findViewById(R.id.input_search);
        this.init();

    }

    @Override
    public int getFragmentLayout() {
        return R.layout.activity_go4lunch;
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed: " + connectionResult.getErrorMessage());
    }

    //---------------------
    // INIT
    //---------------------

    private void init() {
        this.configureToolbar();
        this.configureDrawerLayout();
        this.configureNavigationView();
        this.configureNavHeader();
        this.configureBottomNavigationView();
        this.configureContentFrameFragment(new MapViewFragment());
        this.updateMenuUIOnCreation();
        this.configureAutocomplete();
    }

    private void configureAutocomplete() {
        Log.d(TAG, "configureAutocomplete: ok");
        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .build();

        mSearchText.setOnEditorActionListener((v, actionId, keyEvent) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH
                    || actionId == EditorInfo.IME_ACTION_DONE
                    || keyEvent.getAction() == KeyEvent.ACTION_DOWN
                    || keyEvent.getAction() == KeyEvent.KEYCODE_ENTER) {

                //execute our method for searching
                // Try query autocomplete
                // geoLocate();
                hideSoftKeyboard();

            }
            return false;
        });

        // Handle the adapter for custom search
        mPlaceAutocompleteAdapter = new PlaceAutocompleteAdapter(this, mGoogleApiClient,
                new LatLngBounds(mDefaultLocation, mDefaultLocation), null);

        if (mSearchText != null) {
            mSearchText.setAdapter(mPlaceAutocompleteAdapter);
        }


    }


    private void geoLocate() {
        Log.d(TAG, "geoLocate: geolocating");

        String searchString = mSearchText.getText().toString();

        Geocoder geocoder = new Geocoder(this);
        List<Address> list = new ArrayList<>();
        try {
            list = geocoder.getFromLocationName(searchString, 1);
        } catch (IOException e) {
            Log.e(TAG, "geoLocate: IOException: " + e.getMessage());
        }

        if (list.size() > 0) {
            Address address = list.get(0);

            Log.d(TAG, "geoLocate: found a location: " + address.toString());
            Toast.makeText(this, address.toString(), Toast.LENGTH_SHORT).show();

        }

        hideSoftKeyboard();
    }

    //---------------------
    // NAVIGATION
    //---------------------


    //Handle menu drawer on back press button
    @Override
    public void onBackPressed() {
        // Handle back clickHandler to close menu
        if (this.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            this.drawerLayout.closeDrawer(GravityCompat.START);
        } else if (relativeLayout.getVisibility() == View.VISIBLE) {
            this.relativeLayout.setVisibility(View.GONE);
            this.hideSoftKeyboard();
        } else {
            super.onBackPressed();
        }
    }

    //Handle the clickHandler on MENU DRAWER
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle Navigation Item Click
        int id = item.getItemId();
        switch (id) {
            case R.id.nav_lunch:
                break;
            case R.id.nav_settings:
                this.startActivitySettings();
                break;
            case R.id.nav_logout:
                this.signOutUser();
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

    protected void configureToolbar() {
        this.mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(this.mToolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu and add it to the Toolbar
        getMenuInflater().inflate(R.menu.menu_search, menu);
        return true;
    }


    // Configure Drawer Layout
    private void configureDrawerLayout() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                drawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    // Configure NavigationView
    private void configureNavigationView() {
        this.navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    private void configureNavHeader() {
        // 3 bis - Handle navigation header items
        View headView = navigationView.getHeaderView(0);
        mTextViewUser = (TextView) headView.findViewById(R.id.menu_drawer_user);
        mTextViewEmail = (TextView) headView.findViewById(R.id.menu_drawer_email);
        mImageViewProfile = (ImageView) headView.findViewById(R.id.menu_drawer_imageView);


    }

    // 4 - Configure BottomNavigationView
    public void configureBottomNavigationView() {
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    //Launch activity
    private void startActivitySettings() {
        Intent intent = new Intent(this, SettingActivity.class);
        startActivity(intent);
    }

    //Launch fragments
    private void configureContentFrameFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.content_frame, fragment).commit();
    }


    // -----------------------
    // REST REQUESTS FIREBASE
    // -----------------------


    private void signOutUser() {
        AuthUI.getInstance()
                .signOut(this)
                .addOnSuccessListener(this, updateUiAfterHttpRequestsCompleted(SIGN_OUT_TASK));
    }


    //---------------------
    // UI
    //---------------------


    private void updateMenuUIOnCreation() {

        if (this.getCurrentUser() != null) {

            //data from Firestore
            UserHelper.getUser(this.getCurrentUser().getUid()).addOnSuccessListener(documentSnapshot -> {
                Users currentUser = documentSnapshot.toObject(Users.class);
                assert currentUser != null;

                //Get user picture from providers on Firebase
                if (currentUser.getUrlPicture() != null) {
                    Glide.with(this)
                            .load(currentUser.getUrlPicture())
                            .apply(RequestOptions.circleCropTransform())
                            .into(this.mImageViewProfile);
                }

                //Update picture when registration contain empty photo
                if (currentUser.getUrlPicture() != null) {
                    UserHelper.updateProfilePicture(currentUser.getUrlPicture(),
                            Objects.requireNonNull(this.getCurrentUser()).getUid())
                            .addOnFailureListener(this.onFailureListener(this));
                }

                String username = TextUtils.isEmpty(currentUser.getUserName()) ?
                        getString(R.string.info_no_user_name_found) : currentUser.getUserName();

                String email = TextUtils.isEmpty(getCurrentUser().getEmail()) ?
                        getString(R.string.info_no_email_found) : getCurrentUser().getEmail();
                mTextViewUser.setText(username);
                mTextViewEmail.setText(email);

            });
        }
    }

    private void hideSoftKeyboard() {
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

    }
    //---------------------
    // PERMISSION
    //---------------------


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }


    private OnSuccessListener<Void> updateUiAfterHttpRequestsCompleted(final int taskId) {
        return aVoid -> {
            switch (taskId) {
                case SIGN_OUT_TASK:
                    finish();
                    break;
                default:
                    break;
            }
        };
    }


    // ---------------------
    // ACTION
    // ---------------------

    // Configure clickHandler on menu Toolbar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_search && relativeLayout.getVisibility() == View.GONE) {
            relativeLayout.setVisibility(View.VISIBLE);
        }
        return super.onOptionsItemSelected(item);
    }

    // Ask permission when accessing to this listener
    @AfterPermissionGranted(RC_IMAGE_PERMS)
    public void onClickProfilePicture(View view) {
        if (!EasyPermissions.hasPermissions(getApplicationContext(), PERMS)) {
            EasyPermissions.requestPermissions(this, getString(R.string.popup_title_permission_files_access),
                    RC_IMAGE_PERMS, PERMS);
            return;
        }
        Toast.makeText(this, R.string.allow_pick_picture_on_device, Toast.LENGTH_SHORT).show();
    }


}
