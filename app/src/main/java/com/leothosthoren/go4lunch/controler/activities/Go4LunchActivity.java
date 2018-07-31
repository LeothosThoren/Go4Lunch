package com.leothosthoren.go4lunch.controler.activities;

import android.Manifest;
import android.content.Intent;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnSuccessListener;
import com.leothosthoren.go4lunch.R;
import com.leothosthoren.go4lunch.adapter.PlaceAutocompleteAdapter;
import com.leothosthoren.go4lunch.api.UserHelper;
import com.leothosthoren.go4lunch.base.BaseActivity;
import com.leothosthoren.go4lunch.controler.fragments.MapViewFragment;
import com.leothosthoren.go4lunch.controler.fragments.RestaurantViewFragment;
import com.leothosthoren.go4lunch.controler.fragments.WorkMatesViewFragment;
import com.leothosthoren.go4lunch.model.firebase.Users;

import java.util.Objects;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class Go4LunchActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

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
        this.configureToolbar();
        this.configureDrawerLayout();
        this.configureNavigationView();
        this.configureNavHeader();
        this.configureBottomNavigationView();
        this.configureContentFrameFragment(new MapViewFragment());
        this.updateMenuUIOnCreation();
        this.mSearchText = (AutoCompleteTextView) findViewById(R.id.input_search);
    }

    @Override
    public int getFragmentLayout() {
        return R.layout.activity_go4lunch;
    }


    //---------------------
    // NAVIGATION
    //---------------------


    //Handle menu drawer on back press button
    @Override
    public void onBackPressed() {
        // 5 - Handle back click to close menu
        if (this.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            this.drawerLayout.closeDrawer(GravityCompat.START);

            // Todo add a else if here to handle edittext disparition
        } else {
            super.onBackPressed();
        }
    }

    //Handle the click on MENU DRAWER
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // 4 - Handle Navigation Item Click
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


    @Override
    protected void configureToolbar() {
        this.mToolbar = (Toolbar) findViewById(R.id.activity_main_toolbar);
        setSupportActionBar(this.mToolbar);
        mToolbar.setTitleTextColor(getResources().getColor(R.color.white));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //2 - Inflate the menu and add it to the Toolbar
        getMenuInflater().inflate(R.menu.menu_search, menu);
        return true;
    }

    // Configure click on menu Toolbar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_search) {
//            mSearchText.setVisibility(View.VISIBLE);
        }

        return super.onOptionsItemSelected(item);
    }

    // 2 - Configure Drawer Layout
    private void configureDrawerLayout() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                drawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    // 3 - Configure NavigationView
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
                    UserHelper.updateProfilPicture(currentUser.getUrlPicture(),
                            Objects.requireNonNull(this.getCurrentUser()).getUid())
                            .addOnFailureListener(this.onFailureListener());
                }

                String username = TextUtils.isEmpty(currentUser.getUsername()) ?
                        getString(R.string.info_no_user_name_found) : currentUser.getUsername();

                String email = TextUtils.isEmpty(this.getCurrentUser().getEmail()) ?
                        getString(R.string.info_no_email_found) : this.getCurrentUser().getEmail();
                mTextViewUser.setText(username);
                mTextViewEmail.setText(email);

            });

//            //Get user 's name
//            String username = TextUtils.isEmpty(this.getCurrentUser().getDisplayName()) ?
//                    getString(R.string.info_no_user_name_found) : this.getCurrentUser().getDisplayName();
//
//            String email = TextUtils.isEmpty(this.getCurrentUser().getUserEmail()) ?
//                    getString(R.string.info_no_email_found) : this.getCurrentUser().getUserEmail();
//            //Update view with data
//            mTextViewUser.setText(username);
//            mTextViewEmail.setText(email);
        }
    }


    //---------------------
    // PERMISSION
    //---------------------


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
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
