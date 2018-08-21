package com.leothosthoren.go4lunch.model.firebase;

import android.support.annotation.Nullable;

public class WorkmateAndRestaurant {

    private Users mUsers;
    @Nullable
    private Restaurants mRestaurants;

    public WorkmateAndRestaurant() {
    }

    public WorkmateAndRestaurant(Users mUsers, @Nullable Restaurants mRestaurants) {
        this.mUsers = mUsers;
        this.mRestaurants = mRestaurants;
    }

    public Users getUsers() {
        return mUsers;
    }

    public void setUsers(Users users) {
        this.mUsers = users;
    }

    @Nullable
    public Restaurants getRestaurants() {
        return mRestaurants;
    }

    public void setRestaurants(@Nullable Restaurants restaurants) {
        mRestaurants = restaurants;
    }
}
