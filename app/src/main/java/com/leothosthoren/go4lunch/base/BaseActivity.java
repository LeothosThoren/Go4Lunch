package com.leothosthoren.go4lunch.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.leothosthoren.go4lunch.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Optional;

/**
 * Created by Sofiane M. alias Leothos Thoren on 04/05/2018
 */
public abstract class BaseActivity extends AppCompatActivity {

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


    // --------------------
    // UTILS
    // --------------------

    @Nullable
    protected FirebaseUser getCurrentUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    protected Boolean isCurrentUserLogged() {
        return (this.getCurrentUser() != null);
    }

}