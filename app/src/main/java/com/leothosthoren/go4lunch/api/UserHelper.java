package com.leothosthoren.go4lunch.api;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.leothosthoren.go4lunch.model.firebase.Users;

public class UserHelper {

    private static final String COLLECTION_NAME = "users";


    // --- COLLECTION REFERENCE ---

    public static CollectionReference getUsersCollection() {
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }


    // --- CREATE ---

    public static Task<Void> createUser(String uid, String username, String email, String urlPicture) {
        Users userToCreate = new Users(uid, username, email, urlPicture);
        return UserHelper.getUsersCollection().document(uid).set(userToCreate);
    }

    public static Task<Void> createUserWithoutPicture(String uid, String username, String email) {
        Users userToCreate = new Users(uid, username, email);
        return UserHelper.getUsersCollection().document(uid).set(userToCreate);
    }


    // --- GET ---

    public static Task<DocumentSnapshot> getUser(String uid) {
        return UserHelper.getUsersCollection().document(uid).get();
    }

    public static Query getAllUsers() {
        return UserHelper.getUsersCollection().limit(10);
    }


    // --- UPDATE ---

    public static Task<Void> updateUsername(String username, String uid) {
        return UserHelper.getUsersCollection().document(uid).update("username", username);
    }

    public static Task<Void> updateEmail(String email, String uid) {
        return UserHelper.getUsersCollection().document(uid).update("email", email);
    }

    public static Task<Void> updateProfilePicture(String urlPicture, String uid) {
        return UserHelper.getUsersCollection().document(uid).update("urlPicture", urlPicture);
    }

    public static Task<Void> updateNotification(String uid, Boolean enabled) {
        return UserHelper.getUsersCollection().document(uid).update("notificationEnabled", enabled);
    }


    // --- DELETE ---

    public static Task<Void> deleteUser(String uid) {
        return UserHelper.getUsersCollection().document(uid).delete();
    }

}
