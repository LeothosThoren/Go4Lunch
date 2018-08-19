package com.leothosthoren.go4lunch.adapter;


import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.bumptech.glide.RequestManager;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.leothosthoren.go4lunch.R;
import com.leothosthoren.go4lunch.model.firebase.Restaurants;
import com.leothosthoren.go4lunch.view.JoiningWorkmateViewHolder;


public class JoiningWorkmateAdapter extends FirestoreRecyclerAdapter<Restaurants, JoiningWorkmateViewHolder> {
    //FOR DATA
    private final RequestManager glide;
//    private final String idCurrentUser;

    public JoiningWorkmateAdapter(@NonNull FirestoreRecyclerOptions<Restaurants> restaurantsOptions,
                                  RequestManager glide/*, String idCurrentUser*/) {
        super(restaurantsOptions);
        this.glide = glide;
//        this.idCurrentUser = idCurrentUser;
    }

    @NonNull
    @Override
    public JoiningWorkmateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new JoiningWorkmateViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view_item_workmates, parent, false));
    }


    @Override
    protected void onBindViewHolder(@NonNull JoiningWorkmateViewHolder holder, int position, @NonNull Restaurants model) {
//        if (!model.getWorkmate().getUid().equals(idCurrentUser)) {
        holder.updateWithJoiningWorkmateItem(model, /*this.idCurrentUser,*/ this.glide);
//        }
    }
}
