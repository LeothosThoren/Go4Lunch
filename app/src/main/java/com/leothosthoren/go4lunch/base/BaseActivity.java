package com.leothosthoren.go4lunch.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import com.leothosthoren.go4lunch.utils.DataConverterHelper;
import com.leothosthoren.go4lunch.utils.FireBaseTools;

import butterknife.ButterKnife;
import butterknife.Optional;

public abstract class BaseActivity extends AppCompatActivity implements FireBaseTools, DataConverterHelper {

    // --------------------
    // LIFE CYCLE
    // --------------------

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(getFragmentLayout());
        ButterKnife.bind(this); //Configure Butterknife
    }

    public abstract int getFragmentLayout();

    // --------------------
    // UI
    // --------------------

    @Optional
    protected void configureToolbar() {
        ActionBar ab = getSupportActionBar();
        assert ab != null;
        ab.setDisplayHomeAsUpEnabled(true);
    }

    //Generic activity launcher method
    public void startActivity(Class activity) {
        Intent intent = new Intent(this, activity);
        startActivity(intent);
    }


}