package com.leothosthoren.go4lunch.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.leothosthoren.go4lunch.R;


public interface FireBaseTools {

    String TAG = FireBaseTools.class.getSimpleName();

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
    // RESPONSE HANDLER
    // --------------------

    // Complete
    default OnCompleteListener<Void> onCompleteListener(Context c, String s) {
        return task -> {
            if (task.isSuccessful()) {
                Log.d(TAG, "onCompleteListener: it is successful");
                Toast.makeText(c, s, Toast.LENGTH_SHORT).show();
            }
        };
    }

    // Failure
    default OnFailureListener onFailureListener(Context c) {
        return e -> {
            //Handle it
            Log.e(TAG, "onFailure: an error occurred... " + e.getMessage());
            Toast.makeText(c, R.string.error_unknown_error, Toast.LENGTH_SHORT).show();
        };
    }

}
