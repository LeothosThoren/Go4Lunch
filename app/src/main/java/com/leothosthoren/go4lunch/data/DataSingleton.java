package com.leothosthoren.go4lunch.data;

import com.leothosthoren.go4lunch.model.detail.PlaceDetail;

import java.util.List;

public class DataSingleton {

    private static final DataSingleton ourInstance = new DataSingleton();
    private double deviceLatitude;
    private double deviceLongitude;
    private List<PlaceDetail> mPlaceDetailList;
    private PlaceDetail mPlaceDetail;

    private DataSingleton() {
    }

    public static DataSingleton getInstance() {
        return ourInstance;
    }
    //Todo add getter object place frogot about position

    public PlaceDetail getPlaceDetail() {
        return mPlaceDetail;
    }

    public void setPlaceDetail(PlaceDetail placeDetail) {
        mPlaceDetail = placeDetail;
    }

    public double getDeviceLatitude() {
        return deviceLatitude;
    }

    public void setDeviceLatitude(double deviceLatitude) {
        this.deviceLatitude = deviceLatitude;
    }

    public double getDeviceLongitude() {
        return deviceLongitude;
    }

    public void setDeviceLongitude(double deviceLongitude) {
        this.deviceLongitude = deviceLongitude;
    }

    public List<PlaceDetail> getPlaceDetailList() {
        return mPlaceDetailList;
    }

    public void setPlaceDetailList(List<PlaceDetail> placeDetailList) {
        mPlaceDetailList = placeDetailList;
    }
}
