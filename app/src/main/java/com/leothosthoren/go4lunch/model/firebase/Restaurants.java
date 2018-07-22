package com.leothosthoren.go4lunch.model.firebase;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class Restaurants {

    private Date dateChoice;
    private Users restaurantUsers;
    private String restaurantName;
    private String restaurantPlaceId;
    private String restaurantFoodType;
    private boolean restaurantLike;
    private boolean restaurantSelection;


    public Restaurants(Date dateChoice, Users restaurantUsers, String restaurantName,
                       String restaurantPlaceId, String restaurantFoodType,
                       boolean restaurantLike, boolean restaurantSelection) {
        this.dateChoice = dateChoice;
        this.restaurantUsers = restaurantUsers;
        this.restaurantName = restaurantName;
        this.restaurantPlaceId = restaurantPlaceId;
        this.restaurantFoodType = restaurantFoodType;
        this.restaurantLike = restaurantLike;
        this.restaurantSelection = restaurantSelection;
    }

    public Restaurants() {
    }

    public Users getRestaurantUsers() {
        return restaurantUsers;
    }

    public void setRestaurantUsers(Users restaurantUsers) {
        this.restaurantUsers = restaurantUsers;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public String getRestaurantPlaceId() {
        return restaurantPlaceId;
    }

    public void setRestaurantPlaceId(String restaurantPlaceId) {
        this.restaurantPlaceId = restaurantPlaceId;
    }

    public String getRestaurantFoodType() {
        return restaurantFoodType;
    }

    public void setRestaurantFoodType(String restaurantFoodType) {
        this.restaurantFoodType = restaurantFoodType;
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
