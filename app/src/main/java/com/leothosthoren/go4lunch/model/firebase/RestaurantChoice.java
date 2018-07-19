package com.leothosthoren.go4lunch.model.firebase;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class RestaurantChoice {

    private Date dateChoice;
    private boolean likeRestaurant;
    private boolean restaurantSelection;


    public RestaurantChoice(Date dateChoice, boolean likeRestaurant, boolean restaurantSelection) {
        this.dateChoice = dateChoice;
        this.likeRestaurant = likeRestaurant;
        this.restaurantSelection = restaurantSelection;
    }

    public RestaurantChoice() {
    }

    public RestaurantChoice(boolean likeRestaurant, boolean restaurantSelection) {

        this.likeRestaurant = likeRestaurant;
        this.restaurantSelection = restaurantSelection;
    }

    @ServerTimestamp
    public Date getDateChoice() {
        return dateChoice;
    }

    public void setDateChoice(Date dateChoice) {
        this.dateChoice = dateChoice;
    }

    public boolean isLikeRestaurant() {
        return likeRestaurant;
    }

    public void setLikeRestaurant(boolean likeRestaurant) {
        this.likeRestaurant = likeRestaurant;
    }

    public boolean isRestaurantSelection() {
        return restaurantSelection;
    }

    public void setRestaurantSelection(boolean restaurantSelection) {
        this.restaurantSelection = restaurantSelection;
    }
}
