package com.leothosthoren.go4lunch.model.firebase;

public class RestaurantsLike {

    private String placeId;
    private Boolean isStarChecked;

    public RestaurantsLike() {
    }

    public RestaurantsLike(String placeId, Boolean isStarChecked) {
        this.placeId = placeId;
        this.isStarChecked = isStarChecked;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public Boolean getChecked() {
        return isStarChecked;
    }

    public void setChecked(Boolean checked) {
        isStarChecked = checked;
    }
}
