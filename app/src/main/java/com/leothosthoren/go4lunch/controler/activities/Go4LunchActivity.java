package com.leothosthoren.go4lunch.controler.activities;

import android.Manifest;
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
import android.widget.AdapterView;
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
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.tasks.OnSuccessListener;
import com.leothosthoren.go4lunch.R;
import com.leothosthoren.go4lunch.adapter.PlaceAutocompleteAdapter;
import com.leothosthoren.go4lunch.api.PlaceStreams;
import com.leothosthoren.go4lunch.api.RestaurantHelper;
import com.leothosthoren.go4lunch.api.UserHelper;
import com.leothosthoren.go4lunch.base.BaseActivity;
import com.leothosthoren.go4lunch.controler.fragments.MapViewFragment;
import com.leothosthoren.go4lunch.controler.fragments.RestaurantViewFragment;
import com.leothosthoren.go4lunch.controler.fragments.WorkMatesViewFragment;
import com.leothosthoren.go4lunch.data.DataSingleton;
import com.leothosthoren.go4lunch.model.detail.PlaceDetail;
import com.leothosthoren.go4lunch.model.firebase.Restaurants;
import com.leothosthoren.go4lunch.model.firebase.Users;

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

//import static com.leothosthoren.go4lunch.controler.fragments.MapViewFragment.LATITUDE_BOUND;
//import static com.leothosthoren.go4lunch.controler.fragments.MapViewFragment.LONGITUDE_BOUND;
//import static com.leothosthoren.go4lunch.controler.fragments.MapViewFragment.SENDER_KEY;

