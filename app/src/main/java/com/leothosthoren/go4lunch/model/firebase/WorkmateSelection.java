package com.leothosthoren.go4lunch.model.firebase;

public class WorkmateSelection {

    private String restaurantName;
    private String restaurantId;

    public WorkmateSelection() {
    }

    public WorkmateSelection(String restaurantName, String restaurantId) {
        this.restaurantName = restaurantName;
        this.restaurantId = restaurantId;
    }

    //--- GETTER---


    public String getRestaurantName() {
        return restaurantName;
    }

    public String getRestaurantId() {
        return restaurantId;
    }


    //--- SETTER---


    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }
}
