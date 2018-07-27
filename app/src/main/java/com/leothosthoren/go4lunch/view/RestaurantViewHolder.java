package com.leothosthoren.go4lunch.view;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.leothosthoren.go4lunch.BuildConfig;
import com.leothosthoren.go4lunch.R;
import com.leothosthoren.go4lunch.adapter.RestaurantAdapter;
import com.leothosthoren.go4lunch.model.RestaurantItem;
import com.leothosthoren.go4lunch.model.detail.PlaceDetail;

import java.lang.ref.WeakReference;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RestaurantViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    @Nullable
    @BindView(R.id.item_restaurants_name)
    TextView mRestaurantName;
    @BindView(R.id.item_restaurant_imageView)
    ImageView mRestaurantPhoto;
    @BindView(R.id.item_restaurants_address)
    TextView mRestaurantAddress;
    @BindView(R.id.item_restaurants_opening)
    TextView mRestaurantOpening;
    @BindView(R.id.item_restaurants_distance)
    TextView mRestaurantDistance;
    @BindView(R.id.item_restaurants_nb_workmates)
    TextView mNbOfWorkmates;
    @BindView(R.id.item_restaurants_ratingbar)
    RatingBar mRatingBar;


    private WeakReference<RestaurantAdapter.Listener> callbackWeakRef;

    public RestaurantViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

//    public void updateRestaurantList(RestaurantItem restaurantItem, RequestManager glide) {
//        assert this.mRestaurantName != null;
//        this.mRestaurantName.setText(restaurantItem.getName());
//        this.mRestaurantAddress.setText(restaurantItem.concatTypeAndAdress(restaurantItem.getFoodType(), restaurantItem.getAddress()));
//        this.mRestaurantDistance.setText(restaurantItem.concatDistance(restaurantItem.getDistance()));
//        this.mRestaurantOpening.setText(restaurantItem.getOpeningInfo());
//        this.mNbOfWorkmates.setText(restaurantItem.concatWorkmateQuantity(restaurantItem.getWorkmateQuantity()));
//        this.mRatingBar.setRating(restaurantItem.concatRating(restaurantItem.getRating()));
//        glide.load(restaurantItem.getUrlPhoto()).into(this.mRestaurantPhoto);
//    }

    public void updateRestaurantView(PlaceDetail placeDetail, RequestManager glide){
        assert this.mRestaurantName != null;
        this.mRestaurantName.setText(placeDetail.getResult().getName());
//        if (placeDetail.getResult().getPhotos().get(0).getPhotoReference() != null) {
//            glide.load("https://maps.googleapis.com/maps/api/place/photo?photoreference="
//                    +placeDetail.getResult().getPhotos().get(0).getPhotoReference()+"&key="+ BuildConfig.ApiKey).into(mRestaurantPhoto);
//        }

    }

    @Override
    public void onClick(View v) {

    }
}
