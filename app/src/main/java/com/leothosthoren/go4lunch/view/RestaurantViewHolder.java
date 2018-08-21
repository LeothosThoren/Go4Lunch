package com.leothosthoren.go4lunch.view;

import android.support.constraint.ConstraintLayout;
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
import com.leothosthoren.go4lunch.data.DataSingleton;
import com.leothosthoren.go4lunch.model.detail.PlaceDetail;
import com.leothosthoren.go4lunch.utils.DataConverterHelper;

import java.lang.ref.WeakReference;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RestaurantViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, DataConverterHelper {
    //WIDGET
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
    @BindView(R.id.item_restaurants_workmates_images)
    ImageView mWorkmateImageView;
    @BindView(R.id.item_restaurants_ratingbar)
    RatingBar mRatingBar;
    // VAR
    private double lat = DataSingleton.getInstance().getDeviceLatitude();
    private double lng = DataSingleton.getInstance().getDeviceLongitude();
    private WeakReference<RestaurantAdapter.Listener> callbackWeakRef;

    public RestaurantViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }


    public void updateRestaurantListView(PlaceDetail placeDetail, RequestManager glide,
                                         RestaurantAdapter.Listener callback, int nbOfWorkmate) {

        String request = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=100&maxheight=100&photoreference=";
        String apiKey = "&key=" + BuildConfig.ApiKey;

        if ( placeDetail.getResult() != null)
        {
            //Restaurant name
            this.mRestaurantName.setText(placeDetail.getResult().getName());
            //Restaurant photo
            if (placeDetail.getResult().getPhotos() != null) {
                glide.load(request + placeDetail.getResult().getPhotos().get(0).getPhotoReference() + apiKey)
                        .apply(RequestOptions.centerCropTransform())
                        .into(this.mRestaurantPhoto);
            }
            // Address
            this.mRestaurantAddress.setText(formatAddress(placeDetail.getResult().getFormattedAddress()));
            if (placeDetail.getResult().getRating() != null) {
                this.mRatingBar.setRating(formatRating(placeDetail.getResult().getRating()));
            }
            // Distance
            if (lat != 0 && lng != 0) {
                this.mRestaurantDistance.setText(computeDistance(lat, placeDetail.getResult().getGeometry().getLocation().getLat(),
                        lng, placeDetail.getResult().getGeometry().getLocation().getLng()));
            }
            //Opening time
            if (placeDetail.getResult().getOpeningHours() != null) {
                this.mRestaurantOpening.setText(formatWeekDayText(placeDetail.getResult().getOpeningHours().getWeekdayText()));
            }
            // Number of workmates
            if (nbOfWorkmate > 0) {
                mNbOfWorkmates.setText(String.valueOf("(" + nbOfWorkmate + ")"));
                this.mNbOfWorkmates.setVisibility(View.VISIBLE);
                this.mWorkmateImageView.setVisibility(View.VISIBLE);
            } else {
                this.mNbOfWorkmates.setVisibility(View.INVISIBLE);
                this.mWorkmateImageView.setVisibility(View.INVISIBLE);
            }

        }

        // Configure the clicks
        if (mRestaurantPhoto != null) {
            this.mRestaurantPhoto.setOnClickListener(this);
        }
        this.callbackWeakRef = new WeakReference<RestaurantAdapter.Listener>(callback);
    }

    //Call the click
    @Override
    public void onClick(View v) {
        RestaurantAdapter.Listener callback = callbackWeakRef.get();
        if (callback != null) callback.onClickItemButton(getAdapterPosition());
    }
}
