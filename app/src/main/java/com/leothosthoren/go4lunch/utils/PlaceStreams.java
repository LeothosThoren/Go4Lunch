package com.leothosthoren.go4lunch.utils;

import com.leothosthoren.go4lunch.model.detail.PlaceDetail;
import com.leothosthoren.go4lunch.model.nearbysearch.NearbySearch;
import com.leothosthoren.go4lunch.model.nearbysearch.Result;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
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

    public static Observable<List<PlaceDetail>> streamFetchPlaceDetailList(String placeID) {
        PlaceService service = PlaceService.RETROFIT.create(PlaceService.class);
        return service.getDetail(placeID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .timeout(10, TimeUnit.SECONDS);
    }

    public static Observable<List<PlaceDetail>> streamFetchNearByAndPlaceDetail(String location) {
        return streamFetchNearbyApi(location)
                .flatMap(new Function<NearbySearch, Observable<List<PlaceDetail>> >() {
                    @Override
                    public Observable<List<PlaceDetail>>  apply(NearbySearch nearbySearch) throws Exception {
                        int i = 0;
                        for(; i < nearbySearch.getResults().size(); i++) {
                            return streamFetchPlaceDetailList(nearbySearch.getResults().get(i).getPlaceId());
                        }
                        return null;
                    }
                });
    }
}
