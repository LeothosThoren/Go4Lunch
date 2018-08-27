package com.leothosthoren.go4lunch.controler.fragments;


import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;
import com.leothosthoren.go4lunch.R;
import com.leothosthoren.go4lunch.adapter.WorkmateAdapter;
import com.leothosthoren.go4lunch.api.PlaceStreams;
import com.leothosthoren.go4lunch.api.UserHelper;
import com.leothosthoren.go4lunch.base.BaseFragment;
import com.leothosthoren.go4lunch.controler.activities.RestaurantInfoActivity;
import com.leothosthoren.go4lunch.data.DataSingleton;
import com.leothosthoren.go4lunch.model.detail.PlaceDetail;
import com.leothosthoren.go4lunch.model.firebase.Users;
import com.leothosthoren.go4lunch.utils.ItemClickSupport;

import java.util.Objects;

import butterknife.BindView;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;

/**
 * A simple {@link Fragment} subclass.
 */
public class WorkMatesViewFragment extends BaseFragment implements WorkmateAdapter.Listener {
    // WIDGET
    @BindView(R.id.recycler_view_id)
    RecyclerView mRecyclerView;
    @BindView(R.id.workmates_availability_message)
    TextView mTextViewRecyclerViewEmpty;
    // VAR
    private WorkmateAdapter mWorkmateAdapter;
    private Users modelCurrentUser;
    private Disposable mDisposable;

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
        this.configureRecyclerView();
        this.configureRecyclerViewClick();
    }

    @Override
    protected void updateDesign() {

    }


    // --------------------
    // REST REQUESTS
    // --------------------


    // Create options for RecyclerView from a Query
    private FirestoreRecyclerOptions<Users> generateOptionsForAdapter(Query query) {
        return new FirestoreRecyclerOptions.Builder<Users>()
                .setQuery(query, Users.class)
                .setLifecycleOwner(this)
                .build();
    }

    //------------------------
    // HTTP RxJava
    //------------------------

    public void executeHttpRequestWithPlaceDetail(String placeID) {
        mDisposable = PlaceStreams.streamFetchPlaceDetail(placeID)
                .subscribeWith(new DisposableObserver<PlaceDetail>() {
                    @Override
                    public void onNext(PlaceDetail placeDetail) {
                        Log.d(TAG, "onNext: " + placeDetail.getResult().getName());
                        DataSingleton.getInstance().setPlaceDetail(placeDetail);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError: " + e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete: " + placeID);
                        startActivity(RestaurantInfoActivity.class);
                    }
                });
    }

    // Dispose subscription
    private void disposeWhenDestroy() {
        if (this.mDisposable != null && !this.mDisposable.isDisposed())
            this.mDisposable.dispose();
    }

    // Called for better performances
    @Override
    public void onDestroy() {
        super.onDestroy();
        this.disposeWhenDestroy();
    }


    // --------------------
    // UI
    // --------------------


    private void configureRecyclerView() {
        if (getCurrentUser() != null) {
            this.mWorkmateAdapter =
                    new WorkmateAdapter(generateOptionsForAdapter(UserHelper.getAllUsers()),
                            Glide.with(Objects.requireNonNull(this)),
                            this);

            mWorkmateAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
                @Override
                public void onItemRangeInserted(int positionStart, int itemCount) {
                    mRecyclerView.smoothScrollToPosition(mWorkmateAdapter.getItemCount());
                }

            });
        }
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(this.mWorkmateAdapter);
    }


    // -------------------------------------------------------------------------------------------//
    //                                      ACTION                                                //
    // -------------------------------------------------------------------------------------------//

    private void configureRecyclerViewClick() {
        ItemClickSupport.addTo(mRecyclerView, R.id.item_restaurant_layout)
                .setOnItemClickListener((recyclerView, position, v) -> {
                    Users workmateItem = mWorkmateAdapter.getItem(position);
                    if (workmateItem.getWorkmateSelection() != null) {
                        this.executeHttpRequestWithPlaceDetail(workmateItem.getWorkmateSelection().getRestaurantId());
                    }
                });
    }

    // --------------------
    // CALLBACK
    // --------------------


    @Override
    public void onDataChanged() {
        // Show TextView in case RecyclerView is empty
        mTextViewRecyclerViewEmpty.setVisibility(this.mWorkmateAdapter.getItemCount() == 0 ? View.VISIBLE : View.GONE);
    }
}
