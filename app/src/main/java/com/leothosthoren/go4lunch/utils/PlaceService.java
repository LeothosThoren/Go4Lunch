package com.leothosthoren.go4lunch.utils;

import com.leothosthoren.go4lunch.BuildConfig;
import com.leothosthoren.go4lunch.model.detail.PlaceDetail;
import com.leothosthoren.go4lunch.model.nearbysearch.NearbySearch;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface PlaceService {
    String apiKey = BuildConfig.ApiKey;
    String baseUri = "https://maps.googleapis.com/maps/api/place/";

    Retrofit RETROFIT = new Retrofit.Builder()
            .baseUrl(baseUri)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build();

    //NearbySearch api
    @GET("nearbysearch/json?&radius=300&type=restaurant&key=" + apiKey)
    Observable<NearbySearch> getNearbySearch(@Query("location") String location);

    //Place Detail api
    @GET("details/json?&key=" + apiKey)
    Observable<List<PlaceDetail>> getDetail(@Query("placeid") String placeId);

    //Place Photo api
    @GET("photo?photoreference={photoReference}&key=" + apiKey)
    Observable<?> getPlacePhoto(@Query("photoReference") String photoRef);

}
