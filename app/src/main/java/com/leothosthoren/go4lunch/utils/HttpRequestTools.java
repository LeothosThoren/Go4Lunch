package com.leothosthoren.go4lunch.utils;


import android.content.Context;
import android.graphics.PorterDuff;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.leothosthoren.go4lunch.R;

public interface HttpRequestTools {

    /**
     * @param progressBar ProgressBar
     * @param context     Context
     *                    <p>
     *                    Personalised the progressbar color
     * @method progressBarHandler
     */
    default void progressBarHandler(ProgressBar progressBar, Context context) {
        progressBar.getIndeterminateDrawable()
                .setColorFilter(ContextCompat.getColor(context, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
    }

    /**
     * @param progressBar ProgressBar
     *                    <p>
     *                    The progress bar is launched just before the http request start
     * @method updateUIWhenStartingHTTPRequest
     */
    default void updateUIWhenStartingHTTPRequest(ProgressBar progressBar) {
        progressBar.setVisibility(View.VISIBLE);
    }

    /**
     * @param refresh SwipeRefreshLayout
     * @param bar     ProgressBar
     *                <p>
     *                On complete status the http request, the UI interface stop to refresh screen
     * @method updateUIWhenStopingHTTPRequest
     */
    default void updateUIWhenStopingHTTPRequest(SwipeRefreshLayout refresh, ProgressBar bar) {
        bar.setVisibility(View.GONE);
        refresh.setRefreshing(false);

    }

    /**
     * @param progressBar ProgressBar
     * @param text        String
     * @param context     Context
     *                    <p>
     *                    When the http request is on error status a toast message is displayed to alert the user
     * @method internetDisable
     */
    default void internetDisable(ProgressBar progressBar, String text, Context context) {
        progressBar.setVisibility(View.GONE);
        Toast.makeText(context, text, Toast.LENGTH_LONG).show();
    }

    /**
     * @param d1 Double
     * @param d2 Double
     *           <p>
     *           Format double values into one string for http request parameter
     * @method setLocationIntoString
     */
    default String setLocationIntoString(Double d1, Double d2) {
        return String.valueOf(d1 + "," + d2);
    }

}