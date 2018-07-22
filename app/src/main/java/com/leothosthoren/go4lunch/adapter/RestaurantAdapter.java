package com.leothosthoren.go4lunch.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.RequestManager;
import com.leothosthoren.go4lunch.R;
import com.leothosthoren.go4lunch.model.RestaurantItem;
import com.leothosthoren.go4lunch.view.RestaurantViewHolder;

import java.util.ArrayList;

public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantViewHolder> {


    // FOR COMMUNICATION
//    private final Listener callback;
    // FOR DATA
    private ArrayList<RestaurantItem> mRestaurantItems;
    private RequestManager glide;

    public RestaurantAdapter(ArrayList<RestaurantItem> restaurantItems, RequestManager glide/*, Listener callback*/) {
        this.mRestaurantItems = restaurantItems;
        this.glide = glide;
//        this.callback = callback;
    }

    @NonNull
    @Override
    public RestaurantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_view_item_restaurants, parent, false);

        return new RestaurantViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantViewHolder holder, int position) {
        //RESTAURANT
        holder.updateRestaurantList(this.mRestaurantItems.get(position), this.glide);
    }

    @Override
    public int getItemCount() {
        return this.mRestaurantItems.size();
    }

    public interface Listener {
        void onClickItemButton(int position);
    }

    //Handle click
    public RestaurantItem getRestaurantItem(int position){
        return this.mRestaurantItems.get(position);
    }
}
