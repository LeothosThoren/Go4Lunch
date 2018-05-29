package com.leothosthoren.go4lunch.controlers.fragments;


import android.support.v4.app.Fragment;

import com.leothosthoren.go4lunch.R;
import com.leothosthoren.go4lunch.base.BaseFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListViewFragment extends BaseFragment {


    @Override
    protected BaseFragment newInstance() {
        return new ListViewFragment();
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_list_view;
    }

    @Override
    protected void configureDesign() {

    }

    @Override
    protected void updateDesign() {

    }
}
