package com.leothosthoren.go4lunch.data;

import com.leothosthoren.go4lunch.model.detail.PlaceDetail;
import com.leothosthoren.go4lunch.model.nearbysearch.NearbySearch;

import java.util.List;

public class DataSingleton {

    private static final DataSingleton ourInstance = new DataSingleton();
    private List<NearbySearch> mNearbySearch;
    private List<PlaceDetail> mPlaceDetail;

    private DataSingleton() {
    }

    public static DataSingleton getInstance() {
        return ourInstance;
    }

    public void setPlaceDetail(List<PlaceDetail> placeDetail) {
        mPlaceDetail = placeDetail;
    }

    public List<NearbySearch> getNearbySearch() {
        return mNearbySearch;
    }

    public void setNearbySearch(List<NearbySearch> nearbySearch) {
        mNearbySearch = nearbySearch;
    }

    public List<PlaceDetail> getPlaceDetail() {
        return mPlaceDetail;
    }
}
