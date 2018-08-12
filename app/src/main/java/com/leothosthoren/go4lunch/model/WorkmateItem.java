package com.leothosthoren.go4lunch.model;

import com.leothosthoren.go4lunch.model.firebase.Users;

public class WorkmateItem {

    private String workmateName;
    private String photoUrl;
    private Users mUsers;

    public WorkmateItem(String workmateName, String photoUrl, Users users) {
        this.workmateName = workmateName;
        this.photoUrl = photoUrl;
        mUsers = users;
    }

    public WorkmateItem(String workmateName, String photoUrl) {
        this.workmateName = workmateName;
        this.photoUrl = photoUrl;
    }

    public WorkmateItem() {

    }

    public Users getUsers() {
        return mUsers;
    }

    public void setUsers(Users users) {
        mUsers = users;
    }

    public String getWorkmateName() {
        return workmateName;
    }

    public void setWorkmateName(String workmateName) {
        this.workmateName = workmateName;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
}
