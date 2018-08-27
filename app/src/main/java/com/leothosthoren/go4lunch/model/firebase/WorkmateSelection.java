package com.leothosthoren.go4lunch.model.firebase;

import java.util.Date;

public class WorkmateSelection {

    private String restaurantName;
    private String restaurantId;
    private Date selectionDate;

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

    public Date getSelectionDate() {
        return selectionDate;
    }

    //--- SETTER---

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public void setSelectionDate(Date selectionDate) {
        this.selectionDate = selectionDate;
    }
}
