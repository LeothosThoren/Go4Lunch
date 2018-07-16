package com.leothosthoren.go4lunch.controler.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.leothosthoren.go4lunch.R;
import com.leothosthoren.go4lunch.base.BaseActivity;

public class RestaurantInfoActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int getFragmentLayout() {
        return R.layout.activity_restaurant_info;
    }
}
