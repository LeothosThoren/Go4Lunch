package com.leothosthoren.go4lunch.model.firebase;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class RestaurantChoice {

    private Date dateChoice;
    private Users restaurantUsers;
    private String restaurantName;
    private String restaurantPlaceId;
    private String restaurantFoodType;
    private boolean restaurantLike;
    private boolean restaurantSelection;


    public RestaurantChoice(Date dateChoice, boolean restaurantLike, boolean restaurantSelection) {
        this.dateChoice = dateChoice;
        this.restaurantLike = restaurantLike;
        this.restaurantSelection = restaurantSelection;
    }

    public RestaurantChoice() {
    }

    public RestaurantChoice(boolean restaurantLike, boolean restaurantSelection) {

        this.restaurantLike = restaurantLike;
        this.restaurantSelection = restaurantSelection;
    }

    @ServerTimestamp
    public Date getDateChoice() {
        return dateChoice;
    }

    public void setDateChoice(Date dateChoice) {
        this.dateChoice = dateChoice;
    }

    public boolean isRestaurantLike() {
        return restaurantLike;
    }

    public void setRestaurantLike(boolean restaurantLike) {
        this.restaurantLike = restaurantLike;
    }

    public boolean isRestaurantSelection() {
        return restaurantSelection;
    }

    public void setRestaurantSelection(boolean restaurantSelection) {
        this.restaurantSelection = restaurantSelection;
    }
}
