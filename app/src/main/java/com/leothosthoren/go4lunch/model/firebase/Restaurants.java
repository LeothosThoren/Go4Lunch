package com.leothosthoren.go4lunch.model.firebase;

import com.google.firebase.firestore.IgnoreExtraProperties;
import com.google.firebase.firestore.ServerTimestamp;
import com.leothosthoren.go4lunch.model.detail.PlaceDetail;

import java.util.Date;

@IgnoreExtraProperties
public class Restaurants {

    private Date dateChoice;
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

    // GETTERS

    @ServerTimestamp
    public Date getDateChoice() {
        return dateChoice;
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
