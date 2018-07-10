package com.leothosthoren.go4lunch.model;

public class RestaurantItem {

    private String name;
    private String foodType;
    private String address;
    private String openingInfo;
    private String distance;
    private String workmateQuantity;
    private String url;
    private float rating;


    public RestaurantItem(String name, String foodType, String address, String openingInfo,
                          String distance, String workmateQuantity, String url, float rating) {
        this.name = name;
        this.foodType = foodType;
        this.address = address;
        this.openingInfo = openingInfo;
        this.distance = distance;
        this.workmateQuantity = workmateQuantity;
        this.url = url;
        this.rating = rating;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFoodType() {
        return foodType;
    }

    public void setFoodType(String foodType) {
        this.foodType = foodType;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getOpeningInfo() {
        return openingInfo;
    }

    public void setOpeningInfo(String openingInfo) {
        this.openingInfo = openingInfo;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getWorkmateQuantity() {
        return workmateQuantity;
    }

    public void setWorkmateQuantity(String workmateQuantity) {
        this.workmateQuantity = workmateQuantity;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }
}
