package com.leothosthoren.go4lunch.controler.activities;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.crashlytics.android.Crashlytics;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.firebase.firestore.DocumentSnapshot;
import com.leothosthoren.go4lunch.R;
import com.leothosthoren.go4lunch.api.UserHelper;
import com.leothosthoren.go4lunch.base.BaseActivity;

import java.util.Arrays;
import java.util.Objects;

import butterknife.BindView;
import io.fabric.sdk.android.Fabric;

public class MainActivity extends BaseActivity {


    public static final int ERROR_DIALOG_REQUEST = 69; //ASCII 'E'
    // Identifier for Sign-In Activity
    private static final int RC_SIGN_IN = 82; //ASCII 'R'
    // Widget
    @BindView(R.id.main_activity_coordinator_layout)
    CoordinatorLayout coordinatorLayout;
    @BindView(R.id.main_button)
    Button mLoginButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Fabric.with(this, new Crashlytics());
        // Force application to crash
//        Crashlytics.getInstance().crash();
        this.updateUIWhenResuming();

    }

    @Override
    public int getFragmentLayout() {
        return R.layout.activity_main;
    }

    // --------------------
    // NAVIGATION
    // --------------------

    // Launch Sign-In Activity

    private void startSignInActivity() {
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setTheme(R.style.LoginTheme)
                        .setAvailableProviders(
                                Arrays.asList(new AuthUI.IdpConfig.EmailBuilder().build(),//EMAIL
                                        new AuthUI.IdpConfig.GoogleBuilder().build(),//GOOGLE
                                        new AuthUI.IdpConfig.FacebookBuilder().build(),//FACEBOOK
                                        new AuthUI.IdpConfig.TwitterBuilder().build()))//TWITTER
                        .setIsSmartLockEnabled(false, true)
                        .setLogo(R.drawable.logo)
                        .build(),
                RC_SIGN_IN);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 4 - Handle SignIn Activity response on activity result
        this.handleResponseAfterSignIn(requestCode, resultCode, data);
    }

    // --------------------
    // ACTION
    // --------------------

    public void onClickLoginButton(View v) {
        if (this.isCurrentUserLogged() && isServiceOk()) {
            this.startActivity(Go4LunchActivity.class);
        } else {
            this.startSignInActivity();

        }
    }

    //Generic activity launcher method
    public void startActivity(Class activity) {
        Intent intent = new Intent(this, activity);
        startActivity(intent);
    }

    // --------------------
    // UI
    // --------------------

    // Show Snack Bar with a message
    private void showSnackBar(CoordinatorLayout coordinatorLayout, String message) {
        Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_SHORT).show();
    }

    private void updateUIWhenResuming() {
        this.mLoginButton.setText(this.isCurrentUserLogged() ?
                getString(R.string.button_login_text_logged) : getString(R.string.button_login_text_not_logged));
    }


    // --------------------
    // REST REQUEST
    // --------------------

    private void createUserInFirestore() {
        if (this.getCurrentUser() != null) {

            String urlPicture = (this.getCurrentUser().getPhotoUrl() != null) ?
                    this.getCurrentUser().getPhotoUrl().toString() : null;
            String username = this.getCurrentUser().getDisplayName();
            String email = this.getCurrentUser().getEmail();
            String uid = this.getCurrentUser().getUid();

            // Allow the creation on the data base
            UserHelper.createUser(uid, username, email, urlPicture).addOnFailureListener(this.onFailureListener(this));

        }
    }

    private void checkIfUserExistsInDatabase() {
        UserHelper.getUser(Objects.requireNonNull(getCurrentUser()).getUid())
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot snapshot = task.getResult();
                        if (snapshot.exists()) {
                            Log.d(TAG, "checkIfUserExistsInDatabase: on success" + snapshot.getData());
                        } else {
                            Log.d(TAG, "checkIfUserExistsInDatabase: no such document need to create one");
                            this.createUserInFirestore();
                        }
                    }
                }).addOnFailureListener(this.onFailureListener(this));
    }

    // --------------------
    // UTILS
    // --------------------

    // Method that handles response after SignIn Activity close
    private void handleResponseAfterSignIn(int requestCode, int resultCode, Intent data) {
        IdpResponse response = IdpResponse.fromResultIntent(data);

        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) { // SUCCESS
                this.checkIfUserExistsInDatabase();
                showSnackBar(this.coordinatorLayout, getString(R.string.connection_succeed));
            } else { // ERRORS
                if (response == null) {
                    showSnackBar(this.coordinatorLayout, getString(R.string.error_authentication_canceled));
                } else if (Objects.requireNonNull(response.getError()).getErrorCode() == ErrorCodes.NO_NETWORK) {
                    showSnackBar(this.coordinatorLayout, getString(R.string.error_no_internet));
                } else if (response.getError().getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                    showSnackBar(this.coordinatorLayout, getString(R.string.error_unknown_error));
                }
            }
        }
    }

    public boolean isServiceOk() {
        Log.d(TAG, "isServiceOk: checking google service version");

        int availability = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this);

        if (availability == ConnectionResult.SUCCESS) {
            //We check that the google services is fine and user can make request
            Log.d(TAG, "isServiceOk: Google Play Services is working");
            return true;
        } else if (GoogleApiAvailability.getInstance().isUserResolvableError(availability)) {
            //We have to handle the error status
            Log.d(TAG, "isServiceOk: an error occurred but we can fix it");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(this, availability,
                    ERROR_DIALOG_REQUEST);
            dialog.show();
        }
        return false;
    }


}
