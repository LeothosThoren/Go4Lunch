package com.leothosthoren.go4lunch.view;

import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.RequestManager;
import com.leothosthoren.go4lunch.BuildConfig;
import com.leothosthoren.go4lunch.R;
import com.leothosthoren.go4lunch.adapter.RestaurantAdapter;
import com.leothosthoren.go4lunch.model.detail.Close;
import com.leothosthoren.go4lunch.model.detail.PlaceDetail;
import com.leothosthoren.go4lunch.utils.DataConvertHelper;

import java.lang.ref.WeakReference;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RestaurantViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, DataConvertHelper {
    @BindView(R.id.item_restaurant_layout)
    ConstraintLayout mLayout;
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
//        this.mRatingBar.setRating(restaurantItem.formatRating(restaurantItem.getRating()));
//        glide.load(restaurantItem.getUrlPhoto()).into(this.mRestaurantPhoto);
//    }

    public void updateRestaurantView(PlaceDetail placeDetail, RequestManager glide, RestaurantAdapter.Listener callback) {
        assert this.mRestaurantName != null;
        this.mRestaurantName.setText(placeDetail.getResult().getName());
        if (placeDetail.getResult().getPhotos() != null) {
            glide.load("https://maps.googleapis.com/maps/api/place/photo?maxwidth=100&maxheight=100&photoreference="
                    + placeDetail.getResult().getPhotos().get(0).getPhotoReference() + "&key=" + BuildConfig.ApiKey).into(mRestaurantPhoto);
        }
        this.mRestaurantAddress.setText(formatAddress(placeDetail.getResult().getFormattedAddress()));
        this.mRatingBar.setRating(formatRating(placeDetail.getResult().getRating()));
//        this.mRestaurantOpening.setText(formatOpeningTime(placeDetail.getResult().getOpeningHours().getOpenNow(), placeDetail.getResult().getOpeningHours().getPeriods()));
        //Todo try to test

        // Configure the clicks
        this.mRestaurantPhoto.setOnClickListener(this);
        this.callbackWeakRef = new WeakReference<RestaurantAdapter.Listener>(callback);
    }

    //Call the click
    @Override
    public void onClick(View v) {
        RestaurantAdapter.Listener callback = callbackWeakRef.get();
        if (callback != null) callback.onClickItemButton(getAdapterPosition());
    }
}
