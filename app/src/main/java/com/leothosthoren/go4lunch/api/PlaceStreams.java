package com.leothosthoren.go4lunch.api;

import com.leothosthoren.go4lunch.model.detail.PlaceDetail;
import com.leothosthoren.go4lunch.model.nearbysearch.NearbySearch;
import com.leothosthoren.go4lunch.model.nearbysearch.Result;

import java.util.List;
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

    public static Observable<List<PlaceDetail>> streamFetchListPlaceDetail(String location) {
        return streamFetchNearbyApi(location)
                .map(new Function<NearbySearch, List<Result>>() {
                    @Override
                    public List<Result> apply(NearbySearch restaurant) throws Exception {
                        return restaurant.getResults();
                    }
                })
                .concatMap(new Function<List<Result>, Observable<List<PlaceDetail>>>() {
                    @Override
                    public Observable<List<PlaceDetail>> apply(List<Result> results) throws Exception {
                        return Observable.fromIterable(results)
                                .concatMap(new Function<Result, Observable<PlaceDetail>>() {
                                    @Override
                                    public Observable<PlaceDetail> apply(Result result) throws Exception {
                                        return streamFetchPlaceDetail(result.getPlaceId());
                                    }
                                })
                                .toList()
                                .toObservable();
                    }
                });
    }

//    public static Observable<List<PlaceDetail>> streamFetchListPlaceDetail(String location) {
//        return streamFetchNearbyApi(location)
//                .map(NearbySearch::getResults)
//                .flatMap((Function<List<Result>, Observable<List<PlaceDetail>>>) results -> Observable.fromIterable(results)
//                        .flatMap((Function<Result, Observable<PlaceDetail>>) result -> streamFetchPlaceDetail(result.getPlaceId()))
//                        .toList()
//                        .toObservable());
//    }
}
