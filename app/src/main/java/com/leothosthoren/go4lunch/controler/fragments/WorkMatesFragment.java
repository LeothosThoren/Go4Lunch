package com.leothosthoren.go4lunch.controler.fragments;


import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.widget.TextView;

import com.leothosthoren.go4lunch.R;
import com.leothosthoren.go4lunch.base.BaseFragment;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 */
public class WorkMatesFragment extends BaseFragment {
    @BindView(R.id.textView_mates)
    TextView mTextView;

    @Override
    protected BaseFragment newInstance() {
        return new WorkMatesFragment();
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_work_mates;
    }

    @Override
    protected void configureDesign() {

    }

    @Override
    protected void updateDesign() {

    }


}
