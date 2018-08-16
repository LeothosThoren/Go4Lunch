package com.leothosthoren.go4lunch.utils;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.leothosthoren.go4lunch.R;


public interface FireBaseTools {

    String TAG = FireBaseTools.class.getSimpleName();

    // --------------------
    // UTILS
    // --------------------

    /**
     * @method getCurrentUser
     * <p>
     * Get Firebase singleton instance
     */
    @Nullable
    default FirebaseUser getCurrentUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    /**
     * @method isCurrentUserLogged
     * <p>
     * Check the current user auth
     */
    default Boolean isCurrentUserLogged() {
        return (this.getCurrentUser() != null);
    }

    // --------------------
    // RESPONSE HANDLER
    // --------------------


    /**
     * @param context Context
     * @param s       String
     *                <p>
     *                Check if current task is successful and display a configurable toast message
     * @method onCompleteListener
     */
    // Complete response
    default OnCompleteListener<Void> onCompleteListener(Context context, String s) {
        return task -> {
            if (task.isSuccessful()) {
                Log.d(TAG, "onCompleteListener: it is successful");
                Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
            }
        };
    }

    /**
     * @param context Context
     *                <p>
     *                Check if current task failed and display a default toast error message
     * @method onCompleteListener
     */
    // Failure response
    default OnFailureListener onFailureListener(Context context) {
        return e -> {
            //Handle it
            Log.e(TAG, "onFailure: an error occurred... " + e.getMessage());
            Toast.makeText(context, R.string.error_unknown_error, Toast.LENGTH_SHORT).show();
        };
    }

}
