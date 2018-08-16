package com.leothosthoren.go4lunch.model.firebase;

public class RestaurantsLike {

    private String placeId;
    private Boolean isStarChecked;
    private String restaurantName;

    public RestaurantsLike() {
    }

    public RestaurantsLike(String restaurantName, String placeId, Boolean isStarChecked) {
        this.placeId = placeId;
        this.isStarChecked = isStarChecked;
        this.restaurantName = restaurantName;

    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public Boolean getStarChecked() {
        return isStarChecked;
    }

    public void setStarChecked(Boolean starChecked) {
        isStarChecked = starChecked;
    }
}
