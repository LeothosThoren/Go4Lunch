package com.leothosthoren.go4lunch.api;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
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

    // --- GET ---

    public static Task<DocumentSnapshot> getUser(String uid) {
        return UserHelper.getUsersCollection().document(uid).get();
    }

    public static Task<DocumentSnapshot> getEmail(String uid) {
        return UserHelper.getUsersCollection().document(uid).get();
    }

    // --- Try to get All user from collection with query
    public static Query getAllUsersWorkmates() {
        return UserHelper.getUsersCollection().orderBy("username");
    }

    // --- UPDATE ---

    public static Task<Void> updateUsername(String username, String uid) {
        return UserHelper.getUsersCollection().document(uid).update("username", username);
    }

    public static Task<Void> updateEmail(String email, String uid) {
        return UserHelper.getUsersCollection().document(uid).update("email", email);
    }

    public static Task<Void> updateProfilPicture(String urlPicture, String uid) {
        return UserHelper.getUsersCollection().document(uid).update("urlPicture", urlPicture);
    }

    // --- DELETE ---

    public static Task<Void> deleteUser(String uid) {
        return UserHelper.getUsersCollection().document(uid).delete();
    }

}
