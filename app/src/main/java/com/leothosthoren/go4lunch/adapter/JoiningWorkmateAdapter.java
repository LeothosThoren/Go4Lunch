package com.leothosthoren.go4lunch.adapter;


import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.bumptech.glide.RequestManager;
import com.leothosthoren.go4lunch.R;
import com.leothosthoren.go4lunch.model.firebase.Restaurants;
import com.leothosthoren.go4lunch.view.JoiningWorkmateViewHolder;

import java.util.List;


public class JoiningWorkmateAdapter extends RecyclerView.Adapter<JoiningWorkmateViewHolder> {
    //FOR DATA
    private final RequestManager glide;
    private final String idCurrentUser;
    private final List<Restaurants> restaurantsList;

    public JoiningWorkmateAdapter(List<Restaurants> restaurants,
                                  RequestManager glide, String idCurrentUser) {
        this.restaurantsList = restaurants;
        this.glide = glide;
        this.idCurrentUser = idCurrentUser;
    }

    @NonNull
    @Override
    public JoiningWorkmateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new JoiningWorkmateViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view_item_workmates, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull JoiningWorkmateViewHolder holder, int position) {
        holder.updateWithJoiningWorkmateItem(this.restaurantsList.get(position), this.idCurrentUser, this.glide);
    }

    @Override
    public int getItemCount() {
        return this.restaurantsList.size();
    }

}
