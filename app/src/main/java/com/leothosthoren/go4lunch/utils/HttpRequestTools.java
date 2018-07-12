package com.leothosthoren.go4lunch.utils;


import android.content.Context;
import android.graphics.PorterDuff;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.leothosthoren.go4lunch.R;

public class HttpRequestTools {

    public HttpRequestTools() {
    }

    /*
     * @method progressBarHandler
     * @param progressBar
     * @param context
     *
     * Personalised the progressbar color
     * */
    public void progressBarHandler(ProgressBar progressBar, Context context) {
        progressBar.getIndeterminateDrawable()
                .setColorFilter(ContextCompat.getColor(context, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
    }

    /*
     * @method updateUIWhenStartingHTTPRequest
     * @param progressBar
     *
     * The progress bar is launched just before the http request start
     * */
    public void updateUIWhenStartingHTTPRequest(ProgressBar progressBar) {
        progressBar.setVisibility(View.VISIBLE);
    }

    /*
     * @method updateUIWhenStopingHTTPRequest
     * @param refresh
     * @param bar
     *
     * On complete status the http request, the UI interface stop to refresh screen
     * */
    public void updateUIWhenStopingHTTPRequest(SwipeRefreshLayout refresh, ProgressBar bar) {
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
    public void internetDisable(ProgressBar progressBar, String text, Context context) {
        progressBar.setVisibility(View.GONE);
        Toast.makeText(context, text, Toast.LENGTH_LONG).show();
    }
}