package com.leothosthoren.go4lunch.data;

import com.leothosthoren.go4lunch.model.detail.PlaceDetail;

import java.util.List;

public class DataSingleton {

    private static final DataSingleton ourInstance = new DataSingleton();
    private Double deviceLatitude;
    private Double deviceLongitude;
    private List<PlaceDetail> mPlaceDetailList;
    private PlaceDetail mPlaceDetail;

    private DataSingleton() {
    }

    public static DataSingleton getInstance() {
        return ourInstance;
    }

    public PlaceDetail getPlaceDetail() {
        return mPlaceDetail;
    }

    public void setPlaceDetail(PlaceDetail placeDetail) {
        mPlaceDetail = placeDetail;
    }

    public Double getDeviceLatitude() {
        return deviceLatitude;
    }

    public void setDeviceLatitude(Double deviceLatitude) {
        this.deviceLatitude = deviceLatitude;
    }

    public Double getDeviceLongitude() {
        return deviceLongitude;
    }

    public void setDeviceLongitude(Double deviceLongitude) {
        this.deviceLongitude = deviceLongitude;
    }

    public List<PlaceDetail> getPlaceDetailList() {
        return mPlaceDetailList;
    }

    public void setPlaceDetailList(List<PlaceDetail> placeDetailList) {
        mPlaceDetailList = placeDetailList;
    }
}
