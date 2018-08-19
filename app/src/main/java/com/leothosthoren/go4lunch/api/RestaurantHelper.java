package com.leothosthoren.go4lunch.api;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.leothosthoren.go4lunch.model.detail.PlaceDetail;
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

    public static Task<Void> saveRestaurantChoice(String uid, PlaceDetail placeDetail, Boolean choice, Date date, Users workmate) {
        Restaurants restaurantChoiceToCreate = new Restaurants(date, choice, placeDetail, workmate);
        return RestaurantHelper.getRestaurantCollection().document(uid).set(restaurantChoiceToCreate);
    }

    // --- GET ---

    //Ok
    public static Task<DocumentSnapshot> getRestaurantSelection(String uid) {
        return RestaurantHelper.getRestaurantCollection().document(uid).get();
    }

    //NOk
    public static Task<DocumentSnapshot> getRestaurantsFromDatabase() {
        return RestaurantHelper.getRestaurantCollection().document().get();
    }

    // Ok
    public static Task<QuerySnapshot> getAllDocumentFromRestaurantCollection() {
        return RestaurantHelper.getRestaurantCollection().get();
    }

    //Nok
    public static Task<DocumentSnapshot> isRestaurantSelected() {
        return RestaurantHelper.getRestaurantCollection().document().get();
    }

    public static Query getAllRestaurants() {
        return RestaurantHelper.getRestaurantCollection();
    }


    // --- UPDATE ---

    public static Task<Void> updateRestaurantLike(HashMap<String, Boolean> like, String uid) {
        return RestaurantHelper.getRestaurantCollection().document(uid).update("restaurantLike", like);
    }

    // --- DELETE ---

    public static Task<Void> deleteRestaurantSelection(String uid) {
        return RestaurantHelper.getRestaurantCollection().document(uid).delete();
    }
}
