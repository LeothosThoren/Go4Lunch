package com.leothosthoren.go4lunch.api;

import com.leothosthoren.go4lunch.model.detail.PlaceDetail;
import com.leothosthoren.go4lunch.model.nearbysearch.NearbySearch;
import com.leothosthoren.go4lunch.model.nearbysearch.Result;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class PlaceStreams {

    public static Observable<NearbySearch> streamFetchNearbyApi(String location) {
        PlaceService service = PlaceService.RETROFIT.create(PlaceService.class);
        return service.getNearbySearch(location)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .timeout(10, TimeUnit.SECONDS);
    }

    public static Observable<PlaceDetail> streamFetchPlaceDetail(String placeID) {
        PlaceService service = PlaceService.RETROFIT.create(PlaceService.class);
        return service.getDetail(placeID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .timeout(10, TimeUnit.SECONDS);
    }


    public static Observable<PlaceDetail> streamTest(String location) {
        return streamFetchNearbyApi(location)
                .map(new Function<NearbySearch, Result>() {
                    @Override
                    public Result apply(NearbySearch nearbySearch) throws Exception {
                        return nearbySearch.getResults().get(0);
                    }
                }).flatMap(new Function<Result, Observable<PlaceDetail>>() {
                    @Override
                    public Observable<PlaceDetail> apply(Result result) throws Exception {
                        return streamFetchPlaceDetail(result.getPlaceId());
                    }

                });
    }

//
//    public static Observable<PlaceDetail> streamTest(String location) {
//        return streamFetchNearbyApi(location)
//                .map(new Function<NearbySearch, Result>() {
//                    @Override
//                    public Result apply(NearbySearch nearbySearch) throws Exception {
//                        return nearbySearch.getResults().get(0);
//                    }
//                }).flatMap(new Function<Result, Observable<PlaceDetail>>() {
//                    @Override
//                    public Observable<PlaceDetail> apply(Result result) throws Exception {
//                        return streamFetchPlaceDetail(result.getPlaceId());
//                    }
//
//                });
//    }
}
