package com.leothosthoren.go4lunch.controler.fragments;


import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;
import com.leothosthoren.go4lunch.R;
import com.leothosthoren.go4lunch.adapter.WorkmateAdapter;
import com.leothosthoren.go4lunch.api.RestaurantHelper;
import com.leothosthoren.go4lunch.api.UserHelper;
import com.leothosthoren.go4lunch.base.BaseFragment;
import com.leothosthoren.go4lunch.model.firebase.Restaurants;
import com.leothosthoren.go4lunch.model.firebase.Users;

import java.util.Objects;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 */
public class WorkMatesViewFragment extends BaseFragment implements WorkmateAdapter.Listener {
    // WIDGETS
    @BindView(R.id.recycler_view_id)
    RecyclerView mRecyclerView;
    @BindView(R.id.workmates_availability_message)
    TextView mTextViewRecyclerViewEmpty;
    private WorkmateAdapter mWorkmateAdapter;

    @Nullable
    private Users modelCurrentUser;

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
//        this.getRestaurantWhereUsersAreEating();
        this.getCurrentUserFromFirestore();
        this.configureRecyclerView(null);
    }

    @Override
    protected void updateDesign() {

    }


    // --------------------
    // REST REQUESTS
    // --------------------


    // Get Current User from Firestore
    private void getCurrentUserFromFirestore() {
        UserHelper.getUser(Objects.requireNonNull(getCurrentUser()).getUid())
                .addOnSuccessListener(documentSnapshot ->
                        modelCurrentUser = documentSnapshot.toObject(Users.class));
    }

    // Create options for RecyclerView from a Query
    private FirestoreRecyclerOptions<Users> generateOptionsForAdapter(Query query) {
        return new FirestoreRecyclerOptions.Builder<Users>()
                .setQuery(query, Users.class)
                .setLifecycleOwner(this)
                .build();
    }

//    // Get all restaurant where users are eating
//    private void getRestaurantWhereUsersAreEating() {
//        RestaurantHelper.getRestaurantsFromDatabase()
//                .addOnCompleteListener(task -> {
////                    if (task.isSuccessful()) {
////                        for (QueryDocumentSnapshot document : task.getResult()) {
////                            Log.d(TAG, "getRestaurantWhereUsersAreEating:" + document.getData());
////                            Restaurants restaurants = document.toObject(Restaurants.class);
////                            this.configureRecyclerView(restaurants);
////
////                        }
////                    } else {
////                        Log.e(TAG, "Error getting documents: " + task.getException());
////                    }
//                });
//    }

    // --------------------
    // UI
    // --------------------


    private void configureRecyclerView(Restaurants restaurant) {
        if (modelCurrentUser != null) {
            this.mWorkmateAdapter = new WorkmateAdapter(generateOptionsForAdapter(UserHelper.getAllUsers()),
                    Glide.with(Objects.requireNonNull(this)),
                    this,
                    this.modelCurrentUser.getUid(),
                    restaurant);

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


    // --------------------
    // CALLBACK
    // --------------------


    @Override
    public void onDataChanged() {
        // Show TextView in case RecyclerView is empty
        mTextViewRecyclerViewEmpty.setVisibility(this.mWorkmateAdapter.getItemCount() == 0 ? View.VISIBLE : View.GONE);
    }
}
