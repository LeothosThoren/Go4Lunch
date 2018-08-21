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
import com.google.firebase.firestore.DocumentSnapshot;
import com.leothosthoren.go4lunch.R;
import com.leothosthoren.go4lunch.adapter.RestaurantAdapter;
import com.leothosthoren.go4lunch.api.RestaurantHelper;
import com.leothosthoren.go4lunch.base.BaseFragment;
import com.leothosthoren.go4lunch.controler.activities.RestaurantInfoActivity;
import com.leothosthoren.go4lunch.data.DataSingleton;
import com.leothosthoren.go4lunch.model.detail.PlaceDetail;
import com.leothosthoren.go4lunch.model.firebase.Restaurants;
import com.leothosthoren.go4lunch.utils.ItemClickSupport;

import java.util.ArrayList;
import java.util.List;

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
    private List<PlaceDetail> PlaceDetailListFromSingleton = DataSingleton.getInstance().getPlaceDetailList();
    private List<Restaurants> mRestaurantsFromFireStore = new ArrayList<>();

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
        this.getAllRestaurantSelected();

//        this.configureSwipeRefreshLayout();
//        this.progressBarHandler(mProgressBar, getContext());
        // Handle the case whether the list is empty
        if (PlaceDetailListFromSingleton == null || PlaceDetailListFromSingleton.isEmpty()) {
            mTextViewRecyclerViewEmpty.setVisibility(View.VISIBLE);
        } else {
            this.configureRecyclerView();
            this.configureOnclickRecyclerView();
        }
    }

    @Override
    protected void updateDesign() {
        this.updateUI();
    }


    // -------------------------------------------------------------------------------------------//
    //                                      CONFIGURATION                                         //
    // -------------------------------------------------------------------------------------------//


    public void configureRecyclerView() {
        this.mAdapter = new RestaurantAdapter(PlaceDetailListFromSingleton, Glide.with(this)
                , this, mRestaurantsFromFireStore);
        this.mRecyclerView.setAdapter(this.mAdapter);
        this.mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    /**
     * @method configureSwipeRefreshLayout
     * <p>
     * When the screen is swipe, the http request is executed
     */

//    public void configureSwipeRefreshLayout() {
//        this.mSwipeRefreshLayout.setOnRefreshListener(() -> {
//            updateUIWhenStartingHTTPRequest(mProgressBar);
//            updateUI();
//        });
//    }


    // -------------------------------------------------------------------------------------------//
    //                                    HTTP REQUEST                                            //
    // -------------------------------------------------------------------------------------------//


    private void getAllRestaurantSelected() {
        RestaurantHelper.getAllRestaurants().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (DocumentSnapshot documentSnapshot : task.getResult()) {
                    Restaurants restaurants = documentSnapshot.toObject(Restaurants.class);
                    assert restaurants != null;
                    mRestaurantsFromFireStore.add(restaurants);
                    Log.i(TAG, "getAllRestaurantSelected: "+ restaurants.getPlaceDetail().getResult().getName());
                }
                Log.d(TAG, "getAllRestaurantSelected: " + mRestaurantsFromFireStore.size());
            } else {
                Log.d(TAG, "Error getting documents: ", task.getException());
            }
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

                    // launch activity with data from singleton
                    DataSingleton.getInstance().setPlaceDetail(restaurantItem);
                    startActivity(RestaurantInfoActivity.class);

                });
    }

    @Override
    public void onClickItemButton(int position) {
        PlaceDetail restaurantItem = mAdapter.getRestaurantItem(position);
        if (restaurantItem.getResult().getPhotos() != null) {
            Toast.makeText(getContext(), "CLICK PICTURE on position: " + position + " reference: " +
                    restaurantItem.getResult().getPhotos().get(0).getPhotoReference(), Toast.LENGTH_SHORT).show();
        }

    }


    // -------------------------------------------------------------------------------------------//
    //                                      UI                                                    //
    // -------------------------------------------------------------------------------------------//


    public void updateUI() {
//        this.updateUIWhenStopingHTTPRequest(mSwipeRefreshLayout, mProgressBar);
//        this.mRestaurantItemsList.clear();
        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }

    }


}
