package com.leothosthoren.go4lunch.utils;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public interface FirestoreTools {

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
        return new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
//                Toast.makeText(get, "R.string.error_unknown_error", Toast.LENGTH_LONG).show();
            }
        };
    }

}
