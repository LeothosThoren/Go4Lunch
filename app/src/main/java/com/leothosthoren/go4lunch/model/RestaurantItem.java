package com.leothosthoren.go4lunch.model;

public class RestaurantItem {

    private String name;
    private String foodType;
    private String address;
    private String openingInfo;
    private int distance;
    private int workmateQuantity;
    private String url;
    private double rating;


    public RestaurantItem(String name, String foodType, String address, String openingInfo,
                          int distance, int workmateQuantity, String url, double rating) {
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

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public int getWorkmateQuantity() {
        return workmateQuantity;
    }

    public void setWorkmateQuantity(int workmateQuantity) {
        this.workmateQuantity = workmateQuantity;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    //Some Methods to handle

    public String concatTypeAndAdress(String type, String address){
        return type + " - " + address;
    }

    public String concatWorkmateQuantity(int quantity) {
        return "("+quantity+")";
    }

    public String concatDistance(int distance){
        return distance+"m";
    }

    public float concatRating(double rating){
        return (float)rating;
    }
}
