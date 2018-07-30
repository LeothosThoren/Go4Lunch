package com.leothosthoren.go4lunch.utils;


import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.MultiAutoCompleteTextView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.leothosthoren.go4lunch.R;

public interface HttpRequestTools {

    /*
     * @method progressBarHandler
     * @param progressBar
     * @param context
     *
     * Personalised the progressbar color
     * */
    default void progressBarHandler(ProgressBar progressBar, Context context) {
        progressBar.getIndeterminateDrawable()
                .setColorFilter(ContextCompat.getColor(context, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
    }

    /*
     * @method updateUIWhenStartingHTTPRequest
     * @param progressBar
     *
     * The progress bar is launched just before the http request start
     * */
    default void updateUIWhenStartingHTTPRequest(ProgressBar progressBar) {
        progressBar.setVisibility(View.VISIBLE);
    }

    /*
     * @method updateUIWhenStopingHTTPRequest
     * @param refresh
     * @param bar
     *
     * On complete status the http request, the UI interface stop to refresh screen
     * */
    default void updateUIWhenStopingHTTPRequest(SwipeRefreshLayout refresh, ProgressBar bar) {
        bar.setVisibility(View.GONE);
        refresh.setRefreshing(false);

    }

    /*
     * @method internetDisable
     * @param progressBar
     * @param text
     * @param context
     *
     * When the http request is on error status a toast message is displayed to alert the user
     * */
    default void internetDisable(ProgressBar progressBar, String text, Context context) {
        progressBar.setVisibility(View.GONE);
        Toast.makeText(context, text, Toast.LENGTH_LONG).show();
    }

    default String setLocationIntoString(Double d1, Double d2) {
        return String.valueOf(d1 + "," + d2);
    }

}