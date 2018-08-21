package com.leothosthoren.go4lunch.controler.fragments;


import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.leothosthoren.go4lunch.R;
import com.leothosthoren.go4lunch.adapter.WorkmateAdapter;
import com.leothosthoren.go4lunch.api.RestaurantHelper;
import com.leothosthoren.go4lunch.api.UserHelper;
import com.leothosthoren.go4lunch.base.BaseFragment;
import com.leothosthoren.go4lunch.model.firebase.Restaurants;
import com.leothosthoren.go4lunch.model.firebase.Users;
import com.leothosthoren.go4lunch.model.firebase.WorkmateAndRestaurant;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 */
public class WorkMatesViewFragment extends BaseFragment {
    // WIDGETS
    @BindView(R.id.recycler_view_id)
    RecyclerView mRecyclerView;
    @BindView(R.id.workmates_availability_message)
    TextView mTextViewRecyclerViewEmpty;
    // VAR
    @Nullable
    private Users modelCurrentUser;
    private WorkmateAdapter mWorkmateAdapter;
    private List<WorkmateAndRestaurant> mWorkmateAndRestaurantList = new ArrayList<>();
    //Test
    private List<Restaurants> restaurantsList = new ArrayList<>();
    private List<Users> usersList = new ArrayList<>();


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
        this.getCurrentUserFromFirestore();
        this.getDatasFromFirestore();
//        this.configureRecyclerView();
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

    // Get all elements to display on screen
    private void getDatasFromFirestore() {
        Log.d(TAG, "getDatasFromFirestore: initialisation");
        // User
        UserHelper.getAllWorkmates().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                int i = 0;
                for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                    Users users = documentSnapshot.toObject(Users.class);
                    usersList.add(users);
                    i++;
                    mWorkmateAndRestaurantList.add(new WorkmateAndRestaurant(usersList.get(i), restaurantsList.get(i)));
                }
                // Restaurant
                RestaurantHelper.getAllRestaurants().addOnCompleteListener(taskr -> {
                    if (taskr.isSuccessful()) {
                        int j = 0;
                        for (QueryDocumentSnapshot documentSnapshot : taskr.getResult()) {
                            Restaurants restaurants = documentSnapshot.toObject(Restaurants.class);
                            restaurantsList.add(restaurants);
                            j++;
                            mWorkmateAndRestaurantList.add(new WorkmateAndRestaurant(usersList.get(j), restaurantsList.get(j)));
                        }
                        //Here the recyclerview retrieve data from request above
                        this.configureRecyclerView();
                    } else {
                        Log.d(TAG, "Error getting documents: ", taskr.getException());
                    }
                }).addOnFailureListener(this.onFailureListener(getContext()));
            } else {
                Log.d(TAG, "Error getting documents: ", task.getException());
            }
        }).addOnFailureListener(this.onFailureListener(getContext()));

    }

//    // Create options for RecyclerView from a Query
//    private FirestoreRecyclerOptions<Users> generateOptionsForAdapter(Query query) {
//        return new FirestoreRecyclerOptions.Builder<Users>()
//                .setQuery(query, Users.class)
//                .setLifecycleOwner(this)
//                .build();
//    }
    /**/

    // --------------------
    // UI
    // --------------------


    private void configureRecyclerView() {
        if (modelCurrentUser != null) {
            this.mWorkmateAdapter = new WorkmateAdapter(mWorkmateAndRestaurantList,
                    Glide.with(Objects.requireNonNull(this)),
                    this.modelCurrentUser.getUid());
//            mWorkmateAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
//                @Override
//                public void onItemRangeInserted(int positionStart, int itemCount) {
//                    mRecyclerView.smoothScrollToPosition(mWorkmateAdapter.getItemCount());
//                }
//
//            });
        }
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(this.mWorkmateAdapter);
    }


//    // --------------------
//    // CALLBACK
//    // --------------------
//
//
//    @Override
//    public void onDataChanged() {
//        // Show TextView in case RecyclerView is empty
//        mTextViewRecyclerViewEmpty.setVisibility(this.mWorkmateAdapter.getItemCount() == 0 ? View.VISIBLE : View.GONE);
//    }
}
