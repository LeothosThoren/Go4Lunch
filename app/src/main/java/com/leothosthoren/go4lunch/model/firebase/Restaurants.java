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
    private Boolean latitude;
    private Boolean longitude;

    private Users workmate;


    public Restaurants(Date dateChoice, Users restaurantUsers, String restaurantName,
                       String restaurantPlaceId, boolean restaurantLike, boolean restaurantSelection,
                       Boolean latitude, Boolean longitude, Users workmate) {
        this.dateChoice = dateChoice;
        this.restaurantUsers = restaurantUsers;
        this.restaurantName = restaurantName;
        this.restaurantPlaceId = restaurantPlaceId;
        this.restaurantLike = restaurantLike;
        this.restaurantSelection = restaurantSelection;
        this.latitude = latitude;
        this.longitude = longitude;
        this.workmate = workmate;
    }

    @ServerTimestamp
    public Date getDateChoice() {
        return dateChoice;
    }

    public void setDateChoice(Date dateChoice) {
        this.dateChoice = dateChoice;
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

    public Boolean getLatitude() {
        return latitude;
    }

    public void setLatitude(Boolean latitude) {
        this.latitude = latitude;
    }

    public Boolean getLongitude() {
        return longitude;
    }

    public void setLongitude(Boolean longitude) {
        this.longitude = longitude;
    }

    public Users getWorkmate() {
        return workmate;
    }

    public void setWorkmate(Users workmate) {
        this.workmate = workmate;
    }
}
