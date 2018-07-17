package com.leothosthoren.go4lunch.controler.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.leothosthoren.go4lunch.R;
import com.leothosthoren.go4lunch.base.BaseActivity;

public class RestaurantInfoActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fab();
    }

    @Override
    public int getFragmentLayout() {
        return R.layout.activity_restaurant_info;
    }

    //FOR TESTING
    public void fab() {
        FloatingActionButton fab = findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Here's a Snackbar", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                fab.setImageResource(R.drawable.ic_check_circle);
            }
        });
    }
}
