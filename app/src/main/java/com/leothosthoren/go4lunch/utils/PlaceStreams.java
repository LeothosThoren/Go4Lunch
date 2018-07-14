package com.leothosthoren.go4lunch.utils;

import com.leothosthoren.go4lunch.model.nearbysearch.NearbySearch;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class PlaceStreams {

    public static Observable<NearbySearch> streamFetchPlaceId(/*Double latitude, Double longitude*/) {
        PlaceService service = PlaceService.RETROFIT.create(PlaceService.class);
        return service.getNearbySearch(/*latitude, longitude*/)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .timeout(10, TimeUnit.SECONDS);
    }
}
