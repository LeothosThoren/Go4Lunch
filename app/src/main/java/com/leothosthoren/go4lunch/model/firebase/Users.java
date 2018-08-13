package com.leothosthoren.go4lunch.model.firebase;

import android.support.annotation.Nullable;

public class Users {

    private String uid;
    private String username;
    private String userEmail;
    @Nullable
    private String urlPicture;
    private Boolean notificationEnabled;

    public Users() {
    }

    public Users(String uid, String username, String userEmail, @Nullable String urlPicture) {
        this.uid = uid;
        this.username = username;
        this.userEmail = userEmail;
        this.urlPicture = urlPicture;
    }

    public Users(String uid, String username, String userEmail) {
        this.uid = uid;
        this.username = username;
        this.userEmail = userEmail;
    }

    public Users(String uid, String username, String userEmail, @Nullable String urlPicture, Boolean notificationEnabled) {
        this.uid = uid;
        this.username = username;
        this.userEmail = userEmail;
        this.urlPicture = urlPicture;
        this.notificationEnabled = notificationEnabled;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    @Nullable
    public String getUrlPicture() {
        return urlPicture;
    }

    public void setUrlPicture(@Nullable String urlPicture) {
        this.urlPicture = urlPicture;
    }

    public Boolean getNotificationEnabled() {
        return notificationEnabled;
    }

    public void setNotificationEnabled(Boolean notificationEnabled) {
        this.notificationEnabled = notificationEnabled;
    }
}
