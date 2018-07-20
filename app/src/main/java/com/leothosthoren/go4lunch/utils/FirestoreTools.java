package com.leothosthoren.go4lunch.utils;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public interface FirestoreTools {

     String TAG = FirestoreTools.class.getSimpleName();

    // --------------------
    // UTILS
    // --------------------

    @Nullable
    default FirebaseUser getCurrentUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    default Boolean isCurrentUserLogged() {
        return (this.getCurrentUser() != null);
    }

    // --------------------
    // ERROR HANDLER
    // --------------------

    default OnFailureListener onFailureListener() {
        return e -> {
//                Toast.makeText(get, "R.string.error_unknown_error", Toast.LENGTH_LONG).show();
            //Handle it
            Log.e(TAG, "onFailure: an error occurred... " + e.getMessage());
        };
    }

}
