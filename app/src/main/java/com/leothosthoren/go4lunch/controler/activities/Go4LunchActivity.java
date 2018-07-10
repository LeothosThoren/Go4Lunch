package com.leothosthoren.go4lunch.controler.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnSuccessListener;
import com.leothosthoren.go4lunch.R;
import com.leothosthoren.go4lunch.base.BaseActivity;
import com.leothosthoren.go4lunch.controler.fragments.RestaurantViewFragment;
import com.leothosthoren.go4lunch.controler.fragments.MapViewFragment;
import com.leothosthoren.go4lunch.controler.fragments.WorkMatesViewFragment;

public class Go4LunchActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final int SIGN_OUT_TASK = 83; //ASCII 'S'

    private Toolbar mToolbar;
    //    private TextView mTextViewUser;
//    private TextView mTextViewEmail;
//    private ImageView mImageViewProfile;
    private DrawerLayout drawerLayout;


    private NavigationView navigationView;

    //BOTTOM NAVIGATION VIEW
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()) {
                case R.id.navigation_map:
                    //TODO
                    //Try to launch activity
                    configureFragment(new MapViewFragment());
                    return true;
                case R.id.navigation_list:
                    //TODO
                    configureFragment(new RestaurantViewFragment());
                    return true;
                case R.id.navigation_workmates:
                    //TODO
                    configureFragment(new WorkMatesViewFragment());
                    return true;
            }
            return false;
        }
    };

    @Override
    public void startActivityFromFragment(Fragment fragment, Intent intent, int requestCode) {
        super.startActivityFromFragment(fragment, intent, requestCode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Menu configuration
//        mTextViewUser = (TextView) findViewById(R.id.menu_drawer_user);
//        mTextViewEmail = (TextView)  findViewById(R.id.menu_drawer_email);
//        mImageViewProfile = (ImageView) findViewById(R.id.menu_drawer_imageView);

        this.configureToolbar();
        this.configureDrawerLayout();
        this.configureNavigationView();
        this.configureBottomNavigationView();
//        this.updateUIOnCreation();
        this.configureFragment(new MapViewFragment());
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
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //2 - Inflate the menu and add it to the Toolbar
        getMenuInflater().inflate(R.menu.menu_search, menu);
        return true;
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
    }

    // 2 - Configure Drawer Layout
    private void configureDrawerLayout() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    // 3 - Configure NavigationView
    private void configureNavigationView() {
        this.navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
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
    private void configureFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.content_frame, fragment).commit();
    }

    // --------------------
    // REST REQUESTS
    // --------------------

    private void signOutUser() {
        AuthUI.getInstance()
                .signOut(this)
                .addOnSuccessListener(this, updateUiAfterHttpRequestsCompleted(SIGN_OUT_TASK));
    }

    //---------------------
    // UI
    //---------------------


//    private void updateUIOnCreation() {
//
//        if (this.getCurrentUser() != null) {
//
//            //Get user picture from providers on Firebase
//            if (this.getCurrentUser().getPhotoUrl() != null) {
//                Glide.with(this)
//                        .load(this.getCurrentUser().getPhotoUrl())
//                        .apply(RequestOptions.circleCropTransform())
//                        .into(mImageViewProfile);
//            }
//
//            //Get user 's name
//            String username = TextUtils.isEmpty(this.getCurrentUser().getDisplayName()) ?
//                    getString(R.string.info_no_user_name_found) : this.getCurrentUser().getDisplayName();
//
//            String email = TextUtils.isEmpty(this.getCurrentUser().getEmail()) ?
//                    getString(R.string.info_no_email_found) : this.getCurrentUser().getEmail();
//            //Update view with data
//            mTextViewUser.setText(username);
//            mTextViewEmail.setText(email);
//        }
//    }

    private OnSuccessListener<Void> updateUiAfterHttpRequestsCompleted(final int taskId) {
        return new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                switch (taskId) {
                    case SIGN_OUT_TASK:
                        finish();
                        break;
                    default:
                        break;
                }
            }
        };
    }

}