public class Go4LunchActivity extends BaseActivity implements
        NavigationView.OnNavigationItemSelectedListener, GoogleApiClient.OnConnectionFailedListener {

    // CONSTANT
    private static final int SIGN_OUT_TASK = 83; //ASCII 'S'
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
    private RelativeLayout relativeLayout;
    // VAR
    private GoogleApiClient mGoogleApiClient;
    private PlaceAutocompleteAdapter mPlaceAutocompleteAdapter;
    private Disposable mDisposable;



    // Get resuslt callback and launch request on place detail Api
    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback = places -> {
        if (!places.getStatus().isSuccess()) {
            Log.d(TAG, "onResult: Place query did not complete successfully: " + places.getStatus().toString());
            places.release();
            return;
        }
        final Place place = places.get(0);
        try {
            Log.d(TAG, "onResult: see placeID: " + place.getId());
            //Get the place id and launch a request to get the placeDetail from placeDetail Api
            this.executeHttpRequestWithPlaceDetail(place.getId());
        } catch (NullPointerException e) {
            Log.e(TAG, "onResult: NullPointerException: " + e.getMessage());
        }

        places.release();
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //For autocomplete researching
        this.relativeLayout = (RelativeLayout) findViewById(R.id.autocomplete_relative_layout);
        this.mSearchText = (AutoCompleteTextView) findViewById(R.id.input_search);
        this.init();

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
        this.searchPlaceWithAutocomplete();
    }

    @Override
    public int getFragmentLayout() {
        return R.layout.activity_go4lunch;
    }


    //---------------------
    // NAVIGATION
    //---------------------


    //BOTTOM NAVIGATION VIEW
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = item -> {

        switch (item.getItemId()) {
            case R.id.navigation_map:
                this.updateUI(R.string.title_action_bar_hungry, R.string.search_restaurant);
                this.configureContentFrameFragment(new MapViewFragment());
                return true;
            case R.id.navigation_list:
                this.updateUI(R.string.title_action_bar_hungry, R.string.search_restaurant);
                this.configureContentFrameFragment(new RestaurantViewFragment());
                return true;
            case R.id.navigation_workmates:
                this.updateUI(R.string.title_action_bar_workmate, R.string.search_workmates);
                this.configureContentFrameFragment(new WorkMatesViewFragment());
                return true;
        }
        return false;
    };


    //Handle the clickHandler on MENU DRAWER
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle Navigation Item Click
        int id = item.getItemId();
        switch (id) {
            case R.id.nav_lunch:
                this.showUserLunch();
                break;
            case R.id.nav_settings:
                this.startActivity(SettingActivity.class);
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

//    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
//    @Override
//    protected void onResume() {
//        super.onResume();
//        final String sender = Objects.requireNonNull(this.getIntent().getExtras()).getString(SENDER_KEY);
//
//        Log.d(TAG, "onResume: "+sender);
//        if (sender != null) {
//            this.searchPlaceWithAutocomplete();
//        }
//    }

    /**/

    // ---------------------
    // CONFIGURATION
    // ---------------------

    //Handle menu drawer on back press button
    @Override
    public void onBackPressed() {
        // Handle back clickHandler to close menu
        if (this.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            this.drawerLayout.closeDrawer(GravityCompat.START);
        } else if (relativeLayout.getVisibility() == View.VISIBLE) {
            updateAutocompleteEditText();
        } else {
            super.onBackPressed();
        }
    }

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

    // Configure BottomNavigationView
    public void configureBottomNavigationView() {
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }


    // -----------------------
    // REST REQUESTS
    // -----------------------

    // Launch fragments
    private void configureContentFrameFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.content_frame, fragment).commit();
    }

    // Handle GoogleApiClient, filter and autocomplete Adapter
    private void configureAutocomplete() {
        Log.d(TAG, "configureAutocomplete: ok");
        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .build();

        // Set a filter
        AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                .setTypeFilter(Place.TYPE_RESTAURANT)
                .build();

        //TODO find how to send data fom fragment to activity
//        //Retrieve coordinates from fragment map
//        Intent i = getIntent();
//        Double lat = i.getDoubleExtra(LATITUDE_BOUND, 48.8566);
//        Double lng = i.getDoubleExtra(LONGITUDE_BOUND, 2.35222);

        // Determine latitude and longitude bound from intent
        LatLng deviceBound = new LatLng(48.8566, 2.35222);

        // Handle the adapter for custom search
        mPlaceAutocompleteAdapter = new PlaceAutocompleteAdapter(this, mGoogleApiClient,
                new LatLngBounds(deviceBound, deviceBound), typeFilter);
        if (mSearchText != null) {
            mSearchText.setAdapter(mPlaceAutocompleteAdapter);
        }
    }

    private void signOutUser() {
        AuthUI.getInstance()
                .signOut(this)
                .addOnSuccessListener(this, updateUiAfterHttpRequestsCompleted(SIGN_OUT_TASK));
    }


    //------------------------
    // HTTP RxJava
    //------------------------

    // Get Restaurant selection from Firestore
    private void showUserLunch() {
        if (getCurrentUser() != null) {
            RestaurantHelper.getRestaurantSelection(getCurrentUser().getUid())
                    .addOnSuccessListener(documentSnapshot -> {
                        Restaurants restaurants = documentSnapshot.toObject(Restaurants.class);
                        if (restaurants != null) {
                            Date dateChoice = restaurants.getDateChoice();
                            PlaceDetail placeDetail = restaurants.getPlaceDetail();
                            if (formatDate(dateChoice).equals(formatDate(Calendar.getInstance().getTime()))) {
                                //Set Singleton
                                DataSingleton.getInstance().setPlaceDetail(placeDetail);
                                this.startActivity(RestaurantInfoActivity.class);
                            } else {
                                Toast.makeText(this, R.string.restaurant_selection_availability, Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(this, R.string.restaurant_selection_availability, Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(this.onFailureListener(this));
        }

    }

    public void executeHttpRequestWithPlaceDetail(String placeID) {
        mDisposable = PlaceStreams.streamFetchPlaceDetail(placeID)
                .subscribeWith(new DisposableObserver<PlaceDetail>() {
                    @Override
                    public void onNext(PlaceDetail placeDetail) {
                        Log.d(TAG, "onNext: " + placeDetail.getResult().getName());
                        DataSingleton.getInstance().setPlaceDetail(placeDetail);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError: " + e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete: " + placeID);
                        updateAutocompleteEditText();
                        Toast.makeText(getApplicationContext(), "Search action done", Toast.LENGTH_SHORT).show();
                        startActivity(RestaurantInfoActivity.class);
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

                String username = TextUtils.isEmpty(currentUser.getUsername()) ?
                        getString(R.string.info_no_user_name_found) : currentUser.getUsername();

                String email = TextUtils.isEmpty(getCurrentUser().getEmail()) ?
                        getString(R.string.info_no_email_found) : getCurrentUser().getEmail();
                mTextViewUser.setText(username);
                mTextViewEmail.setText(email);

            });
        }
    }

    private void updateAutocompleteEditText() {
        if (relativeLayout.getVisibility() == View.VISIBLE) {
            this.mSearchText.setText("");
            this.relativeLayout.setVisibility(View.GONE);
            this.hideSoftKeyboard();
        }
    }

    private void updateUI(int title, int hint) {
        Objects.requireNonNull(getSupportActionBar()).setTitle(getString(title));
        mSearchText.setHint(getString(hint));
    }


    //---------------------
    // PERMISSION
    //---------------------

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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    // ---------------------
    // ACTION
    // ---------------------

    // Configure click on menu Toolbar
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

    //Handle autocomplete adapter clickListener
    private AdapterView.OnItemClickListener mAutocompleteClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            hideSoftKeyboard();

            final AutocompletePrediction item = mPlaceAutocompleteAdapter.getItem(i);
            assert item != null;
            final String placeId = item.getPlaceId();

            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                    .getPlaceById(mGoogleApiClient, placeId);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallback);
        }
    };


    private void searchPlaceWithAutocomplete() {
        this.configureAutocomplete();
        // Autocomplete edit text to perform the search
        mSearchText.setOnEditorActionListener((v, actionId, keyEvent) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH
                    || actionId == EditorInfo.IME_ACTION_DONE
                    || keyEvent.getAction() == KeyEvent.ACTION_DOWN
                    || keyEvent.getAction() == KeyEvent.KEYCODE_ENTER) {
                //Handle the click adapter
                mSearchText.setOnItemClickListener(mAutocompleteClickListener);
                hideSoftKeyboard();
            }

            return false;
        });
    }


    // ---------------------
    // UTILS
    // ---------------------

    //When user finished to edit text keyboard disappear
    private void hideSoftKeyboard() {
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed: " + connectionResult.getErrorMessage());
    }


}
