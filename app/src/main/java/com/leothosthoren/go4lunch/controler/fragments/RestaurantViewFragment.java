package com.leothosthoren.go4lunch.controler.fragments;


import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.leothosthoren.go4lunch.R;
import com.leothosthoren.go4lunch.adapter.RestaurantAdapter;
import com.leothosthoren.go4lunch.base.BaseFragment;
import com.leothosthoren.go4lunch.controler.activities.RestaurantInfoActivity;
import com.leothosthoren.go4lunch.data.DataSingleton;
import com.leothosthoren.go4lunch.model.detail.PlaceDetail;
import com.leothosthoren.go4lunch.utils.ItemClickSupport;

import java.util.ArrayList;

import butterknife.BindView;
import io.reactivex.disposables.Disposable;

/**
 * A simple {@link Fragment} subclass.
 */
public class RestaurantViewFragment extends BaseFragment implements RestaurantAdapter.Listener {

    private static final String TAG = RestaurantViewFragment.class.getSimpleName();
    //VIEW
    @BindView(R.id.recycler_view_id)
    RecyclerView mRecyclerView;
    @BindView(R.id.swipe_container)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.progress_bar)
    ProgressBar mProgressBar;
    @BindView(R.id.restaurant_availability_message)
    TextView mTextViewRecyclerViewEmpty;
    //VAR
    private RestaurantAdapter mAdapter;
    private Disposable disposable;
    //DATA
    private ArrayList<PlaceDetail> PlaceDetailListFromSingleton = (ArrayList<PlaceDetail>) DataSingleton.getInstance().getPlaceDetail();
    private ArrayList<PlaceDetail> mRestaurantItemsList;


    @Override
    protected BaseFragment newInstance() {
        return new RestaurantViewFragment();
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_restaurant_list;
    }

    @Override
    protected void configureDesign() {
        this.configureSwipeRefreshLayout();
        this.progressBarHandler(mProgressBar, getContext());
        // Handle the case whether the list is empty
        if (mRestaurantItemsList != null) {
            this.configureRecyclerView();
            this.configureOnclickRecyclerView();
        } else {
            mTextViewRecyclerViewEmpty.setVisibility(View.VISIBLE);
        }


    }

    @Override
    protected void updateDesign() {
//        //TEST
//        this.checkSingletonContent();
        //Feed Array
        if (mRestaurantItemsList != null )
        this.updateUI();

    }

    // -------------------------------------------------------------------------------------------//
    //                                      CONFIGURATION                                         //
    // -------------------------------------------------------------------------------------------//


    public void configureRecyclerView() {
        this.mRestaurantItemsList = new ArrayList<>();
        this.mAdapter = new RestaurantAdapter(mRestaurantItemsList, Glide.with(this), this);
        this.mRecyclerView.setAdapter(this.mAdapter);
        this.mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    /*
     * @method configureSwipeRefreshLayout
     *
     * When the screen is swipe, the http request is executed
     * */

    public void configureSwipeRefreshLayout() {
        this.mSwipeRefreshLayout.setOnRefreshListener(() -> {
//                http request to execute
            updateUIWhenStartingHTTPRequest(mProgressBar);
            updateUI();
        });
    }


    // -------------------------------------------------------------------------------------------//
    //                                      ACTION                                                //
    // -------------------------------------------------------------------------------------------//

    private void configureOnclickRecyclerView() {
        ItemClickSupport.addTo(mRecyclerView, R.id.item_restaurant_layout)
                .setOnItemClickListener((recyclerView, position, v) -> {
                    PlaceDetail restaurantItem = mAdapter.getRestaurantItem(position);

                    Toast.makeText(getContext(), "CLICK on position: " + position + " name: " +
                            restaurantItem.getResult().getName(), Toast.LENGTH_SHORT).show();
                    // Get position and launch activity with data from singleton
                    DataSingleton.getInstance().setPosition(position);
                    startActivity(RestaurantInfoActivity.class);

                });
    }

    @Override
    public void onClickItemButton(int position) {
        PlaceDetail restaurantItem = mAdapter.getRestaurantItem(position);
        Toast.makeText(getContext(), "CLICK PICTURE on position: " + position + " name: " +
                restaurantItem.getResult().getPhotos().get(0).getPhotoReference(), Toast.LENGTH_SHORT).show();
    }

    //---------------------
    // UTILS
    // --------------------

//    private void checkSingletonContent() {
//        Log.d(TAG, "checkSingletonContent: " + PlaceDetailListFromSingleton.size());
//
//    }


    // -------------------------------------------------------------------------------------------//
    //                                      UI                                                    //
    // -------------------------------------------------------------------------------------------//

    public void updateUI() {
        this.updateUIWhenStopingHTTPRequest(mSwipeRefreshLayout, mProgressBar);
        this.mRestaurantItemsList.clear();
        mRestaurantItemsList.addAll(PlaceDetailListFromSingleton);
        mAdapter.notifyDataSetChanged();

    }


}
