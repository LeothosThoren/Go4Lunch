package com.leothosthoren.go4lunch.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.RequestManager;
import com.leothosthoren.go4lunch.R;
import com.leothosthoren.go4lunch.model.detail.PlaceDetail;
import com.leothosthoren.go4lunch.model.firebase.Restaurants;
import com.leothosthoren.go4lunch.view.RestaurantViewHolder;

import java.util.List;

public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantViewHolder> {


    // FOR COMMUNICATION
    private final Listener callback;
    // FOR DATA
    private List<PlaceDetail> mRestaurantItems;
    private RequestManager glide;
    private List<Restaurants> mRestaurantsListFromFirestore;


    public RestaurantAdapter(List<PlaceDetail> restaurantItems, RequestManager glide, Listener callback, List<Restaurants> restaurantsListFromFirestore) {
        this.mRestaurantItems = restaurantItems;
        this.glide = glide;
        this.callback = callback;
        this.mRestaurantsListFromFirestore = restaurantsListFromFirestore;
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
        int mNbOfWorkmate = 0;
        for (int i = 0; i < mRestaurantsListFromFirestore.size(); i++) {
            if (mRestaurantItems.get(position).getResult().getPlaceId()
                    .equals(mRestaurantsListFromFirestore.get(i).getPlaceDetail().getResult().getPlaceId())) {
                //add 1 when counting workmate
                mNbOfWorkmate++;
            }
        }
        holder.updateRestaurantListView(mRestaurantItems.get(position), this.glide, this.callback, mNbOfWorkmate);
    }

    @Override
    public int getItemCount() {
        return this.mRestaurantItems.size();
    }

    //Handle click on recycler view list
    public PlaceDetail getRestaurantItem(int position) {
        return this.mRestaurantItems.get(position);
    }

    // Implementation of the interface
    public interface Listener {
        void onClickItemButton(int position);
    }
}
