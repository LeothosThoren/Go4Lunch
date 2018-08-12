package com.leothosthoren.go4lunch.model.firebase;

import android.support.annotation.Nullable;

public class Users {

    private String uid;
    private String userName;
    private String userEmail;
//    @Nullable
    private String urlPicture;
    private Boolean isNotificationEnabled;

    public Users() {
    }

    public Users(String uid, String userName, String userEmail, /*@Nullable*/ String urlPicture) {
        this.uid = uid;
        this.userName = userName;
        this.userEmail = userEmail;
        this.urlPicture = urlPicture;
    }

    public Users(String uid, String userName, String userEmail) {
        this.uid = uid;
        this.userName = userName;
        this.userEmail = userEmail;
    }

    public Users(String uid, String userName, String userEmail, /*@Nullable*/ String urlPicture, Boolean isNotificationEnabled) {
        this.uid = uid;
        this.userName = userName;
        this.userEmail = userEmail;
        this.urlPicture = urlPicture;
        this.isNotificationEnabled = isNotificationEnabled;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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
        return isNotificationEnabled;
    }

    public void setNotificationEnabled(Boolean notificationEnabled) {
        isNotificationEnabled = notificationEnabled;
    }
}
