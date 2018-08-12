package com.leothosthoren.go4lunch.controler.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.leothosthoren.go4lunch.BuildConfig;
import com.leothosthoren.go4lunch.R;
import com.leothosthoren.go4lunch.api.RestaurantHelper;
import com.leothosthoren.go4lunch.api.UserHelper;
import com.leothosthoren.go4lunch.base.BaseActivity;
import com.leothosthoren.go4lunch.data.DataSingleton;
import com.leothosthoren.go4lunch.model.detail.PlaceDetail;
import com.leothosthoren.go4lunch.model.firebase.Users;
import com.leothosthoren.go4lunch.utils.DataConverterHelper;
import com.leothosthoren.go4lunch.utils.FireBaseTools;

import java.util.Objects;

import butterknife.BindView;

public class RestaurantInfoActivity extends BaseActivity implements DataConverterHelper, FireBaseTools {

    // VIEW
    @BindView(R.id.restaurant_info_phone_button)
    Button mButtonCall;
    @BindView(R.id.floatingActionButton)
    FloatingActionButton mFloatingActionButton;
    @BindView(R.id.restaurant_info_website_button)
    Button mWebsiteButton;
    @BindView(R.id.restaurant_info_like_button)
    Button mLikeButton;
    @BindView(R.id.restaurant_info_name)
    TextView restaurantName;
    @BindView(R.id.restaurant_info_address)
    TextView restaurantAddress;
    @BindView(R.id.restaurant_info_photo)
    ImageView restaurantPhoto;
    @BindView(R.id.restaurant_info_rating_bar)
    RatingBar restaurantRatingBar;
    // VAR
    private boolean isCheckFab = true;
    private boolean isCheckLike = true;
    private Users currentUser;
    // DATA
    private PlaceDetail mPlaceDetail = DataSingleton.getInstance().getPlaceDetail();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (mPlaceDetail.getResult() != null) {
            setUpViews();
        }

        this.getCurrentUserFromFirestore();
        this.clickHandler();
    }

    @Override
    public int getFragmentLayout() {
        return R.layout.activity_restaurant_info;
    }


    // ---------------------
    // CONFIGURATION
    // ---------------------

    //RESTAURANT SELECTION
    public void clickHandler() {
        mFloatingActionButton.setOnClickListener(v -> {

            if (isCheckFab) {
                //Firestore
                RestaurantHelper.saveRestaurantChoice(FirebaseAuth.getInstance().getUid(), mPlaceDetail.getResult().getPlaceId(),
                        true, null, currentUser)
                        .addOnCompleteListener(onCompleteListener(this, "Choice saved"))
                        .addOnFailureListener(onFailureListener(this));
                /*.addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        mFloatingActionButton.setImageResource(R.drawable.ic_check_circle);
                        isCheckFab = false;
                        Snackbar.make(v, "checkFab true", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    } else {
                        Toast.makeText(this, "Something is wrong check the log", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "clickHandler: fab :" + task.getException());
                    }

                }).addOnFailureListener(this.onFailureListener(v));*/

            } else {

                // Firestore
//                RestaurantHelper.saveRestaurantChoice(FirebaseAuth.getInstance().getUid(), null,
//                        false, null, currentUser);

                mFloatingActionButton.setImageResource(R.drawable.ic_uncheck_circle);
                isCheckFab = true;
                Snackbar.make(v, "checkFab false", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }

        });

        //LIKE BUTTON
        mLikeButton.setOnClickListener(v -> {

            if (isCheckLike) {
                mLikeButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_star_full, 0, 0);
                isCheckLike = false;
                Toast.makeText(this, "clickHandler on star button true", Toast.LENGTH_SHORT).show();
            } else {
                mLikeButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_star_border, 0, 0);
                isCheckLike = true;
                Toast.makeText(this, "clickHandler on star button false", Toast.LENGTH_SHORT).show();
            }

        });

        //CALLING
        mButtonCall.setOnClickListener(v -> dialPhoneNumber(mPlaceDetail.getResult().getFormattedPhoneNumber()));

        //BROWSE URL
        mWebsiteButton.setOnClickListener(v -> openWebsitePage(mPlaceDetail.getResult().getWebsite()));

    }

    // --------------------
    // REST REQUESTS
    // --------------------

    // Get Current User from Firestore
    private void getCurrentUserFromFirestore() {
        UserHelper.getUser(Objects.requireNonNull(getCurrentUser()).getUid())
                .addOnSuccessListener(documentSnapshot ->
                        currentUser = documentSnapshot.toObject(Users.class));
    }

    // ---------------------
    // UI
    // ---------------------

    private void setUpViews() {
        //Restaurant name
        restaurantName.setText(mPlaceDetail.getResult().getName());
        //Restaurant address
        restaurantAddress.setText(formatAddress(mPlaceDetail.getResult().getFormattedAddress()));
        //Restaurant rating
        restaurantRatingBar.setRating(formatRating(mPlaceDetail.getResult().getRating()));
        //Restaurant Photo
        if (mPlaceDetail.getResult().getPhotos() != null) {
            String request = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&maxheight=400" +
                    "&photoreference=";
            String apiKey = "&key=" + BuildConfig.ApiKey;
            Glide.with(this)
                    .load(request + mPlaceDetail.getResult().getPhotos().get(0).getPhotoReference() + apiKey)
                    .into(this.restaurantPhoto);
        }


    }

    private void dialPhoneNumber(String phoneNumber) {
        if (phoneNumber != null) {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + phoneNumber));
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            } else {
                Toast.makeText(RestaurantInfoActivity.this, R.string.phone_no_available, Toast.LENGTH_SHORT).show();
            }
        }


    }

    public void openWebsitePage(String url) {
        if (url != null) {
            Uri webpage = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            }
        } else {
            Toast.makeText(RestaurantInfoActivity.this, R.string.website_no_available, Toast.LENGTH_SHORT).show();
        }

    }


}

