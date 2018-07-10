package com.leothosthoren.go4lunch.controler.fragments;


import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;

import com.leothosthoren.go4lunch.R;
import com.leothosthoren.go4lunch.adapter.RestaurantRVAdapter;
import com.leothosthoren.go4lunch.base.BaseFragment;
import com.leothosthoren.go4lunch.model.WorkmateItem;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 */
public class WorkMatesViewFragment extends BaseFragment {
//    @BindView(R.id.recycler_view_id)
//    RecyclerView mRecyclerView;
//
//    private ArrayList<WorkmateItem> mWorkmateItemsList;
//    private RestaurantRVAdapter mAdapter;

    @Override
    protected BaseFragment newInstance() {
        return new WorkMatesViewFragment();
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_workmates_list;
    }

    @Override
    protected void configureDesign() {

    }

    @Override
    protected void updateDesign() {

//        updateUI(mWorkmateItemsList);
    }


//    private void configureWorkmateRecyclerView(){
//        this.mWorkmateItemsList = new ArrayList<>();
//        this.mAdapter = new RestaurantRVAdapter(this.mWorkmateItemsList);
//        this.mRecyclerView.setAdapter(this.mAdapter);
//        this.mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//    }

//    private void updateUI(ArrayList<WorkmateItem> workmateItems) {
//        mWorkmateItemsList.addAll(workmateItems);
//        mAdapter.notifyDataSetChanged();
//    }

}
