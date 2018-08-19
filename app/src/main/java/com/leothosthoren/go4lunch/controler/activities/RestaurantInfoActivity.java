package com.leothosthoren.go4lunch.controler.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.leothosthoren.go4lunch.BuildConfig;
import com.leothosthoren.go4lunch.R;
import com.leothosthoren.go4lunch.adapter.JoiningWorkmateAdapter;
import com.leothosthoren.go4lunch.api.RestaurantHelper;
import com.leothosthoren.go4lunch.api.RestaurantLikeHelper;
import com.leothosthoren.go4lunch.api.UserHelper;
import com.leothosthoren.go4lunch.base.BaseActivity;
import com.leothosthoren.go4lunch.data.DataSingleton;
import com.leothosthoren.go4lunch.model.detail.PlaceDetail;
import com.leothosthoren.go4lunch.model.firebase.Restaurants;
import com.leothosthoren.go4lunch.model.firebase.Users;
import com.leothosthoren.go4lunch.utils.DataConverterHelper;
import com.leothosthoren.go4lunch.utils.FireBaseTools;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;

public class RestaurantInfoActivity extends BaseActivity implements DataConverterHelper, FireBaseTools {

    // VIEW
    @BindView(R.id.restaurant_info_phone_button)
    Button mButtonCall;
    @BindView(R.id.floatingActionButton)
    FloatingActionButton mFab;
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
    @BindView(R.id.recycler_view_id)
    RecyclerView mRecyclerView;
    // VAR
    private boolean isCheckFab = true;
    private boolean isCheckLike = true;
    private JoiningWorkmateAdapter mJoiningWorkmateAdapter;
    // DATA
    private PlaceDetail mPlaceDetail = DataSingleton.getInstance().getPlaceDetail();
    private Users mCurrentUser;
    private Date mDate;
    private String mPlaceID;
    private Boolean mRestaurantSelection;
    //Test
    private List<Restaurants> mRestaurantsFromFireStore = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.init();
    }

    @Override
    public int getFragmentLayout() {
        return R.layout.activity_restaurant_info;
    }

    // ---------------------
    // INIT
    // ---------------------

    private void init() {
        if (mPlaceDetail.getResult() != null) {
            this.getCurrentUserFromFirestore();
            this.getWorkmatesWhichSelectTheSamePlace();
            this.getUserSelectionPlaceFromFirestore();
            this.getRestaurantLikeFromFirestore();
            this.setUpViewsWithPlaceApi();
        }
        this.clickHandler();
    }


    // ---------------------
    // CONFIGURATION
    // ---------------------


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

    private void configureRecyclerView() {
        Log.d(TAG, "configureRecyclerView: OK");
        this.mJoiningWorkmateAdapter = new JoiningWorkmateAdapter(mRestaurantsFromFireStore,
                Glide.with(Objects.requireNonNull(this)),
                Objects.requireNonNull(Objects.requireNonNull(getCurrentUser()).getUid()));

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(this.mJoiningWorkmateAdapter);
    }


    public void clickHandler() {
        //RESTAURANT SELECTION
        mFab.setOnClickListener(v -> {
            if (isCheckFab) {
                mFab.setImageResource(R.drawable.ic_check_circle);
                if (getCurrentUser() != null) {
                    RestaurantHelper.saveRestaurantChoice(getCurrentUser().getUid(), mPlaceDetail,
                            true, null, mCurrentUser)
                            .addOnFailureListener(onFailureListener(this));
                    Snackbar.make(v, getString(R.string.save_place_selection), Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
                isCheckFab = false;

            } else {
                mFab.setImageResource(R.drawable.ic_uncheck_circle);
                if (getCurrentUser() != null) {
                    RestaurantHelper.deleteRestaurantSelection(getCurrentUser().getUid())
                            .addOnFailureListener(this.onFailureListener(this));
                    Snackbar.make(v, getString(R.string.delete_place_selection), Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
                isCheckFab = true;
            }

        });

        //LIKE BUTTON
        mLikeButton.setOnClickListener(v -> {
            if (isCheckLike) {
                mLikeButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_star_full, 0, 0);
                if (getCurrentUser() != null) {

                    RestaurantLikeHelper.likeRestaurant(getCurrentUser().getUid(),
                            mPlaceDetail.getResult().getName(), mPlaceDetail.getResult().getPlaceId(), true)
                            .addOnFailureListener(this.onFailureListener(this));
                    Snackbar.make(v, R.string.restaurant_like, Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
                isCheckLike = false;

            } else {
                mLikeButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_star_border, 0, 0);
                if (getCurrentUser() != null) {
                    RestaurantLikeHelper.dislikeRestaurant(getCurrentUser().getUid(), mPlaceDetail.getResult().getPlaceId())
                            .addOnFailureListener(this.onFailureListener(this));
                    Snackbar.make(v, R.string.restaurant_no_like, Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
                isCheckLike = true;
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
                        mCurrentUser = documentSnapshot.toObject(Users.class));
    }

    // Get Restaurant selection from Firestore
    private void getUserSelectionPlaceFromFirestore() {
        if (getCurrentUser() != null) {
            RestaurantHelper.getRestaurantSelection(getCurrentUser().getUid())
                    .addOnSuccessListener(documentSnapshot -> {
                        Restaurants restaurants = documentSnapshot.toObject(Restaurants.class);
                        if (restaurants != null) {
                            this.mDate = restaurants.getDateChoice();
                            this.mPlaceID = restaurants.getPlaceDetail().getResult().getPlaceId();
                            this.mRestaurantSelection = restaurants.getRestaurantSelection();

                            // Update view after calling database to check user choice
                            this.setUpFabViewWithFirestoreDatabase(mPlaceID, mRestaurantSelection, mDate);
                        }
                    }).addOnFailureListener(this.onFailureListener(this));
        }

    }

    // Get the user like star of the current restaurant
    private void getRestaurantLikeFromFirestore() {
        if (getCurrentUser() != null) {
            RestaurantLikeHelper.getRestaurantLike(getCurrentUser().getUid(), mPlaceDetail.getResult().getPlaceId())
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            String placeid = Objects.requireNonNull(documentSnapshot.getId());
                            this.setUpLikeViewWithFirestoreDatabase(placeid);
                        }
                    }).addOnFailureListener(this.onFailureListener(this));

        }
    }

    private void getWorkmatesWhichSelectTheSamePlace() {
        RestaurantHelper.getRestaurantDocuments().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (DocumentSnapshot documentSnapshot : task.getResult()) {
                    Restaurants restaurants = documentSnapshot.toObject(Restaurants.class);
                    assert restaurants != null;
                    if (restaurants.getWorkmate() != null) {
                        if (!restaurants.getWorkmate().getUid().equals(mCurrentUser.getUid())
                                && restaurants.getPlaceDetail().getResult().getPlaceId().equals(mPlaceDetail.getResult().getPlaceId()))
                            // Store needed data inside arrayList
                            mRestaurantsFromFireStore.add(restaurants);
                    }
                }
                //Here the recyclerview retrieve data from request above
                this.configureRecyclerView();
            } else {
                Log.d(TAG, "Error getting documents: ", task.getException());
            }
        }).addOnFailureListener(this.onFailureListener(this));
    }


    // ---------------------
    // UI
    // ---------------------

    private void setUpViewsWithPlaceApi() {
        //Restaurant name
        restaurantName.setText(mPlaceDetail.getResult().getName());
        //Restaurant address
        restaurantAddress.setText(mPlaceDetail.getResult().getVicinity());
        //Restaurant rating
        restaurantRatingBar.setRating(formatRating(mPlaceDetail.getResult().getRating()));
        //Restaurant Photo
        if (mPlaceDetail.getResult().getPhotos() != null) {
            String request = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&maxheight=400" +
                    "&photoreference=";
            String apiKey = "&key=" + BuildConfig.ApiKey;
            Glide.with(this)
                    .load(request + mPlaceDetail.getResult().getPhotos().get(0).getPhotoReference() + apiKey)
                    .apply(RequestOptions.centerCropTransform())
                    .into(this.restaurantPhoto);
        }

    }

    private void setUpFabViewWithFirestoreDatabase(String placeID, Boolean selection, Date date) {
        // FAB
        // if selection is true and date is from today and the placeId restaurant equal placeID from singleton
        Date d = Calendar.getInstance().getTime();
        if (selection && mPlaceDetail.getResult().getPlaceId().equals(placeID) && formatDate(d).equals(formatDate(date))) {
            mFab.setImageResource(R.drawable.ic_check_circle);
        }

    }

    private void setUpLikeViewWithFirestoreDatabase(String placeID) {
        //LIKE
        // when parcouring documents you find the same placeID with placeID from singleton
        if (mPlaceDetail.getResult().getPlaceId().equals(placeID)) {
            mLikeButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_star_full, 0, 0);
        }
    }

}

