package com.leothosthoren.go4lunch.controler.fragments;


import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.leothosthoren.go4lunch.R;
import com.leothosthoren.go4lunch.adapter.RestaurantAdapter;
import com.leothosthoren.go4lunch.api.PlaceStreams;
import com.leothosthoren.go4lunch.base.BaseFragment;
import com.leothosthoren.go4lunch.data.DataSingleton;
import com.leothosthoren.go4lunch.model.detail.PlaceDetail;
import com.leothosthoren.go4lunch.model.nearbysearch.Result;
import com.leothosthoren.go4lunch.utils.ItemClickSupport;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;

/**
 * A simple {@link Fragment} subclass.
 */
public class RestaurantViewFragment extends BaseFragment {

    private static final String TAG = RestaurantViewFragment.class.getSimpleName();
    //VIEW
    @BindView(R.id.recycler_view_id)
    RecyclerView mRecyclerView;
    @BindView(R.id.swipe_container)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.progress_bar)
    ProgressBar mProgressBar;
    //VAR
    private RestaurantAdapter mAdapter;
    private ArrayList<PlaceDetail> mRestaurantItemsList;
    private Disposable disposable;
    //DATA
    private ArrayList<Result> NearbySearchListFromSingleton =
            (ArrayList<Result>) DataSingleton.getInstance().getNearbySearch();


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
        this.configureRecyclerView();
        this.configureSwipeRefreshLayout();
        this.progressBarHandler(mProgressBar, getContext());
        this.configureOnclickRecyclerView();

    }

    @Override
    protected void updateDesign() {
        //TEST
        this.checkSingletonContent();
        //HTTP
//        this.executeHttpRequestWithPlaceDetail();
        this.feedMyArrayListWithMyObservable();

    }

    // -------------------------------------------------------------------------------------------//
    //                                      CONFIGURATION                                         //
    // -------------------------------------------------------------------------------------------//


    public void configureRecyclerView() {
        this.mRestaurantItemsList = new ArrayList<>();
        this.mAdapter = new RestaurantAdapter(this.mRestaurantItemsList, Glide.with(this));
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
                });
    }

    //---------------------
    // TEST
    // --------------------

    private void checkSingletonContent() {
        Log.d(TAG, "checkSingletonContent: " + NearbySearchListFromSingleton.size());

    }

    public void executeHttpRequestWithPlaceDetail(String placeID) {
        disposable = PlaceStreams.streamFetchListPlaceDetail(placeID)
                .subscribeWith(new DisposableObserver<List<PlaceDetail>>() {
                    @Override
                    public void onNext(List<PlaceDetail> placeDetail) {
                        Log.d(TAG, "onNext: ");
                        updateUI(placeDetail);

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError: " + e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete: " + mRestaurantItemsList.size());

                    }
                });
    }

    // -------------------------------------------------------------------------------------------//
    //                                      UI                                                    //
    // -------------------------------------------------------------------------------------------//

    public void updateUI(List<PlaceDetail> restaurantItems) {
        this.updateUIWhenStopingHTTPRequest(mSwipeRefreshLayout, mProgressBar);
        mRestaurantItemsList.addAll(restaurantItems);
        mAdapter.notifyDataSetChanged();
//        mRestaurantItemsList.add(new RestaurantItem("Le Chalutier", "Française", "15 rue des Ardeches",
//                "Open", 120, 2,
//                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRTb97dCdvbN7JMYo3vYzIa5ib7ihCLzzr9wORiAmPWfemek1Qv", 3));
//        mRestaurantItemsList.add(new RestaurantItem("Pizza Roberto", "Italienne", "1 rue des Gentilly",
//                "Open at 1pm", 70, 5,
//                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSyGhyR__TMnS93qBoyR6cKddzWjjpRWfFfVaEpgDD7BjIkiRBh", 1.5));
//        mRestaurantItemsList.add(new RestaurantItem("Kebab Futur", "Turc", "70 rue des boulets",
//                "Closed", 12, 0,
//                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSI-zPkYqtvaQxd5nCaWct9CmlKq9w1HZNiCZkLU1ixl-UuQIiW", 2));

    }

    public void feedMyArrayListWithMyObservable() {
        for (int i = 0; i < NearbySearchListFromSingleton.size(); i++) {
//            executeHttpRequestWithPlaceDetail(NearbySearchListFromSingleton.get(i).getPlaceId());
            //HTTP
            this.executeHttpRequestWithPlaceDetail(NearbySearchListFromSingleton.get(i).getPlaceId());
        }

    }

}
