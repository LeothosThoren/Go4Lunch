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
import com.leothosthoren.go4lunch.api.UserHelper;
import com.leothosthoren.go4lunch.base.BaseFragment;
import com.leothosthoren.go4lunch.model.firebase.Users;

import java.util.Objects;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 */
public class WorkMatesViewFragment extends BaseFragment implements WorkmateAdapter.Listener {
    //VAR
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
        this.configureRecyclerView();
        this.getCurrentUserFromFirestore();
    }

    @Override
    protected void updateDesign() {

    }

    // --------------------
    // REST REQUESTS
    // --------------------
    // 4 - Get Current User from Firestore
    private void getCurrentUserFromFirestore() {
        UserHelper.getUser(Objects.requireNonNull(getCurrentUser()).getUid())
                .addOnSuccessListener(documentSnapshot ->
                        modelCurrentUser = documentSnapshot.toObject(Users.class));
    }

    // --------------------
    // UI
    // --------------------

    private void configureRecyclerView() {
        this.mWorkmateAdapter = new WorkmateAdapter(generateOptionsForAdapter(UserHelper
                .getAllUsersWorkmates()),
                Glide.with(Objects.requireNonNull(this)),
                this,
                Objects.requireNonNull(this.getCurrentUser()).getUid());

        mWorkmateAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                mRecyclerView.smoothScrollToPosition(mWorkmateAdapter.getItemCount());
            }

        });

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(this.mWorkmateAdapter);
        mWorkmateAdapter.notifyDataSetChanged();

    }

    // 6 - Create options for RecyclerView from a Query
    private FirestoreRecyclerOptions<Users> generateOptionsForAdapter(Query query) {
        return new FirestoreRecyclerOptions.Builder<Users>()
                .setQuery(query, Users.class)
                .setLifecycleOwner(this)
                .build();
    }

    // --------------------
    // CALLBACK
    // --------------------

    @Override
    public void onDataChanged() {
        // 7 - Show TextView in case RecyclerView is empty
        mTextViewRecyclerViewEmpty.setVisibility(this.mWorkmateAdapter.getItemCount() == 0 ? View.VISIBLE : View.GONE);
    }
}
