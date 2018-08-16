package com.leothosthoren.go4lunch.api;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.leothosthoren.go4lunch.model.firebase.RestaurantsLike;

public class RestaurantLikeHelper {

    private static final String COLLECTION_NAME = "likes";

    // --- CREATE ---

    public static Task<Void> likeRestaurant(String uid, String restaurantName, String placeID, Boolean isStarChecked) {

        RestaurantsLike like = new RestaurantsLike(restaurantName, placeID, isStarChecked);

        return UserHelper.getUsersCollection()
                .document(uid)
                .collection(COLLECTION_NAME)
                .document(placeID)
                .set(like);
    }

    // --- GET ---

    public static Task<DocumentSnapshot> getRestaurantLike(String uid, String currentPlaceId) {
        return UserHelper.getUsersCollection()
                .document(uid)
                .collection(COLLECTION_NAME)
                .document(currentPlaceId).get();
    }

    // --- DELETE ---

    public static Task<Void> dislikeRestaurant(String uid, String currentPlaceId) {
        return UserHelper.getUsersCollection()
                .document(uid)
                .collection(COLLECTION_NAME)
                .document(currentPlaceId)
                .delete();
    }
}
