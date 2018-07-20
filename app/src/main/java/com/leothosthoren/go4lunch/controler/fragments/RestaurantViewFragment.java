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
import com.leothosthoren.go4lunch.adapter.RestaurantRVAdapter;
import com.leothosthoren.go4lunch.base.BaseFragment;
import com.leothosthoren.go4lunch.utils.RecyclerViewBuilder;
import com.leothosthoren.go4lunch.data.DataSingleton;
import com.leothosthoren.go4lunch.model.RestaurantItem;
import com.leothosthoren.go4lunch.model.detail.PlaceDetail;
import com.leothosthoren.go4lunch.model.nearbysearch.Result;
import com.leothosthoren.go4lunch.utils.ItemClickSupport;
import com.leothosthoren.go4lunch.api.PlaceStreams;

import java.util.ArrayList;

import butterknife.BindView;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;

/**
 * A simple {@link Fragment} subclass.
 */
public class RestaurantViewFragment extends BaseFragment implements RecyclerViewBuilder {

    private static final String TAG = RestaurantViewFragment.class.getSimpleName();
    //VIEW
    @BindView(R.id.recycler_view_id)
    RecyclerView mRecyclerView;
    @BindView(R.id.swipe_container)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.progress_bar)
    ProgressBar mProgressBar;
    //VAR
    private RestaurantRVAdapter mAdapter;
    private ArrayList<RestaurantItem> mRestaurantItemsList;
    private Disposable disposable;
    //DATA
    private ArrayList<Result> NearbySearchListFromSingleton =
            (ArrayList<Result>) DataSingleton.getInstance().getNearbySearch();
    public static ArrayList<PlaceDetail> mPlaceDetailArrayList = new ArrayList<>();

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
//        this.feedMyArrayListWithMyObservable();
    }

    @Override
    protected void updateDesign() {
        this.updateUI();
        //TEST
        this.checkSingletonContent();
        Log.d(TAG, "test: " + mPlaceDetailArrayList.size());

    }

    // -------------------------------------------------------------------------------------------//
    //                                      CONFIGURATION                                         //
    // -------------------------------------------------------------------------------------------//

    @Override
    public void configureRecyclerView() {
        this.mRestaurantItemsList = new ArrayList<>();
        this.mAdapter = new RestaurantRVAdapter(this.mRestaurantItemsList, Glide.with(this));
        this.mRecyclerView.setAdapter(this.mAdapter);
        this.mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    /*
     * @method configureSwipeRefreshLayout
     *
     * When the screen is swipe, the http request is executed
     * */
    @Override
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
                    RestaurantItem restaurantItem = mAdapter.getRestaurantItem(position);
                    Toast.makeText(getContext(), "CLICK on position: " + position + " name: " + restaurantItem.getName(), Toast.LENGTH_SHORT).show();
                });
    }

    // -------------------------------------------------------------------------------------------//
    //                                      UI                                                    //
    // -------------------------------------------------------------------------------------------//

    @Override
    public void updateUI(/*ArrayList<RestaurantItem> restaurantItems*/) {
        this.updateUIWhenStopingHTTPRequest(mSwipeRefreshLayout, mProgressBar);

        mRestaurantItemsList.add(new RestaurantItem("Le Chalutier", "Française", "15 rue des Ardeches",
                "Open", 120, 2,
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRTb97dCdvbN7JMYo3vYzIa5ib7ihCLzzr9wORiAmPWfemek1Qv", 3));
        mRestaurantItemsList.add(new RestaurantItem("Pizza Roberto", "Italienne", "1 rue des Gentilly",
                "Open at 1pm", 70, 5,
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSyGhyR__TMnS93qBoyR6cKddzWjjpRWfFfVaEpgDD7BjIkiRBh", 1.5));
        mRestaurantItemsList.add(new RestaurantItem("Kebab Futur", "Turc", "70 rue des boulets",
                "Closed", 12, 0,
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSI-zPkYqtvaQxd5nCaWct9CmlKq9w1HZNiCZkLU1ixl-UuQIiW", 2));
//        mRestaurantItemsList.addAll(restaurantItems);
        mAdapter.notifyDataSetChanged();
    }

    //---------------------
    // TEST
    // --------------------

    private void checkSingletonContent() {
        Log.d(TAG, "checkSingletonContent: " + NearbySearchListFromSingleton.size());

    }

    public void executeHttpRequestWithPlaceDetail(String placeID) {
        disposable = PlaceStreams.streamFetchPlaceDetail(placeID)
                .subscribeWith(new DisposableObserver<PlaceDetail>() {
                    @Override
                    public void onNext(PlaceDetail placeDetail) {
                        Log.d(TAG, "onNext: " + placeDetail.getResult().getName());


                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError: " + e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete: " + mPlaceDetailArrayList.size());
                        Log.d(TAG, "feedMyArrayListWithMyObservable: " + mPlaceDetailArrayList.size());
                    }
                });
    }

    public void feedMyArrayListWithMyObservable() {
        for (int i = 0; i < NearbySearchListFromSingleton.size(); i++) {
//            executeHttpRequestWithPlaceDetail(NearbySearchListFromSingleton.get(i).getPlaceId());
        }

    }

}
