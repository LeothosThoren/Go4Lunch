package com.leothosthoren.go4lunch.api;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.leothosthoren.go4lunch.model.firebase.Users;
import com.leothosthoren.go4lunch.model.firebase.WorkmateSelection;

import java.util.Date;

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

    // --- GET ---

    public static Task<DocumentSnapshot> getUser(String uid) {
        return UserHelper.getUsersCollection().document(uid).get();
    }

    public static Task<QuerySnapshot> getAllWorkmates() {
        return UserHelper.getUsersCollection().get();
    }

    public static Query getAllUsers() {
        return UserHelper.getUsersCollection().limit(15);
    }


    // --- UPDATE ---

    public static Task<Void> updateUsername(String username, String uid) {
        return UserHelper.getUsersCollection().document(uid).update("username", username);
    }

    public static Task<Void> updateProfilePicture(String urlPicture, String uid) {
        return UserHelper.getUsersCollection().document(uid).update("urlPicture", urlPicture);
    }

    public static Task<Void> updateNotification(String uid, Boolean enabled) {
        return UserHelper.getUsersCollection().document(uid).update("notificationEnabled", enabled);
    }

    public static Task<Void> updateRestaurantSelection(String uid, String restaurantName, String restaurantId) {
        WorkmateSelection workmateSelection = new WorkmateSelection(restaurantName, restaurantId);
        return UserHelper.getUsersCollection().document(uid).update("workmateSelection.restaurantName", workmateSelection.getRestaurantName(),
                                                                    "workmateSelection.restaurantId", workmateSelection.getRestaurantId(),
                                                                            "workmateSelection.selectionDate", FieldValue.serverTimestamp());
    }


    // --- DELETE ---

    public static Task<Void> deleteUser(String uid) {
        return UserHelper.getUsersCollection().document(uid).delete();
    }

    public static Task<Void> deleteRestaurantSelectionFields(String uid) {
        return UserHelper.getUsersCollection().document(uid).update("workmateSelection", FieldValue.delete());
    }

}
