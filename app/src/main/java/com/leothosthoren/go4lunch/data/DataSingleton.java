package com.leothosthoren.go4lunch.data;

import com.leothosthoren.go4lunch.model.detail.PlaceDetail;

import java.util.List;

public class DataSingleton {

    private static final DataSingleton ourInstance = new DataSingleton();

    private List<PlaceDetail> mPlaceDetail;
    private int position;

    private DataSingleton() {
    }

    public static DataSingleton getInstance() {
        return ourInstance;
    }

    public List<PlaceDetail> getPlaceDetail() {
        return mPlaceDetail;
    }

    public void setPlaceDetail(List<PlaceDetail> placeDetail) {
        mPlaceDetail = placeDetail;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
