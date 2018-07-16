package com.leothosthoren.go4lunch.data;

import com.leothosthoren.go4lunch.model.detail.PlaceDetail;
import com.leothosthoren.go4lunch.model.nearbysearch.NearbySearch;
import com.leothosthoren.go4lunch.model.nearbysearch.Result;

import java.util.List;

public class DataSingleton {

    private static final DataSingleton ourInstance = new DataSingleton();
    private List<Result> mNearbySearch;
    private List<PlaceDetail> mPlaceDetail;

    private DataSingleton() {
    }

    public static DataSingleton getInstance() {
        return ourInstance;
    }

    public void setPlaceDetail(List<PlaceDetail> placeDetail) {
        mPlaceDetail = placeDetail;
    }

    public List<Result> getNearbySearch() {
        return mNearbySearch;
    }

    public void setNearbySearch(List<Result> nearbySearch) {
        mNearbySearch = nearbySearch;
    }

    public List<PlaceDetail> getPlaceDetail() {
        return mPlaceDetail;
    }
}
