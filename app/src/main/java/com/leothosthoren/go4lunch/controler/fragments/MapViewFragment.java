package com.leothosthoren.go4lunch.controler.fragments;


import android.support.v4.app.Fragment;

import com.leothosthoren.go4lunch.R;
import com.leothosthoren.go4lunch.base.BaseFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapViewFragment extends BaseFragment {

    public static final String TAG = MapViewFragment.class.getSimpleName();


    @Override
    protected BaseFragment newInstance() {
        return new MapViewFragment();
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_map_view;
    }

    @Override
    protected void configureDesign() {

    }

    @Override
    protected void updateDesign() {

    }

}

