package com.leothosthoren.go4lunch.model.firebase;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;
import java.util.HashMap;

public class Restaurants {

    private Date dateChoice;
    private HashMap<String, Boolean> restaurantLike;
    private boolean restaurantSelection;
    private String placeId;
    private Users workmate;

    public Restaurants() {
    }

    public Restaurants(Date dateChoice, HashMap<String, Boolean> restaurantLike,
                       boolean restaurantSelection, String placeId, Users workmate) {
        this.dateChoice = dateChoice;
        this.restaurantLike = restaurantLike;
        this.restaurantSelection = restaurantSelection;
        this.placeId = placeId;
        this.workmate = workmate;
    }

    public Restaurants(Date dateChoice, boolean restaurantSelection, String placeId, Users workmate) {
        this.dateChoice = dateChoice;
        this.restaurantSelection = restaurantSelection;
        this.placeId = placeId;
        this.workmate = workmate;
    }

    // GETTERS

    @ServerTimestamp
    public Date getDateChoice() {
        return dateChoice;
    }

    public void setDateChoice(Date dateChoice) {
        this.dateChoice = dateChoice;
    }

    public HashMap<String, Boolean> getRestaurantLike() {
        return restaurantLike;
    }

    public void setRestaurantLike(HashMap<String, Boolean> restaurantLike) {
        this.restaurantLike = restaurantLike;
    }

    public boolean isRestaurantSelection() {
        return restaurantSelection;
    }

    // SETTERS

    public void setRestaurantSelection(boolean restaurantSelection) {
        this.restaurantSelection = restaurantSelection;
    }

    public String getPlaceID() {
        return placeId;
    }

    public Users getWorkmate() {
        return workmate;
    }

    public void setWorkmate(Users workmate) {
        this.workmate = workmate;
    }

    public void setPlaceId(String placeDetail) {
        this.placeId = placeDetail;
    }
}
