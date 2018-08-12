package com.leothosthoren.go4lunch.api;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.leothosthoren.go4lunch.model.firebase.Restaurants;
import com.leothosthoren.go4lunch.model.firebase.Users;

import java.util.Date;
import java.util.HashMap;


public class RestaurantHelper {

    private static final String COLLECTION_NAME = "restaurants";

    // --- COLLECTION REFERENCE ---

    public static CollectionReference getRestaurantCollection() {
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }

    // --- CREATE ---

    public static Task<Void> saveRestaurantChoice(String uid, String placeID, Boolean choice, Date date, Users workmate) {
        Restaurants restaurantChoiceToCreate = new Restaurants(date, choice, placeID, workmate);
        return RestaurantHelper.getRestaurantCollection().document(uid).set(restaurantChoiceToCreate);
    }

    // --- GET ---

    public static Task<DocumentSnapshot> getRestaurantChoice(String uid) {
        return RestaurantHelper.getRestaurantCollection().document(uid).get();
    }

    public static Query isRestaurantSelected() {
        return RestaurantHelper.getRestaurantCollection().whereEqualTo("choice", true);
    }

    // --- UPDATE ---

    public static Task<Void> updateRestaurantLike(HashMap<String, Boolean> like, String uid) {
        return RestaurantHelper.getRestaurantCollection().document(uid).update("restaurantLike", like);
    }

    // --- DELETE ---

    public static Task<Void> deleteRestaurantLike(HashMap<String, Boolean> like, String uid) {
        return RestaurantHelper.getRestaurantCollection().document(uid).delete();
    }
}
