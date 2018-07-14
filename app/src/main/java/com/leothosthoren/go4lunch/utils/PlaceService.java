package com.leothosthoren.go4lunch.utils;

import com.leothosthoren.go4lunch.model.nearbysearch.NearbySearch;

import io.reactivex.Observable;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface PlaceService {
    String apiKey = "AIzaSyBDL58mNZkTUDJKLJ8boce0frl7VIhSMAQ";
    String baseUri = "https://maps.googleapis.com/maps/api/place/";

    Retrofit RETROFIT = new Retrofit.Builder()
            .baseUrl(baseUri)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build();

    //NearbySearch api
    @GET("nearbysearch/json?location=48.856614, 2.3522219&radius=500&type=restaurant&key=" + apiKey)
    Observable<NearbySearch> getNearbySearch(/*@Query("latitude") Double latitude,
                                             @Query("longitude") Double longitude*/);

    //Place Detail api
    @GET("details/json?placeid={placeID}&key=" + apiKey)
    Observable<?> getDetail(@Query("placeID") String placeId);

    //Place Photo api
    @GET("photo?photoreference={photoReference}&key=" + apiKey)
    Observable<?> getPlacePhoto(@Query("photoReference") String photoRef);

}
