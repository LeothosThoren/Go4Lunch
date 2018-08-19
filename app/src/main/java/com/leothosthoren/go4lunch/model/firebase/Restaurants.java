package com.leothosthoren.go4lunch.model.firebase;

import android.support.annotation.Nullable;

import com.google.firebase.firestore.IgnoreExtraProperties;
import com.google.firebase.firestore.ServerTimestamp;
import com.leothosthoren.go4lunch.model.detail.PlaceDetail;

import java.util.Date;
import java.util.HashMap;

@IgnoreExtraProperties
public class Restaurants {

    private Date dateChoice;
    @Nullable
    private HashMap<String, Boolean> restaurantLike;
    private boolean restaurantSelection;
    private PlaceDetail placeDetail;
    private Users workmate;

    public Restaurants() {
    }

    public Restaurants(Date dateChoice, boolean restaurantSelection, PlaceDetail placeDetail, Users workmate) {
        this.dateChoice = dateChoice;
        this.restaurantSelection = restaurantSelection;
        this.placeDetail = placeDetail;
        this.workmate = workmate;
    }

    public Restaurants(Date dateChoice, @Nullable HashMap<String, Boolean> restaurantLike,
                       boolean restaurantSelection, PlaceDetail placeDetail, Users workmate) {
        this.dateChoice = dateChoice;
        this.restaurantLike = restaurantLike;
        this.restaurantSelection = restaurantSelection;
        this.placeDetail = placeDetail;
        this.workmate = workmate;
    }

    // GETTERS

    @ServerTimestamp
    public Date getDateChoice() {
        return dateChoice;
    }

    @Nullable
    public HashMap<String, Boolean> getRestaurantLike() {
        return restaurantLike;
    }

    public PlaceDetail getPlaceDetail() {
        return placeDetail;
    }

    public boolean getRestaurantSelection() {
        return restaurantSelection;
    }

    public Users getWorkmate() {
        return workmate;
    }

    // SETTERS

    public void setRestaurantSelection(boolean restaurantSelection) {
        this.restaurantSelection = restaurantSelection;
    }

    public void setRestaurantLike(@Nullable HashMap<String, Boolean> restaurantLike) {
        this.restaurantLike = restaurantLike;
    }

    public void setDateChoice(Date dateChoice) {
        this.dateChoice = dateChoice;
    }

    public void setPlaceDetail(PlaceDetail placeDetail) {
        this.placeDetail = placeDetail;
    }

    public void setWorkmate(Users workmate) {
        this.workmate = workmate;
    }
}
