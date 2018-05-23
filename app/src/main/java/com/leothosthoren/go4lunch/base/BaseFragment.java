package com.leothosthoren.go4lunch.base;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;

/**
 * Created by Sofiane M. alias Leothos Thoren on 23/05/2018
 */
public abstract class BaseFragment extends Fragment {
    // 1 - Force developer implement those methods
    protected abstract BaseFragment newInstance();
    protected abstract int getFragmentLayout();
    protected abstract void configureDesign();
    protected abstract void updateDesign();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // 2 - Get layout identifier from abstract method
        View view = inflater.inflate(getFragmentLayout(), container, false);
        // 3 - Binding Views
        ButterKnife.bind(this, view);
        // 4 - Configure Design (Developer will call this method instead of override onCreateView())
        this.configureDesign();
        return(view);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // 7 - Update Design (Developer will call this method instead of override onActivityCreated())
        this.updateDesign();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}
